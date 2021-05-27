package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collection;
import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    /**
     * @author JohanVonElectrum
     */
    @Overwrite
    public static void removeConflicts(List<EnchantmentLevelEntry> possibleEntries, EnchantmentLevelEntry pickedEntry) {
        if (JohanSettings.compatibleEnchantments)
            return;

        possibleEntries.removeIf(o -> !pickedEntry.enchantment.canCombine(((EnchantmentLevelEntry) o).enchantment));
    }

    /**
     * @author JohanVonElectrum
     */
    @Overwrite
    public static boolean isCompatible(Collection<Enchantment> existing, Enchantment candidate) {
        if (JohanSettings.compatibleEnchantments)
            return true;

        for (Enchantment enchantment: existing) {
            if (!enchantment.canCombine(candidate))
                return false;
        }

        return true;
    }

}
