package org.mixin.O;

import dev.ShaoYu.Naven.Fabric.Naven;
import dev.ShaoYu.Naven.Fabric.events.api.types.EventType;
import dev.ShaoYu.Naven.Fabric.events.impl.EventRenderScoreboard;
import dev.ShaoYu.Naven.Fabric.events.impl.EventSetTitle;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.HUD;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.NoRender;
import dev.ShaoYu.Naven.Fabric.modules.impl.render.Scoreboard;
import dev.ShaoYu.Naven.Fabric.ui.BetterHotBar;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import org.msgpack.core.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {Gui.class},
   priority = 100
)
public class MixinGui {

}
