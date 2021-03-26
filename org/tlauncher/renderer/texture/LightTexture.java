package org.tlauncher.renderer.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.lang.reflect.Method;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.resources.IResourceManager;
import org.lwjgl.opengl.GL11;

public class LightTexture extends Texture implements AutoCloseable {
  private static Method prepImage;
  
  private static Method bindTex;
  
  private static Method delTex;
  
  private static Method image_getWidth;
  
  private static Method image_getHeight;
  
  private static Method image_uploadTexture;
  
  private static Method image_close;
  
  protected int glTextureId = -1;
  
  public LightTexture(Object imageWrap) {
    try {
      if (prepImage == null)
        prepImage = Class.forName("org.tlauncher.util.TextureUtils").getDeclaredMethod("prepareImage", new Class[] { int.class, int.class, int.class }); 
      if (bindTex == null)
        bindTex = Class.forName("org.tlauncher.util.TextureUtils").getDeclaredMethod("bindTexture", new Class[] { int.class }); 
      if (!RenderSystem.isOnRenderThread()) {
        RenderSystem.recordRenderCall(() -> load(imageWrap));
      } else {
        load(imageWrap);
      } 
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  private void load(Object imageWrap) {
    try {
      if (image_getWidth == null) {
        image_getWidth = imageWrap.getClass().getMethod("getWidth", new Class[0]);
        image_getHeight = imageWrap.getClass().getMethod("getHeight", new Class[0]);
        image_uploadTexture = imageWrap.getClass().getMethod("uploadTexture", new Class[] { int.class });
        image_close = imageWrap.getClass().getMethod("close", new Class[0]);
      } 
      Object width = image_getWidth.invoke(imageWrap, new Object[0]);
      Object height = image_getHeight.invoke(imageWrap, new Object[0]);
      prepImage.invoke(null, new Object[] { Integer.valueOf(func_110552_b()), width, height });
      bindTex.invoke(null, new Object[] { Integer.valueOf(func_110552_b()) });
      image_uploadTexture.invoke(imageWrap, new Object[] { Integer.valueOf(func_110552_b()) });
      image_close.invoke(imageWrap, new Object[0]);
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public int func_110552_b() {
    if (this.glTextureId == -1)
      this.glTextureId = GL11.glGenTextures(); 
    return this.glTextureId;
  }
  
  public void func_195413_a(IResourceManager resourceManager) throws IOException {}
  
  public void close() {
    try {
      if (this.glTextureId != -1) {
        if (delTex == null)
          delTex = Class.forName("org.tlauncher.util.TextureUtils").getDeclaredMethod("deleteTexture", new Class[] { int.class }); 
        delTex.invoke(null, new Object[] { Integer.valueOf(this.glTextureId) });
        this.glTextureId = -1;
      } 
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
}
