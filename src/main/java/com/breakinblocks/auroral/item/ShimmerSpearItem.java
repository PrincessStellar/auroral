package com.breakinblocks.auroral.item;

import net.minecraft.world.item.Item;

/**
 * Shimmer Spear - A Shimmersteel-tier spear.
 *
 * Behavior is provided by vanilla 26.1 spear mechanics via {@code Item.Properties#spear()}:
 * piercing stab attack, charge-to-stab right-click, and 4.5-block attack reach.
 *
 * Aurora-themed effects (Frostbite on hit, bonus damage during aurora events) are applied
 * in {@link com.breakinblocks.auroral.events.ShimmersteelEventHandler}.
 */
public class ShimmerSpearItem extends Item {

    public ShimmerSpearItem(Properties properties) {
        super(properties);
    }
}
