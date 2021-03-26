package org.tlauncher.util;

import com.google.common.collect.Iterables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tlauncher.model.MinecraftProfileTextureDTO;
import org.tlauncher.model.MinecraftTexturesPayload1;
import org.tlauncher.model.Performance;
import org.tlauncher.model.PerformanceTextureData;
import org.tlauncher.model.PlayerName;
import org.tlauncher.model.PreparedTextureData;
import org.tlauncher.renderer.image.ImageWrap;
import org.tlauncher.renderer.image.ImageWrapFactory;

public class PreparedProfileManager {
  private static final Logger LOGGER = LogManager.getLogger();
  
  public static String ELITRA_TEST_272 = "elitra_test_272";
  
  public static String CAPE_TEST_272 = "cape_test_272";
  
  private final ExecutorService pool = Executors.newFixedThreadPool(1, (new ThreadFactoryBuilder()).setDaemon(true).build());
  
  private final Set<PlayerName> addedNames = new HashSet<>();
  
  private final LinkedBlockingQueue<PreparedTextureData> preparedData = new LinkedBlockingQueue<>();
  
  public static double MAX_TIME_LOAD_FRAMES = 10.0D;
  
  private Performance animatedElitraPerformance = Performance.TEXTURE_272;
  
  private Performance animatedCapePerformance = Performance.TEXTURE_272;
  
  public PreparedProfileManager() {
    addPerformanceTexture();
  }
  
  private void addPerformanceTexture() {
    try {
      addTestTexture(false, CAPE_TEST_272);
      addTestTexture(true, ELITRA_TEST_272);
    } catch (Exception t) {
      LOGGER.error("couldn't calculate size of the cape", t);
    } 
  }
  
  private void addTestTexture(boolean elitraCape, String name) {
    PerformanceTextureData performanceTextureData = new PerformanceTextureData();
    final MinecraftProfileTextureDTO test = new MinecraftProfileTextureDTO();
    performanceTextureData.setName(new PlayerName(name));
    performanceTextureData.setElytra(elitraCape);
    test.setAnimatedElytra(Boolean.valueOf(elitraCape));
    test.setCapeHeight(Integer.valueOf(272));
    test.setAnimatedCape(Boolean.valueOf(true));
    test.setFps(Integer.valueOf(10));
    final ImageWrap nativeImage = ImageWrapFactory.create(PreparedProfileManager.class
        .getResourceAsStream(name + ".png"));
    performanceTextureData.setImages(new HashMap<MinecraftProfileTexture.Type, ImageWrap>() {
        
        });
    performanceTextureData.setProfileTextureDTO(new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO>() {
        
        });
    preparedFramesForCape((PreparedTextureData)performanceTextureData);
    this.preparedData.add(performanceTextureData);
  }
  
  private void fillGameProfileData(PlayerName name, GameProfile profile) {
    Property textureProperty = (Property)Iterables.getFirst(profile.getProperties().get("textures"), null);
    if (textureProperty == null)
      return; 
    try {
      String json = new String(Base64.decodeBase64(textureProperty.getValue()), StandardCharsets.UTF_8);
      Gson g = new Gson();
      MinecraftTexturesPayload1 result = (MinecraftTexturesPayload1)g.fromJson(json, MinecraftTexturesPayload1.class);
      name.fill(result);
    } catch (JsonParseException e) {
      LOGGER.warn("Could not decode textures payload", (Throwable)e);
    } 
  }
  
  public void addNewName(PlayerName p) {
    if (!this.addedNames.contains(p)) {
      this.addedNames.add(p);
      loadByName(p);
    } 
  }
  
  private void loadByName(PlayerName p) {
    CompletableFuture.runAsync(() -> {
          PreparedTextureData texture = new PreparedTextureData();
          texture.setName(p);
          try {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> profile = loadProfileData0(p);
            fillIfEmptyWithNativeGameProfileTextures(profile, p);
            texture.setProfileTextureDTO(profile);
            downloadAndValidateImage(profile, texture);
            preparedFramesForCape(texture);
            preparedFrameForSkin(texture);
            LOGGER.debug("finished downloading data " + p.getName() + " " + p.getDisplayName());
          } catch (Exception io) {
            LOGGER.warn("error " + p.toString(), io);
          } 
          this.preparedData.add(texture);
        }this.pool);
  }
  
