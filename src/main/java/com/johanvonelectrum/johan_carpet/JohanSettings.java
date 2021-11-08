package com.johanvonelectrum.johan_carpet;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import carpet.settings.Validator;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Arrays;

import static carpet.settings.RuleCategory.*;

public class JohanSettings {
    public static final String johanSettingsCategory = "johan-addon";
    public static final String EndSettingsCategory = "johan-end-features";
    public static final String CHEAT = "cheats";
    public static final String ENCHANTMENT = "enchantment";

    @Rule(
            desc = "Only show the version of Johan Carpet Addon only when there is an update.",
            category = { johanSettingsCategory, FEATURE }
    )
    public static boolean onlyUpdateWarn = false;

    /* ===== Begin TheEnd Rules ===== */

    @Rule(
            desc = "Toggle for end gateway cooldown.",
            category = { johanSettingsCategory, EndSettingsCategory, FEATURE }
    )
    public static boolean endGatewayCooldown = true;

    @Rule(
            desc = "Toggle for the main end island structure generation, turns off portal, egg, obsidian pillars, gateways and crystals.",
            category = { johanSettingsCategory, EndSettingsCategory, FEATURE }
    )
    public static boolean endMainIslandStructureGen = true;

    @Rule(
            desc = "Toggle for end obsidian platform generation excluding players.",
            category = { johanSettingsCategory, EndSettingsCategory, FEATURE }
    )
    public static boolean noObsidianPlatform = false;

    /* ===== End TheEnd Rules ===== */

    /* ===== Begin Cannon Rules ===== */

    /* Begin keepProjectilesTicked stuff */
    private static final String[] keepProjectilesTickedOptions = new String[] { "default", "all", "player-only", "enderpearls" };
    @Rule(
            desc = "Toggle for projectiles are ticked the whole time.",
            category = { johanSettingsCategory, CHEAT },
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
            desc = "Debug TNT momentum transfer to enderpearls in console.",
            category = { johanSettingsCategory, CREATIVE }
    )
    public static boolean logTNTMomentum = false;

    @Rule(
            desc = "TNT optimized for large amounts in Cannons.",
            category = { johanSettingsCategory, SURVIVAL, OPTIMIZATION }
    )
    public static boolean ftlTNT = false;

    @Rule(
            desc = "Hopper entity processing chunk loading.",
            category = { johanSettingsCategory, SURVIVAL, FEATURE }
    )
    public static boolean hopperLoading = false;

    /* ===== End Cannon Rules ===== */

    /* ===== Begin Commands Rules ===== */

    @Rule(
            desc = "Enables /location command to know where is a player.",
            category = { johanSettingsCategory, COMMAND }
    )
    public static boolean commandLocation = false;

    @Rule(
            desc = "Enables /signal command to get a container with comparator value.",
            category = { johanSettingsCategory, CREATIVE, COMMAND }
    )
    public static boolean commandSignal = false;

    @Rule(
            desc = "Enables /enderchest command to open the enderchest of a player.",
            category = { johanSettingsCategory, FEATURE, COMMAND }
    )
    public static boolean commandEnderchest = false;

    @Rule(
            desc = "Enables /total command to know the total sum of a scoreboard.",
            category = { johanSettingsCategory, COMMAND }
    )
    public static boolean commandTotal = false;

    @Rule(
            desc = "Enables /computation command to test redstone contraptions.",
            category = { johanSettingsCategory, COMMAND }
    )
    public static boolean commandComputation = false;

    @Rule(
            desc = "Enables /batch command to execute commands multiple times.",
            category = { johanSettingsCategory, COMMAND }
    )
    public static boolean commandBatch = false;

    @Rule(
            desc = "Enables /item command to get item data.",
            category = { johanSettingsCategory, COMMAND }
    )
    public static boolean commandItem = false;

    /* ===== End Commands Rules ===== */

    /* ===== Begin Score Rules ===== */

    @Rule(
            desc = "Bots don't appear on scoreboards and do not count in the total.",
            category = { johanSettingsCategory, SURVIVAL, FEATURE }
    )
    public static boolean filterBotsInScores = false;

    @Rule(
            desc = "The scoreboard total appears on the scoreboard.",
            category = { johanSettingsCategory, SURVIVAL, FEATURE }
    )
    public static boolean totalScore = false;

    /* ===== End Score Rules ===== */

    /* ===== Begin Entity Rules ===== */

