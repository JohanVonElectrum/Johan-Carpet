package net.alephsmp.aleph_carpet.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.alephsmp.aleph_carpet.AlephSettings;
import net.minecraft.command.argument.ObjectiveArgumentType;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.text.LiteralText;
import net.minecraft.util.UserCache;

import java.util.List;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class CommandTotal {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder literalargumentbuilder = literal("total")
                .requires((player) -> AlephSettings.commandTotal)
                .then(argument("objective", ObjectiveArgumentType.objective())
                        .executes(context -> execute(context.getSource(), ObjectiveArgumentType.getObjective(context, "objective")))
                        .then(argument("bots", BoolArgumentType.bool())
                                .executes(context -> execute(context.getSource(), ObjectiveArgumentType.getObjective(context, "objective"), BoolArgumentType.getBool(context, "bots")))
                        )
                );

        dispatcher.register(literalargumentbuilder);
    }

    private static int execute(ServerCommandSource source, ScoreboardObjective objective) {
        return execute(source, objective, false);
    }

    private static int execute(ServerCommandSource source, ScoreboardObjective objective, boolean bots) {
        long i = 0L;
        for (ScoreboardPlayerScore score: source.getMinecraftServer().getScoreboard().getAllPlayerScores(objective)) {
            if (!bots && source.getMinecraftServer().getScoreboard().getPlayerTeam(score.getPlayerName()) == null)
                continue;
            i += score.getScore();
        }

        BaseText total = new LiteralText("");
        total.append("[").append(objective.getDisplayName()).append("] Total: " + i);
        source.sendFeedback(total, false);

        return 1;
    }

}
