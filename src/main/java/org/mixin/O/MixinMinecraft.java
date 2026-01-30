package org.mixin.O;

import cc.unitednetwork.live.shaoyu.LiveClient;

import cc.unitednetwork.live.shaoyu.netty.LiveProto;
import dev.ShaoYu.Naven.Fabric.Naven;
import dev.ShaoYu.Naven.Fabric.events.api.types.EventType;
import dev.ShaoYu.Naven.Fabric.events.impl.EventClick;
import dev.ShaoYu.Naven.Fabric.events.impl.EventRunTicks;
import dev.ShaoYu.Naven.Fabric.events.impl.EventShutdown;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.Glow;
import dev.ShaoYu.Naven.Fabric.utils.AnimationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.main.GameConfig;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Minecraft.class})
public abstract class MixinMinecraft {

}
