package com.fractalsmp.fractal_carpet_addon.mixins;

import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.block.entity.EndPortalBlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EndGatewayBlockEntity.class)
public class EndGateway_noCooldownMixin extends EndPortalBlockEntity {
    public void startTeleportCooldown() {}
}