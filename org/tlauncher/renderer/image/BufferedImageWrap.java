package org.tlauncher.renderer.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import org.tlauncher.util.TypeLocator;

public class BufferedImageWrap implements ImageWrap, TypeLocator {
  private static Method uploadTexture;
  
  private static Method allocateTexture;
  
  private final BufferedImage bufferedImage;
  
  public BufferedImageWrap(InputStream inputStream) {
    try {
      this.bufferedImage = ImageIO.read(inputStream);
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public BufferedImageWrap(int width, int height) {
    this.bufferedImage = new BufferedImage(width, height, 2);
  }
  
  public BufferedImageWrap(BufferedImage bufferedImage) {
    this.bufferedImage = bufferedImage;
  }
  
  public int getWidth() {
    return this.bufferedImage.getWidth();
  }
  
  public int getHeight() {
    return this.bufferedImage.getHeight();
  }
  
  public int getRGB(int x, int y) {
    return this.bufferedImage.getRGB(x, y);
  }
  
  public void setRGB(int x, int y, int color) {
    this.bufferedImage.setRGB(x, y, color);
  }
  
  public void allocateTexture(int textureId) {
    try {
      if (allocateTexture == null)
        allocateTexture = findMethod(new TypeLocator.ClassNames(new String[] { "net.minecraft.client.renderer.texture.TextureUtil", "cdt", "bml", "cuj" }, ), new TypeLocator.MethodData[] { new TypeLocator.MethodData("allocateTexture", new Class[] { int.class, int.class, int.class }), new TypeLocator.MethodData("a", new Class[] { int.class, int.class, int.class }) }); 
      allocateTexture.invoke(null, new Object[] { Integer.valueOf(textureId), Integer.valueOf(getWidth()), Integer.valueOf(getHeight()) });
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public void uploadTexture(int textureId) {
    try {
      int[] dynamicTextureData = new int[getWidth() * getHeight()];
      this.bufferedImage.getRGB(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), dynamicTextureData, 0, this.bufferedImage.getWidth());
      if (uploadTexture == null)
        uploadTexture = findMethod(new TypeLocator.ClassNames(new String[] { "net.minecraft.client.renderer.texture.TextureUtil", "cdt", "bml", "cuj" }, ), new TypeLocator.MethodData[] { new TypeLocator.MethodData("uploadTexture", new Class[] { int.class, int[].class, int.class, int.class }), new TypeLocator.MethodData("a", new Class[] { int.class, int[].class, int.class, int.class }) }); 
      uploadTexture.invoke(null, new Object[] { Integer.valueOf(textureId), dynamicTextureData, Integer.valueOf(getWidth()), Integer.valueOf(getHeight()) });
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public void close() {}
}
