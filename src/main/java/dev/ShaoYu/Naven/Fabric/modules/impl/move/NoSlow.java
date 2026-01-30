package com.heypixel.heypixelmod.obsoverlay.modules.impl.move;

import com.heypixel.heypixelmod.obsoverlay.events.api.EventTarget;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventSlowdown;
import com.heypixel.heypixelmod.obsoverlay.modules.Category;
import com.heypixel.heypixelmod.obsoverlay.modules.Module;
import com.heypixel.heypixelmod.obsoverlay.modules.ModuleInfo;

@ModuleInfo(
   name = "NoSlow",
   description = "NoSlowDown",
   category = Category.MOVEMENT
)
public class NoSlow extends Module {
   // 移除 StartSlow 相关功能，保留基础防减速逻辑

   @EventTarget
   public void onSlow(EventSlowdown eventSlowdown) {
      if (mc == null || mc.player == null) return;
      // 基础防减速逻辑
      if (mc.player.getUseItemRemainingTicks() % 2 != 0 && mc.player.getUseItemRemainingTicks() <= 30) {
         eventSlowdown.setSlowdown(false);
         mc.player.setSprinting(true);
      }
   }
}
