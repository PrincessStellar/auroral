package com.breakinblocks.auroral.events;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.config.AuroralConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

public class ItemEventHandler {

    public static void register(IEventBus eventBus) {
        eventBus.addListener(ItemEventHandler::onModifyDefaultComponents);
    }

    private static void onModifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        int maxStackSize = AuroralConfig.STARTUP.snowballMaxStackSize.get();

        if (maxStackSize != 16) {
            event.modify(Items.SNOWBALL, builder -> {
                builder.set(DataComponents.MAX_STACK_SIZE, maxStackSize);
            });
            Auroral.LOGGER.debug("Modified snowball max stack size to {}", maxStackSize);
        }
    }
}