  private void fillIfEmptyWithNativeGameProfileTextures(Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> profile, PlayerName p) {
    if (profile.isEmpty() && Objects.nonNull(p.getNativeTextures()))
      p.getNativeTextures().forEach((k, v) -> {
            MinecraftProfileTextureDTO m = new MinecraftProfileTextureDTO();
            m.setUrl(v.getUrl());
            profile.put(k, m);
          }); 
  }
  
  private void preparedFrameForSkin(PreparedTextureData texture) {
    ImageWrap imageWrap = (ImageWrap)texture.getImages().get(MinecraftProfileTexture.Type.SKIN);
    if (Objects.nonNull(imageWrap))
      texture.setSkin(imageWrap); 
  }
  
  private void preparedFramesForCape(PreparedTextureData texture) {
    MinecraftProfileTextureDTO p = (MinecraftProfileTextureDTO)texture.getProfileTextureDTO().get(MinecraftProfileTexture.Type.CAPE);
    if (Objects.isNull(p) || Objects.isNull(texture.getImages().get(MinecraftProfileTexture.Type.CAPE)))
      return; 
    if (!Objects.equals(p.getAnimatedCape(), Boolean.valueOf(true))) {
      ImageWrap imageWrap = (ImageWrap)texture.getImages().get(MinecraftProfileTexture.Type.CAPE);
      p.setCapeWidth(Integer.valueOf(imageWrap.getWidth()));
      p.setCapeHeight(Integer.valueOf(imageWrap.getHeight()));
      texture.getCapeFrames().add(imageWrap);
      return;
    } 
    for (ImageWrap imageWrap : cutAnimatedCape(texture, p))
      texture.getCapeFrames().add(imageWrap); 
  }
  
  private List<ImageWrap> cutAnimatedCape(PreparedTextureData texture, MinecraftProfileTextureDTO p) {
    ImageWrap imageWrap = (ImageWrap)texture.getImages().get(MinecraftProfileTexture.Type.CAPE);
    try {
      boolean animatedElytra = (Objects.nonNull(p.getAnimatedElytra()) && p.getAnimatedElytra().booleanValue());
      int frameHeight = animatedElytra ? (p.getCapeHeight().intValue() / 17 * 32) : p.getCapeHeight().intValue();
      int frameWidth = animatedElytra ? (p.getCapeHeight().intValue() / 17 * 64) : (p.getCapeHeight().intValue() / 17 * 22);
      p.setCapeWidth(Integer.valueOf(frameWidth));
      int xFrames = imageWrap.getWidth() / frameWidth;
      int yFrames = imageWrap.getHeight() / frameHeight;
      texture.setElytra(animatedElytra);
      List<ImageWrap> list = new ArrayList<>();
      for (int j = 0; j < xFrames; j++) {
        for (int i = 0; i < yFrames; i++) {
          ImageWrap imageWrap1;
          if (animatedElytra) {
            imageWrap1 = ImageWrapFactory.create(frameWidth, frameHeight);
          } else {
            imageWrap1 = ImageWrapFactory.create(frameWidth / 22 * 64, frameHeight / 17 * 32);
          } 
          boolean flag = false;
          for (int y = 0; y < frameHeight; y++) {
            for (int x = 0; x < frameWidth; x++) {
              int color = -1;
              try {
                color = imageWrap.getRGB(j * frameWidth + x, i * frameHeight + y);
                if (!flag) {
                  int alpha = color >>> 24 & 0xFF;
                  if (alpha != 0)
                    flag = true; 
                } 
                imageWrap1.setRGB(x, y, color);
              } catch (ArrayIndexOutOfBoundsException e) {
                LOGGER.error("Coordinates: x " + x + " y " + y + " color " + color, e);
              } 
            } 
          } 
          if (!flag)
            return list; 
          list.add(imageWrap1);
        } 
      } 
      return list;
    } finally {
      imageWrap.close();
    } 
  }
  
