package org.tlauncher.model;

import org.tlauncher.minecraft.Resource;
import org.tlauncher.renderer.texture.FramedTexture;

public class ProfileTexture {
  private static final long REMOVAL_TIME_MILLS = 85000L;
  
  private Resource skin;
  
  private FramedTexture cape;
  
  private boolean capeReady;
  
  private long time;
  
  private String skinType;
  
  private boolean elytra;
  
  public void setSkin(Resource skin) {
    this.skin = skin;
  }
  
  public void setCape(FramedTexture cape) {
    this.cape = cape;
  }
  
  public void setCapeReady(boolean capeReady) {
    this.capeReady = capeReady;
  }
  
  public void setTime(long time) {
    this.time = time;
  }
  
  public void setSkinType(String skinType) {
    this.skinType = skinType;
  }
  
  public void setElytra(boolean elytra) {
    this.elytra = elytra;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ProfileTexture))
      return false; 
    ProfileTexture other = (ProfileTexture)o;
    if (!other.canEqual(this))
      return false; 
    Object this$skin = getSkin(), other$skin = other.getSkin();
    if ((this$skin == null) ? (other$skin != null) : !this$skin.equals(other$skin))
      return false; 
    Object this$cape = getCape(), other$cape = other.getCape();
    if ((this$cape == null) ? (other$cape != null) : !this$cape.equals(other$cape))
      return false; 
    if (isCapeReady() != other.isCapeReady())
      return false; 
    if (getTime() != other.getTime())
      return false; 
    Object this$skinType = getSkinType(), other$skinType = other.getSkinType();
    return ((this$skinType == null) ? (other$skinType != null) : !this$skinType.equals(other$skinType)) ? false : (!(isElytra() != other.isElytra()));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ProfileTexture;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $skin = getSkin();
    result = result * 59 + (($skin == null) ? 43 : $skin.hashCode());
    Object $cape = getCape();
    result = result * 59 + (($cape == null) ? 43 : $cape.hashCode());
    result = result * 59 + (isCapeReady() ? 79 : 97);
    long $time = getTime();
    result = result * 59 + (int)($time >>> 32L ^ $time);
    Object $skinType = getSkinType();
    result = result * 59 + (($skinType == null) ? 43 : $skinType.hashCode());
    return result * 59 + (isElytra() ? 79 : 97);
  }
  
  public String toString() {
    return "ProfileTexture(skin=" + getSkin() + ", cape=" + getCape() + ", capeReady=" + isCapeReady() + ", time=" + getTime() + ", skinType=" + getSkinType() + ", elytra=" + isElytra() + ")";
  }
  
  public Resource getSkin() {
    return this.skin;
  }
  
  public FramedTexture getCape() {
    return this.cape;
  }
  
  public boolean isCapeReady() {
    return this.capeReady;
  }
  
  public long getTime() {
    return this.time;
  }
  
  public String getSkinType() {
    return this.skinType;
  }
  
  public boolean isElytra() {
    return this.elytra;
  }
  
  public void updateTime() {
    setTime(System.currentTimeMillis() + 85000L);
  }
}
