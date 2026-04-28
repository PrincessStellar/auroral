package com.breakinblocks.auroral.item;

import com.breakinblocks.auroral.integration.guideme.AuroralGuide;
import guideme.GuidesCommon;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class AuroralGuideItem extends Item {

    public AuroralGuideItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            GuidesCommon.openGuide(player, AuroralGuide.GUIDE_ID);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }
}
