package com.johanvonelectrum.johan_carpet.commands;

import com.johanvonelectrum.johan_carpet.JohanData;
import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.johanvonelectrum.johan_carpet.models.CsInfo;
import com.johanvonelectrum.johan_carpet.utils.PlayerHelper;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandCs {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandCs.class);
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("cs").executes(CommandCs::execute));
    }

    private static int execute(CommandContext<ServerCommandSource> source) {
        if (!JohanSettings.commandCs)
            return 0;

        JohanData johanData = JohanData.getInstance();
        Map<String, CsInfo> csInfoMap = johanData.getCsInfoMap();
        try {
            ServerPlayerEntity player = source.getSource().getPlayer();
            String name = player.getName().asString();
            Iterable<ServerWorld> serverWorlds = source.getSource().getServer().getWorlds();

            if (csInfoMap.containsKey(name)) {
                CsInfo csInfo = csInfoMap.get(name);
                csInfoMap.remove(name);
                player.changeGameMode(GameMode.byId(csInfo.getGamemode()));

                ServerWorld world = null;
                for (ServerWorld w: serverWorlds) {
                    if (w.getRegistryKey().getValue().toString().equals(csInfo.getDimension())) {
                        world = w;
                        break;
                    }
                }

                if (world == null) {
                    source.getSource().sendError(Text.of("Unexpected error, couldn't find the origin dimension."));
                }
                player.teleport(world, csInfo.getX(), csInfo.getY(), csInfo.getZ(), csInfo.getYaw(), csInfo.getPitch());
            } else {
                CsInfo csInfo = new CsInfo();
                csInfo.setX(player.getX());
                csInfo.setY(player.getY());
                csInfo.setZ(player.getZ());
                csInfo.setDimension(PlayerHelper.getFullDimensionName(player));
                csInfo.setGamemode(player.interactionManager.getGameMode().getId());
                csInfo.setYaw(player.getYaw());
                csInfo.setPitch(player.getPitch());
                csInfoMap.put(name, csInfo);
                player.changeGameMode(GameMode.SPECTATOR);
            }
            return 1;
        } catch (Exception e) {
            LOGGER.error("Error on cs command execution", e);
            return 0;
        }
    }
}
