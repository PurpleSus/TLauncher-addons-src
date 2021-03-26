package org.tlauncher.minecraft;

import org.tlauncher.renderer.texture.LightTexture;

public interface GameTextureManager {
  void loadTexture(Resource paramResource, LightTexture paramLightTexture);
  
  void deleteTexture(Resource paramResource);
  
  LightTexture getTexture(Resource paramResource);
}
