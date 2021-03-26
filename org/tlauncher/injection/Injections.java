package org.tlauncher.injection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.tlauncher.injection.mapping.MappingManager;
import org.tlauncher.injection.mapping.Mappings;
import org.tlauncher.injection.mapping.ObfClass;
import org.tlauncher.tweaker.Tweaker;
import org.tlauncher.util.TLModCfg;

public class Injections {
  void registerInjections(InjectionManager injectionManager) {
    Mappings mappings = MappingManager.instance().getMappings();
    ObfClass abstractClientPlayer = mappings.getClass("AbstractClientPlayer");
    ObfClass entityPlayer = mappings.getClass("EntityPlayer");
    String resourceLocation = mappings.getClass("ResourceLocation").getObfName();
    String locCapeMethodName = abstractClientPlayer.getMethod("getLocationCape").getObfName();
    String abstrPlayerClassName = abstractClientPlayer.getObfName();
    String locSkinMethodName = abstractClientPlayer.getMethod("getLocationSkin").getObfName();
    String skinTypeMethodName = abstractClientPlayer.getMethod("getSkinType").getObfName();
    String playerClassName = entityPlayer.getObfName();
    String wearingMethodName = entityPlayer.getMethod("isWearing").getObfName();
    String locElytraMethodName = abstractClientPlayer.getMethod("getLocationElytra").getObfName();
    String minecraftClassName = mappings.getClass("Minecraft").getObfName();
    String gameProfileClassName = entityPlayer.getMethod("getGameProfile").getObfName();
    String resourceLocationDesc = String.format("()L%s;", new Object[] { resourceLocation });
    String modelPartDesc = String.format("(L%s;)Z", new Object[] { mappings.getClass("EnumPlayerModelParts").getObfName() });
    if (Tweaker.isTLSkinCapeEnabled) {
      injectionManager.addInjection(abstrPlayerClassName, (bytes, injectionHelper) -> {
            List<InjectionResult> injectionResults = new ArrayList<>();
            injectionHelper.init(bytes).findMethod(locCapeMethodName, resourceLocationDesc).thenAccept(()).findMethod(locSkinMethodName, resourceLocationDesc).thenAccept(()).findMethod(locElytraMethodName, resourceLocationDesc).nonNull().thenAccept(()).findMethod(skinTypeMethodName, "()Ljava/lang/String;").thenAccept(());
            for (InjectionResult injectionResult : injectionResults) {
              if (injectionResult != InjectionResult.SUCCESS)
                return InjectionResult.FAILURE; 
            } 
            return InjectionResult.SUCCESS;
          });
      if (!Tweaker.useOldWearing) {
        ObfClass layerCape = mappings.getClass("LayerCape");
        injectionManager.addInjection(layerCape.getObfName(), (bytes, injectionHelper) -> (InjectionResult)injectionHelper.init(bytes).findMethod(layerCape.getMethod("doRenderLayer").getObfName(), Desc.builder().contains(abstrPlayerClassName).endsWith("FFFFFF)V").build()).<InjectionResult>thenApply(()));
      } else {
        injectionManager.addInjection(playerClassName, (bytes, injectionHelper) -> (InjectionResult)injectionHelper.init(bytes).findMethod(wearingMethodName, modelPartDesc).<InjectionResult>thenApply(()));
      } 
      ObfClass guiMainMenu = mappings.getClass("GuiMainMenu");
      injectionManager.addInjection(guiMainMenu.getObfName(), (bytes, injectionHelper) -> {
            String descriptor = TLModCfg.getMinecraftVersion().startsWith("1.16") ? String.format("(L%s;IIF)V", new Object[] { mappings.getClass("MatrixStack").getObfName() }) : "(IIF)V";
            return injectionHelper.init(bytes).findMethod(guiMainMenu.getMethod("render").getObfName(), descriptor).<InjectionResult>thenApply(());
          });
      injectionManager.addInjection("net.minecraft.client.main.Main", (bytes, injectionHelper) -> (InjectionResult)injectionHelper.init(bytes).findMethod("main", "([Ljava/lang/String;)V").<InjectionResult>thenApply(()));
      injectionManager.addInjection(minecraftClassName, (bytes, injectionHelper) -> {
            injectionHelper.init(bytes).findMethod("<init>").forEach(());
            return InjectionResult.SUCCESS;
          });
    } 
  }
}
