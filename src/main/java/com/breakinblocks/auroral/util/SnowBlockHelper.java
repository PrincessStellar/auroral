package com.breakinblocks.auroral.util;

import com.breakinblocks.auroral.registry.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class SnowBlockHelper {

    private SnowBlockHelper() {}

    public static boolean isSnow(BlockState state) {
        return state.is(Blocks.SNOW) ||
               state.is(Blocks.SNOW_BLOCK) ||
               state.is(Blocks.POWDER_SNOW);
    }

    public static boolean isSolidSnow(BlockState state) {
        return state.is(Blocks.SNOW_BLOCK) ||
               state.is(Blocks.POWDER_SNOW);
    }

    public static boolean isSnowLayer(BlockState state) {
        return state.is(Blocks.SNOW);
    }

    public static boolean isBloomSurface(BlockState state) {
        return isSnow(state) ||
               state.is(ModBlocks.SHIMMERING_ICE.get());
    }

    public static boolean isSnowOrIce(BlockState state) {
        return isSnow(state) ||
               state.is(ModBlocks.SHIMMERING_ICE.get());
    }

    public static boolean canTillToShimmerSoil(BlockState state) {
        return state.is(Blocks.SNOW_BLOCK) ||
               state.is(Blocks.POWDER_SNOW) ||
               state.is(Blocks.DIRT) ||
               state.is(Blocks.GRASS_BLOCK) ||
               state.is(Blocks.DIRT_PATH) ||
               state.is(Blocks.COARSE_DIRT) ||
               state.is(Blocks.ROOTED_DIRT);
    }
}
