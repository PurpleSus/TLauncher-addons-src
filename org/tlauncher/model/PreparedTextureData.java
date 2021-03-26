package org.tlauncher.model;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.tlauncher.renderer.image.ImageWrap;

public class PreparedTextureData {
  private PlayerName name;
  
  public void setName(PlayerName name) {
    this.name = name;
  }
  
  public void setProfileTextureDTO(Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> profileTextureDTO) {
    this.profileTextureDTO = profileTextureDTO;
  }
  
  public void setImages(Map<MinecraftProfileTexture.Type, ImageWrap> images) {
    this.images = images;
  }
  
  public void setCapeFrames(List<ImageWrap> capeFrames) {
    this.capeFrames = capeFrames;
  }
  
  public void setSkin(ImageWrap skin) {
    this.skin = skin;
  }
  
  public void setPreparedIndexFrame(int preparedIndexFrame) {
    this.preparedIndexFrame = preparedIndexFrame;
  }
  
  public void setInitTime(long initTime) {
    this.initTime = initTime;
  }
  
  public void setElytra(boolean elytra) {
    this.elytra = elytra;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof PreparedTextureData))
      return false; 
    PreparedTextureData other = (PreparedTextureData)o;
    if (!other.canEqual(this))
      return false; 
    Object this$name = getName(), other$name = other.getName();
    if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name))
      return false; 
    Object<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> this$profileTextureDTO = (Object<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO>)getProfileTextureDTO(), other$profileTextureDTO = (Object<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO>)other.getProfileTextureDTO();
    if ((this$profileTextureDTO == null) ? (other$profileTextureDTO != null) : !this$profileTextureDTO.equals(other$profileTextureDTO))
      return false; 
    Object<MinecraftProfileTexture.Type, ImageWrap> this$images = (Object<MinecraftProfileTexture.Type, ImageWrap>)getImages(), other$images = (Object<MinecraftProfileTexture.Type, ImageWrap>)other.getImages();
    if ((this$images == null) ? (other$images != null) : !this$images.equals(other$images))
      return false; 
    Object<ImageWrap> this$capeFrames = (Object<ImageWrap>)getCapeFrames(), other$capeFrames = (Object<ImageWrap>)other.getCapeFrames();
    if ((this$capeFrames == null) ? (other$capeFrames != null) : !this$capeFrames.equals(other$capeFrames))
      return false; 
    Object this$skin = getSkin(), other$skin = other.getSkin();
    return ((this$skin == null) ? (other$skin != null) : !this$skin.equals(other$skin)) ? false : ((getPreparedIndexFrame() != other.getPreparedIndexFrame()) ? false : ((getMaxTimeLoad() != other.getMaxTimeLoad()) ? false : ((getInitTime() != other.getInitTime()) ? false : (!(isElytra() != other.isElytra())))));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof PreparedTextureData;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $name = getName();
    result = result * 59 + (($name == null) ? 43 : $name.hashCode());
    Object<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> $profileTextureDTO = (Object<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO>)getProfileTextureDTO();
    result = result * 59 + (($profileTextureDTO == null) ? 43 : $profileTextureDTO.hashCode());
    Object<MinecraftProfileTexture.Type, ImageWrap> $images = (Object<MinecraftProfileTexture.Type, ImageWrap>)getImages();
    result = result * 59 + (($images == null) ? 43 : $images.hashCode());
    Object<ImageWrap> $capeFrames = (Object<ImageWrap>)getCapeFrames();
    result = result * 59 + (($capeFrames == null) ? 43 : $capeFrames.hashCode());
    Object $skin = getSkin();
    result = result * 59 + (($skin == null) ? 43 : $skin.hashCode());
    result = result * 59 + getPreparedIndexFrame();
    long $maxTimeLoad = getMaxTimeLoad();
    result = result * 59 + (int)($maxTimeLoad >>> 32L ^ $maxTimeLoad);
    long $initTime = getInitTime();
    result = result * 59 + (int)($initTime >>> 32L ^ $initTime);
    return result * 59 + (isElytra() ? 79 : 97);
  }
  
  public String toString() {
    return "PreparedTextureData(name=" + getName() + ", profileTextureDTO=" + getProfileTextureDTO() + ", images=" + getImages() + ", capeFrames=" + getCapeFrames() + ", skin=" + getSkin() + ", preparedIndexFrame=" + getPreparedIndexFrame() + ", maxTimeLoad=" + getMaxTimeLoad() + ", initTime=" + getInitTime() + ", elytra=" + isElytra() + ")";
  }
  
  public PlayerName getName() {
    return this.name;
  }
  
  private volatile Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> profileTextureDTO = new HashMap<>();
  
  public Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> getProfileTextureDTO() {
    return this.profileTextureDTO;
  }
  
  private Map<MinecraftProfileTexture.Type, ImageWrap> images = Collections.synchronizedMap(new HashMap<>());
  
  public Map<MinecraftProfileTexture.Type, ImageWrap> getImages() {
    return this.images;
  }
  
  private List<ImageWrap> capeFrames = new ArrayList<>();
  
  private ImageWrap skin;
  
  private int preparedIndexFrame;
  
  private long maxTimeLoad;
  
  public List<ImageWrap> getCapeFrames() {
    return this.capeFrames;
  }
  
  public ImageWrap getSkin() {
    return this.skin;
  }
  
  public int getPreparedIndexFrame() {
    return this.preparedIndexFrame;
  }
  
  public long getMaxTimeLoad() {
    return this.maxTimeLoad;
  }
  
  private long initTime = System.currentTimeMillis();
  
  private boolean elytra;
  
  public long getInitTime() {
    return this.initTime;
  }
  
  public boolean isElytra() {
    return this.elytra;
  }
  
  public boolean hasFrame() {
    return (this.preparedIndexFrame < this.capeFrames.size());
  }
  
  public int getPreparedIndexFrameAndIncrease() {
    return this.preparedIndexFrame++;
  }
  
  public void setMaxTimeLoad(long maxTimeLoad) {
    if (this.maxTimeLoad < maxTimeLoad)
      this.maxTimeLoad = maxTimeLoad; 
  }
}
