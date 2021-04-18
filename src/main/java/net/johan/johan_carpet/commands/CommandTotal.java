package net.johan.johan_carpet.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.johan.johan_carpet.JohanSettings;
import net.minecraft.command.argument.ObjectiveArgumentType;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandTotal {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder literalargumentbuilder = literal("total")
                .requires((player) -> JohanSettings.commandTotal)
                .then(argument("objective", ObjectiveArgumentType.objective())
                        .executes(context -> execute(context.getSource(), ObjectiveArgumentType.getObjective(context, "objective")))
                        .then(argument("bots", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), ObjectiveArgumentType.getObjective(context, "objective"), BoolArgumentType.getBool(context, "bots")))
                        )
                );

        dispatcher.register(literalargumentbuilder);
    }

    private static int execute(ServerCommandSource source, ScoreboardObjective objective) {
        return execute(source, objective, JohanSettings.filterBotsInScores);
    }

    private static int execute(ServerCommandSource source, ScoreboardObjective objective, boolean bots) {
        BaseText total = new LiteralText("");
        total.append("[").append(objective.getDisplayName()).append("] Total: " + getTotal(source, objective, bots));
        source.sendFeedback(total, false);

        return 1;
    }

    public static int getTotal(ServerCommandSource source, ScoreboardObjective objective, boolean bots) {
        int i = 0;
        for (ScoreboardPlayerScore score: source.getMinecraftServer().getScoreboard().getAllPlayerScores(objective)) {
            if (!bots && source.getMinecraftServer().getScoreboard().getPlayerTeam(score.getPlayerName()) == null)
                continue;
            i += score.getScore();
        }
        return i;
    }

}
