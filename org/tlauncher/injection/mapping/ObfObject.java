package org.tlauncher.injection.mapping;

public abstract class ObfObject {
  private final String name;
  
  private final String obfName;
  
  public ObfObject(String name, String obfName) {
    this.name = name;
    this.obfName = obfName;
  }
  
  protected String getName() {
    return this.name;
  }
  
  public String getObfName() {
    return this.obfName;
  }
}
