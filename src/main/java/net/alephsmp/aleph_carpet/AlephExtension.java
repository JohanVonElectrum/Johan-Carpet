package net.alephsmp.aleph_carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import com.mojang.brigadier.CommandDispatcher;
import net.alephsmp.aleph_carpet.commands.CommandEnderchest;
import net.alephsmp.aleph_carpet.commands.CommandLocation;
import net.alephsmp.aleph_carpet.commands.CommandTotal;
import net.minecraft.server.command.ServerCommandSource;

public class AlephExtension implements CarpetExtension {
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
    }
}
