package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {

    PlayerInventory inventory = (PlayerInventory) (Object) this;

    @Shadow protected abstract int addStack(int slot, ItemStack stack);

    @Inject(method = "addStack(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void addStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int first = inventory.getOccupiedSlotWithRoomForStack(stack);
        if (first == -1) {
            List<Byte>[] map = scanInventory();
            if (map[1].isEmpty())
                first = map[0].isEmpty() ? -1 : map[0].get(0);
            else if (JohanSettings.shulkerInsert && (JohanSettings.shulkerInception || !(Block.getBlockFromItem(stack.getItem()) instanceof ShulkerBoxBlock))) {
                for (byte i: map[1]) {
                    ItemStack itemStack = inventory.main.get(i);
                    if (stack.isEmpty() || addStackToShulker(itemStack, stack, map[0].isEmpty() ? -1 : map[0].get(0)))
                        break;
                }
            } else
                first = map[0].isEmpty() ? -1 : map[0].get(0);
        }
        if (first != -1)
            stack.setCount(this.addStack(first, stack));

        cir.setReturnValue(stack.getCount());
    }

    private List<Byte>[] scanInventory() {
        List<Byte>[] map = new ArrayList[] {new ArrayList<>(), new ArrayList<>(), new ArrayList<>()};
        for (byte i = 0, j; i < inventory.main.size(); i++) {
            ItemStack itemStack = inventory.main.get(i);
            if (itemStack.isEmpty())
                j = 0;
            else if (Block.getBlockFromItem(itemStack.getItem()) instanceof ShulkerBoxBlock)
                j = (byte) (itemStack.getCount() == 1 ? 1 : 2);
            else
                continue;
            map[j].add(i);
        }
        map[1].addAll(map[2]);
        return new List[] { map[0], map[1] };
    }

    private boolean addStackToShulker(ItemStack shulker, ItemStack stack, int emptySlot) {
        ItemStack target = shulker;
        System.out.println(target.getCount() + " : " + emptySlot);

        if (shulker.getCount() > 1) {
            if (emptySlot == -1)
                return true;
            else {
                shulker.decrement(1);
                ItemStack newShulker = shulker.getItem().getDefaultStack();
                newShulker.setCount(1);
                addStack(emptySlot, newShulker);
                target = inventory.main.get(emptySlot);
            }
        }

        System.out.println(target.getCount() + " : " + emptySlot);

        EnderChestInventory shulkerInventory = new EnderChestInventory();
        NbtCompound shulkerTag = target.getNbt();
        if (shulkerTag != null && shulkerTag.contains("BlockEntityTag", 10)) {
            NbtCompound blockEntityTag = shulkerTag.getCompound("BlockEntityTag");
            if (blockEntityTag.contains("Items", 9)) {
                NbtList itemsTag = blockEntityTag.getList("Items", 10);

                for (NbtElement itemTag : itemsTag) {
                    NbtCompound itemCTag = (NbtCompound) itemTag;
                    ItemStack insideStack = ItemStack.fromNbt(itemCTag);
                    int slot = itemCTag.getByte("Slot");

                    if (slot >= 0)
                        shulkerInventory.setStack(slot, insideStack);
                }
            }
        } else {
            shulkerTag = new NbtCompound();
            shulkerTag.put("BlockEntityTag", new NbtCompound());
        }
        ItemStack remaining = shulkerInventory.addStack(stack);
        ((NbtCompound)shulkerTag.get("BlockEntityTag")).put("Items", shulkerInventory.toNbtList());
        target.setNbt(shulkerTag);
        return remaining.isEmpty();
    }

}
