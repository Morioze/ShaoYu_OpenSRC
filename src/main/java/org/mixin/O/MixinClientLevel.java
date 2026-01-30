package org.mixin.O;

import dev.ShaoYu.Naven.Fabric.Naven;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientLevel.class})
public class MixinClientLevel {

   @Inject(method = "tickNonPassenger", at = @At("HEAD"), cancellable = true)
   private void onTickNonPassenger(Entity entity, CallbackInfo ci) {
      if (skipTicks(entity)) {
         ci.cancel();
      }
   }

   private static boolean skipTicks(Entity entity) {
      Minecraft mc = Minecraft.getInstance();
      if (mc.player != null && entity == mc.player && Naven.skipTicks > 0) {
         Naven.skipTicks--;
         return true;
      }
      return false;
   }
   @Redirect(
      method = {"tickNonPassenger"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;tick()V"
      )
   )
   public void hookSkipTicks(Entity instance) {
      if (!Naven.skipTasks.isEmpty() && instance == Minecraft.getInstance().player) {
         Runnable task = Naven.skipTasks.poll();
         if (task != null) {
            task.run();
         }
      } else {
         instance.tick();
      }
   }
}
