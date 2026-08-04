/*******************************************************************************
 * AbyssalCraft
 * Copyright (c) 2012 - 2025 Shinoow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Contributors:
 *     Shinoow -  implementation
 ******************************************************************************/
package com.shinoow.abyssalcraft.client.gui;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.common.inventory.EngraverMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Screen for the Engraver.
 * <p>
 * It draws only the progress arrow, since there is no flame: the engraving stamp is the consumable
 * rather than fuel. Sheet coordinates are the ones the 1.12.2 screen used.
 */
public class EngraverScreen extends AbstractContainerScreen<EngraverMenu> {

    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/gui/container/engraver.png");

    private static final int ARROW_SHEET_X = 176;
    private static final int ARROW_SHEET_Y = 14;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 16;

    public EngraverScreen(EngraverMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(BACKGROUND, x, y, 0, 0, imageWidth, imageHeight);

        int progress = menu.getEngraveProgress(ARROW_WIDTH);
        graphics.blit(BACKGROUND, x + 79, y + 34, ARROW_SHEET_X, ARROW_SHEET_Y,
                progress + 1, ARROW_HEIGHT);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
