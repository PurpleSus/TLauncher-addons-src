package org.tlauncher.renderer.image;

public interface ImageWrap {
  int getWidth();
  
  int getHeight();
  
  int getRGB(int paramInt1, int paramInt2);
  
  void setRGB(int paramInt1, int paramInt2, int paramInt3);
  
  void allocateTexture(int paramInt);
  
  void uploadTexture(int paramInt);
  
  void close();
}
