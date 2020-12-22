package net.alephsmp.aleph_carpet.mixins;

import net.alephsmp.aleph_carpet.AlephSettings;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.boss.dragon.EnderDragonSpawnState;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.entity.boss.dragon.EnderDragonSpawnState.SUMMONING_DRAGON;

@Mixin(EnderDragonFight.class)
public abstract class FightManagerMixin {

    @Shadow private EnderDragonSpawnState dragonSpawnState;
    @Shadow private ServerBossBar bossBar;
    @Shadow protected abstract void setSpawnState(EnderDragonSpawnState enderDragonSpawnState);

    private EnderDragonSpawnState tempDragonSpawnState;

    @Inject(method="tick", at=@At("HEAD"))
    public void suppressTowerExplosionAndBlockDeletion(CallbackInfo ci) {
        if (!AlephSettings.endMainIslandStructureGen
            && !this.bossBar.getPlayers().isEmpty()
            && this.dragonSpawnState != null
            && this.dragonSpawnState == EnderDragonSpawnState.SUMMONING_PILLARS) {
            this.setSpawnState(SUMMONING_DRAGON);   // THE ORDER OF THESE STATEMENTS IS VERY IMPORTANT
            tempDragonSpawnState = dragonSpawnState;
            dragonSpawnState = null;

        }
    }

    @Inject(method="tick", at=@At("RETURN"))
    public void suppressTowerExplosionAndBlockDeletionCleanup(CallbackInfo ci) {
        if (tempDragonSpawnState != null) {
            dragonSpawnState = tempDragonSpawnState;
            tempDragonSpawnState = null;
        }
    }

    private void cancelIfFeatureFalse(CallbackInfo ci) {
        if (!AlephSettings.endMainIslandStructureGen) {
            ci.cancel();
        }
    }

    @Inject(method="generateEndGateway", at=@At("HEAD"), cancellable = true)
    public void suppressGateway(CallbackInfo callbackInfo) {
        cancelIfFeatureFalse(callbackInfo);
    }

    @Inject(method="generateEndGateway", at=@At("HEAD"), cancellable = true)
    public void suppressGateway2(BlockPos blockPos, CallbackInfo callbackInfo) {
        cancelIfFeatureFalse(callbackInfo);
    }

    @Inject(method="generateEndPortal", at=@At("HEAD"), cancellable = true)
    public void suppressEndPortal(boolean previouslyKilled, CallbackInfo ci) {
        cancelIfFeatureFalse(ci);
    }

    @Inject(method="resetEndCrystals", at=@At("HEAD"), cancellable = true)
    public void suppressEndCrystals(CallbackInfo ci) {
        cancelIfFeatureFalse(ci);
    }
}
