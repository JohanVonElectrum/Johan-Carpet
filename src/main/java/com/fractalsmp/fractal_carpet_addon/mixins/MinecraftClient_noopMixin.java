package com.fractalsmp.fractal_carpet_addon.mixins;


import com.fractalsmp.fractal_carpet_addon.FractalExtension;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClient_noopMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void loadExtension(CallbackInfo ci) {
        FractalExtension.noop();
    }
}