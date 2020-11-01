package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndGatewayBlockEntity.class)
public class EndGateway_noCooldownMixin extends EndPortalBlockEntity {
    @Inject(method="needsCooldownBeforeTeleporting", at=@At("RETURN"), cancellable = true)
    private void suppressCooldown(CallbackInfoReturnable<Boolean> cir) {
        if (!FractalSimpleSettings.endGatewayCooldown) {
            cir.setReturnValue(false);// Returns target method early
        }
    }
}