package com.breakinblocks.auroral.item;

import com.breakinblocks.auroral.config.AuroralConfig;
import com.breakinblocks.auroral.registry.ModEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ShimmersteelSwordItem extends Item {

    public static final float EXECUTE_THRESHOLD = 0.15f;
    public static final int FROSTBITE_DURATION = 60;

    public ShimmersteelSwordItem(Properties properties) {
        super(properties.sword(ModToolTiers.SHIMMERSTEEL, 3, -2.4f));
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addEffect(new MobEffectInstance(ModEffects.FROSTBITE, FROSTBITE_DURATION, 0));

        super.postHurtEnemy(stack, target, attacker);
    }

    public static boolean shouldExecute(LivingEntity target) {
        float healthPercent = target.getHealth() / target.getMaxHealth();
        float threshold = AuroralConfig.SERVER.executeThreshold.get().floatValue();
        return healthPercent < threshold && healthPercent > 0;
    }

    public static void placeSnowOnKill(Level level, BlockPos pos) {
        if (level instanceof ServerLevel) {
            // Only place snow if the space is air and the block below is solid
            if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isSolid()) {
                level.setBlock(pos, Blocks.SNOW.defaultBlockState(), 3);
            }
        }
    }
}
