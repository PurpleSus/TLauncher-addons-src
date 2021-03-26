package org.tlauncher.injection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Desc {
  private final String startsWith;
  
  private final String endsWith;
  
  private final List<String> containsList;
  
  private final String desc;
  
  Desc(String startsWith, String endsWith, List<String> containsList, String desc) {
    this.startsWith = startsWith;
    this.endsWith = endsWith;
    this.containsList = containsList;
    this.desc = desc;
  }
  
  public static DescBuilder builder() {
    return new DescBuilder();
  }
  
  public static class DescBuilder {
    private String startsWith;
    
    private String endsWith;
    
    private ArrayList<String> containsList;
    
    private String desc;
    
    public DescBuilder startsWith(String startsWith) {
      this.startsWith = startsWith;
      return this;
    }
    
    public DescBuilder endsWith(String endsWith) {
      this.endsWith = endsWith;
      return this;
    }
    
    public DescBuilder contains(String contains) {
      if (this.containsList == null)
        this.containsList = new ArrayList<>(); 
      this.containsList.add(contains);
      return this;
    }
    
    public DescBuilder containsList(Collection<? extends String> containsList) {
      if (this.containsList == null)
        this.containsList = new ArrayList<>(); 
      this.containsList.addAll(containsList);
      return this;
    }
    
    public DescBuilder clearContainsList() {
      if (this.containsList != null)
        this.containsList.clear(); 
      return this;
    }
    
    public DescBuilder desc(String desc) {
      this.desc = desc;
      return this;
    }
    
    public Desc build() {
      switch ((this.containsList == null) ? 0 : this.containsList.size()) {
        case false:
          containsList = Collections.emptyList();
          return new Desc(this.startsWith, this.endsWith, containsList, this.desc);
        case true:
          containsList = Collections.singletonList(this.containsList.get(0));
          return new Desc(this.startsWith, this.endsWith, containsList, this.desc);
      } 
      List<String> containsList = Collections.unmodifiableList(new ArrayList<>(this.containsList));
      return new Desc(this.startsWith, this.endsWith, containsList, this.desc);
    }
    
    public String toString() {
      return "Desc.DescBuilder(startsWith=" + this.startsWith + ", endsWith=" + this.endsWith + ", containsList=" + this.containsList + ", desc=" + this.desc + ")";
    }
  }
  
  public boolean test(String desc) {
    boolean b = true;
    if (this.startsWith != null)
      b = desc.startsWith(this.startsWith); 
    if (this.endsWith != null)
      b = desc.endsWith(this.endsWith); 
    if (this.desc != null)
      b = this.desc.equals(desc); 
    if (this.containsList != null)
      for (String s : this.containsList) {
        if (b)
          b = desc.contains(s); 
      }  
    return b;
  }
  
  public static Desc empty() {
    return new Desc(null, null, null, null);
  }
}
