package net.johan.johan_carpet.mixins;

import net.johan.johan_carpet.JohanSettings;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {

    @Redirect(method = "method_30032", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z"))
    private static boolean canPlaceAt(BlockState blockState, WorldView world, BlockPos pos){
        if(JohanSettings.oldFlintAndSteelBehavior)
            return true;
        return blockState.canPlaceAt(world,pos);
    }

}
