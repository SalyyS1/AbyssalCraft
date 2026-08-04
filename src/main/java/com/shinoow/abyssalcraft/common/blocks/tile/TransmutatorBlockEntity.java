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
package com.shinoow.abyssalcraft.common.blocks.tile;

import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.shinoow.abyssalcraft.common.inventory.TransmutatorMenu;
import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The Transmutator converts an input item into a single output.
 * <p>
 * Slots are carried over from 1.12.2: 0 input, 1 fuel, 2 output.
 */
public class TransmutatorBlockEntity extends AbstractMachineBlockEntity {

    private static final int[] OUTPUT_SLOTS = {SLOT_OUTPUT};

    public TransmutatorBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.TRANSMUTATOR.get(), pos, state, 3);
    }

    @Override
    protected ItemStack[] resultsFor(ItemStack input) {
        return new ItemStack[]{TransmutatorRecipes.instance().getTransmutationResult(input)};
    }

    @Override
    protected int[] outputSlots() {
        return OUTPUT_SLOTS;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.abyssalcraft.transmutator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TransmutatorMenu(id, inventory, this, getData());
    }
}
