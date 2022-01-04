package com.johanvonelectrum.johan_carpet.mixins.anvilEnchantments;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "removeConflicts", at = @At(value = "INVOKE"), cancellable = true)
    private static void compatibleEnchantments(List<EnchantmentLevelEntry> possibleEntries, EnchantmentLevelEntry pickedEntry, CallbackInfo ci) {
        if (JohanSettings.compatibleEnchantments)
            ci.cancel();
    }

    @Inject(method = "isCompatible", at = @At(value = "INVOKE"), cancellable = true)
    private static void isCompatible(Collection<Enchantment> existing, Enchantment candidate, CallbackInfoReturnable<Boolean> cir) {
        if (JohanSettings.compatibleEnchantments)
            cir.setReturnValue(true);
    }

}
