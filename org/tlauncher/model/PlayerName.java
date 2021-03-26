package org.tlauncher.model;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class PlayerName {
  private final String displayName;
  
  private String name;
  
  private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> nativeTextures;
  
  public PlayerName(String displayName) {
    this.displayName = displayName;
    this.name = displayName;
  }
  
  public PlayerName(GameProfile p) {
    if (StringUtils.isBlank(p.getName()) && Objects.nonNull(p.getId())) {
      this.displayName = p.getId().toString();
    } else {
      this.displayName = p.getName();
    } 
    this.name = this.displayName;
  }
  
  public String getDisplayName() {
    return this.displayName;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public boolean equals(Object o) {
    if (this == o)
      return true; 
    if (o == null || getClass() != o.getClass())
      return false; 
    PlayerName that = (PlayerName)o;
    return Objects.equals(this.displayName, that.displayName);
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { this.displayName });
  }
  
  public String toString() {
    return "displayName=" + this.displayName + ", name=" + this.name;
  }
  
  public void fill(MinecraftTexturesPayload1 result) {
    if (Objects.isNull(result))
      return; 
    if (StringUtils.isNotBlank(result.getProfileName()))
      setName(result.getProfileName()); 
    if (Objects.nonNull(result.getTextures()) && !result.getTextures().isEmpty())
      this.nativeTextures = result.getTextures(); 
  }
  
  public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getNativeTextures() {
    return this.nativeTextures;
  }
}
