package com.johanvonelectrum.johan_carpet.mixins.zeroTickFarms;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CactusBlock.class)
public class CactusBlockMixin {

    @Shadow
    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {}

    @Inject(at = @At("TAIL"), method = "scheduledTick")
    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random, CallbackInfo info) {
        if(JohanSettings.zeroTickFarms && !world.isAir(pos.down()) && world.isAir(pos.up()) && world.isAir(pos.north()) && world.isAir(pos.south()) && world.isAir(pos.west()) && world.isAir(pos.east()))
            this.randomTick(state, world, pos, random);
    }

}
