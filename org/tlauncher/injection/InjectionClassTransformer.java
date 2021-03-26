package org.tlauncher.injection;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class InjectionClassTransformer implements IClassTransformer {
  private static final String LOG_PREFIX = "[Injection] ";
  
  private final InjectionManager injectionManager;
  
  public InjectionClassTransformer() {
    this.injectionManager = new InjectionManager();
    registerInjections();
  }
  
  public byte[] transform(String name, String transformedName, byte[] basicClass) {
    List<Injection> injections = this.injectionManager.getInjections(transformedName);
    int i;
    for (i = 0; i < injections.size(); i++) {
      Injection injection = injections.remove(i);
      InjectionHelper helper = this.injectionManager.getHelper(transformedName);
      InjectionResult injectionResult = injection.process(basicClass, helper);
      if (injectionResult == InjectionResult.SUCCESS) {
        basicClass = helper.getClassBytes();
      } else {
        System.out.printf("[Injection] Injection#%s into %s failed with result: %s.\n", new Object[] { Integer.valueOf(i), transformedName, injectionResult });
        return basicClass;
      } 
    } 
    if (i > 0)
      System.out.printf("[Injection] Injection into %s ended successfully with %s injections.\n", new Object[] { transformedName, Integer.valueOf(i) }); 
    return basicClass;
  }
  
  private void registerInjections() {
    Injections injections = new Injections();
    injections.registerInjections(this.injectionManager);
  }
  
  protected static void printNodes(InsnList insnList) {
    ListIterator<AbstractInsnNode> iterator = insnList.iterator();
    int i = 0;
    System.out.println();
    while (iterator.hasNext()) {
      System.out.print(i + ": ");
      System.out.println(getNodePrint(iterator.next()));
      i++;
    } 
  }
  
  protected static void saveNodesPrint(InsnList insnList, File file) {
    try {
      ListIterator<AbstractInsnNode> iterator = insnList.iterator();
      int i = 0;
      FileWriter fileWriter = new FileWriter(file);
      while (iterator.hasNext()) {
        fileWriter
          .append(String.valueOf(i))
          .append(": ")
          .append(getNodePrint(iterator.next()))
          .append("\n");
        i++;
      } 
      fileWriter.flush();
      fileWriter.close();
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  protected static String getNodePrint(AbstractInsnNode node) {
    String s = "";
    s = s + getOpcodeName(node.getOpcode()) + " ";
    if (node instanceof MethodInsnNode) {
      s = s + ((MethodInsnNode)node).owner + " " + ((MethodInsnNode)node).name + " " + ((MethodInsnNode)node).desc;
    } else if (node instanceof VarInsnNode) {
      s = s + ((VarInsnNode)node).var;
    } else if (node instanceof FieldInsnNode) {
      s = s + ((FieldInsnNode)node).owner + " " + ((FieldInsnNode)node).name + " " + ((FieldInsnNode)node).desc;
    } else {
      s = s + node;
    } 
    return s;
  }
  
  protected static String getOpcodeName(int opcode) {
    return Arrays.<Field>stream(Opcodes.class.getDeclaredFields())
      .filter(field -> (field.getType() == int.class || field.getType() == Integer.class))
      .filter(field -> Modifier.isPublic(field.getModifiers()))
      .filter(field -> Modifier.isStatic(field.getModifiers()))
      .filter(field -> {
          try {
            Integer integer = (Integer)field.get(null);
            return (integer.intValue() == opcode);
          } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
          } 
        }).findFirst()
      .map(Field::getName)
      .orElse(Integer.toString(opcode));
  }
}
