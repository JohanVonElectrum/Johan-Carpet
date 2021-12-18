package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Mixin(PiglinBrain.class)
public abstract class PiglinBrainMixin {

    @Shadow
    private static Vec3d findGround(PiglinEntity piglin) { return null; }

    private static Random rng = new Random();

    @Inject(method = "getBarteredItem", at = @At("HEAD"))
    private static void getBarteredItem(PiglinEntity piglin, CallbackInfoReturnable<List<ItemStack>> cir) {
        if (rng.nextInt(100) < JohanSettings.ancientBartering) {
            Optional<PlayerEntity> optional = piglin.getBrain().getOptionalMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
            LookTargetUtil.give(piglin, Items.ANCIENT_DEBRIS.getDefaultStack(), optional.isPresent() ? optional.get().getPos() : findGround(piglin));
        }
    }
}