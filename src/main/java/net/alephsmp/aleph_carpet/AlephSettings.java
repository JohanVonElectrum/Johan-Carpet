package net.alephsmp.aleph_carpet;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;

import static carpet.settings.RuleCategory.*;

public class AlephSettings {
    public static final String AlephSettingsCategory = "aleph-addon";
    public static final String EndSettingsCategory = "aleph-end-features";
    @Rule(
            desc = "Enables old donkey / llama dupe bug.",
            category = {SURVIVAL,FEATURE, AlephSettingsCategory}
    )
    public static boolean llamaDupeExploit = false;

    @Rule(
            desc = "Toggle for end gateway cooldown",
            category = { AlephSettingsCategory, FEATURE, EndSettingsCategory }
    )
    public static boolean endGatewayCooldown = true;

    @Rule(
            desc = "Toggle for the main end island structure generation, turns off portal, egg, obsidian pillars, gateways and crystals",
            category = { AlephSettingsCategory, FEATURE, EndSettingsCategory }
    )
    public static boolean endMainIslandStructureGen = true;

    @Rule(
            desc = "Toggle for end obsidian platform generation excluding players",
            category = { AlephSettingsCategory, FEATURE, EndSettingsCategory }
    )
    public static boolean noObsidianPlatform = false;

    /* Begin keepProjectilesTicked stuff */
    private static final String[] keepProjectilesTickedOptions = new String[] { "default", "all", "player-only", "enderpearls" };
    @Rule(
            desc = "Toggle for projectiles are ticked the whole time",
            category = { AlephSettingsCategory, FEATURE },
            options = { "default", "all", "player-only", "enderpearls" },
            validate = { keepProjectilesTickedValidator.class }
    )
    public static String keepProjectilesTicked = "default";

    private static class keepProjectilesTickedValidator extends Validator<String> {

        @Override
        public String validate(ServerCommandSource serverCommandSource, ParsedRule<String> parsedRule, String s, String s2) {
            if ((serverCommandSource == null || parsedRule.get().equals(s)) && Arrays.asList(keepProjectilesTickedOptions).contains(s))
                keepProjectilesTicked = s;
            return s;
        }
    }
    /* End keepProjectilesTicked stuff */

    @Rule(
            desc = "Force shulkers to teleport when stay in invalid positions",
            category = { AlephSettingsCategory, SURVIVAL, FEATURE, EndSettingsCategory }
    )
    public static boolean forceShulkerTeleport = false;

    @Rule(
            desc = "Enables /location command to know where is a player",
            category = { AlephSettingsCategory, SURVIVAL, COMMAND }
    )
    public static boolean commandLocation = false;

    @Rule(
            desc = "Enables /signal command to get a container with comparator value",
            category = { AlephSettingsCategory, CREATIVE, COMMAND }
    )
    public static boolean commandSignal = false;

    @Rule(
            desc = "Fishes only can spawn between y:45 and y:63, both excluded",
            category = { AlephSettingsCategory, SURVIVAL, BUGFIX }
    )
    public static boolean seaLevelFishes = false;

    @Rule(
            desc = "Enables /enderchest command to open the enderchest of a player",
            category = { AlephSettingsCategory, CREATIVE, COMMAND }
    )
    public static boolean commandEnderchest = false;

    @Rule(
            desc = "Enables /total command to know the total sum of a scoreboard",
            category = { AlephSettingsCategory, CREATIVE, COMMAND }
    )
    public static boolean commandTotal = false;

    @Rule(
            desc = "Backports 1.12 flint and steel behavior. Flint and steel can be used for updating observers / buds.",
            category = { AlephSettingsCategory, SURVIVAL, FEATURE }
    )
    public static boolean oldFlintAndSteelBehavior = false;

    /* Begin CarefulBreak stuff */
    private static final String[] carefulBreakOptions = new String[] { "never", "always", "sneaking", "no-sneaking" };
    @Rule(
            desc = "Places the mined block in the player inventory when sneaking.",
            category = { AlephSettingsCategory, SURVIVAL, FEATURE, EXPERIMENTAL },
            options = { "never", "always", "sneaking", "no-sneaking" },
            validate = { carefulBreakValidator.class }
    )
    public static String carefulBreak = "never";

    private static class carefulBreakValidator extends Validator<String> {

        @Override
        public String validate(ServerCommandSource serverCommandSource, ParsedRule<String> parsedRule, String s, String s2) {
            if ((serverCommandSource == null || parsedRule.get().equals(s)) && Arrays.asList(carefulBreakOptions).contains(s))
                carefulBreak = s;
            return s;
        }
    }
    /* End CarefulBreak stuff */
}
