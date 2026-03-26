package com.breakinblocks.auroral.item;

import com.breakinblocks.auroral.registry.ModTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class ShimmersteelPickaxeItem extends Item {

    private static final Set<Block> GEM_BLOCKS = Set.of(
        Blocks.DIAMOND_ORE,
        Blocks.DEEPSLATE_DIAMOND_ORE,
        Blocks.EMERALD_ORE,
        Blocks.DEEPSLATE_EMERALD_ORE,
        Blocks.LAPIS_ORE,
        Blocks.DEEPSLATE_LAPIS_ORE,
        Blocks.REDSTONE_ORE,
        Blocks.DEEPSLATE_REDSTONE_ORE,
        Blocks.NETHER_QUARTZ_ORE,
        Blocks.AMETHYST_CLUSTER,
        Blocks.LARGE_AMETHYST_BUD,
        Blocks.MEDIUM_AMETHYST_BUD,
        Blocks.SMALL_AMETHYST_BUD
    );

    public ShimmersteelPickaxeItem(Properties properties) {
        super(properties.pickaxe(ModToolTiers.SHIMMERSTEEL, 1.0f, -2.8f));
    }

    public static boolean isGemBlock(BlockState state) {
        if (GEM_BLOCKS.contains(state.getBlock())) {
            return true;
        }
        return state.is(ModTags.Blocks.GEM_ORES);
    }

    public static int getEffectiveFortuneLevel(BlockState state) {
        if (isGemBlock(state)) {
            return 3;
        }
        return 0;
    }
}
