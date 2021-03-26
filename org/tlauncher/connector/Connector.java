package org.tlauncher.connector;

import com.google.common.collect.Maps;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import org.tlauncher.injection.mapping.MappingManager;

public class Connector extends AbstractConnector {
  private Minecraft client;
  
  private Map<ServerData, ExtendedServerListData> serverDataTag;
  
  public Connector(Minecraft minecraft) {
    this.client = minecraft;
  }
  
  public void showGuiScreen(@Nullable Object clientGuiElement) {
    GuiScreen gui = (GuiScreen)clientGuiElement;
    try {
      Method method = this.client.getClass().getMethod(MappingManager.instance().getMappings().getClass("Minecraft").getMethod("displayGuiScreen").getObfName(), new Class[] { GuiScreen.class });
      method.invoke(this.client, new Object[] { gui });
    } catch (NoSuchMethodException|IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
      e.printStackTrace();
    } 
  }
  
  public void setupServerList() {
    this.serverDataTag = Collections.synchronizedMap(Maps.newHashMap());
  }
  
  public void connectToServer(String host, int port) {
    setupServerList();
    ServerData serverData = null;
    String command_line = "Command Line";
    String serverPath = host + ":" + port;
    try {
      ServerData.class.getConstructor(new Class[] { String.class, String.class, boolean.class });
      serverData = new ServerData(command_line, serverPath, false);
    } catch (NoSuchMethodException e) {
      try {
        serverData = ServerData.class.getConstructor(new Class[] { String.class, String.class }).newInstance(new Object[] { command_line, serverPath });
      } catch (InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException|NoSuchMethodException instantiationException) {
        instantiationException.printStackTrace();
      } 
    } 
    if (serverData != null)
      connectToServer((GuiScreen)null, serverData); 
  }
  
  public void connectToServer(GuiScreen guiMultiplayer, ServerData serverEntry) {
    ExtendedServerListData extendedData = this.serverDataTag.get(serverEntry);
    if (extendedData != null && extendedData.isBlocked) {
      showGuiScreen(null);
    } else {
      showGuiScreen(new GuiConnecting(guiMultiplayer, this.client, serverEntry));
    } 
  }
}
