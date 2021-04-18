package net.johan.johan_carpet.mixins;

import net.johan.johan_carpet.JohanExtension;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "<init>", at=@At("RETURN"))
    private void loadExtension(CallbackInfo ci) {
        JohanExtension.noop();
    }
}
