package net.alephsmp.aleph_carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import net.alephsmp.aleph_carpet.commands.*;
import net.alephsmp.aleph_carpet.utils.HttpHelper;
import net.minecraft.server.command.ServerCommandSource;

import java.time.Instant;

public class AlephExtension implements CarpetExtension {

    public static final String MOD_NAME = "AlephSMP Carpet Addon";
    public static final String MOD_ID = "aleph-carpet";
    public static final String VERSION = "2021.4.11";

    public static void noop() {}

    static {
        AlephExtension extension = new AlephExtension();
        CarpetServer.manageExtension(extension);
        extension.onGameStarted();
    }
    @Override
    public void onGameStarted() {
        CarpetServer.settingsManager.parseSettingsClass(AlephSettings.class);
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
    public static boolean shouldUpdate() {
        Instant now = Instant.now();
        if (lastUpdateCheck.plusSeconds(3600).isAfter(now))
            return false;
        lastUpdateCheck = now;
        return !AlephExtension.VERSION.equals(HttpHelper.getLatestRelease());
    }

}
