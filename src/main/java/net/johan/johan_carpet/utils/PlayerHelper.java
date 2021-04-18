package net.johan.johan_carpet.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class PlayerHelper {

    public static String getDimensionName(ServerPlayerEntity player) {
        return player.getEntityWorld().getRegistryKey().getValue().getPath();
    }

    public static BlockPos getCoords(ServerPlayerEntity player) {
        return player.getBlockPos();
    }

}
