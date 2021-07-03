package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    PlayerEntity player = (PlayerEntity) (Object) this;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (JohanSettings.xpBarMending) {
            Map.Entry<EquipmentSlot, ItemStack> entry = EnchantmentHelper.chooseEquipmentWith(Enchantments.MENDING, player, ItemStack::isDamaged);
            if (entry != null && player.isInSneakingPose()) {
                ItemStack itemStack = (ItemStack)entry.getValue();
                if (!itemStack.isEmpty() && itemStack.isDamaged()) {
                    int i = Math.min(player.totalExperience * 2, itemStack.getDamage());
                    player.addExperience(-(int)Math.ceil(i / 2D));
                    itemStack.setDamage(itemStack.getDamage() - i);
                }
            }
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void attack(Entity target, CallbackInfo ci) {
        if (JohanSettings.creativeKill && player.isCreative() && target.isAttackable() && !target.handleAttack(player))
            target.kill();
    }

}
