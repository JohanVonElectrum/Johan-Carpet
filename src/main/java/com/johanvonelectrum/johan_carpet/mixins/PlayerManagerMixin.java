package com.johanvonelectrum.johan_carpet.mixins;

import carpet.utils.Messenger;
import com.johanvonelectrum.johan_carpet.JohanExtension;
import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.johanvonelectrum.johan_carpet.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.util.Formatting;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin{
    @Redirect(method="remove", at = @At(value="FIELD", target = "Lnet/minecraft/entity/Entity;removed:Z", opcode = Opcodes.PUTFIELD))
    private void replaceRemove(Entity entity, boolean value) {
        entity.removed = !JohanSettings.llamaDupeExploit;
    }

    @Inject(method = "onPlayerConnect", at = @At("RETURN"))
    private void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        boolean shouldUpdate = JohanExtension.shouldUpdate();
        if (!JohanSettings.onlyUpdateWarn || shouldUpdate)
            player.sendMessage(Messenger.s("You are playing with §5" + Reference.MOD_NAME + "§r: §5" + Reference.MOD_ID + "-" + Reference.MOD_VERSION), false);
        if (player.hasPermissionLevel(3) && shouldUpdate) {
            BaseText url = Messenger.s("https://github.com/JohanVonElectrum/Johan-Carpet/releases/latest");
            ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/JohanVonElectrum/Johan-Carpet/releases/latest");
            url.setStyle(
                    url.getStyle()
                            .withClickEvent(event)
                            .withColor(Formatting.BLUE)
            );
            player.sendMessage(Messenger.s("Your §5" + Reference.MOD_NAME + "§r is outdated. Please, update it at ").append(url), false);
        }
    }
}