package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import java.util.function.Function;

@Mixin(EndSpikeFeature.class)
public abstract class EndSpikeFeature_generationMixin extends Feature<EndSpikeFeatureConfig> {
    public EndSpikeFeature_generationMixin(Function<Dynamic<?>, ? extends EndSpikeFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Inject(method="generate", at = @At("HEAD"), cancellable = true)
    public void suppressGenerate(IWorld iWorld, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, Random random, BlockPos blockPos, EndSpikeFeatureConfig endSpikeFeatureConfig, CallbackInfoReturnable<Boolean> ci) {
        if (!FractalSimpleSettings.endMainIslandStructureGen) {
            ci.setReturnValue(true);
        }
    }

    @Inject(method="generateSpike", at=@At("HEAD"), cancellable = true)
    public void suppressGenerateSpike(IWorld world, Random random, EndSpikeFeatureConfig config, EndSpikeFeature.Spike spike, CallbackInfo ci) {
        if (!FractalSimpleSettings.endMainIslandStructureGen) {
            ci.cancel();
        }
    }
}
