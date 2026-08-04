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
package com.shinoow.abyssalcraft.common.inventory;

import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.shinoow.abyssalcraft.init.ACMenus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/** Menu for the Transmutator: one input, one fuel, one output. */
public class TransmutatorMenu extends AbstractMachineMenu {

    private static final int MACHINE_SLOTS = 3;

    /** Client-side constructor; the menu type resolves the block entity by position. */
    public TransmutatorMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(MACHINE_SLOTS), new SimpleContainerData(3));
    }

    public TransmutatorMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ACMenus.TRANSMUTATOR.get(), id, inventory, container, data, MACHINE_SLOTS);
    }

    @Override
    protected void addMachineSlots(Container container) {
        addSlot(new Slot(container, SLOT_INPUT, 56, 17));
        addSlot(new FuelSlot(container, SLOT_FUEL, 56, 53));
        addSlot(new OutputSlot(container, SLOT_OUTPUT, 116, 35));
    }

    @Override
    protected boolean hasRecipeFor(ItemStack stack) {
        return !TransmutatorRecipes.instance().getTransmutationResult(stack).isEmpty();
    }
}
