package org.mixin.O;

import cc.unitednetwork.live.shaoyu.LiveClient;
import cc.unitednetwork.live.shaoyu.netty.LiveProto;
import dev.ShaoYu.Naven.Fabric.Naven;
import dev.ShaoYu.Naven.Fabric.events.impl.EventServerSetPosition;
import dev.ShaoYu.Naven.Fabric.utils.HttpUtils;
import java.io.IOException;
import java.util.Map;

import dev.ShaoYu.Naven.Fabric.utils.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPacketListener.class})
public class MixinClientPacketListener {
   @Redirect(
      method = {"handleMovePlayer"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/Connection;send(Lnet/minecraft/network/protocol/Packet;)V",
         ordinal = 1
      )
   )
   public void onSendPacket(Connection instance, Packet<?> pPacket) {
      EventServerSetPosition event = new EventServerSetPosition(pPacket);
      Naven.getInstance().getEventManager().call(event);
      instance.send(event.getPacket());
   }

   @Inject(method = "handleLogin", at = @At("TAIL"))
   private void onLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
//      LiveClient.INSTANCE.sendPacket(LiveProto.createUpdateMinecraftProfile(
//              Minecraft.getInstance().player.getUUID(),
//              Minecraft.getInstance().player.getGameProfile().getName()
//      ));
   }

   @Inject(method = "handleRespawn", at = @At("TAIL"))
   private void onRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
//      LiveClient.INSTANCE.sendPacket(LiveProto.createUpdateMinecraftProfile(
//              Minecraft.getInstance().player.getUUID(),
//              Minecraft.getInstance().player.getGameProfile().getName()
//      ));
   }
}
