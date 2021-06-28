package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "getMaxCount", at=@At("HEAD"), cancellable = true)
    public final void getMaxCount(CallbackInfoReturnable<Integer> cir) {
        if (JohanSettings.allStackable)
            cir.setReturnValue(64);
    }

}
