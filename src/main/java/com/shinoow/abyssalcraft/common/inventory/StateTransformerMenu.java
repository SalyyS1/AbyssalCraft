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

import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.common.blocks.tile.StateTransformerBlockEntity;
import com.shinoow.abyssalcraft.init.ACMenus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Menu for the State Transformer: one tablet slot and a 7x7 payload grid.
 * <p>
 * The grid is the 49 slots the tablet's contents are packed from and unpacked into, so all 50 slots
 * are real storage; unlike the Materializer none of them are a derived preview.
 */
public class StateTransformerMenu extends AbstractContainerMenu {

    private static final int SLOT_TABLET = 0;
    private static final int GRID_SIZE = 7;
    private static final int MACHINE_SLOTS = 1 + GRID_SIZE * GRID_SIZE;
    private static final int INVENTORY_END = MACHINE_SLOTS + 27;
    private static final int HOTBAR_END = INVENTORY_END + 9;

    /** The player inventory sits lower than usual to clear the 7-row grid. */
    private static final int PLAYER_INVENTORY_Y = 84 + 72;

    private final Container container;
    private final ContainerData data;

    /** Client-side constructor; the menu type resolves the block entity by position. */
    public StateTransformerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(MACHINE_SLOTS), new SimpleContainerData(2));
    }

    public StateTransformerMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ACMenus.STATE_TRANSFORMER.get(), id);
        checkContainerDataCount(data, 2);
        this.container = container;
        this.data = data;

        addSlot(new TabletSlot(container, SLOT_TABLET, 8, 71));

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                addSlot(new Slot(container, 1 + column + row * GRID_SIZE,
                        44 + column * 18, 17 + row * 18));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9,
                        8 + column * 18, PLAYER_INVENTORY_Y + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, PLAYER_INVENTORY_Y + 58));
        }

        addDataSlots(data);
    }

    /** Arrow progress width in pixels. */
    public int getTransformProgress(int pixels) {
        return data.get(0) * pixels / StateTransformerBlockEntity.PROCESS_TIME;
    }

    public int getMode() {
        return data.get(1);
    }

    @Override
    public boolean stillValid(Player player) {
        return container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < MACHINE_SLOTS) {
            if (!moveItemStackTo(stack, MACHINE_SLOTS, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.is(ACItems.stone_tablet.get())) {
            if (!moveItemStackTo(stack, SLOT_TABLET, SLOT_TABLET + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, SLOT_TABLET + 1, MACHINE_SLOTS, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }
        slot.onTake(player, stack);
        return original;
    }

    /** Accepts only a stone tablet, and only one at a time. */
    private static class TabletSlot extends Slot {

        TabletSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ACItems.stone_tablet.get());
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }
}
