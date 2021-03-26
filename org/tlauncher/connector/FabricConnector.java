package org.tlauncher.connector;

import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import org.tlauncher.util.TypeLocator;

public class FabricConnector extends AbstractConnector implements TypeLocator {
  private final Minecraft client;
  
  public FabricConnector(Minecraft client) {
    this.client = client;
  }
  
  public void showGuiScreen(@Nullable Object clientGuiElement) {
    this.client.func_147108_a((GuiScreen)clientGuiElement);
  }
  
  public void connectToServer(String host, int port) {
    ServerData serverData = new ServerData("Command Line", host + ":" + port, false);
    connectToServer((Object)null, serverData);
  }
  
  public void connectToServer(Object guiMultiplayer, ServerData serverEntry) {
    showGuiScreen(new GuiConnecting((GuiScreen)guiMultiplayer, this.client, serverEntry));
  }
}
