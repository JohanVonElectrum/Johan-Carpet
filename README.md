# aleph-carpet
A carpet extension made for AlephSMP in Minecraft 1.16

This is a fork of FractalCarpetAddon. When Aleph changed from 1.15 to 1.16 it also underwent a name change from FractalSMP. This was the perfect opportunity to justify a repository move to the organization page to go along with the version update and name change.
## Current Features
### noObsidianPlatform
entities do not generate the obsidian platform in the end, except players
* Type: `boolean`
* Default value: `false`
* Required options: `true`, `false`
* Categories: `aleph-addon`, `feature`, `aleph-end-features`
### endMainIslandStructureGen
no end spikes, portal, crystal, egg or gateway generation when false
* Type: `boolean`
* Default value: `true`
* Required options: `true`, `false`
* Categories: `aleph-addon`, `feature`, `aleph-end-features`
### endGatewayCooldown
no end gateway cooldown rule
* Type: `boolean`
* Default value: `true`
* Required options: `true`, `false`
* Categories: `aleph-addon`, `feature`, `aleph-end-features`
### keepProjectilesTicked
Keep projectiles ticked in unloaded chunks.
* Type: `boolean`
* Default value: `false`
* Required options: `true`, `false`
* Categories: `aleph-addon`, `feature` 
## Features in Development
- Experimental ender pearl ticking for cannons 
## 1.16 Roadmap
-   CarefulBreak // I have the feature code for 1.16 we only need to implement it in FractalCarpet
-   StackeableShulkerBoxesInInventories // But not when shulkers get into a hopper from a chest or dispensers or droppers
