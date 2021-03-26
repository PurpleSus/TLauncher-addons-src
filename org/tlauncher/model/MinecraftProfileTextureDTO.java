package org.tlauncher.model;

import java.util.Map;

public class MinecraftProfileTextureDTO {
  private String url;
  
  private Map<String, String> metadata;
  
  public void setUrl(String url) {
    this.url = url;
  }
  
  public void setMetadata(Map<String, String> metadata) {
    this.metadata = metadata;
  }
  
  public void setAnimatedCape(Boolean animatedCape) {
    this.animatedCape = animatedCape;
  }
  
  public void setAnimatedElytra(Boolean animatedElytra) {
    this.animatedElytra = animatedElytra;
  }
  
  public void setCapeHeight(Integer capeHeight) {
    this.capeHeight = capeHeight;
  }
  
  public void setCapeWidth(Integer capeWidth) {
    this.capeWidth = capeWidth;
  }
  
  public void setFps(Integer fps) {
    this.fps = fps;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof MinecraftProfileTextureDTO))
      return false; 
    MinecraftProfileTextureDTO other = (MinecraftProfileTextureDTO)o;
    if (!other.canEqual(this))
      return false; 
    Object this$url = getUrl(), other$url = other.getUrl();
    if ((this$url == null) ? (other$url != null) : !this$url.equals(other$url))
      return false; 
    Object<String, String> this$metadata = (Object<String, String>)getMetadata(), other$metadata = (Object<String, String>)other.getMetadata();
    if ((this$metadata == null) ? (other$metadata != null) : !this$metadata.equals(other$metadata))
      return false; 
    Object this$animatedCape = getAnimatedCape(), other$animatedCape = other.getAnimatedCape();
    if ((this$animatedCape == null) ? (other$animatedCape != null) : !this$animatedCape.equals(other$animatedCape))
      return false; 
    Object this$animatedElytra = getAnimatedElytra(), other$animatedElytra = other.getAnimatedElytra();
    if ((this$animatedElytra == null) ? (other$animatedElytra != null) : !this$animatedElytra.equals(other$animatedElytra))
      return false; 
    Object this$capeHeight = getCapeHeight(), other$capeHeight = other.getCapeHeight();
    if ((this$capeHeight == null) ? (other$capeHeight != null) : !this$capeHeight.equals(other$capeHeight))
      return false; 
    Object this$capeWidth = getCapeWidth(), other$capeWidth = other.getCapeWidth();
    if ((this$capeWidth == null) ? (other$capeWidth != null) : !this$capeWidth.equals(other$capeWidth))
      return false; 
    Object this$fps = getFps(), other$fps = other.getFps();
    return !((this$fps == null) ? (other$fps != null) : !this$fps.equals(other$fps));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof MinecraftProfileTextureDTO;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $url = getUrl();
    result = result * 59 + (($url == null) ? 43 : $url.hashCode());
    Object<String, String> $metadata = (Object<String, String>)getMetadata();
    result = result * 59 + (($metadata == null) ? 43 : $metadata.hashCode());
    Object $animatedCape = getAnimatedCape();
    result = result * 59 + (($animatedCape == null) ? 43 : $animatedCape.hashCode());
    Object $animatedElytra = getAnimatedElytra();
    result = result * 59 + (($animatedElytra == null) ? 43 : $animatedElytra.hashCode());
    Object $capeHeight = getCapeHeight();
    result = result * 59 + (($capeHeight == null) ? 43 : $capeHeight.hashCode());
    Object $capeWidth = getCapeWidth();
    result = result * 59 + (($capeWidth == null) ? 43 : $capeWidth.hashCode());
    Object $fps = getFps();
    return result * 59 + (($fps == null) ? 43 : $fps.hashCode());
  }
  
  public String toString() {
    return "MinecraftProfileTextureDTO(url=" + getUrl() + ", metadata=" + getMetadata() + ", animatedCape=" + getAnimatedCape() + ", animatedElytra=" + getAnimatedElytra() + ", capeHeight=" + getCapeHeight() + ", capeWidth=" + getCapeWidth() + ", fps=" + getFps() + ")";
  }
  
  public String getUrl() {
    return this.url;
  }
  
  public Map<String, String> getMetadata() {
    return this.metadata;
  }
  
  private Boolean animatedCape = Boolean.FALSE;
  
  public Boolean getAnimatedCape() {
    return this.animatedCape;
  }
  
  private Boolean animatedElytra = Boolean.FALSE;
  
  private Integer capeHeight;
  
  private Integer capeWidth;
  
  private Integer fps;
  
  public Boolean getAnimatedElytra() {
    return this.animatedElytra;
  }
  
  public Integer getCapeHeight() {
    return this.capeHeight;
  }
  
  public Integer getCapeWidth() {
    return this.capeWidth;
  }
  
  public Integer getFps() {
    return this.fps;
  }
}
