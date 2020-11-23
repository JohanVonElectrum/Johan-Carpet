package net.alephsmp.aleph_carpet;

import carpet.settings.Rule;
import static carpet.settings.RuleCategory.FEATURE;

public class AlephSimpleSettings {
    public static final String AlephSettingsCategory = "aleph-addon";
    public static final String EndSettingsCategory = "aleph-end-features";
    @Rule(
            desc = "Toggle for end gateway cooldown",
            category = {AlephSettingsCategory, FEATURE, EndSettingsCategory}
    )
    public static boolean endGatewayCooldown = true;

    @Rule(
            desc = "Toggle for the main end island structure generation, turns off portal, egg, obsidian pillars, gateways and crystals",
            category = {AlephSettingsCategory, FEATURE, EndSettingsCategory}
    )
    public static boolean endMainIslandStructureGen = true;

    @Rule(
            desc = "Toggle for end obsidian platform generation excluding players",
            category = {AlephSettingsCategory, FEATURE, EndSettingsCategory}
    )
    public static boolean noObsidianPlatform = false;
}
