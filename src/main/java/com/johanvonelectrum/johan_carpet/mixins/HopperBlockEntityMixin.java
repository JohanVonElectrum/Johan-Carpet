package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.johanvonelectrum.johan_carpet.utils.ChunkHelper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public class HopperBlockEntityMixin {

    @Inject(method = "getInventoryAt(Lnet/minecraft/world/World;DDD)Lnet/minecraft/inventory/Inventory;", at = @At("HEAD"))
    private static void getBlockState(World world, double x, double y, double z, CallbackInfoReturnable<Inventory> cir) {
        if (JohanSettings.hopperLoading)
            ChunkHelper.loadEntityTicking((ServerWorld) world, new ChunkPos((int)x >> 4, (int)z >> 4));
    }

}
