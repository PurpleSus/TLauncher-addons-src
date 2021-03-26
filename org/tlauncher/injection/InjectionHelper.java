package org.tlauncher.injection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

public class InjectionHelper {
  private final Logger logger;
  
  private ClassNode classNode;
  
  private ClassReader classReader;
  
  private ClassWriter classWriter;
  
  private Processor processor;
  
  public InjectionHelper(Logger logger) {
    this.logger = logger;
  }
  
  public ClassNode getClassNode() {
    return this.classNode;
  }
  
  public Processor init(byte[] bytes) {
    return init(bytes, 0);
  }
  
  public Processor init(byte[] bytes, int classReaderFlags) {
    this.classNode = new ClassNode();
    this.classReader = new ClassReader(bytes);
    this.classReader.accept((ClassVisitor)this.classNode, classReaderFlags);
    this.classWriter = new ClassWriter(3);
    return this.processor = new Processor();
  }
  
  public byte[] getClassBytes() {
    this.classNode.accept(this.classWriter);
    return this.classWriter.toByteArray();
  }
  
  public class Processor {
    public InjectionHelper.MethodNodeContainer<Object> findMethod(String name, String desc) {
      InjectionHelper.this.logger.debug("Looking for a {}{} method...", name, desc);
      for (MethodNode method : InjectionHelper.this.classNode.methods) {
        if (method.name.equals(name) && method.desc.equals(desc)) {
          InjectionHelper.this.logger.debug("FOUND");
          return new InjectionHelper.MethodNodeContainer(method);
        } 
      } 
      InjectionHelper.this.logger.debug("NOT FOUND");
      return new InjectionHelper.MethodNodeContainer(null);
    }
    
    public InjectionHelper.MethodNodeContainer<Object> findMethod(String name, Desc desc) {
      InjectionHelper.this.logger.debug("Looking for a {}{} method...", name, desc);
      for (MethodNode method : InjectionHelper.this.classNode.methods) {
        if (method.name.equals(name) && desc.test(method.desc)) {
          InjectionHelper.this.logger.debug("FOUND");
          return new InjectionHelper.MethodNodeContainer(method);
        } 
      } 
      InjectionHelper.this.logger.debug("NOT FOUND");
      return new InjectionHelper.MethodNodeContainer(null);
    }
    
    public List<InjectionHelper.MethodNodeContainer<Object>> findMethod(String name) {
      InjectionHelper.this.logger.debug("Looking for a {} methods...", name);
      List<InjectionHelper.MethodNodeContainer<Object>> list = new ArrayList<>();
      for (MethodNode method : InjectionHelper.this.classNode.methods) {
        if (method.name.equals(name)) {
          list.add(new InjectionHelper.MethodNodeContainer(method));
          break;
        } 
      } 
      if (list.isEmpty()) {
        InjectionHelper.this.logger.debug("NOT FOUND");
      } else {
        InjectionHelper.this.logger.debug("FOUND");
      } 
      return list;
    }
    
    public FieldNode findField(String name, String desc) {
      InjectionHelper.this.logger.debug("Looking for a {}{} field...", name, desc);
      for (FieldNode field : InjectionHelper.this.classNode.fields) {
        if (field.name.equals(name) && field.desc.equals(desc)) {
          InjectionHelper.this.logger.debug("FOUND");
          return field;
        } 
      } 
      InjectionHelper.this.logger.debug("NOT FOUND");
      return null;
    }
    
    private boolean isPrimitiveType(String type) {
      String primitives = "JISBCFDZV";
      for (char primitive : primitives.toCharArray()) {
        if (type.charAt(0) == primitive)
          return true; 
      } 
      return false;
    }
    
    public byte[] getClassBytes() {
      InjectionHelper.this.classNode.accept(InjectionHelper.this.classWriter);
      return InjectionHelper.this.classWriter.toByteArray();
    }
  }
  
  public class MethodNodeContainer<V> extends Processor {
    private final MethodNodeWrap source;
    
    private V nullResult;
    
    private boolean chain = true;
    
    public MethodNodeContainer(MethodNode source) {
      this.source = new MethodNodeWrap(source);
    }
    
    public MethodNodeContainer(MethodNode methodNode, V nullResult) {
      this(methodNode);
      this.nullResult = nullResult;
    }
    
    public MethodNodeContainer<V> nonNull() {
      this.chain = (this.source.getSource() != null);
      return this;
    }
    
