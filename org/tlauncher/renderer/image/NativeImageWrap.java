package org.tlauncher.renderer.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.texture.NativeImage;
import org.tlauncher.util.TextureUtils;
import org.tlauncher.util.TypeLocator;

public class NativeImageWrap implements ImageWrap, TypeLocator {
  private static Method readNativeImage;
  
  private final NativeImage nativeImage;
  
  public NativeImageWrap(InputStream inputStream) {
    this.nativeImage = readNativeImage(inputStream);
  }
  
  public NativeImageWrap(int width, int height) {
    this.nativeImage = new NativeImage(width, height, true);
  }
  
  public NativeImageWrap(BufferedImage bufferedImage) {
    try {
      this.nativeImage = getNativeImage(bufferedImage);
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  private NativeImage getNativeImage(BufferedImage bufferedImage) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    return readNativeImage(byteArrayInputStream);
  }
  
  private NativeImage readNativeImage(InputStream inputStream) {
    try {
      if (readNativeImage == null)
        readNativeImage = findMethod(NativeImage.class, new TypeLocator.MethodData[] { new TypeLocator.MethodData(true, "a", new Class[] { InputStream.class }), new TypeLocator.MethodData(true, "func_195713_a", new Class[] { InputStream.class }), new TypeLocator.MethodData(true, "method_4309", new Class[] { InputStream.class }) }); 
      return (NativeImage)readNativeImage.invoke(null, new Object[] { inputStream });
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public int getWidth() {
    return this.nativeImage.func_195702_a();
  }
  
  public int getHeight() {
    return this.nativeImage.func_195714_b();
  }
  
  public int getRGB(int x, int y) {
    return this.nativeImage.func_195709_a(x, y);
  }
  
  public void setRGB(int x, int y, int color) {
    this.nativeImage.func_195700_a(x, y, color);
  }
  
  public void allocateTexture(int textureId) {
    TextureUtils.prepareImage(textureId, getWidth(), getHeight());
  }
  
  public void uploadTexture(int textureId) {
    TextureUtils.bindTexture(textureId);
    this.nativeImage.func_195697_a(0, 0, 0, false);
    this.nativeImage.close();
  }
  
  public void close() {
    this.nativeImage.close();
  }
}
