package org.tlauncher.connector;

public abstract class AbstractConnector {
  public void connectToServer() {
    String serverName = System.getProperty("org.tlauncher.tlskincape.server");
    String serverPort0 = System.getProperty("org.tlauncher.tlskincape.port");
    if (serverName != null) {
      int serverPort;
      if (serverPort0 != null) {
        serverPort = Integer.parseInt(serverPort0);
      } else {
        serverPort = 25565;
      } 
      connectToServer(serverName, serverPort);
    } 
  }
  
  public abstract void connectToServer(String paramString, int paramInt);
}
