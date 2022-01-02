
package com.johanvonelectrum.johan_carpet.mixins.anvilEnchantments;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin {

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I", ordinal = 1))
    private int disableAnvilXpLimit(Property property) {
        return JohanSettings.disableAnvilXpLimit ? 0 : property.get();
    }

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I"))
    private int disableEnchantmentCap(Enchantment enchantment) {
        return JohanSettings.disableEnchantmentCap ? Integer.MAX_VALUE : enchantment.getMaxLevel();
    }

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z"))
    private boolean compatibleEnchantmentsCombine(Enchantment enchantment, Enchantment other) {
        return JohanSettings.compatibleEnchantments || enchantment.canCombine(other);
    }

    @Redirect(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean compatibleEnchantmentsAcceptItems(Enchantment enchantment, ItemStack stack) {
        return JohanSettings.compatibleEnchantments || enchantment.isAcceptableItem(stack);
    }

}