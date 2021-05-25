package com.johanvonelectrum.johan_carpet.mixins;

import carpet.fakes.TntEntityInterface;
import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static net.minecraft.world.explosion.Explosion.getExposure;

@Mixin(Explosion.class)
public class ExplosionMixin {

    @Final @Shadow
    private static final ExplosionBehavior field_25818 = new ExplosionBehavior();
    @Final @Shadow
    private boolean createFire;
    @Final @Shadow
    private Explosion.DestructionType destructionType;
    @Final @Shadow
    private Random random;
    @Final @Shadow
    private World world;
    @Final @Shadow
    private double x;
    @Final @Shadow
    private double y;
    @Final @Shadow
    private double z;
    @Final @Shadow
    @Nullable
    private Entity entity;
    @Final @Shadow
    private float power;
    @Final @Shadow
    private DamageSource damageSource;
    @Final @Shadow
    private ExplosionBehavior behavior;
    @Final @Shadow
    private List<BlockPos> affectedBlocks;
    @Final @Shadow
    private Map<PlayerEntity, Vec3d> affectedPlayers;

    @Inject(method = "collectBlocksAndDamageEntities", at = @At("HEAD"), cancellable = true)
    public void collectBlocksAndDamageEntities(CallbackInfo ci) {
        if (JohanSettings.logTNTMomentum || JohanSettings.ftlTNT) {
            float q = this.power * 2.0F;
            int k = MathHelper.floor(this.x - (double) q - 1.0D);
            int l = MathHelper.floor(this.x + (double) q + 1.0D);
            int t = MathHelper.floor(this.y - (double) q - 1.0D);
            int u = MathHelper.floor(this.y + (double) q + 1.0D);
            int v = MathHelper.floor(this.z - (double) q - 1.0D);
            int w = MathHelper.floor(this.z + (double) q + 1.0D);
            List<Entity> list = this.world.getOtherEntities(this.entity, new Box(k, t, v, l, u, w));
            Vec3d vec3d = new Vec3d(this.x, this.y, this.z);

            for (int x = 0; x < list.size(); ++x) {
                Entity entity = list.get(x);
                if (!entity.isImmuneToExplosion()) {
                    double y = MathHelper.sqrt(entity.squaredDistanceTo(vec3d)) / q;
                    if (y <= 1.0D) {
                        double z = entity.getX() - this.x;
                        double aa = (entity instanceof TntEntity ? entity.getY() : entity.getEyeY()) - this.y;
                        double ab = entity.getZ() - this.z;
                        double ac = MathHelper.sqrt(z * z + aa * aa + ab * ab);
                        if (ac != 0.0D) {
                            z /= ac;
                            aa /= ac;
                            ab /= ac;
                            double ad = getExposure(vec3d, entity);
                            double af = (1.0D - y) * ad;

                            Vec3d vel = entity.getVelocity();
                            Vec3d delta = new Vec3d(z * af, aa * af, ab * af);
                            Vec3d res = vel.add(delta.multiply(entity instanceof TntEntity ? ((TntEntityInterface) this.entity).getMergedTNT() : 1));

                            if (JohanSettings.ftlTNT)
                                entity.setVelocity(res);

                            if (JohanSettings.logTNTMomentum) {
                                System.out.println(z * ac + ", " + aa * ac + ", " + ab * ac + " | " + ad + " -> " + af);
                                System.out.println(res.toString() + " = " + vel.toString() + " + " + delta.toString());
                            }
                        }
                    }
                }
            }
            if (JohanSettings.ftlTNT && entity instanceof TntEntity && ((TntEntityInterface) entity).getMergedTNT() > 1)
                ci.cancel();
        }
    }
}
