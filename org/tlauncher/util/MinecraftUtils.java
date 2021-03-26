package org.tlauncher.util;

import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;

public class MinecraftUtils {
  public static String getSessionUsername() {
    return Minecraft.func_71410_x().func_110432_I().func_111285_a();
  }
  
  public static NativeImage readNativeImage(InputStream inputStream) {
    return NativeImage.func_195713_a(inputStream);
  }
}
