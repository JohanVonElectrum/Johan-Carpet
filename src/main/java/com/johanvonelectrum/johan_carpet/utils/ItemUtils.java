package com.johanvonelectrum.johan_carpet.utils;

import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.stream.Stream;

public class ItemUtils {

    public static void spawnItemContents(ItemEntity itemEntity, Stream<ItemStack> contents) {
        World world = itemEntity.world;
        if (world.isClient) {
            return;
        }
        contents.forEach(stack -> world.spawnEntity(new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), (ItemStack)stack)));
    }

}
