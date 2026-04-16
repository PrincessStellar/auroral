package com.breakinblocks.auroral.datagen;

import com.breakinblocks.auroral.registry.ModBlocks;
import com.breakinblocks.auroral.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import com.breakinblocks.auroral.block.AuroraBloomBlock;
import com.breakinblocks.auroral.block.EnderBloomBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Data generator for loot tables (block drops).
 */
public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
            new SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK)
        ), registries);
    }


    /**
     * Block loot table sub-provider.
     */
    public static class ModBlockLootTables extends BlockLootSubProvider {

        protected ModBlockLootTables(HolderLookup.Provider provider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        protected void generate() {
            // Standard blocks that drop themselves
            dropSelf(ModBlocks.GLACIAL_BASIN.get());
            dropSelf(ModBlocks.COLD_BREWING_STAND.get());
            dropSelf(ModBlocks.HEARTHWOOD_LOG.get());
            dropSelf(ModBlocks.SHIMMERING_ICE.get());
            dropSelf(ModBlocks.AURORA_BLOOM_DECORATIVE.get());

            // Potted decorative aurora bloom drops the pot and the decorative bloom
            add(ModBlocks.POTTED_AURORA_BLOOM_DECORATIVE.get(),
                createPotFlowerItemTable(ModBlocks.AURORA_BLOOM_DECORATIVE.get()));

            // Aurora Bloom: silk-touch at max age yields decorative variant; otherwise
            // fully grown blooms drop Frozen Petals (1-4 with fortune). Harvesting early yields nothing.
            var fortuneEnchant = this.registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
            add(ModBlocks.AURORA_BLOOM.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.AURORA_BLOOM.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AuroraBloomBlock.AGE, AuroraBloomBlock.MAX_AGE)))
                    .when(this.hasSilkTouch())
                    .add(LootItem.lootTableItem(ModBlocks.AURORA_BLOOM_DECORATIVE.get())))
                .withPool(LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.AURORA_BLOOM.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AuroraBloomBlock.AGE, AuroraBloomBlock.MAX_AGE)))
                    .when(this.doesNotHaveSilkTouch())
                    .add(LootItem.lootTableItem(ModItems.FROZEN_PETALS.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                        .apply(ApplyBonusCount.addUniformBonusCount(fortuneEnchant, 1))))
                .withPool(LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.AURORA_BLOOM.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AuroraBloomBlock.AGE, AuroraBloomBlock.MAX_AGE)))
                    .when(this.doesNotHaveSilkTouch())
                    .add(LootItem.lootTableItem(ModBlocks.AURORA_BLOOM.get())))
                .withPool(LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.AURORA_BLOOM.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(AuroraBloomBlock.AGE, AuroraBloomBlock.MAX_AGE)))
                    .when(this.doesNotHaveSilkTouch())
                    .when(LootItemRandomChanceCondition.randomChance(0.15f))
                    .add(LootItem.lootTableItem(ModBlocks.AURORA_BLOOM.get())))
                );

            // Ender Bloom always drops itself (stage 0 on placement), plus an Aurora Ender Shard when fully grown
            // and a 5% chance for an additional Ender Bloom at full growth.
            add(ModBlocks.ENDER_BLOOM.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .add(LootItem.lootTableItem(ModBlocks.ENDER_BLOOM.get())))
                .withPool(LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_BLOOM.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(EnderBloomBlock.AGE, EnderBloomBlock.MAX_AGE)))
                    .add(LootItem.lootTableItem(ModItems.AURORA_ENDER_SHARD.get())))
                .withPool(LootPool.lootPool()
                    .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ENDER_BLOOM.get())
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(EnderBloomBlock.AGE, EnderBloomBlock.MAX_AGE)))
                    .when(LootItemRandomChanceCondition.randomChance(0.05f))
                    .add(LootItem.lootTableItem(ModBlocks.ENDER_BLOOM.get()))
                ));

            // Glow-Leek drops itself when mature, seeds otherwise
            // For now, drop the seeds as a simple implementation
            dropOther(ModBlocks.GLOW_LEEK.get(), ModItems.GLOW_LEEK_SEEDS.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(
                ModBlocks.GLACIAL_BASIN.get(),
                ModBlocks.COLD_BREWING_STAND.get(),
                ModBlocks.HEARTHWOOD_LOG.get(),
                ModBlocks.SHIMMERING_ICE.get(),
                ModBlocks.AURORA_BLOOM.get(),
                ModBlocks.AURORA_BLOOM_DECORATIVE.get(),
                ModBlocks.POTTED_AURORA_BLOOM_DECORATIVE.get(),
                ModBlocks.ENDER_BLOOM.get(),
                ModBlocks.GLOW_LEEK.get()
            );
        }
    }
}
