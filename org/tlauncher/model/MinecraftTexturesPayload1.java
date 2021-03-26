package org.tlauncher.model;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;

public class MinecraftTexturesPayload1 {
  private String profileName;
  
  private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;
  
  public String getProfileName() {
    return this.profileName;
  }
  
  public void setProfileName(String profileName) {
    this.profileName = profileName;
  }
  
  public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures() {
    return this.textures;
  }
  
  public void setTextures(Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures) {
    this.textures = textures;
  }
}
