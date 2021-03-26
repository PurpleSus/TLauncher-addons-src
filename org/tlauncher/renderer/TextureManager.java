package org.tlauncher.renderer;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.tlauncher.minecraft.GameTextureManager;
import org.tlauncher.minecraft.Resource;
import org.tlauncher.model.MinecraftProfileTextureDTO;
import org.tlauncher.model.Performance;
import org.tlauncher.model.PerformanceTextureData;
import org.tlauncher.model.PlayerName;
import org.tlauncher.model.PreparedTextureData;
import org.tlauncher.model.ProfileTexture;
import org.tlauncher.renderer.image.ImageWrap;
import org.tlauncher.renderer.texture.FramedTexture;
import org.tlauncher.renderer.texture.LightTexture;
import org.tlauncher.util.MinecraftUtils;
import org.tlauncher.util.PreparedProfileManager;

public class TextureManager {
  private static final long TIME_GUP = 2000L;
  
  private static int SKIN_COUNTER;
  
  private final Logger LOGGER = LogManager.getLogger(getClass());
  
  private final Map<PlayerName, ProfileTexture> textures = new HashMap<>();
  
  private PreparedProfileManager preparedProfileManager;
  
  private long updateTime;
  
  private long nextCleanTimeMills;
  
  private boolean calculatedElitraPerformance;
  
  private boolean calculatedCapePerformance;
  
  private final GameTextureManager gameTextureManager;
  
  public TextureManager(GameTextureManager gameTextureManager) {
    this.gameTextureManager = gameTextureManager;
  }
  
  private void cleanOldTextures() {
    if (this.nextCleanTimeMills < System.currentTimeMillis()) {
      long l = System.currentTimeMillis();
      List<PlayerName> removed = (List<PlayerName>)this.textures.entrySet().stream().filter(e -> (l > ((ProfileTexture)e.getValue()).getTime())).map(Map.Entry::getKey).collect(Collectors.toList());
      PlayerName n = new PlayerName(MinecraftUtils.getSessionUsername());
      removed.remove(n);
      for (PlayerName s : removed) {
        ProfileTexture profileTexture = this.textures.get(s);
        if (Objects.nonNull(profileTexture.getSkin())) {
          LightTexture texture = this.gameTextureManager.getTexture(profileTexture.getSkin());
          if (texture != null)
            texture.close(); 
          this.gameTextureManager.deleteTexture(profileTexture.getSkin());
        } 
        if (Objects.nonNull(profileTexture.getCape()))
          for (Resource resourceLocation : ((ProfileTexture)this.textures.get(s)).getCape().getFrames()) {
            LightTexture texture = this.gameTextureManager.getTexture(resourceLocation);
            if (texture != null)
              texture.close(); 
            this.gameTextureManager.deleteTexture(resourceLocation);
          }  
      } 
      removed.forEach(this.textures::remove);
      this.LOGGER.trace(String.format("removed %s , during %s", new Object[] { removed
              .stream().map(PlayerName::getName).collect(Collectors.joining(",")), 
              Long.valueOf(System.currentTimeMillis() - l) }));
      this.nextCleanTimeMills = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(15L);
    } 
  }
  
  public boolean isInit(PlayerName n, MinecraftProfileTexture.Type type) {
    ProfileTexture p = this.textures.get(n);
    if (Objects.isNull(p))
      return false; 
    updateCache(n);
    switch (type) {
      case SKIN:
        return true;
      case CAPE:
        return p.isCapeReady();
    } 
    return false;
  }
  
  private void updateCache(PlayerName p) {
    if (this.updateTime < System.currentTimeMillis()) {
      this.updateTime = System.currentTimeMillis() + 2000L;
      ProfileTexture profileTexture = this.textures.get(p);
      if (Objects.nonNull(profileTexture)) {
        this.LOGGER.debug("updated cache value " + p);
        profileTexture.updateTime();
      } 
    } 
  }
  
