package com.johanvonelectrum.johan_carpet.mixins;

import com.johanvonelectrum.johan_carpet.JohanSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ItemFrameEntity.class, priority = 900)
public class ItemFrameEntityMixin extends AbstractDecorationEntity {

    @Shadow
    private boolean fixed;
    @Shadow
    private float itemDropChance;
    @Shadow
    private void dropHeldStack(@Nullable Entity entity, boolean alwaysDrop) {}
    @Shadow
    private void removeFromFrame(ItemStack map) {}
    private int removed = 0;

    protected ItemFrameEntityMixin(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();
        ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;
        if (JohanSettings.itemFrameDelay > 0) {
            if (removed >= JohanSettings.itemFrameDelay) {
                itemFrame.setHeldItemStack(ItemStack.EMPTY);
                removed = 0;
            } else if (removed > 0) {
                removed++;
            }
        }
    }

    /**
     * @author JohanVonElectrum
     * @reason itemFrameDelay
     */
    @Overwrite
    public boolean canStayAttached() {
        ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;

        BlockState blockState = itemFrame.world.getBlockState(this.attachmentPos.offset(this.facing.getOpposite()));
        return this.fixed || ((blockState.getMaterial().isSolid() || this.facing.getAxis().isHorizontal() && AbstractRedstoneGateBlock.isRedstoneGate(blockState)) && itemFrame.world.isSpaceEmpty(itemFrame) && this.world.getOtherEntities(this, this.getBoundingBox(), PREDICATE).isEmpty());
    }

    /**
     * @author JohanVonElectrum
     * @reason itemFrameDelay
     */
    @Overwrite
    public boolean damage(DamageSource source, float amount) {
        ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;
        if (this.fixed) {
            return (source == DamageSource.OUT_OF_WORLD || source.isSourceCreativePlayer()) && super.damage(source, amount);
        } else if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!source.isExplosive() && !itemFrame.getHeldItemStack().isEmpty()) {
            if (!this.world.isClient) {
                this.dropHeldStackDamage(source.getAttacker(), false);
                this.playSound(SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
            }

            return true;
        } else {
            return super.damage(source, amount);
        }
    }

    private void dropHeldStackDamage(@Nullable Entity entity, boolean alwaysDrop) {
        ItemFrameEntity itemFrame = (ItemFrameEntity) (Object) this;
        if (!this.fixed && this.removed == 0) {
            ItemStack itemStack = itemFrame.getHeldItemStack();
            if (JohanSettings.itemFrameDelay == 0)
                itemFrame.setHeldItemStack(ItemStack.EMPTY);
            if (!this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                if (entity == null) {
                    removeFromFrame(itemStack);
                }
            } else {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity playerEntity = (PlayerEntity) entity;
                    if (playerEntity.abilities.creativeMode) {
                        itemFrame.setHeldItemStack(ItemStack.EMPTY);
                        return;
                    }
                }

                if (alwaysDrop) {
                    this.dropItem(Items.ITEM_FRAME);
                }

                if (!itemStack.isEmpty()) {
                    itemStack = itemStack.copy();
                    if (JohanSettings.itemFrameDelay > 0) {
                        removed = 1;
                    } else
                        this.removeFromFrame(itemStack);
                    if (this.random.nextFloat() < itemDropChance) {
                        this.dropStack(itemStack);
                    }
                }
            }
        }
    }

    @Override
    public int getWidthPixels() {
        return 12;
    }

    @Override
    public int getHeightPixels() {
        return 12;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {
        this.playSound(SoundEvents.ENTITY_ITEM_FRAME_BREAK, 1.0F, 1.0F);
        this.dropHeldStack(entity, true);
    }

    @Override
    public void onPlace() {
        this.playSound(SoundEvents.ENTITY_ITEM_FRAME_PLACE, 1.0F, 1.0F);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, this.getType(), this.facing.getId(), this.getDecorationBlockPos());
    }
}
