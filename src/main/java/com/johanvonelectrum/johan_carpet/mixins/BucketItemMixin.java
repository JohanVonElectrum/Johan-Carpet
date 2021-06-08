package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    @Shadow @Final private Fluid fluid;

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack itemStack = user.getStackInHand(hand);
        boolean sponge = JohanSettings.bucketSponge &&
                itemStack.getCount() == 1 && this.fluid == Fluids.EMPTY &&
                itemStack.hasCustomName() && itemStack.getName().asString().equals("sponge");

        if (sponge) {
            doSpongeStuff(world, user);
            cir.setReturnValue(TypedActionResult.pass(itemStack));
        }
    }

    private void doSpongeStuff(World world, PlayerEntity user) {
        BlockPos pos = user.getBlockPos();
        BlockPos blockPos;
        for (int x = -3; x < 4; x++) {
            for (int y = -3; y < 4; y++) {
                for (int z = -3; z < 4; z++) {
                    blockPos = pos.add(x, y, z);
                    BlockState state = world.getBlockState(blockPos);
                    Block block = state.getBlock();
                    if (block instanceof FluidDrainable)
                        if (((FluidDrainable) block).tryDrainFluid(world, blockPos, state) != Fluids.EMPTY)
                            user.incrementStat(Stats.USED.getOrCreateStat((BucketItem) (Object) this));
                }
            }
        }
    }

}
