package org.tlauncher.injection.mapping;

import java.util.ArrayList;
import java.util.List;

public class Mappings {
  private final List<ObfClass> classes = new ArrayList<>();
  
  protected List<ObfClass> getClasses() {
    return this.classes;
  }
  
  public ObfClass getClass(String name) {
    for (ObfClass aClass : this.classes) {
      if (aClass.getName().equals(name))
        return aClass; 
    } 
    return new ObfClass(name, name);
  }
}
