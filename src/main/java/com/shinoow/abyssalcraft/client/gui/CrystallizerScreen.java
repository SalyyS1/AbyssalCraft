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
import com.shinoow.abyssalcraft.common.inventory.CrystallizerMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** Screen for the Crystallizer. The flame sits at x=56, matching the 1.12.2 texture. */
public class CrystallizerScreen extends AbstractMachineScreen<CrystallizerMenu> {

    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/gui/container/crystallizer.png");

    public CrystallizerScreen(CrystallizerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND, 56);
    }
}