    @Rule(
            desc = "Force shulkers to teleport when stay in invalid positions.",
            category = { johanSettingsCategory, EndSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean forceShulkerTeleport = false;

    @Rule(
            desc = "Fishes only can spawn between y:45 and y:63, both excluded.",
            category = { johanSettingsCategory, SURVIVAL, BUGFIX }
    )
    public static boolean seaLevelFishes = false;

    /* Begin SpawnMaxY stuff */
    @Rule(
            desc = "Set the max value possible for heightmap. USE AT YOUR OWN RISK!",
            category = { johanSettingsCategory, SURVIVAL, EXPERIMENTAL, OPTIMIZATION, CHEAT },
            strict = false,
            validate = { SpawnMaxYValidator.class }
    )
    public static int maxHeightmap = 255;

    private static class SpawnMaxYValidator extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource serverCommandSource, ParsedRule<Integer> parsedRule, Integer integer, String s) {
            return integer >= 0 && integer < 256 ? integer : null;

        }
    }
    /* End SpawnMaxY stuff */

    @Rule(
            desc = "Enables old donkey / llama dupe bug.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean llamaDupeExploit = false;

    /* ===== End Entity Rules ===== */

    /* ===== Begin PlayerTweaks Rules =====*/

    @Rule(
            desc = "Backports 1.12 flint and steel behavior. Flint and steel can be used for updating observers / buds.",
            category = { johanSettingsCategory, SURVIVAL, FEATURE }
    )
    public static boolean oldFlintAndSteelBehavior = false;

    /* Begin CarefulBreak stuff */
    private static final String[] carefulBreakOptions = new String[] { "never", "always", "sneaking", "no-sneaking" };
    @Rule(
            desc = "Places the mined block in the player inventory when sneaking.",
            category = { johanSettingsCategory, SURVIVAL, FEATURE, EXPERIMENTAL },
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

    @Rule(
            desc = "Emerald ore acts as an update suppressor.",
            category = { johanSettingsCategory, CREATIVE, CHEAT }
    )
    public static boolean oreUpdateSuppressor = false;

    @Rule(
            desc = "Enable the possibility to store shulkerboxes inside shulkerboxes.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean shulkerInception = false;

    @Rule(
            desc = "Disable enchantment compatibility checks.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT, ENCHANTMENT }
    )
    public static boolean compatibleEnchantments = false;

    @Rule(
            desc = "Disable enchantment max level cap.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT, ENCHANTMENT }
    )
    public static boolean disableEnchantmentCap = false;

    @Rule(
            desc = "Disable anvil max xp cap.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT, ENCHANTMENT }
    )
    public static boolean disableAnvilXpLimit = false;

    @Rule(
            desc = "Makes merchant offers unlimited.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean infiniteTrades = false;

    @Rule(
            desc = "All items have 64 stack size.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean allStackable = false;

    @Rule(
            desc = "Using a bucket renamed to \"sponge\" removes fluids in the area.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean bucketSponge = false;

    @Rule(
            desc = "Sponges remove lava too.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean lavaSponge = false;

    @Rule(
            desc = "Sneak to repair items with your stored xp.",
            category = { johanSettingsCategory, SURVIVAL, CHEAT }
    )
    public static boolean xpBarMending = false;

    @Rule(
            desc = "Insta-kill entities when you are in creative mode.",
            category = { johanSettingsCategory, CREATIVE, FEATURE }
    )
    public static boolean creativeKill = false;

    @Rule(
            desc = "Items are stored directly inside shulkers in inventory when players collect them.",
            category = { johanSettingsCategory, SURVIVAL, FEATURE, EXPERIMENTAL }
    )
    public static boolean shulkerInsert = false;

    /* Begin itemFrameDelay stuff */
    @Rule(
            desc = "Item frame reset delay.",
            category = { johanSettingsCategory, SURVIVAL, EXPERIMENTAL, CHEAT },
            options = {"0", "3", "6", "22"},
            strict = false,
            validate = { itemFrameDelayValidator.class }
    )
    public static int itemFrameDelay = 0;

    private static class itemFrameDelayValidator extends Validator<Integer> {
        @Override
        public Integer validate(ServerCommandSource serverCommandSource, ParsedRule<Integer> parsedRule, Integer integer, String s) {
            return integer >= 0 ? integer : null;
        }
    }
    /* End itemFrameDelay stuff */

    /* ===== End PlayerTweaks Rules =====*/

}
