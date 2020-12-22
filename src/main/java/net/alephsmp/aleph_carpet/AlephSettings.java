package net.alephsmp.aleph_carpet;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.*;

public class AlephSettings {
    public static final String AlephSettingsCategory = "aleph-addon";
    public static final String EndSettingsCategory = "aleph-end-features";
    @Rule(
            desc = "Enables old donkey / llmama dupe bug.",
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

    @Rule(
            desc = "Toggle for projectiles are ticked the whole time",
            category = { AlephSettingsCategory, FEATURE }
    )
    public static boolean keepProjectilesTicked = false;

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
            desc = "Fishes only can spawn between y:45 and y:63, both excluded",
            category = { AlephSettingsCategory, SURVIVAL, BUGFIX }
    )
    public static boolean seaLevelFishes = false;
}
