package net.alephsmp.aleph_carpet.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.alephsmp.aleph_carpet.AlephSettings;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandEnderchest {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder literalargumentbuilder = literal("enderchest")
                .requires((player) -> AlephSettings.commandEnderchest && player.hasPermissionLevel(3))
                .then(argument("player", EntityArgumentType.player())
                        .executes(context -> open(context.getSource(), EntityArgumentType.getPlayer(context, "player")))
                );

        dispatcher.register(literalargumentbuilder);
    }

    private static int open(ServerCommandSource source, ServerPlayerEntity player) throws CommandSyntaxException {
        source.getPlayer().openHandledScreen(new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) -> {
            return GenericContainerScreenHandler.createGeneric9x3(i, playerInventory, player.getEnderChestInventory());
        }, player.getDisplayName()));

        return 1;
    }

}
