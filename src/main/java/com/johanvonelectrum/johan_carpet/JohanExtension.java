package com.johanvonelectrum.johan_carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.johanvonelectrum.johan_carpet.commands.*;
import com.johanvonelectrum.johan_carpet.utils.HttpHelper;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

public class JohanExtension implements CarpetExtension {

    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_NAME);

    public static void noop() {}

    static {
        JohanExtension extension = new JohanExtension();
        CarpetServer.manageExtension(extension);
        extension.onGameStarted();
    }
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(JohanSettings.class);
    }

    @Override
    public void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        CommandLocation.register(dispatcher);
        CommandEnderchest.register(dispatcher);
        CommandTotal.register(dispatcher);
        CommandSignal.register(dispatcher);
        CommandComputation.register(dispatcher);
        CommandBatch.register(dispatcher);
        CommandItem.register(dispatcher);
    }

    private static Instant lastUpdateCheck = Instant.MIN;
    private static boolean lastUpdateResult = false;
    public static boolean shouldUpdate() {
        Instant now = Instant.now();
        if (lastUpdateResult || lastUpdateCheck.plusSeconds(3600).isAfter(now))
            return lastUpdateResult;
        lastUpdateCheck = now;
        lastUpdateResult = !Reference.MOD_VERSION.toString().replaceAll("([a-zA-Z0-9-]+)-([0-9.]+)-", "").equals(HttpHelper.getLatestRelease());
        return lastUpdateResult;
    }

}
