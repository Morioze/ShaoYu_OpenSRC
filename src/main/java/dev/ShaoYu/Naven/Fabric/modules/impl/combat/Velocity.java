package com.heypixel.heypixelmod.obsoverlay.modules.impl.combat;

import com.heypixel.heypixelmod.obsoverlay.events.api.EventTarget;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventHandlePacket;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventRespawn;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventRunTicks;
import com.heypixel.heypixelmod.obsoverlay.modules.Category;
import com.heypixel.heypixelmod.obsoverlay.modules.Module;
import com.heypixel.heypixelmod.obsoverlay.modules.ModuleInfo;
import com.heypixel.heypixelmod.obsoverlay.values.ValueBuilder;
import com.heypixel.heypixelmod.obsoverlay.values.impl.FloatValue;
import com.heypixel.heypixelmod.obsoverlay.values.impl.ModeValue;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.util.RandomSource;

@ModuleInfo(
        name = "Velocity",
        description = "Reduces knockback.",
        category = Category.COMBAT
)
public class Velocity extends Module {
   public ModeValue mode = ValueBuilder.create(this, "Mode")
           .setDefaultModeIndex(0)
           .setModes("JumpReset")
           .build()
           .getModeValue();
   public FloatValue jumpChance = ValueBuilder.create(this, "Change")
           .setDefaultFloatValue(100.0F)
           .setFloatStep(1.0F)
           .setMinFloatValue(0.0F)
           .setMaxFloatValue(100.0F)
           .setVisibility(() -> mode.isCurrentMode("JumpReset")) // 仅在 JumpReset 模式显示
           .build()
           .getFloatValue();
   private int jumpTimer = 0;
   private boolean shouldJump = false;
   private boolean isJumping = false;
   private int cooldownTicks = 0;
   private boolean jrForwardStarted = false;
   @Override
   public void onEnable() {
      jumpTimer = 0;
      shouldJump = false;
      isJumping = false;
      cooldownTicks = 0;
      jrForwardStarted = false;
   }

   @Override
   public void onDisable() {
      jumpTimer = 0;
      shouldJump = false;
      isJumping = false;
      cooldownTicks = 0;
      if (mc != null && mc.options != null) {
         try {
            mc.options.keyJump.setDown(false);
            if (jrForwardStarted) mc.options.keyUp.setDown(false);
         } catch (Throwable ignored) {}
      }
      jrForwardStarted = false;
   }

   @EventTarget
   public void onWorld(EventRespawn eventRespawn) {
      jumpTimer = 0;
      shouldJump = false;
      isJumping = false;
      cooldownTicks = 0;
      if (mc != null && mc.options != null) {
         try {
            mc.options.keyJump.setDown(false);
            if (jrForwardStarted) mc.options.keyUp.setDown(false);
         } catch (Throwable ignored) {}
      }
      jrForwardStarted = false;
   }
   @EventTarget
   public void onTick(EventRunTicks eventRunTicks) {
      if (mc.player == null) return;
      if (mode.isCurrentMode("JumpReset")) {
         int ht = mc.player.hurtTime;
         if (ht >= 8) {
            try { mc.options.keyJump.setDown(true); } catch (Throwable ignored) {}
            if (ht >= 7 && !mc.options.keyUp.isDown()) {
               try { mc.options.keyUp.setDown(true); } catch (Throwable ignored) {}
               jrForwardStarted = true;
            }
         }
         else if (ht >= 7) {
            if (!mc.options.keyUp.isDown()) {
               try { mc.options.keyUp.setDown(true); } catch (Throwable ignored) {}
               jrForwardStarted = true;
            }
         }
         if (ht < 7 && ht > 0) {
            try { mc.options.keyJump.setDown(false); } catch (Throwable ignored) {}
            if (jrForwardStarted) {
               try { mc.options.keyUp.setDown(false); } catch (Throwable ignored) {}
               jrForwardStarted = false;
            }
         }
         return;
      }
      if (cooldownTicks > 0) cooldownTicks--;
      if (mc.player.onGround()) isJumping = false;
      if (jumpTimer > 0 && !isJumping && cooldownTicks == 0) {
         jumpTimer--;
         if (jumpTimer == 0 && shouldJump) {
            mc.player.jumpFromGround();
            isJumping = true;
            shouldJump = false;
            cooldownTicks = 5;
         }
      }
   }
   @EventTarget
   public void onPacket(EventHandlePacket e) {
      if (mc.player == null || mc.getConnection() == null || mc.gameMode == null || mc.player.isUsingItem()) return;
      if (mode.isCurrentMode("JumpReset")) {
         return;
      }
      Packet<?> packet = e.getPacket();
      if (packet instanceof ClientboundSetEntityMotionPacket velocity) {
         if (velocity.getId() != mc.player.getId()) return;
         if (mc.player.onGround() && RandomSource.create().nextFloat() * 100 <= jumpChance.getCurrentValue()) {
            mc.player.jumpFromGround();
         }
      }
   }
}