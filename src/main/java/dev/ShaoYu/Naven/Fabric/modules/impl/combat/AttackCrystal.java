package com.heypixel.heypixelmod.obsoverlay.modules.impl.combat;

import com.heypixel.heypixelmod.obsoverlay.events.api.EventTarget;
import com.heypixel.heypixelmod.obsoverlay.events.api.types.EventType;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventClick;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventPacket;
import com.heypixel.heypixelmod.obsoverlay.events.impl.EventRunTicks;
import com.heypixel.heypixelmod.obsoverlay.modules.Category;
import com.heypixel.heypixelmod.obsoverlay.modules.Module;
import com.heypixel.heypixelmod.obsoverlay.modules.ModuleInfo;
import com.heypixel.heypixelmod.obsoverlay.utils.PacketUtils;
import com.heypixel.heypixelmod.obsoverlay.utils.Vector2f;
import com.heypixel.heypixelmod.obsoverlay.utils.rotation.RotationManager;
import com.heypixel.heypixelmod.obsoverlay.utils.rotation.RotationUtils;
import com.heypixel.heypixelmod.obsoverlay.values.ValueBuilder;
import com.heypixel.heypixelmod.obsoverlay.values.impl.BooleanValue;
import com.heypixel.heypixelmod.obsoverlay.values.impl.FloatValue;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.EndCrystalItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

@ModuleInfo(
        name = "CrystalAura",
        category = Category.COMBAT,
        description = "Automatically attacks end crystals"
)
public class AttackCrystal extends Module {
   public static Vector2f rotations;
   private Entity entity;
   BooleanValue packet = ValueBuilder.create(this, "Attack on Packet (Danger)").setDefaultBooleanValue(false).build().getBooleanValue();

   // AutoPlace 设置
   public BooleanValue autoPlace = ValueBuilder.create(this, "AutoPlace").setDefaultBooleanValue(true).build().getBooleanValue();
   public FloatValue placeRange = ValueBuilder.create(this, "Place Range").setDefaultFloatValue(4.5F).setFloatStep(0.5F).setMinFloatValue(2.0F).setMaxFloatValue(6.0F).build().getFloatValue();
   public FloatValue minTargetDamage = ValueBuilder.create(this, "Min Target Dmg").setDefaultFloatValue(6.0F).setFloatStep(0.5F).setMinFloatValue(0.0F).setMaxFloatValue(20.0F).build().getFloatValue();
   public FloatValue maxSelfDamage = ValueBuilder.create(this, "Max Self Dmg").setDefaultFloatValue(6.0F).setFloatStep(0.5F).setMinFloatValue(0.0F).setMaxFloatValue(20.0F).build().getFloatValue();
   public BooleanValue rotateOnPlace = ValueBuilder.create(this, "Rotate On Place").setDefaultBooleanValue(true).build().getBooleanValue();
   public BooleanValue silentRotations = ValueBuilder.create(this, "Silent Rotations")
           .setVisibility(this.rotateOnPlace::getCurrentValue)
           .setDefaultBooleanValue(true)
           .build()
           .getBooleanValue();
   public BooleanValue autoSwitch = ValueBuilder.create(this, "AutoSwitch").setDefaultBooleanValue(true).build().getBooleanValue();

