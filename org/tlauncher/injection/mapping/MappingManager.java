package org.tlauncher.injection.mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.tlauncher.util.TLModCfg;

public class MappingManager {
  public static MappingManager instance() {
    return instance;
  }
  
  private static final MappingManager instance = loadFromStream(MappingManager.class.getResourceAsStream("mappings.json"));
  
  private final Map<String, Mappings> versions = new HashMap<>();
  
  protected Map<String, Mappings> getVersions() {
    return this.versions;
  }
  
  public Mappings getMappings() {
    return this.versions.get(TLModCfg.getMinecraftVersion());
  }
  
  private static MappingManager loadFromStream(InputStream inputStream) {
    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
      StringBuilder json = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null)
        json.append(line).append("\n"); 
      Gson gson = (new GsonBuilder()).create();
      return (MappingManager)gson.fromJson(json.toString(), MappingManager.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }
}
