package com.breakinblocks.auroral.item;

import com.breakinblocks.auroral.registry.ModBlocks;
import com.breakinblocks.auroral.util.SnowBlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Shimmersteel Hoe with inherent Silk Touch and snow-to-shimmer-soil tilling.
 *
 * Special abilities:
 * - Inherent Silk Touch when harvesting crops/blocks
 * - Can till snow blocks into Shimmer Soil (right-click)
 * - Standard hoe tilling behavior on dirt/grass
 *
 * The silk touch behavior is implemented via event handler in
 * {@link com.breakinblocks.auroral.events.ShimmersteelEventHandler}.
 */
public class ShimmersteelHoeItem extends Item {

    public ShimmersteelHoeItem(Properties properties) {
        super(properties.hoe(ModToolTiers.SHIMMERSTEEL, -2.0f, -1.0f));
    }

    public boolean hasInherentSilkTouch() {
        return true;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();

        if (SnowBlockHelper.isSnowLayer(state)) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);

            if (SnowBlockHelper.canTillToShimmerSoil(belowState)) {
                BlockState shimmerSoil = ModBlocks.SHIMMER_SOIL.get().defaultBlockState();
                if (!level.isClientSide()) {
                    level.removeBlock(pos, false);
                    level.setBlock(belowPos, shimmerSoil, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, belowPos, GameEvent.Context.of(player, shimmerSoil));
                }
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                damageItem(context, player);
                return InteractionResult.SUCCESS;
            }
        }
        else if (SnowBlockHelper.isSolidSnow(state)) {
            if (level.getBlockState(pos.above()).isAir()) {
                BlockState shimmerSoil = ModBlocks.SHIMMER_SOIL.get().defaultBlockState();
                if (!level.isClientSide()) {
                    level.setBlock(pos, shimmerSoil, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, shimmerSoil));
                }
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                damageItem(context, player);
                return InteractionResult.SUCCESS;
            }
        }
        else if (state.is(Blocks.FARMLAND)) {
            BlockState shimmerSoil = ModBlocks.SHIMMER_SOIL.get().defaultBlockState();
            int moisture = state.getValue(net.minecraft.world.level.block.FarmlandBlock.MOISTURE);
            shimmerSoil = shimmerSoil.setValue(net.minecraft.world.level.block.FarmlandBlock.MOISTURE, moisture);

            if (!level.isClientSide()) {
                level.setBlock(pos, shimmerSoil, 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, shimmerSoil));
            }
            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
            damageItem(context, player);
            return InteractionResult.SUCCESS;
        }
        else if (SnowBlockHelper.canTillToShimmerSoil(state)) {
            if (level.getBlockState(pos.above()).isAir()) {
                BlockState shimmerSoil = ModBlocks.SHIMMER_SOIL.get().defaultBlockState();
                if (!level.isClientSide()) {
                    level.setBlock(pos, shimmerSoil, 11);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, shimmerSoil));
                }
                level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                damageItem(context, player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static void damageItem(UseOnContext context, Player player) {
        if (player != null) {
            context.getItemInHand().hurtAndBreak(1, player,
                context.getHand() == net.minecraft.world.InteractionHand.MAIN_HAND
                    ? net.minecraft.world.entity.EquipmentSlot.MAINHAND
                    : net.minecraft.world.entity.EquipmentSlot.OFFHAND);
        }
    }
}
