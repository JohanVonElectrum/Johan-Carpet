package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.entity.Entity;
import net.minecraft.server.PlayerManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin{
    @Redirect(method="remove", at = @At(value="FIELD", target = "Lnet/minecraft/entity/Entity;removed:Z", opcode = Opcodes.PUTFIELD))
    private void replaceRemove(Entity entity, boolean value) {
        entity.removed = !JohanSettings.llamaDupeExploit;
    }
}