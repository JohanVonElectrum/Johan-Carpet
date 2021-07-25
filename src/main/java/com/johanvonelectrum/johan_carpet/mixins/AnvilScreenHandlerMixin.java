package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.text.LiteralText;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;

import java.util.Iterator;
import java.util.Map;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {

    @Mutable
    @Final
    @Shadow
    private final Property levelCost;
    @Shadow
    private int repairItemUsage;
    @Shadow
    private String newItemName;

    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
        this.levelCost = Property.create();
        this.addProperty(this.levelCost);
    }

    /**
     * @author JohanVonElectrum
     * @reason AnvilXPCap and compatibility fixed
     */
    @Overwrite
    public void updateResult() {
        ItemStack itemStack = this.input.getStack(0);
        this.levelCost.set(1);
        int i = 0;
        int j = 0;
        int k = 0;
        if (itemStack.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.levelCost.set(0);
        } else {
            ItemStack itemStack2 = itemStack.copy();
            ItemStack itemStack3 = this.input.getStack(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.get(itemStack2);
            j = j + itemStack.getRepairCost() + (itemStack3.isEmpty() ? 0 : itemStack3.getRepairCost());
            this.repairItemUsage = 0;
            if (!itemStack3.isEmpty()) {
                boolean bl = itemStack3.getItem() == Items.ENCHANTED_BOOK && !EnchantedBookItem.getEnchantmentTag(itemStack3).isEmpty();
                int o;
                int p;
                int q;
                if (itemStack2.isDamageable() && itemStack2.getItem().canRepair(itemStack, itemStack3)) {
                    o = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    if (o <= 0) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }

                    for(p = 0; o > 0 && p < itemStack3.getCount(); ++p) {
                        q = itemStack2.getDamage() - o;
                        itemStack2.setDamage(q);
                        ++i;
                        o = Math.min(itemStack2.getDamage(), itemStack2.getMaxDamage() / 4);
                    }

                    this.repairItemUsage = p;
                } else {
                    if (!bl && (itemStack2.getItem() != itemStack3.getItem() || !itemStack2.isDamageable())) {
                        this.output.setStack(0, ItemStack.EMPTY);
                        this.levelCost.set(0);
                        return;
                    }

                    if (itemStack2.isDamageable() && !bl) {
                        o = itemStack.getMaxDamage() - itemStack.getDamage();
                        p = itemStack3.getMaxDamage() - itemStack3.getDamage();
                        q = p + itemStack2.getMaxDamage() * 12 / 100;
                        int r = o + q;
                        int s = itemStack2.getMaxDamage() - r;
                        if (s < 0) {
                            s = 0;
                        }

                        if (s < itemStack2.getDamage()) {
                            itemStack2.setDamage(s);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map2 = EnchantmentHelper.get(itemStack3);
                    boolean bl2 = false;
                    boolean bl3 = false;
                    Iterator var24 = map2.keySet().iterator();

                    label155:
                    while(true) {
                        Enchantment enchantment;
                        do {
                            if (!var24.hasNext()) {
                                if (bl3 && !bl2) {
                                    this.output.setStack(0, ItemStack.EMPTY);
                                    this.levelCost.set(0);
                                    return;
                                }
                                break label155;
                            }

                            enchantment = (Enchantment)var24.next();
                        } while(enchantment == null);

                        int t = (Integer)map.getOrDefault(enchantment, 0);
                        int u = (Integer)map2.get(enchantment);
                        u = t == u ? u + 1 : Math.max(u, t);
                        boolean bl4 = enchantment.isAcceptableItem(itemStack);
                        if (this.player.abilities.creativeMode || itemStack.getItem() == Items.ENCHANTED_BOOK || JohanSettings.compatibleEnchantments) {
                            bl4 = true;
                        }

                        Iterator var17 = map.keySet().iterator();

                        while(var17.hasNext()) {
                            Enchantment enchantment2 = (Enchantment)var17.next();
                            if (enchantment2 != enchantment && (!enchantment.canCombine(enchantment2) && !JohanSettings.compatibleEnchantments)) {
                                bl4 = false;
                                ++i;
                            }
                        }

                        if (!bl4) {
                            bl3 = true;
                        } else {
                            bl2 = true;
                            if (!JohanSettings.disableEnchantmentCap && u > enchantment.getMaxLevel()) {
                                u = enchantment.getMaxLevel();
                            }

                            map.put(enchantment, u);
                            int v = 0;
                            switch(enchantment.getRarity()) {
                                case COMMON:
                                    v = 1;
                                    break;
                                case UNCOMMON:
                                    v = 2;
                                    break;
                                case RARE:
                                    v = 4;
                                    break;
                                case VERY_RARE:
                                    v = 8;
                            }

                            if (bl) {
                                v = Math.max(1, v / 2);
                            }

                            i += v * u;
                            if (itemStack.getCount() > 1) {
                                i = 40;
                            }
                        }
                    }
                }
            }

            if (StringUtils.isBlank(this.newItemName)) {
                if (itemStack.hasCustomName()) {
                    k = 1;
                    i += k;
                    itemStack2.removeCustomName();
                }
            } else if (!this.newItemName.equals(itemStack.getName().getString())) {
                k = 1;
                i += k;
                itemStack2.setCustomName(new LiteralText(this.newItemName));
            }

            this.levelCost.set(j + i);
            if (i <= 0) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.levelCost.get() >= 40 && !JohanSettings.disableAnvilXpLimit) {
                this.levelCost.set(39);
            }

            if (this.levelCost.get() >= 40 && !this.player.abilities.creativeMode && !JohanSettings.disableAnvilXpLimit) {
                itemStack2 = ItemStack.EMPTY;
            }

            if (!itemStack2.isEmpty()) {
                int w = itemStack2.getRepairCost();
                if (!itemStack3.isEmpty() && w < itemStack3.getRepairCost()) {
                    w = itemStack3.getRepairCost();
                }

                if (k != i || k == 0) {
                    w = AnvilScreenHandler.getNextCost(w);
                }

                itemStack2.setRepairCost(w);
                EnchantmentHelper.set(map, itemStack2);
            }

            this.output.setStack(0, itemStack2);
            this.sendContentUpdates();
        }
    }

}
