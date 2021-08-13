package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ShulkerEntity.class)
public abstract class ShulkerEntityMixin {

    @Shadow protected abstract boolean tryTeleport();

    @Shadow @Nullable protected abstract Direction findAttachSide(BlockPos pos);

    @Shadow protected abstract void setAttachedFace(Direction face);

    @Shadow abstract void setPeekAmount(int peekAmount);

    @Redirect(method = "tryAttachOrTeleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/ShulkerEntity;tryTeleport()Z"))
    public boolean tryTeleport(ShulkerEntity shulkerEntity) {
        if (JohanSettings.forceShulkerTeleport)
            return forceTeleport(shulkerEntity);
        else
            return tryTeleport();
    }

    protected boolean forceTeleport(ShulkerEntity shulker) {
        BlockPos blockPos = shulker.getBlockPos();

        for(int dx = 0; dx < 17; dx++) {
            for(int dy = 0; dy < 17; dy++) {
                for(int dz = 0; dz < 17; dz++) {
                    BlockPos blockPos2 = blockPos.add(8 - dx, 8 - dy, 8 - dz);
                    if (blockPos2.getY() > 0 && shulker.world.isAir(blockPos2) && shulker.world.getWorldBorder().contains(blockPos2) && shulker.world.isSpaceEmpty(shulker, new Box(blockPos2))) {
                        Direction direction = findAttachSide(blockPos2);
                        if (direction != null) {
                            setAttachedFace(direction);
                            shulker.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1.0F, 1.0F);
                            setPeekAmount(0);
                            shulker.setTarget(null);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
