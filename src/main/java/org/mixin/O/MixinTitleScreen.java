package org.mixin.O;

import dev.ShaoYu.Naven.Fabric.ui.MainUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        Minecraft.getInstance().execute(() -> {
            if (Minecraft.getInstance().screen instanceof TitleScreen) {
                Minecraft.getInstance().setScreen(new MainUI());
            }
        });
    }

}