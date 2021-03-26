package org.tlauncher.renderer;

import java.lang.reflect.Method;
import org.lwjgl.opengl.GL11;
import org.tlauncher.util.TLModCfg;
import org.tlauncher.util.TypeLocator;

public class TextureHandlerBee implements ITextureHandler, TypeLocator {
  private Method allocateTexture;
  
  public void bindTex(int textureId) {
    try {
      GL11.glBindTexture(3553, textureId);
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public void delTex(int textureId) {
    try {
      GL11.glDeleteTextures(textureId);
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public void prepImage(int textureId, int width, int height) {
    try {
      if (TLModCfg.isForgeDetected()) {
        if (this.allocateTexture == null)
          this.allocateTexture = Class.forName("net.minecraft.client.renderer.texture.TextureUtil").getDeclaredMethod("func_225680_a_", new Class[] { int.class, int.class, int.class }); 
        this.allocateTexture.invoke(null, new Object[] { Integer.valueOf(textureId), Integer.valueOf(width), Integer.valueOf(height) });
      } else if (TLModCfg.isFabricDetected()) {
        if (this.allocateTexture == null) {
          Class<?> aClass = Class.forName("net.minecraft.class_4536");
          this.allocateTexture = findMethod(aClass, new TypeLocator.MethodData[] { new TypeLocator.MethodData("method_24958", new Class[] { int.class, int.class, int.class }), new TypeLocator.MethodData("prepareImage", new Class[] { int.class, int.class, int.class }) });
        } 
        this.allocateTexture.invoke(null, new Object[] { Integer.valueOf(textureId), Integer.valueOf(width), Integer.valueOf(height) });
      } 
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
}
