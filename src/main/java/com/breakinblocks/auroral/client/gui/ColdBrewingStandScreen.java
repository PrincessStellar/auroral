package com.breakinblocks.auroral.client.gui;

import com.breakinblocks.auroral.Auroral;
import com.breakinblocks.auroral.inventory.ColdBrewingStandMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class ColdBrewingStandScreen extends AbstractContainerScreen<ColdBrewingStandMenu> {
    private static final Identifier FUEL_LENGTH_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/fuel_length");
    private static final Identifier BREW_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/brew_progress");
    private static final Identifier BUBBLES_SPRITE = Identifier.withDefaultNamespace("container/brewing_stand/bubbles");
    private static final Identifier BREWING_STAND_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/brewing_stand.png");

    private static final int[] BUBBLELENGTHS = new int[]{29, 24, 20, 16, 11, 6, 0};

    public ColdBrewingStandScreen(ColdBrewingStandMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, BREWING_STAND_LOCATION, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        int fuel = this.menu.getFuel();
        int fuelLength = Mth.clamp((18 * fuel + 20 - 1) / 20, 0, 18);
        if (fuelLength > 0) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FUEL_LENGTH_SPRITE, 18, 4, 0, 0, x + 60, y + 44, fuelLength, 4);
        }

        int brewingTicks = this.menu.getBrewingTicks();
        if (brewingTicks > 0) {
            int progress = (int)(28.0F * (1.0F - brewingTicks / 400.0F));
            if (progress > 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BREW_PROGRESS_SPRITE, 9, 28, 0, 0, x + 97, y + 16, 9, progress);
            }

            int bubbleHeight = BUBBLELENGTHS[brewingTicks / 2 % 7];
            if (bubbleHeight > 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BUBBLES_SPRITE, 12, 29, 0, 29 - bubbleHeight, x + 63, y + 14 + 29 - bubbleHeight, 12, bubbleHeight);
            }
        }
    }
}
