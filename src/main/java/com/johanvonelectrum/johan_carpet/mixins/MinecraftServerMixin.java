package com.johanvonelectrum.johan_carpet.mixins;

import com.google.gson.Gson;
import com.johanvonelectrum.johan_carpet.JohanData;
import com.johanvonelectrum.johan_carpet.JohanExtension;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    private static final Logger LOGGER = JohanExtension.LOGGER;
    @Inject(method = "<init>", at=@At("RETURN"))
    private void loadExtension(CallbackInfo ci) {
        JohanExtension.noop();
    }

    @Inject(method = "save", at = @At("HEAD"))
    private void saveCustomProperties(boolean suppressLogs, boolean flush, boolean force, CallbackInfoReturnable<Boolean> cir) {
        JohanData toSave = JohanData.getInstance();

        try {
            FileWriter fileWriter = new FileWriter("johan_data.json");
            Gson gson = new Gson();
            gson.toJson(toSave, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception e) {
            LOGGER.error("Error writing johan_data", e);
        }
    }

    @Inject(method = "runServer", at = @At("HEAD"))
    private void loadCustomProperties(CallbackInfo ci) {
        JohanData johanData = JohanData.getInstance();

        try {
            FileReader fileReader = new FileReader("johan_data.json");
            Gson gson = new Gson();
            johanData = gson.fromJson(fileReader, JohanData.class);
            fileReader.close();
        } catch (Exception e) {
            LOGGER.error("Error reading johan_data", e);
        }
    }
}
