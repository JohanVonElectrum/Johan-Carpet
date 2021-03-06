package com.johanvonelectrum.johan_carpet.commands;

import com.google.common.collect.Sets;
import com.johanvonelectrum.johan_carpet.JohanSettings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;

import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandComputation {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = literal("computation")
                .requires((player) -> JohanSettings.commandComputation)
                .then(literal("convert")
                        .then(argument("number", IntegerArgumentType.integer())
                                .then(argument("from_base", IntegerArgumentType.integer(1))
                                        .suggests(((context, builder) -> CommandSource.suggestMatching(getDefaultBases(), builder)))
                                        .then(argument("to_base", IntegerArgumentType.integer(1))
                                                .suggests(((context, builder) -> CommandSource.suggestMatching(getDefaultBases(), builder)))
                                                .executes(context -> convert(context.getSource(), IntegerArgumentType.getInteger(context, "number"), IntegerArgumentType.getInteger(context, "from_base"), IntegerArgumentType.getInteger(context, "to_base")))
                                        )
                                )
                        )
                );

        dispatcher.register(literalArgumentBuilder);
    }

    private static Collection<String> getDefaultBases() {
        return Sets.newLinkedHashSet(Arrays.asList("2", "8", "10", "16"));
    }

    private static int convert(ServerCommandSource source, int input, int base1, int base2) {
        source.sendFeedback(new LiteralText("base" + base1 + ": " + input), false);
        source.sendFeedback(new LiteralText("base" + base2 + ": " + Integer.toString(Integer.parseInt(input + "", base1), base2)), false);

        return 1;
    }

}