package com.breakinblocks.auroral.registry;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.block.*;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Auroral.MOD_ID);

    public static final DeferredBlock<GlacialBasinBlock> GLACIAL_BASIN = BLOCKS.registerBlock("glacial_basin",
        props -> new GlacialBasinBlock(props
            .strength(2.0f, 6.0f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .lightLevel(state -> state.getValue(GlacialBasinBlock.AURA_LEVEL) * 3)
        ));

    public static final DeferredBlock<ColdBrewingStandBlock> COLD_BREWING_STAND = BLOCKS.registerBlock("cold_brewing_stand",
        props -> new ColdBrewingStandBlock(props
            .strength(0.5f)
            .sound(SoundType.METAL)
            .noOcclusion()
            .lightLevel(state -> 1)
        ));

    public static final DeferredBlock<HearthwoodLogBlock> HEARTHWOOD_LOG = BLOCKS.registerBlock("hearthwood_log",
        props -> new HearthwoodLogBlock(props
            .mapColor(MapColor.WOOD)
            .strength(2.0f)
            .sound(SoundType.WOOD)
            .noOcclusion()
            .lightLevel(state -> state.getValue(HearthwoodLogBlock.LIT) ? 15 : 0)
        ));

    public static final DeferredBlock<ShimmeringIceBlock> SHIMMERING_ICE = BLOCKS.registerBlock("shimmering_ice",
        props -> new ShimmeringIceBlock(props
            .mapColor(MapColor.ICE)
            .strength(0.5f)
            .friction(0.98f)
            .sound(SoundType.GLASS)
            .noOcclusion()
            .lightLevel(state -> 8)
        ));

    public static final DeferredBlock<GlowLeekBlock> GLOW_LEEK = BLOCKS.registerBlock("glow_leek",
        props -> new GlowLeekBlock(props
            .mapColor(MapColor.PLANT)
            .noCollision()
            .instabreak()
            .sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY)
        ));

    public static final DeferredBlock<AuroraBloomBlock> AURORA_BLOOM = BLOCKS.registerBlock("aurora_bloom",
        props -> new AuroraBloomBlock(props
            .mapColor(MapColor.SNOW)
            .noCollision()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .randomTicks()
        ));

    public static final DeferredBlock<EnderBloomBlock> ENDER_BLOOM = BLOCKS.registerBlock("ender_bloom",
        props -> new EnderBloomBlock(props
            .mapColor(MapColor.COLOR_PURPLE)
            .noCollision()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .randomTicks()
        ));

    public static final DeferredBlock<AuroraBloomDecorativeBlock> AURORA_BLOOM_DECORATIVE = BLOCKS.registerBlock("aurora_bloom_decorative",
        props -> new AuroraBloomDecorativeBlock(props
            .mapColor(MapColor.SNOW)
            .noCollision()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY)
            .lightLevel(state -> 8)
        ));

    public static final DeferredBlock<FlowerPotBlock> POTTED_AURORA_BLOOM_DECORATIVE = BLOCKS.registerBlock("potted_aurora_bloom_decorative",
        props -> new FlowerPotBlock(AURORA_BLOOM_DECORATIVE.get(), props
            .instabreak()
            .noOcclusion()
            .lightLevel(state -> 8)
        ));

    public static final DeferredBlock<AuroraLanternBlock> AURORA_LANTERN = BLOCKS.registerBlock("aurora_lantern",
        props -> new AuroraLanternBlock(props
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .strength(0.3f)
            .sound(SoundType.LANTERN)
            .noOcclusion()
            .lightLevel(state -> 15)
        ));

    public static final DeferredBlock<SnowAngelBlock> SNOW_ANGEL = BLOCKS.registerBlock("snow_angel",
        props -> new SnowAngelBlock(props
            .mapColor(MapColor.SNOW)
            .noCollision()
            .instabreak()
            .sound(SoundType.SNOW)
            .pushReaction(PushReaction.DESTROY)
        ));

    public static final DeferredBlock<ShimmerSoilBlock> SHIMMER_SOIL = BLOCKS.registerBlock("shimmer_soil",
        props -> new ShimmerSoilBlock(props
            .mapColor(MapColor.COLOR_LIGHT_BLUE)
            .strength(0.6f)
            .sound(SoundType.GRAVEL)
            .randomTicks()
        ));
}
