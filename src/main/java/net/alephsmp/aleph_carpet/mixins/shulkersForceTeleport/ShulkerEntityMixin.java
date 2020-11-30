package net.alephsmp.aleph_carpet.mixins.shulkersForceTeleport;

import net.alephsmp.aleph_carpet.AlephSimpleSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin extends GolemEntity {

    protected ShulkerEntityMixin(EntityType<? extends GolemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow @Final protected static TrackedData<Direction> ATTACHED_FACE;
    @Shadow @Final protected static TrackedData<Optional<BlockPos>> ATTACHED_BLOCK;
    @Shadow @Final protected static TrackedData<Byte> PEEK_AMOUNT;

    @Shadow protected abstract boolean canStay(BlockPos pos, Direction attachSide);
    @Shadow protected @Nullable abstract Direction findAttachSide(BlockPos pos);



    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (!AlephSimpleSettings.forceShulkerTeleport)
            return;

        ShulkerEntity shulkerEntity = (ShulkerEntity) (Object) this;

        BlockPos blockPos = (BlockPos)((Optional)this.dataTracker.get(ATTACHED_BLOCK)).orElse((Object)null);
        if (blockPos == null && !shulkerEntity.world.isClient) {
            blockPos = shulkerEntity.getBlockPos();
            this.dataTracker.set(ATTACHED_BLOCK, Optional.of(blockPos));
        }

        if (!shulkerEntity.hasVehicle() && !shulkerEntity.world.isClient) {
            Direction direction2 = shulkerEntity.getAttachedFace();
            if (!this.canStay(blockPos, direction2) && this.findAttachSide(blockPos) == null) {
                this.forceTeleport();

                ci.cancel();
            }
        }
    }

    protected void forceTeleport() {
        BlockPos blockPos = this.getBlockPos();

        for(int dx = 0; dx < 17; dx++) {
            for(int dy = 0; dy < 17; dy++) {
                for(int dz = 0; dz < 17; dz++) {
                    BlockPos blockPos2 = blockPos.add(8 - dx, 8 - dy, 8 - dz);
                    if (blockPos2.getY() > 0 && this.world.isAir(blockPos2) && this.world.getWorldBorder().contains(blockPos2) && this.world.isSpaceEmpty(this, new Box(blockPos2))) {
                        Direction direction = this.findAttachSide(blockPos2);
                        if (direction != null) {
                            this.dataTracker.set(ATTACHED_FACE, direction);
                            this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                            this.dataTracker.set(ATTACHED_BLOCK, Optional.of(blockPos2));
                            this.dataTracker.set(PEEK_AMOUNT, (byte)0);
                            this.setTarget(null);
                            return;
                        }
                    }
                }
            }
        }
    }

}
