package org.mixin.O;

import dev.ShaoYu.Naven.Fabric.Naven;
import dev.ShaoYu.Naven.Fabric.events.impl.EventRender;
import dev.ShaoYu.Naven.Fabric.events.impl.EventRender2D;
import dev.ShaoYu.Naven.Fabric.events.impl.EventRenderAfterWorld;
import dev.ShaoYu.Naven.Fabric.events.impl.EventRenderSkia;
import dev.ShaoYu.Naven.Fabric.events.impl.sb.EventSB;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.DynamicIsland;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.FullBright;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.MotionBlur;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.NoHurtCam;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.ShaoYu.Naven.Fabric.utils.renderer.skia.Skia;
import dev.ShaoYu.Naven.Fabric.utils.renderer.skia.context.SkiaContext;
import dev.ShaoYu.Naven.Fabric.utils.renderer.skia.shader.impl.KawaseBlur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameRenderer.class})
public class MixinGameRenderer {

}