  public void createFramedTextures() {
    long l = System.currentTimeMillis();
    cleanOldTextures();
    PreparedTextureData preparedTextureData = this.preparedProfileManager.peek();
    if (Objects.isNull(preparedTextureData))
      return; 
    PlayerName playerName = preparedTextureData.getName();
    ProfileTexture profile = this.textures.get(playerName);
    try {
      ImageWrap imageWrap = (ImageWrap)preparedTextureData.getImages().get(MinecraftProfileTexture.Type.CAPE);
      if (Objects.isNull(profile)) {
        profile = new ProfileTexture();
        profile.updateTime();
        this.textures.put(playerName, profile);
        ImageWrap skin = (ImageWrap)preparedTextureData.getImages().get(MinecraftProfileTexture.Type.SKIN);
        Resource resourceLocation = null;
        if (Objects.nonNull(skin)) {
          resourceLocation = new Resource(String.format("dynamic/skin_%s", new Object[] { Integer.valueOf(SKIN_COUNTER++) }));
          LightTexture textureObject = new LightTexture(skin);
          this.gameTextureManager.loadTexture(resourceLocation, textureObject);
          Map<String, String> map = ((MinecraftProfileTextureDTO)preparedTextureData.getProfileTextureDTO().get(MinecraftProfileTexture.Type.SKIN)).getMetadata();
          if (Objects.nonNull(map))
            profile.setSkinType(map.get("model")); 
        } 
        profile.setSkin(resourceLocation);
        if (Objects.nonNull(imageWrap))
          return; 
      } 
      MinecraftProfileTextureDTO p = (MinecraftProfileTextureDTO)preparedTextureData.getProfileTextureDTO().get(MinecraftProfileTexture.Type.CAPE);
      if (Objects.nonNull(p) && !preparedTextureData.getCapeFrames().isEmpty())
        if (Objects.equals(p.getAnimatedCape(), Boolean.valueOf(true))) {
          FramedTexture cape = profile.getCape();
          if (Objects.isNull(cape)) {
            int fps = (p.getFps().intValue() > 0) ? p.getFps().intValue() : 1;
            cape = new FramedTexture(p.getCapeHeight().intValue(), 1000000000L / fps, true);
            profile.setCape(cape);
          } 
          if (!fillCape(preparedTextureData, cape))
            return; 
        } else {
          profile.setCape(FramedTexture.createOneFramedTexture(this.gameTextureManager, preparedTextureData.getCapeFrames().get(0)));
        }  
    } catch (Throwable e) {
      this.LOGGER.error(e);
      e.printStackTrace();
    } 
    if (profile.getSkinType() == null)
      profile.setSkinType("undefined"); 
    profile.setElytra(preparedTextureData.isElytra());
    preparedTextureData.setMaxTimeLoad(System.currentTimeMillis() - l);
    setReady(playerName, profile);
    this.preparedProfileManager.poll();
    String log = String.format("textures '%s' was added, skin: %s,cape: %s, max waiting: %s ,during : %s ", new Object[] { playerName
          .getDisplayName(), Boolean.valueOf(Objects.nonNull(profile.getSkin())), Boolean.valueOf(Objects.nonNull(profile.getCape())), 
          Long.valueOf(preparedTextureData.getMaxTimeLoad()), Long.valueOf(System.currentTimeMillis() - preparedTextureData.getInitTime()) });
    if (preparedTextureData.getMaxTimeLoad() > 15L) {
      this.LOGGER.info(log);
    } else {
      this.LOGGER.debug(log);
    } 
    defineProperPerformance(preparedTextureData);
  }
  
  private void defineProperPerformance(PreparedTextureData t) {
    if (!this.calculatedCapePerformance && t instanceof PerformanceTextureData && t
      .getName().getName().equals(PreparedProfileManager.CAPE_TEST_272)) {
      PerformanceTextureData t1 = (PerformanceTextureData)t;
      double time = t1.getMiddleInitFrameTime();
      if (time > PreparedProfileManager.MAX_TIME_LOAD_FRAMES)
        this.preparedProfileManager.setAnimatedCapePerformance(Performance.TEXTURE_136); 
      this.LOGGER.info("calculated cape performance:{}, middle time: {}", this.preparedProfileManager
          .getAnimatedCapePerformance(), Double.valueOf(time));
      this.calculatedCapePerformance = true;
    } else if (!this.calculatedElitraPerformance && t instanceof PerformanceTextureData && t
      .getName().getName().equals(PreparedProfileManager.ELITRA_TEST_272)) {
      PerformanceTextureData t1 = (PerformanceTextureData)t;
      double time = t1.getMiddleInitFrameTime();
      if (time > PreparedProfileManager.MAX_TIME_LOAD_FRAMES)
        this.preparedProfileManager.setAnimatedElitraPerformance(Performance.TEXTURE_136); 
      this.LOGGER.info("can use texture size:{}, middle time: {}", this.preparedProfileManager
          .getAnimatedElitraPerformance(), Double.valueOf(time));
      this.calculatedElitraPerformance = true;
    } 
  }
  
  private boolean fillCape(PreparedTextureData preparedTextureData, FramedTexture cape) {
    int size = preparedTextureData.getCapeFrames().size();
    long current = System.currentTimeMillis();
    for (int i = 1; i > 0 && cape.getFrames().size() < size; i--) {
      int index = cape.getFrames().size();
      cape.initByOneImage(this.gameTextureManager, preparedTextureData.getCapeFrames().get(index));
    } 
    long c = System.currentTimeMillis() - current;
    preparedTextureData.setMaxTimeLoad(c);
    if ((!this.calculatedCapePerformance || !this.calculatedElitraPerformance) && preparedTextureData instanceof PerformanceTextureData)
      ((PerformanceTextureData)preparedTextureData).getInitFrameTime().add(Long.valueOf(c)); 
    return (size == cape.getFrames().size());
  }
  
  private void setReady(PlayerName playerName, ProfileTexture profile) {
    profile.setCapeReady(true);
    this.preparedProfileManager.removeByName(playerName);
  }
  
  public void setPreparedProfileManager(PreparedProfileManager preparedProfileManager) {
    this.preparedProfileManager = preparedProfileManager;
  }
  
  public ProfileTexture get(PlayerName username) {
    return this.textures.get(username);
  }
}
