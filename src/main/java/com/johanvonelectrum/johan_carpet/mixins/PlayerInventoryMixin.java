package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    PlayerInventory inventory = (PlayerInventory) (Object) this;

    @Shadow protected abstract int addStack(int slot, ItemStack stack);

    /**
     * @author JohanVonElectrum
     */
    @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void addStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int first = inventory.getOccupiedSlotWithRoomForStack(stack);
        if (first != -1)
            stack.setCount(this.addStack(first, stack));
        for(int i = 0; i < inventory.main.size() && !stack.isEmpty(); ++i) {
            ItemStack itemStack = inventory.main.get(i);
            if (itemStack.isEmpty() || itemStack.getItem().equals(stack.getItem()))
                stack.setCount(this.addStack(i, stack));
            else if (!(Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock) && JohanSettings.shulkerInsert && Block.getBlockFromItem(itemStack.getItem()) instanceof ShulkerBoxBlock)
                stack = addStackToShulker(itemStack, stack);
        }
        cir.setReturnValue(stack.getCount());
    }

    private ItemStack addStackToShulker(ItemStack shulker, ItemStack stack) {
        EnderChestInventory shulkerInventory = new EnderChestInventory();
        CompoundTag shulkerTag = shulker.getTag();
        if (shulkerTag != null && shulkerTag.contains("BlockEntityTag", 10)) {
            CompoundTag blockEntityTag = shulkerTag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("Items", 9)) {
                ListTag itemsTag = blockEntityTag.getList("Items", 10);

                for (Tag itemTag : itemsTag) {
                    CompoundTag itemCTag = (CompoundTag) itemTag;
                    ItemStack insideStack = ItemStack.fromTag(itemCTag);
                    int slot = itemCTag.getByte("Slot");

                    if (slot >= 0)
                        shulkerInventory.setStack(slot, insideStack);
                }
            }
        } else {
            shulkerTag = new CompoundTag();
            shulkerTag.put("BlockEntityTag", new CompoundTag());
        }
        ItemStack remaining = shulkerInventory.addStack(stack);
        ((CompoundTag)shulkerTag.get("BlockEntityTag")).put("Items", shulkerInventory.getTags());
        shulker.setTag(shulkerTag);
        return remaining;
    }

}
