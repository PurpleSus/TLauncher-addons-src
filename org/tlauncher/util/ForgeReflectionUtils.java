package org.tlauncher.util;

import java.lang.reflect.Field;
import java.util.AbstractList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class ForgeReflectionUtils {
  private static AbstractList<ItemStack> armorInventoryList;
  
  private ForgeReflectionUtils() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
  
  public static boolean isElytraEquipped(int slotIndex, boolean forceUpdate) {
    try {
      if (armorInventoryList == null || forceUpdate)
        try {
          return ((ItemStack)(Minecraft.func_71410_x()).field_71439_g.field_71071_by.field_70460_b.get(slotIndex)).func_77973_b() instanceof net.minecraft.item.ItemElytra;
        } catch (NoSuchFieldError e) {
          for (Field declaredField : Minecraft.class.getDeclaredFields()) {
            if (Entity.class.isAssignableFrom(declaredField.getType())) {
              Class<?> playerClass = declaredField.getType();
              for (Field declaredField1 : playerClass.getSuperclass().getSuperclass().getDeclaredFields()) {
                if (IInventory.class.isAssignableFrom(declaredField1.getType())) {
                  for (Field declaredField2 : declaredField1.getType().getDeclaredFields()) {
                    if (AbstractList.class.isAssignableFrom(declaredField2.getType())) {
                      AbstractList<ItemStack> list = (AbstractList<ItemStack>)declaredField2.get(declaredField1.get(declaredField.get(Minecraft.func_71410_x())));
                      if (list.size() == 4) {
                        armorInventoryList = list;
                        break;
                      } 
                    } 
                  } 
                  break;
                } 
              } 
              break;
            } 
          } 
        }  
      if (armorInventoryList != null) {
        String name = ((ItemStack)armorInventoryList.get(slotIndex)).func_77973_b().getClass().getName();
        return (name.endsWith("ItemElytra") || name.endsWith("ElytraItem"));
      } 
      return false;
    } catch (Throwable $ex) {
      throw $ex;
    } 
  }
}
