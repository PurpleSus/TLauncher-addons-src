package org.tlauncher.renderer.image;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import org.tlauncher.util.TLModCfg;

public final class ImageWrapFactory {
  private ImageWrapFactory() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  public static ImageWrap create(BufferedImage bufferedImage) {
    if (!TLModCfg.isNativeImageSupported())
      return new BufferedImageWrap(bufferedImage); 
    return new NativeImageWrap(bufferedImage);
  }
  
  public static ImageWrap create(InputStream inputStream) {
    try {
      if (!TLModCfg.isNativeImageSupported())
        return new BufferedImageWrap(inputStream); 
      return new NativeImageWrap(inputStream);
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public static ImageWrap create(int width, int height) {
    if (!TLModCfg.isNativeImageSupported())
      return new BufferedImageWrap(width, height); 
    return new NativeImageWrap(width, height);
  }
}
