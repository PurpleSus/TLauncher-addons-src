package org.tlauncher.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.annotation.Nullable;
import org.tlauncher.renderer.ITextureHandler;

public final class TextureUtils {
  private static ITextureHandler TEXTURE_HANDLER;
  
  private TextureUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  static {
    init();
  }
  
  private static void init() {
    try {
      switch (TLModCfg.getGameVersionCategory()) {
        case OLD:
          TEXTURE_HANDLER = (ITextureHandler)Class.forName("org.tlauncher.renderer.TextureHandlerOld").newInstance();
          break;
        case VILLAGE_AND_PILLAGE:
          TEXTURE_HANDLER = (ITextureHandler)Class.forName("org.tlauncher.renderer.TextureHandlerVAP").newInstance();
          break;
        case BUZZY_BEES:
          TEXTURE_HANDLER = (ITextureHandler)Class.forName("org.tlauncher.renderer.TextureHandlerBee").newInstance();
          break;
      } 
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public static void deleteTexture(int textureID) {
    TEXTURE_HANDLER.delTex(textureID);
  }
  
  public static void bindTexture(int textureID) {
    TEXTURE_HANDLER.bindTex(textureID);
  }
  
  public static void prepareImage(int textureID, int width, int height) {
    TEXTURE_HANDLER.prepImage(textureID, width, height);
  }
  
  @Nullable
  static BufferedImage getRightSkin(BufferedImage image) {
    if (image == null)
      return null; 
    if (image.getWidth() % 64 == 0 && image.getHeight() % 64 == 0 && image.getHeight() == image.getWidth())
      return image; 
    BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight() * 2, 6);
    int m = image.getWidth() / 64;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (j >= 16 * m && j < 32 * m) {
          if (i < m * 4) {
            temp.setRGB(m * 19 - i, 32 * m + j, image.getRGB(i, j));
          } else if (i < m * 8) {
            temp.setRGB(m * 27 - i, 32 * m + j, image.getRGB(i, j));
          } else if (i < m * 12) {
            temp.setRGB(m * 35 - i, 32 * m + j, image.getRGB(i, j));
          } else if (i < m * 16) {
            temp.setRGB(m * 43 - i, 32 * m + j, image.getRGB(i, j));
          } 
          if (i >= 40 * m)
            if (i < 44 * m) {
              temp.setRGB(83 * m - i, 32 * m + j, image.getRGB(i, j));
            } else if (i < 48 * m) {
              temp.setRGB(83 * m - i, 32 * m + j, image.getRGB(i, j));
            } else if (i < 52 * m) {
              if (j >= 20 * m) {
                temp.setRGB(83 * m - i, 32 * m + j, image.getRGB(i, j));
              } else {
                temp.setRGB(91 * m - i, 32 * m + j, image.getRGB(i, j));
              } 
            } else if (i < 56 * m) {
              temp.setRGB(99 * m - i, 32 * m + j, image.getRGB(i, j));
            }  
        } 
        temp.setRGB(i, j, image.getRGB(i, j));
      } 
    } 
    return temp;
  }
  
  static BufferedImage resize(BufferedImage img, int newW, int newH) {
    Image tmp = img.getScaledInstance(newW, newH, 4);
    BufferedImage dimg = new BufferedImage(newW, newH, 2);
    Graphics2D g2d = dimg.createGraphics();
    g2d.drawImage(tmp, 0, 0, (ImageObserver)null);
    g2d.dispose();
    return dimg;
  }
}
