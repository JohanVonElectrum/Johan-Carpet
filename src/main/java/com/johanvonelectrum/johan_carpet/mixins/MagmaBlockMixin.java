package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MagmaBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MagmaBlock.class)
public class MagmaBlockMixin extends Block {
    public MagmaBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "onSteppedOn", at = @At("HEAD"), cancellable = true)
    public void avoidPlayerDamage(World world, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        ci.cancel();
        boolean shouldDamagePlayer = !entity.isFireImmune() && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity);
        if (JohanSettings.ignorePlayerDamageFromMagmaBlock) {
            shouldDamagePlayer = shouldDamagePlayer && !(entity instanceof PlayerEntity);
        }

        if (shouldDamagePlayer) {
            entity.damage(DamageSource.HOT_FLOOR, 1.0F);
        }

        super.onSteppedOn(world, pos, state, entity);
    }
}
