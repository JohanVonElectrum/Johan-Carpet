package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantEntity.class)
public class MerchantEntityMixin {

    @Inject(method = "trade", at = @At("TAIL"))
    public void trade(TradeOffer offer, CallbackInfo ci) {
        if (JohanSettings.infiniteTrades)
            offer.resetUses();
    }

}
