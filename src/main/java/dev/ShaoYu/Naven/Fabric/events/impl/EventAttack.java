package com.heypixel.heypixelmod.obsoverlay.events.impl;

import com.heypixel.heypixelmod.obsoverlay.events.api.events.callables.EventCancellable;
import net.minecraft.world.entity.Entity;

public class EventAttack extends EventCancellable {
    private final Entity targetEntity;

    public EventAttack(Entity targetEntity) {
        this.targetEntity = targetEntity;
    }

    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
