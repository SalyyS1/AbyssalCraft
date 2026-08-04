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
import com.shinoow.abyssalcraft.common.inventory.TransmutatorMenu;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/** Screen for the Transmutator. The flame sits at x=57, matching the 1.12.2 texture. */
public class TransmutatorScreen extends AbstractMachineScreen<TransmutatorMenu> {

    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/gui/container/transmutator.png");

    public TransmutatorScreen(TransmutatorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND, 57);
    }
}
