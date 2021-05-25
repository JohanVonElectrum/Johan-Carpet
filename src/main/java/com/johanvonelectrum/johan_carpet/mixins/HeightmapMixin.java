package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(Heightmap.class)
public abstract class HeightmapMixin {

    @Shadow @Final public Predicate<BlockState> blockPredicate;
    @Shadow public abstract void set(int x, int z, int height);
    @Shadow public abstract int get(int x, int z);
    @Shadow @Final private Chunk chunk;

    /**
     * @author JohanVonElectrum
     */
    @Overwrite
    public static void populateHeightmaps(Chunk chunk, Set<Heightmap.Type> types) {
        int i = types.size();
        ObjectList<Heightmap> objectList = new ObjectArrayList(i);
        ObjectListIterator<Heightmap> objectListIterator = objectList.iterator();
        int j = chunk.getHighestNonEmptySectionYOffset() + 16;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int k = 0; k < 16; ++k) {
            for(int l = 0; l < 16; ++l) {
                Iterator var9 = types.iterator();

                while(var9.hasNext()) {
                    Heightmap.Type type = (Heightmap.Type)var9.next();
                    objectList.add(chunk.getHeightmap(type));
                }

                for(int m = j - 1; m >= 0; --m) {
                    if (m > JohanSettings.maxHeightmap)
                        continue;
                    mutable.set(k, m, l);
                    BlockState blockState = chunk.getBlockState(mutable);
                    if (!blockState.isOf(Blocks.AIR)) {
                        while(objectListIterator.hasNext()) {
                            Heightmap heightmap = (Heightmap)objectListIterator.next();
                            if (heightmap.blockPredicate.test(blockState)) {
                                heightmap.set(k, l, m + 1);
                                objectListIterator.remove();
                            }
                        }

                        if (objectList.isEmpty()) {
                            break;
                        }

                        objectListIterator.back(i);
                    }
                }
            }
        }
    }

    /**
     * @author JohanVonElectrum
     */
    @Overwrite
    public boolean trackUpdate(int x, int y, int z, BlockState state) {
        int i = this.get(x, z);
        if (y > i - 2 && y <= JohanSettings.maxHeightmap) {
            if (this.blockPredicate.test(state)) {
                if (y >= i) {
                    this.set(x, z, y + 1);
                    return true;
                }
            } else if (i - 1 == y) {
                BlockPos.Mutable mutable = new BlockPos.Mutable();

                for (int j = y - 1; j >= 0; --j) {
                    mutable.set(x, j, z);
                    if (this.blockPredicate.test(this.chunk.getBlockState(mutable))) {
                        this.set(x, z, j + 1);
                        return true;
                    }
                }

                this.set(x, z, 0);
                return true;
            }

        }
        return false;
    }
}
