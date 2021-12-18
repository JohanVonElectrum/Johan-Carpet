package com.johanvonelectrum.johan_carpet.mixins.zeroTickFarms;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.block.AbstractPlantPartBlock;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(AbstractPlantPartBlock.class)
public class AbstractPlantPartBlockMixin {

    @Inject(at = @At("TAIL"), method = "scheduledTick")
    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random, CallbackInfo info) {
        if(!JohanSettings.zeroTickFarms)
            return;

        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
            return;
        }

        Block target = state.getBlock();
        BlockPos position = pos;

        while (target.toString().contains("_plant")) {
            position = target.toString().contains("twisting") ? position.up() : position.down();
            target = world.getBlockState(position).getBlock();
        }

        if(!world.isAir(position))
            ((AbstractPlantStemBlock) target).randomTick(world.getBlockState(position), world, position, random);
    }

}
