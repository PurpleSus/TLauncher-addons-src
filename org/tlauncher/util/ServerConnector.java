package org.tlauncher.util;

import org.tlauncher.TLSkinCape;
import org.tlauncher.connector.AbstractConnector;
import org.tlauncher.connector.FabricConnector;
import org.tlauncher.connector.ForgeConnector;

public class ServerConnector {
  private boolean flag;
  
  public void tryToConnect() {
    if (!this.flag) {
      this.flag = true;
      AbstractConnector connector = null;
      if (TLModCfg.isForgeDetected()) {
        ForgeConnector forgeConnector = new ForgeConnector(TLSkinCape.getMinecraftInstance());
      } else if (TLModCfg.isFabricDetected()) {
        FabricConnector fabricConnector = new FabricConnector(TLSkinCape.getMinecraftInstance());
      } else {
        try {
          Class<?> aClass = Class.forName("org.tlauncher.connector.Connector");
          connector = aClass.getConstructor(new Class[] { TLSkinCape.getMinecraftInstance().getClass() }).newInstance(new Object[] { TLSkinCape.getMinecraftInstance() });
        } catch (InstantiationException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|IllegalAccessException|ClassNotFoundException e) {
          TLSkinCape.LOGGER.error("No suitable connector found.", e);
        } 
      } 
      if (connector != null)
        connector.connectToServer(); 
    } 
  }
}