    public MethodNodeContainer<V> isNull() {
      this.chain = (this.source.getSource() == null);
      return this;
    }
    
    public <T> MethodNodeContainer<T> ifNullResult(T result) {
      return new MethodNodeContainer(this.source.getSource(), (V)result);
    }
    
    public InjectionHelper.Processor thenAccept(Consumer<MethodNodeWrap> consumer) {
      if (this.chain)
        consumer.accept(this.source); 
      return InjectionHelper.this.processor;
    }
    
    public <T> T thenApply(Function<MethodNodeWrap, T> function) {
      return thenApply(function, (this.nullResult == null) ? null : (T)this.nullResult);
    }
    
    public <T> T thenApply(Function<MethodNodeWrap, T> function, T ifNull) {
      return this.chain ? function.apply(this.source) : ifNull;
    }
    
    public class MethodNodeWrap extends InjectionHelper.Processor {
      private final MethodNode source;
      
      public MethodNodeWrap(MethodNode source) {
        this.source = source;
      }
      
      public MethodNode getSource() {
        return this.source;
      }
      
      @Nullable
      public AbstractInsnNode findInstruction(Predicate<AbstractInsnNode> predicate) {
        if (this.source != null) {
          ListIterator<AbstractInsnNode> iterator = getInstructions().iterator();
          while (iterator.hasNext()) {
            AbstractInsnNode next = iterator.next();
            if (predicate.test(next))
              return next; 
          } 
        } 
        return null;
      }
      
      public void addInstruction(InsnNode insnNode) {
        if (this.source != null)
          this.source.instructions.add((AbstractInsnNode)insnNode); 
      }
      
      public Stream<AbstractInsnNode> findInstruction() {
        return (this.source != null) ? Arrays.<AbstractInsnNode>stream(getInstructions().toArray()) : Stream.<AbstractInsnNode>empty();
      }
      
      public InsnList getInstructions() {
        return this.source.instructions;
      }
    }
  }
  
  public static class MethodModel {
    private final int accessLevel;
    
    private final String name;
    
    private final String returnType;
    
    private final List<String> params;
    
    private final InsnList instructions;
    
    MethodModel(int accessLevel, String name, String returnType, List<String> params, InsnList instructions) {
      this.accessLevel = accessLevel;
      this.name = name;
      this.returnType = returnType;
      this.params = params;
      this.instructions = instructions;
    }
    
    public static MethodModelBuilder builder() {
      return new MethodModelBuilder();
    }
    
    public static class MethodModelBuilder {
      private int accessLevel;
      
      private String name;
      
      private String returnType;
      
      private ArrayList<String> params;
      
      private InsnList instructions;
      
      public MethodModelBuilder accessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
        return this;
      }
      
      public MethodModelBuilder name(String name) {
        this.name = name;
        return this;
      }
      
      public MethodModelBuilder returnType(String returnType) {
        this.returnType = returnType;
        return this;
      }
      
      public MethodModelBuilder param(String param) {
        if (this.params == null)
          this.params = new ArrayList<>(); 
        this.params.add(param);
        return this;
      }
      
      public MethodModelBuilder params(Collection<? extends String> params) {
        if (this.params == null)
          this.params = new ArrayList<>(); 
        this.params.addAll(params);
        return this;
      }
      
      public MethodModelBuilder clearParams() {
        if (this.params != null)
          this.params.clear(); 
        return this;
      }
      
      public MethodModelBuilder instructions(InsnList instructions) {
        this.instructions = instructions;
        return this;
      }
      
      public InjectionHelper.MethodModel build() {
        switch ((this.params == null) ? 0 : this.params.size()) {
          case false:
            params = Collections.emptyList();
            return new InjectionHelper.MethodModel(this.accessLevel, this.name, this.returnType, params, this.instructions);
          case true:
            params = Collections.singletonList(this.params.get(0));
            return new InjectionHelper.MethodModel(this.accessLevel, this.name, this.returnType, params, this.instructions);
        } 
        List<String> params = Collections.unmodifiableList(new ArrayList<>(this.params));
        return new InjectionHelper.MethodModel(this.accessLevel, this.name, this.returnType, params, this.instructions);
      }
      
      public String toString() {
        return "InjectionHelper.MethodModel.MethodModelBuilder(accessLevel=" + this.accessLevel + ", name=" + this.name + ", returnType=" + this.returnType + ", params=" + this.params + ", instructions=" + this.instructions + ")";
      }
    }
  }
}
