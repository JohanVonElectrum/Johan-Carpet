package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.johanvonelectrum.johan_carpet.utils.ItemUtils;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Shadow @Final private static TrackedData<ItemStack> STACK;

    @Redirect(method = "damage", at = @At(value="INVOKE", target="Lnet/minecraft/entity/ItemEntity;remove()V"))
    public void damage(ItemEntity entity) {
        if (JohanSettings.shulkerFireDrops && entity.isOnFire()) {
            ItemStack itemStack = entity.getDataTracker().get(STACK);
            if (itemStack.getItem() instanceof BlockItem) {
                if (((BlockItem) itemStack.getItem()).getBlock() instanceof ShulkerBoxBlock)
                    drop(entity, itemStack);
            }
        }
        entity.remove();
    }

    private void drop(ItemEntity entity, ItemStack stack) {
        CompoundTag nbtCompound = stack.getSubTag("BlockEntityTag");
        if (nbtCompound != null && nbtCompound.contains("Items", NbtType.LIST)) {
            ListTag nbtList = nbtCompound.getList("Items", NbtType.COMPOUND);
            nbtList.stream().map(CompoundTag.class::cast).map(ItemStack::fromTag);
            ItemUtils.spawnItemContents(entity, nbtList.stream().map(CompoundTag.class::cast).map(ItemStack::fromTag));
        }
    }

}
