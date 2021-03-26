package org.tlauncher.renderer.texture;

import java.util.ArrayList;
import java.util.List;
import org.tlauncher.minecraft.GameTextureManager;
import org.tlauncher.minecraft.Resource;
import org.tlauncher.renderer.image.ImageWrap;

public class FramedTexture {
  private static int FRAME_COUNTER;
  
  private int currentFrameIndex;
  
  private long lastTimeNano = System.nanoTime();
  
  private final long updatingTextureInNano;
  
  private final boolean animated;
  
  private List<Resource> frames;
  
  public List<Resource> getFrames() {
    return this.frames;
  }
  
  public FramedTexture(long updatingTextureInNano, boolean animated) {
    this.updatingTextureInNano = updatingTextureInNano;
    this.frames = new ArrayList<>();
    this.animated = animated;
  }
  
  public FramedTexture(int framesSize, long updatingTextureInNano, boolean animated) {
    this(updatingTextureInNano, animated);
    this.frames = new ArrayList<>(framesSize);
  }
  
  public static FramedTexture createOneFramedTexture(GameTextureManager gameTextureManager, ImageWrap imageWrap) {
    FramedTexture texture = new FramedTexture(1L, false);
    Resource resourceLocation = new Resource(String.format("dynamic/framedtexture%s.png", new Object[] { Integer.valueOf(getNextFrameIndex()) }));
    LightTexture lightTexture = new LightTexture(imageWrap);
    gameTextureManager.loadTexture(resourceLocation, lightTexture);
    texture.frames.add(resourceLocation);
    return texture;
  }
  
  public void initByOneImage(GameTextureManager gameTextureManager, ImageWrap imageWrap) {
    Resource res = new Resource("dynamic/framedtexture" + getNextFrameIndex() + ".png");
    LightTexture lightTexture = new LightTexture(imageWrap);
    gameTextureManager.loadTexture(res, lightTexture);
    this.frames.add(res);
  }
  
  private Resource getFirstFrame() {
    return this.frames.get(0);
  }
  
  private static int getNextFrameIndex() {
    return ++FRAME_COUNTER;
  }
  
  public Resource getFrame() {
    if (!this.animated)
      return getFirstFrame(); 
    if (System.nanoTime() - this.lastTimeNano >= this.updatingTextureInNano) {
      this.lastTimeNano = System.nanoTime();
      if (this.currentFrameIndex + 1 < this.frames.size()) {
        this.currentFrameIndex++;
      } else {
        this.currentFrameIndex = 0;
      } 
    } 
    return this.frames.get(this.currentFrameIndex);
  }
}
