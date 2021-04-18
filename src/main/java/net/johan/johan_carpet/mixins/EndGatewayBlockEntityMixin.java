package net.johan.johan_carpet.mixins;

import net.johan.johan_carpet.JohanSettings;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndGatewayBlockEntity.class)
public abstract class EndGatewayBlockEntityMixin extends EndPortalBlockEntity {
    @Redirect(method="method_30276", at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;hasNetherPortalCooldown()Z"))
    private static boolean returnFalseIfNoCooldown(Entity entity) {
        if (!JohanSettings.endGatewayCooldown) {
            return false;
        } else {
            return entity.hasNetherPortalCooldown();
        }
    }
}
