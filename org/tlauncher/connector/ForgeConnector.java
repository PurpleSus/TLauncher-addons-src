package org.tlauncher.connector;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.tlauncher.util.TypeLocator;

public class ForgeConnector extends AbstractConnector implements TypeLocator {
  private static Class<?> guiScreenClass;
  
  private final Minecraft client;
  
  private Map<ServerData, ExtendedServerListData> serverDataTag;
  
  public ForgeConnector(Minecraft client) {
    guiScreenClass = findClass(new String[] { "net.minecraft.client.gui.GuiScreen", "net.minecraft.client.gui.screen.Screen" });
    this.client = client;
  }
  
  public void showGuiScreen(@Nullable Object clientGuiElement) {
    try {
      Method method = this.client.getClass().getMethod("func_147108_a", new Class[] { guiScreenClass });
      method.invoke(this.client, new Object[] { clientGuiElement });
    } catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
      e.printStackTrace();
    } 
  }
  
  public void setupServerList() {
    this.serverDataTag = Collections.synchronizedMap(Maps.newHashMap());
  }
  
  public void connectToServer(String host, int port) {
    setupServerList();
    ServerData serverData = new ServerData("Command Line", host + ":" + port, false);
    connectToServer((Object)null, serverData);
  }
  
  public void connectToServer(Object guiMultiplayer, ServerData serverEntry) {
    ExtendedServerListData extendedData = this.serverDataTag.get(serverEntry);
    if (extendedData != null && extendedData.isBlocked) {
      showGuiScreen((Object)null);
    } else {
      Object clientGuiElement = findConstructor(new TypeLocator.ClassNames(new String[] { "net.minecraft.client.multiplayer.GuiConnecting", "net.minecraft.client.gui.screen.ConnectingScreen", "net.minecraft.client.gui.GuiConnecting" }, ), new TypeLocator.ParamsData(new Class[] { guiScreenClass, Minecraft.class, ServerData.class })).newInstance(new Object[] { guiMultiplayer, this.client, serverEntry });
      showGuiScreen(clientGuiElement);
    } 
  }
}
