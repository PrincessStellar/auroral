package com.breakinblocks.auroral.client.renderer;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemStack;

public class AuroralNautilusRenderState extends LivingEntityRenderState {
    public int size;
    public ItemStack bodyArmorItem = ItemStack.EMPTY;
}
