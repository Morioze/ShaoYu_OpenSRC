package com.heypixel.heypixelmod.obsoverlay.modules.impl.misc;

import com.heypixel.heypixelmod.obsoverlay.events.api.EventTarget;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventMouseClick;
import com.heypixel.heypixelmod.obsoverlay.modules.Category;
import com.heypixel.heypixelmod.obsoverlay.modules.Module;
import com.heypixel.heypixelmod.obsoverlay.modules.ModuleInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@ModuleInfo(
        name = "MidPearl",
        description = "Middle click to auto switch and throw ender pearl",
        category = Category.MISC
)
public class MidPearl extends Module {
    private boolean useOffhand = true;
    private boolean switchBack = true;

    @EventTarget
    public void onMouse(EventMouseClick e) {
        // e.isState() == true => release, false => press (see MixinMouseHandler)
        if (e.isState() || e.getKey() != 2) return; // only on middle button press
        if (mc.player == null || mc.gameMode == null) return;

        LocalPlayer player = mc.player;
        InteractionHand hand = findPearlHand();
        if (hand == null) return;

        int lastSlot = -1;
        if (hand == InteractionHand.MAIN_HAND) {
            int pearlSlot = findPearlSlot();
            if (pearlSlot == -1) return;
            lastSlot = player.getInventory().selected;
            player.getInventory().selected = pearlSlot;
        }

        player.swing(hand);
        mc.gameMode.useItem(player, hand);

        if (switchBack && lastSlot != -1) {
            player.getInventory().selected = lastSlot;
        }
    }

    private InteractionHand findPearlHand() {
        if (mc.player == null) return null;
        if (useOffhand && mc.player.getOffhandItem().getItem() == Items.ENDER_PEARL) {
            return InteractionHand.OFF_HAND;
        }
        if (mc.player.getMainHandItem().getItem() == Items.ENDER_PEARL) {
            return InteractionHand.MAIN_HAND;
        }
        // if can auto switch to hotbar slot
        return findPearlSlot() != -1 ? InteractionHand.MAIN_HAND : null;
    }

    private int findPearlSlot() {
        LocalPlayer player = mc.player;
        if (player == null) return -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() == Items.ENDER_PEARL) {
                return i;
            }
        }
        return -1;
    }
}
