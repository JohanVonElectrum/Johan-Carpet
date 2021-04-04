package net.alephsmp.aleph_carpet.mixins;

import carpet.CarpetServer;
import net.alephsmp.aleph_carpet.AlephSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import static net.minecraft.block.Block.*;

@Mixin(Block.class)
public abstract class BlockMixin implements ItemConvertible {

    /**
     * CarefulBreak
     * Adapted by JohanVonElectrum
     *   TODO: cleanup code
     *      bug fixing with multi-blocks
     * @author whoImT & JohanVonElectrum
     * @reason carefulBreak
     */

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At("HEAD"), cancellable = true)
    private static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo ci) {
        if (world instanceof ServerWorld && entity instanceof PlayerEntity) {
            if (
                    (AlephSettings.carefulBreak.equals("sneaking") && entity.isInSneakingPose()) ||
                    (AlephSettings.carefulBreak.equals("no-sneaking") && !entity.isInSneakingPose()) ||
                    AlephSettings.carefulBreak.equals("always")
            ) {
                getDroppedStacks(state, (ServerWorld) world, pos, blockEntity, entity, stack).forEach((itemStack) -> {
                    Item item = itemStack.getItem();
                    int itemAmount = itemStack.getCount();
                    if (((PlayerEntity) entity).inventory.insertStack(itemStack)) {
                        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, (CarpetServer.rand.nextFloat() - CarpetServer.rand.nextFloat()) * 1.4F + 2.0F);
                        ((PlayerEntity) entity).increaseStat(Stats.PICKED_UP.getOrCreateStat(item), itemAmount);
                    } else {
                        Block.dropStack(world, pos, itemStack);
                    }
                });
                state.onStacksDropped((ServerWorld)world, pos, stack);
                ci.cancel();
            }
        }
    }

    //carefulBreak Multi-Blocks
    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci) {
        if (
                (AlephSettings.carefulBreak.equals("sneaking") && player.isInSneakingPose()) ||
                (AlephSettings.carefulBreak.equals("no-sneaking") && !player.isInSneakingPose()) ||
                AlephSettings.carefulBreak.equals("always")
        ) {
            if (Blocks.PISTON_HEAD.equals(state.getBlock())) {
                Direction direction = state.get(FacingBlock.FACING).getOpposite();
                BlockPos pos2 = pos.offset(direction);
                BlockState blockState = world.getBlockState(pos2);
                Block block = world.getBlockState(pos2).getBlock();
                if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON && blockState.get(PistonBlock.EXTENDED)) {
                    Block.dropStacks(blockState, world, pos2, null, player, player.getMainHandStack());
                    world.removeBlock(pos2, false);
                }
            } /*else if (state.getBlock() instanceof BedBlock) {
                Direction direction = state.get(HorizontalFacingBlock.FACING);
                pos = pos.offset(direction);
                BlockState blockState = world.getBlockState(pos);
                Block block = world.getBlockState(pos).getBlock();
                if (block instanceof BedBlock && BedBlock.getBedPart(blockState).equals(BedPart.HEAD)) {
                    Block.dropStacks(blockState, world, pos, null, player, player.getMainHandStack());
                    world.removeBlock(pos, false);
                }
            }*/
        }
    }
}