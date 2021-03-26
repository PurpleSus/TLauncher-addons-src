package org.tlauncher.util.reflection;

import java.lang.reflect.Method;
import javax.annotation.Nonnull;
import org.tlauncher.util.TLModCfg;

public final class ReflectionUtils {
  private static final int SLOT_INDEX = 2;
  
  private static Method method;
  
  private ReflectionUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  private static boolean isInited = true;
  
  public static boolean isElytraEquipped(@Nonnull ClassLoader classLoader) {
    try {
      if (!TLModCfg.isElytraSupported())
        return false; 
      if (TLModCfg.isForgeDetected()) {
        if (method == null)
          method = Class.forName("org.tlauncher.util.ForgeReflectionUtils").getMethod("isElytraEquipped", new Class[] { int.class, boolean.class }); 
        boolean bool = ((Boolean)method.invoke(null, new Object[] { Integer.valueOf(2), Boolean.valueOf(!isInited) })).booleanValue();
        isInited = true;
        return bool;
      } 
      if (TLModCfg.isFabricDetected()) {
        if (method == null)
          method = Class.forName("org.tlauncher.util.FabricReflectionUtils").getMethod("isElytraEquipped", new Class[] { int.class, ClassLoader.class, boolean.class }); 
        boolean bool = ((Boolean)method.invoke(null, new Object[] { Integer.valueOf(2), classLoader, Boolean.valueOf(!isInited) })).booleanValue();
        isInited = true;
        return bool;
      } 
      if (method == null)
        method = Class.forName("org.tlauncher.util.reflection.VanillaReflectionUtils").getMethod("isElytraEquipped", new Class[] { int.class, ClassLoader.class, boolean.class }); 
      boolean invoke = ((Boolean)method.invoke(null, new Object[] { Integer.valueOf(2), classLoader, Boolean.valueOf(!isInited) })).booleanValue();
      isInited = true;
      return invoke;
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
  
  public static void forceUpdate() {
    isInited = false;
  }
}
