package org.tlauncher.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PerformanceTextureData extends PreparedTextureData {
  public String toString() {
    return "PerformanceTextureData(initFrameTime=" + getInitFrameTime() + ")";
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object<Long> $initFrameTime = (Object<Long>)getInitFrameTime();
    return result * 59 + (($initFrameTime == null) ? 43 : $initFrameTime.hashCode());
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof PerformanceTextureData;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof PerformanceTextureData))
      return false; 
    PerformanceTextureData other = (PerformanceTextureData)o;
    if (!other.canEqual(this))
      return false; 
    Object<Long> this$initFrameTime = (Object<Long>)getInitFrameTime(), other$initFrameTime = (Object<Long>)other.getInitFrameTime();
    return !((this$initFrameTime == null) ? (other$initFrameTime != null) : !this$initFrameTime.equals(other$initFrameTime));
  }
  
  public void setInitFrameTime(List<Long> initFrameTime) {
    this.initFrameTime = initFrameTime;
  }
  
  private List<Long> initFrameTime = new ArrayList<>();
  
  public List<Long> getInitFrameTime() {
    return this.initFrameTime;
  }
  
  public double getMiddleInitFrameTime() {
    return ((Double)this.initFrameTime.stream().collect(Collectors.averagingLong(Long::longValue))).doubleValue();
  }
}
