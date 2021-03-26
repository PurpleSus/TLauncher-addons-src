package org.tlauncher.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

public interface TypeLocator {
  Method findMethod(Class<?> aClass, MethodData... methodData) {
    for (MethodData md : methodData) {
      try {
        return md.get(aClass);
      } catch (NoSuchMethodException noSuchMethodException) {}
    } 
    throw new RuntimeException("Method not found: " + Arrays.toString(methodData));
  }
  
  Method findMethod(ClassNames classNames, MethodData... methodData) {
    for (MethodData md : methodData) {
      for (String name : classNames.names) {
        try {
          return md.get(Class.forName(name));
        } catch (NoSuchMethodException|ClassNotFoundException noSuchMethodException) {}
      } 
    } 
    throw new RuntimeException("Method not found: " + Arrays.toString(methodData));
  }
  
  Class<?> findClass(String... names) {
    for (String name : names) {
      try {
        return Class.forName(name);
      } catch (ClassNotFoundException classNotFoundException) {}
    } 
    throw new RuntimeException("Class not found: " + Arrays.toString(names));
  }
  
  default ConstructorContainer<?> findConstructor(ClassNames classNames, ParamsData paramsData) {
    for (String name : classNames.names) {
      try {
        return new ConstructorContainer(Class.forName(name).getConstructor(paramsData.params));
      } catch (NoSuchMethodException|ClassNotFoundException noSuchMethodException) {}
    } 
    throw new RuntimeException("Class not found: " + classNames + " " + paramsData);
  }
  
  public static class ClassNames {
    protected final String[] names;
    
    public String toString() {
      return "TypeLocator.ClassNames(names=" + Arrays.deepToString((Object[])this.names) + ")";
    }
    
    public ClassNames(String... names) {
      this.names = names;
    }
  }
  
  public static class MethodData extends ParamsData {
    protected final String name;
    
    protected final boolean isDeclared;
    
    public String toString() {
      return "TypeLocator.MethodData(super=" + super.toString() + ", name=" + this.name + ", isDeclared=" + this.isDeclared + ")";
    }
    
    public MethodData(String name, Class<?>... params) {
      this(false, name, params);
    }
    
    public MethodData(boolean isDeclared, String name, Class<?>... params) {
      super(params);
      this.name = name;
      this.isDeclared = isDeclared;
    }
    
    public Method get(Class<?> aClass) throws NoSuchMethodException {
      return this.isDeclared ? aClass.getDeclaredMethod(this.name, this.params) : aClass.getMethod(this.name, this.params);
    }
  }
  
  public static class ParamsData {
    protected final Class<?>[] params;
    
    public String toString() {
      return "TypeLocator.ParamsData(params=" + Arrays.deepToString((Object[])this.params) + ")";
    }
    
    public ParamsData(Class<?>... params) {
      this.params = params;
    }
  }
  
  public static class ConstructorContainer<T> {
    private final Constructor<T> constructor;
    
    public Constructor<T> getConstructor() {
      return this.constructor;
    }
    
    public ConstructorContainer(Constructor<T> constructor) {
      this.constructor = constructor;
    }
    
    public T newInstance(Object... objects) {
      try {
        return this.constructor.newInstance(objects);
      } catch (InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
        throw new RuntimeException(e);
      } 
    }
  }
}
