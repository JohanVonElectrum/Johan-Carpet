package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeatureMixin extends Feature<EndSpikeFeatureConfig> {
    public EndSpikeFeatureMixin(Codec<EndSpikeFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Inject(method="generate", at = @At("HEAD"), cancellable = true)
    public void generate(FeatureContext<EndSpikeFeatureConfig> context, CallbackInfoReturnable<Boolean> cir) {
        if (!JohanSettings.endMainIslandStructureGen) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method="generateSpike", at=@At("HEAD"), cancellable = true)
    public void generateSpike(ServerWorldAccess world, Random random, EndSpikeFeatureConfig config, EndSpikeFeature.Spike spike, CallbackInfo ci) {
        if (!JohanSettings.endMainIslandStructureGen) {
            ci.cancel();
        }
    }
}
