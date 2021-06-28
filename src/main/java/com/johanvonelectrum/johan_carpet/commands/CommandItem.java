package com.johanvonelectrum.johan_carpet.commands;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandItem {

    public static final Map<Integer, List<Item>> ITEMS = new HashMap<>();

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder literalargumentbuilder = literal("item")
                .requires((player) -> JohanSettings.commandItem)
                .then(argument("stack_size", IntegerArgumentType.integer(1, 64))
                        .executes(context -> getItemCount(context.getSource(), IntegerArgumentType.getInteger(context, "stack_size"), false))
                        .then(argument("list", BoolArgumentType.bool())
                                .executes(context -> getItemCount(context.getSource(), IntegerArgumentType.getInteger(context, "stack_size"), BoolArgumentType.getBool(context, "list")))
                        )
                );

        dispatcher.register(literalargumentbuilder);
    }

    private static int getItemCount(ServerCommandSource source, int stackSize, boolean list) throws CommandSyntaxException {
        List<Item> items = ITEMS.get(stackSize);
        if (items != null) {
            source.sendFeedback(new LiteralText("There are " + items.size() + " items whose stack size is " + stackSize + "."), false);

            if (!list)
                return 1;

            String result = items.stream()
                    .map(n -> Registry.ITEM.getId(n).toString())
                    .collect(Collectors.joining("\n", "", ""));
            source.sendFeedback(new LiteralText(result), false);
            return 0;
        } else
            source.sendError(new LiteralText("There are no items whose stack size is " + stackSize + "."));

        return 0;
    }

}