  private void downloadAndValidateImage(Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> profile, PreparedTextureData texture) throws IOException {
    for (Map.Entry<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> entry : profile.entrySet()) {
      BufferedImage image = getImage(entry);
      if (((MinecraftProfileTexture.Type)entry.getKey()).equals(MinecraftProfileTexture.Type.SKIN))
        image = TextureUtils.getRightSkin(image); 
      if (image == null)
        continue; 
      boolean isCape = ((MinecraftProfileTexture.Type)entry.getKey()).equals(MinecraftProfileTexture.Type.CAPE);
      if (isCape && !texture.isElytra() && (image.getWidth() % 22 != 0 || image.getHeight() % 17 != 0) && (image
        .getWidth() % 64 != 0 || image.getHeight() % 32 != 0)) {
        LOGGER.warn("not proper cape should multiple 22x17 or 64x32, w={}, h={}", Integer.valueOf(image.getWidth()), Integer.valueOf(image.getHeight()));
        continue;
      } 
      if (isCape && ((((MinecraftProfileTextureDTO)entry.getValue()).getAnimatedElytra().booleanValue() && this.animatedElitraPerformance.equals(Performance.TEXTURE_136)) || (((MinecraftProfileTextureDTO)entry
        .getValue()).getAnimatedCape().booleanValue() && this.animatedCapePerformance.equals(Performance.TEXTURE_136)))) {
        MinecraftProfileTextureDTO texture1 = profile.get(MinecraftProfileTexture.Type.CAPE);
        if (Objects.equals(texture1.getAnimatedCape(), Boolean.valueOf(true)) && texture1.getCapeHeight().intValue() > 200) {
          int resizeValue = 2;
          texture1.setCapeHeight(Integer.valueOf(texture1.getCapeHeight().intValue() / resizeValue));
          image = TextureUtils.resize(image, image.getWidth() / resizeValue, image.getHeight() / resizeValue);
          LOGGER.debug("decreased animated texture {}", texture.getName());
        } 
      } 
      ImageWrap iImageWrap = ImageWrapFactory.create(image);
      texture.getImages().put(entry.getKey(), iImageWrap);
    } 
  }
  
  private BufferedImage getImage(Map.Entry<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> entry) throws IOException {
    IOException exception = new IOException("can't load image");
    for (int i = 0; i < 3; i++)
      HttpURLConnection c = null; 
    throw exception;
  }
  
  private Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> loadProfileData0(PlayerName playerName) throws IOException {
    IOException exception = new IOException("can't download model");
    for (int i = 0; i < 3; i++) {
      try {
        return loadProfileData(playerName.getName());
      } catch (IOException e) {
        exception = e;
        try {
          Thread.sleep(2000L);
        } catch (InterruptedException interruptedException) {}
      } 
    } 
    throw exception;
  }
  
  public void addNewName(GameProfile p, PlayerName name) {
    if (!this.addedNames.contains(name)) {
      this.addedNames.add(name);
      fillGameProfileData(name, p);
      loadByName(name);
    } 
  }
  
  private Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO> loadProfileData(String playerName) throws IOException {
    Gson gson = new Gson();
    if (Objects.nonNull(System.getProperty("memoryLeakTest")))
      playerName = "rob6"; 
    playerName = URLEncoder.encode(playerName, "UTF-8");
    URL url = new URL(String.format("http://auth.tlauncher.org/skin/profile/texture/login/%s", new Object[] { playerName }));
    URLConnection urlConnection = url.openConnection();
    urlConnection.setConnectTimeout(60000);
    urlConnection.setReadTimeout(60000);
    StringWriter writer = new StringWriter();
    try (InputStream inputStream = urlConnection.getInputStream()) {
      IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
    } 
    String response = writer.toString();
    return (Map<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO>)gson.fromJson(response, (new TypeToken<HashMap<MinecraftProfileTexture.Type, MinecraftProfileTextureDTO>>() {
        
        }).getType());
  }
  
  public PreparedTextureData peek() {
    return this.preparedData.peek();
  }
  
  public void poll() {
    this.preparedData.poll();
  }
  
  public void removeByName(PlayerName name) {
    this.addedNames.remove(name);
  }
  
  public Performance getAnimatedElitraPerformance() {
    return this.animatedElitraPerformance;
  }
  
  public void setAnimatedElitraPerformance(Performance animatedElitraPerformance) {
    this.animatedElitraPerformance = animatedElitraPerformance;
  }
  
  public Performance getAnimatedCapePerformance() {
    return this.animatedCapePerformance;
  }
  
  public void setAnimatedCapePerformance(Performance animatedCapePerformance) {
    this.animatedCapePerformance = animatedCapePerformance;
  }
}
