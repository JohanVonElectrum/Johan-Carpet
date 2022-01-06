package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    MobEntity t = (MobEntity) (Object) this;

    @Inject(method = "getDropChance", at = @At("HEAD"), cancellable = true)
    private void drownedDropTrident(EquipmentSlot slot, CallbackInfoReturnable<Float> cir) {
        if (JohanSettings.drownedDropTrident && t instanceof DrownedEntity) {
            if (Items.TRIDENT.equals(t.getMainHandStack().getItem())) {
                cir.setReturnValue(Float.MAX_VALUE);
            }
        }
    }
}
