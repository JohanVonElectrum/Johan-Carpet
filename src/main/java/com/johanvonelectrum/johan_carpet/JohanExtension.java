package com.johanvonelectrum.johan_carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.johanvonelectrum.johan_carpet.commands.*;
import com.johanvonelectrum.johan_carpet.utils.HttpHelper;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

import java.time.Instant;

public class JohanExtension implements CarpetExtension {

    public static final String MOD_NAME = "Johan Carpet Addon";
    public static final String MOD_ID = "johan-carpet";
    public static final String VERSION = "2021.5.31";

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
    }

    private static Instant lastUpdateCheck = Instant.MIN;
    private static boolean lastUpdateResult = false;
    public static boolean shouldUpdate() {
        Instant now = Instant.now();
        if (lastUpdateResult || lastUpdateCheck.plusSeconds(3600).isAfter(now))
            return lastUpdateResult;
        lastUpdateCheck = now;
        lastUpdateResult = !JohanExtension.VERSION.equals(HttpHelper.getLatestRelease());
        return lastUpdateResult;
    }

}
