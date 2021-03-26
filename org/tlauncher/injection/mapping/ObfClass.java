package org.tlauncher.injection.mapping;

import java.util.ArrayList;
import java.util.List;

public class ObfClass extends ObfObject {
  private final List<ObfMethod> methods = new ArrayList<>();
  
  protected List<ObfMethod> getMethods() {
    return this.methods;
  }
  
  private final List<ObfField> fields = new ArrayList<>();
  
  protected List<ObfField> getFields() {
    return this.fields;
  }
  
  public ObfClass(String name, String obfName) {
    super(name, obfName);
  }
  
  public ObfMethod getMethod(String name) {
    for (ObfMethod method : this.methods) {
      if (method.getName().equals(name))
        return method; 
    } 
    return new ObfMethod(name, name);
  }
  
  public ObfField getField(String name) {
    for (ObfField field : this.fields) {
      if (field.getName().equals(name))
        return field; 
    } 
    return new ObfField(name, name);
  }
}
