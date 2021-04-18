package net.johan.johan_carpet.mixins;

import carpet.utils.Messenger;
import com.mojang.authlib.GameProfile;
import net.johan.johan_carpet.JohanExtension;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements ScreenHandlerListener {

    @Shadow public abstract void sendMessage(Text message, boolean actionBar);

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "onSpawn", at = @At("HEAD"))
    public void onSpawn(CallbackInfo ci) {
        this.sendMessage(Messenger.s("You are playing with §5" + JohanExtension.MOD_NAME + "§r: §5" + JohanExtension.MOD_ID + "-1.16.4-" + JohanExtension.VERSION), false);
        if (this.hasPermissionLevel(3) && JohanExtension.shouldUpdate()) {
            BaseText url = Messenger.s("https://github.com/JohanVonElectrum/Johan-Carpet/releases/latest");
            ClickEvent event = new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/JohanVonElectrum/Johan-Carpet/releases/latest");
            url.setStyle(
                    url.getStyle()
                            .withClickEvent(event)
                            .withColor(Formatting.BLUE)
            );
            this.sendMessage(Messenger.s("Your §5" + JohanExtension.MOD_NAME + "§r is outdated. Please, update it at ").append(url), false);
        }
    }

}
