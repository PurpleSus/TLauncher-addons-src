package org.tlauncher.util;

import org.tlauncher.TLSkinCape;

public final class TLModCfg {
  private TLModCfg() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  public enum GameVersionCategory {
    OLD, VILLAGE_AND_PILLAGE, BUZZY_BEES;
  }
  
  private static String MINECRAFT_VERSION = "forge";
  
  private static GameVersionCategory GAME_VERSION_CATEGORY;
  
  private static boolean isForgeDetected;
  
  private static boolean isOptiFineDetected;
  
  private static boolean isFabricDetected;
  
  public static boolean isForgeDetected() {
    return isForgeDetected;
  }
  
  public static boolean isOptiFineDetected() {
    return isOptiFineDetected;
  }
  
  public static boolean isFabricDetected() {
    return isFabricDetected;
  }
  
  static {
    System.out.println("[TLSkinCape] Current TLSkinCape version is 1.17");
    if (getMinecraftVersion().equals("forge")) {
      isForgeDetected = true;
      try {
        Class.forName("com.mojang.blaze3d.platform.GlStateManager");
        MINECRAFT_VERSION = "1.14";
      } catch (ClassNotFoundException e) {
        MINECRAFT_VERSION = "1.13";
      } 
      try {
        Class.forName("com.mojang.blaze3d.systems.RenderSystem");
        try {
          Class.forName("net.minecraft.realms.Realms");
          MINECRAFT_VERSION = "1.16";
        } catch (ClassNotFoundException ignored) {
          MINECRAFT_VERSION = "1.15";
        } 
      } catch (ClassNotFoundException classNotFoundException) {}
    } 
    if (getMinecraftVersion().equals("fabric")) {
      isFabricDetected = true;
      MINECRAFT_VERSION = "1.16";
    } 
    System.out.println(String.format("[TLSkinCape] Current Minecraft version is %s.", new Object[] { getMinecraftVersion() }));
    try {
      Class.forName("net.optifine.shaders.ShadersTex", false, TLModCfg.class.getClassLoader());
      isOptiFineDetected = true;
      System.out.println("[TLSkinCape] OptiFine detected.");
    } catch (ClassNotFoundException classNotFoundException) {}
  }
  
  private static void defineGameVersionCategory(ClassLoader classLoader) {
    GAME_VERSION_CATEGORY = GameVersionCategory.OLD;
    try {
      Class.forName("com.mojang.blaze3d.platform.GlStateManager", false, classLoader);
      GAME_VERSION_CATEGORY = GameVersionCategory.VILLAGE_AND_PILLAGE;
    } catch (ClassNotFoundException classNotFoundException) {}
    try {
      Class.forName("com.mojang.blaze3d.systems.RenderSystem", false, classLoader);
      GAME_VERSION_CATEGORY = GameVersionCategory.BUZZY_BEES;
    } catch (ClassNotFoundException classNotFoundException) {}
  }
  
  public static String getMinecraftVersion() {
    return MINECRAFT_VERSION;
  }
  
  public static boolean isElytraSupported() {
    return !MINECRAFT_VERSION.startsWith("1.8");
  }
  
  public static boolean isNativeImageSupported() {
    return (!MINECRAFT_VERSION.startsWith("1.8") && !MINECRAFT_VERSION.equals("1.12.2"));
  }
  
  static GameVersionCategory getGameVersionCategory() {
    if (GAME_VERSION_CATEGORY == null)
      defineGameVersionCategory(TLSkinCape.getMinecraftInstance().getClass().getClassLoader()); 
    return GAME_VERSION_CATEGORY;
  }
}
