package net.alephsmp.aleph_carpet.mixins;

import net.alephsmp.aleph_carpet.AlephSettings;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.EndSpikeFeature;
import net.minecraft.world.gen.feature.EndSpikeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
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
    public void suppressGenerate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, EndSpikeFeatureConfig endSpikeFeatureConfig, CallbackInfoReturnable<Boolean> cir) {
        if (!AlephSettings.endMainIslandStructureGen) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method="generateSpike", at=@At("HEAD"), cancellable = true)
    public void suppressGenerateSpike(ServerWorldAccess world, Random random, EndSpikeFeatureConfig config, EndSpikeFeature.Spike spike, CallbackInfo ci) {
        if (!AlephSettings.endMainIslandStructureGen) {
            ci.cancel();
        }
    }
}