   @EventTarget
   public void onPacket(EventPacket e) {
      if (e.getType() == EventType.RECEIVE && e.getPacket() instanceof ClientboundAddEntityPacket && this.packet.getCurrentValue()) {
         ClientboundAddEntityPacket packet = (ClientboundAddEntityPacket)e.getPacket();
         if (packet.getType() == EntityType.END_CRYSTAL) {
            EndCrystal pTarget = new EndCrystal(mc.level, packet.getX(), packet.getY(), packet.getZ());
            pTarget.setId(packet.getId());
            if (mc.player.distanceTo(pTarget) <= 4.0F) {
               Vector2f rotations = RotationUtils.getRotations(pTarget);
               mc.getConnection()
                       .send(new PosRot(mc.player.getX(), mc.player.getY(), mc.player.getZ(), rotations.getX(), rotations.getY(), mc.player.onGround()));
               PacketUtils.sendSequencedPacket(id -> new ServerboundUseItemPacket(InteractionHand.MAIN_HAND, id));
               float currentYaw = mc.player.getYRot();
               float currentPitch = mc.player.getXRot();
               mc.player.setYRot(RotationManager.rotations.x);
               mc.player.setXRot(RotationManager.rotations.y);
               mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(pTarget, false));
               mc.player.swing(InteractionHand.MAIN_HAND);
               mc.player.setYRot(currentYaw);
               mc.player.setXRot(currentPitch);
            }
         }
      }
   }

   @EventTarget
   public void onEarlyTick(EventRunTicks e) {
      if (e.getType() == EventType.PRE && mc.player != null && mc.level != null) {
         Optional<Entity> any = StreamSupport.<Entity>stream(mc.level.entitiesForRendering().spliterator(), true)
                 .filter(entityx -> entityx instanceof EndCrystal)
                 .findAny();
         rotations = null;
         if (any.isPresent()) {
            Entity entity = any.get();
            Vector2f rots = RotationUtils.getRotations(entity);
            double minDistance = RotationUtils.getMinDistance(entity, rots);
            if (minDistance <= 3.0) {
               rotations = rots;
               this.entity = entity;
            }
         }

         // AutoPlace：自动放置末影水晶
         if (autoPlace.getCurrentValue()) {
            autoPlace();
         }
      }
   }

   @EventTarget
   public void onClick(EventClick e) {
      if (this.entity != null) {
         float currentYaw = mc.player.getYRot();
         float currentPitch = mc.player.getXRot();
         mc.player.setYRot(rotations.x);
         mc.player.setXRot(rotations.y);
         mc.getConnection().send(ServerboundInteractPacket.createAttackPacket(this.entity, false));
         mc.player.swing(InteractionHand.MAIN_HAND);
         mc.player.setYRot(currentYaw);
         mc.player.setXRot(currentPitch);
         this.entity = null;
      }
   }

   // =============== AutoPlace 实现 ===============
   private void autoPlace() {
      if (mc.player == null || mc.level == null || mc.gameMode == null) return;

      // 找到最近的敌对玩家（非自己，存活，且不是假人/机器人）
      Player target = mc.level.players().stream()
              .filter(p -> p != mc.player && p.isAlive())
              .filter(p -> !AntiBots.isBot(p) && !AntiBots.isBedWarsBot(p))
              .min((a, b) -> Float.compare(mc.player.distanceTo(a), mc.player.distanceTo(b)))
              .orElse(null);
      if (target == null) return;

      // 确保手上或快捷栏有末影水晶
      int crystalSlot = findCrystalInHotbar();
      if (crystalSlot == -1) return;

      // 搜索最佳放置位置
      BestPos best = findBestPlacePos(target, placeRange.getCurrentValue());
      if (best == null) return;

      // 安全性检查
      if (best.targetDamage < minTargetDamage.getCurrentValue()) return;
      if (best.selfDamage > maxSelfDamage.getCurrentValue()) return;
      if (best.selfDamage >= mc.player.getHealth() + mc.player.getAbsorptionAmount() - 1.0F) return;

      // 切换到末影水晶（仅当 AutoSwitch 开启时）
      if (mc.player.getInventory().selected != crystalSlot) {
         if (autoSwitch.getCurrentValue()) {
            mc.player.getInventory().selected = crystalSlot;
         } else {
            // 未开启自动切换且当前未选中水晶，跳过放置
            return;
         }
      }

      // 旋转并放置
      Vec3 hit = Vec3.atCenterOf(best.basePos).add(0, 0.5, 0);
      if (rotateOnPlace.getCurrentValue()) {
         Vector2f rot = RotationUtils.getRotationsVector(hit);
         if (silentRotations.getCurrentValue()) {
            // 静默旋转：只向服务器发送旋转，不改变客户端视角
            if (mc.getConnection() != null) {
               mc.getConnection().send(new PosRot(
                       mc.player.getX(),
                       mc.player.getY(),
                       mc.player.getZ(),
                       rot.getX(),
                       rot.getY(),
                       mc.player.onGround()
               ));
            }
         } else {
            // 非静默：直接设置客户端视角
            mc.player.setYRot(rot.getX());
            mc.player.setXRot(rot.getY());
         }
      }

      BlockHitResult bhr = new BlockHitResult(hit, Direction.UP, best.basePos, false);
      mc.gameMode.useItemOn(mc.player, InteractionHand.MAIN_HAND, bhr);
   }

   private int findCrystalInHotbar() {
      for (int i = 0; i < 9; i++) {
         if (mc.player.getInventory().getItem(i).getItem() instanceof EndCrystalItem) {
            return i;
         }
      }
      return -1;
   }

   private BestPos findBestPlacePos(Player target, float range) {
      BlockPos playerPos = mc.player.blockPosition();
      BlockPos targetPos = target.blockPosition();
      BestPos best = null;

      int r = (int)Math.ceil(range);
      for (int dx = -r; dx <= r; dx++) {
         for (int dz = -r; dz <= r; dz++) {
            for (int dy = -1; dy <= 2; dy++) {
               BlockPos base = targetPos.offset(dx, dy, dz);
               if (!isValidBase(base)) continue;
               if (mc.player.blockPosition().distSqr(base) > (double)(range * range)) continue;

               // 爆炸点近似：基座上一格的中心
               Vec3 explode = Vec3.atCenterOf(base).add(0, 1.0, 0);

               float tDmg = estimateDamage(explode, target);
               float sDmg = estimateDamage(explode, mc.player);

               if (best == null || (tDmg - sDmg * 0.7F) > (best.targetDamage - best.selfDamage * 0.7F)) {
                  best = new BestPos(base, tDmg, sDmg);
               }
            }
         }
      }
      return best;
   }

   private boolean isValidBase(BlockPos base) {
      // 任何方块都可作为基座：仅要求上方两格为空气，避免与方块/实体碰撞
      BlockPos up1 = base.above();
      BlockPos up2 = up1.above();
      return mc.level.isEmptyBlock(up1) && mc.level.isEmptyBlock(up2);
   }

   // 粗略伤害估计：随距离线性衰减，最大约 12，简化避免复杂计算/穿透判断
   private float estimateDamage(Vec3 explode, Player target) {
      double dist = explode.distanceTo(target.position());
      double maxRadius = 6.0; // 末影水晶爆炸半径近似
      if (dist > maxRadius) return 0.0F;
      double factor = 1.0 - dist / maxRadius;
      double maxDmg = 12.0; // 简化最大伤害
      float dmg = (float)(maxDmg * factor);
      // 盔甲/抗性等未计算，作为保守估计
      return Math.max(0.0F, dmg);
   }

   private static class BestPos {
      final BlockPos basePos;
      final float targetDamage;
      final float selfDamage;
      BestPos(BlockPos pos, float t, float s) { this.basePos = pos; this.targetDamage = t; this.selfDamage = s; }
   }
}