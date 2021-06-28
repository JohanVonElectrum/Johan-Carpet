package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.commands.CommandItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Items.class)
public class ItemsMixin {

    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At("HEAD"))
    private static void register(Identifier id, Item item, CallbackInfoReturnable<Item> cir) {
        List<Item> eqSizeItems = CommandItem.ITEMS.computeIfAbsent(item.getMaxCount(), k -> new ArrayList<>());

        eqSizeItems.add(item);
    }

}
