package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntity_noObsidianPlatform extends PlayerEntity {
    public ServerPlayerEntity_noObsidianPlatform(World world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(method="changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"))
    private boolean blockSetBlock(ServerWorld serverWorld, BlockPos pos, BlockState blockState) {
        if (FractalSimpleSettings.noObsidianPlatform) {
            return true;
        } else {
            return serverWorld.setBlockState(pos, blockState);
        }
    }
}
