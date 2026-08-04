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

import com.shinoow.abyssalcraft.common.inventory.AbstractMachineMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Shared screen for the fuel-burning machines.
 * <p>
 * {@code GuiContainer} became {@link AbstractContainerScreen}, and drawing moved from
 * {@code drawTexturedModalRect} to {@link GuiGraphics#blit}. The flame and arrow gauge coordinates
 * are the ones the 1.12.2 screens used, so the overlays land exactly where the textures expect.
 */
public abstract class AbstractMachineScreen<T extends AbstractMachineMenu>
        extends AbstractContainerScreen<T> {

    /** Flame gauge is 14x12 at sheet offset (176, 0), and fills upward. */
    private static final int FLAME_SHEET_X = 176;
    private static final int FLAME_WIDTH = 14;
    private static final int FLAME_HEIGHT = 12;

    /** Progress arrow is 24 wide at sheet offset (176, 14). */
    private static final int ARROW_SHEET_X = 176;
    private static final int ARROW_SHEET_Y = 14;
    private static final int ARROW_WIDTH = 24;
    private static final int ARROW_HEIGHT = 16;

    private final ResourceLocation background;
    private final int flameX;

    protected AbstractMachineScreen(T menu, Inventory inventory, Component title,
            ResourceLocation background, int flameX) {
        super(menu, inventory, title);
        this.background = background;
        this.flameX = flameX;
    }

    @Override
    protected void init() {
        super.init();
        // Centre the title the way the 1.12.2 screens did.
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(background, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isBurning()) {
            int lit = menu.getBurnProgress(FLAME_HEIGHT);
            graphics.blit(background, x + flameX, y + 36 + FLAME_HEIGHT - lit,
                    FLAME_SHEET_X, FLAME_HEIGHT - lit, FLAME_WIDTH, lit + 2);
        }

        int progress = menu.getProcessProgress(ARROW_WIDTH);
        graphics.blit(background, x + 79, y + 34, ARROW_SHEET_X, ARROW_SHEET_Y,
                progress + 1, ARROW_HEIGHT);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
