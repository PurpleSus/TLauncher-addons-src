package org.tlauncher.minecraft;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.Texture;
import org.tlauncher.renderer.texture.LightTexture;

public class GameTextureManagerImpl implements GameTextureManager {
  private final Supplier<Minecraft> getMinecraft;
  
  public GameTextureManagerImpl(Supplier<Minecraft> getMinecraft) {
    this.getMinecraft = getMinecraft;
  }
  
  public void loadTexture(Resource resource, LightTexture lightTexture) {
    ((Minecraft)this.getMinecraft.get()).func_110434_K().func_229263_a_(resource, (Texture)lightTexture);
  }
  
  public void deleteTexture(Resource resource) {
    ((Minecraft)this.getMinecraft.get()).func_110434_K().func_147645_c(resource);
  }
  
  public LightTexture getTexture(Resource resource) {
    return (LightTexture)((Minecraft)this.getMinecraft.get()).func_110434_K().func_229267_b_(resource);
  }
}
