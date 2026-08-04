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
import com.shinoow.abyssalcraft.common.blocks.tile.MaterializerBlockEntity;
import com.shinoow.abyssalcraft.init.ACMenus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * Menu for the Materializer.
 * <p>
 * The result slots are a read-only view of what the crystal bag can afford, so they refuse
 * insertions and charge the bag when a player takes from them. The visible grid is 3x6, matching
 * 1.12.2; the block entity holds more result slots than are shown.
 */
public class MaterializerMenu extends AbstractContainerMenu {

    private static final int SLOT_BAG = 0;
    private static final int SLOT_BOOK = 1;
    private static final int FIRST_RESULT_SLOT = 2;
    private static final int RESULT_ROWS = 3;
    private static final int RESULT_COLUMNS = 6;
    private static final int VISIBLE_SLOTS = FIRST_RESULT_SLOT + RESULT_ROWS * RESULT_COLUMNS;
    private static final int INVENTORY_END = VISIBLE_SLOTS + 27;
    private static final int HOTBAR_END = INVENTORY_END + 9;

    private final Container container;

    /** Client-side constructor; the menu type resolves the block entity by position. */
    public MaterializerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(VISIBLE_SLOTS));
    }

    public MaterializerMenu(int id, Inventory inventory, Container container) {
        super(ACMenus.MATERIALIZER.get(), id);
        this.container = container;

        addSlot(new CrystalBagSlot(container, SLOT_BAG, 14, 17));
        addSlot(new NecronomiconSlot(container, SLOT_BOOK, 14, 53));

        for (int row = 0; row < RESULT_ROWS; row++) {
            for (int column = 0; column < RESULT_COLUMNS; column++) {
                addSlot(new ResultSlot(container, FIRST_RESULT_SLOT + column + row * RESULT_COLUMNS,
                        44 + column * 17, 17 + row * 17));
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }
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

        if (index < VISIBLE_SLOTS) {
            if (!moveItemStackTo(stack, VISIBLE_SLOTS, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (!moveItemStackTo(stack, SLOT_BAG, FIRST_RESULT_SLOT, false)) {
            // Nothing from the player inventory belongs in the result slots, so only the bag and
            // book slots are offered as destinations.
            if (index < INVENTORY_END) {
                if (!moveItemStackTo(stack, INVENTORY_END, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, VISIBLE_SLOTS, INVENTORY_END, false)) {
                return ItemStack.EMPTY;
            }
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

    /** Accepts only crystal bags. */
    private static class CrystalBagSlot extends Slot {

        CrystalBagSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ACItems.small_crystal_bag.get()) || stack.is(ACItems.medium_crystal_bag.get())
                    || stack.is(ACItems.large_crystal_bag.get()) || stack.is(ACItems.huge_crystal_bag.get());
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    /** Accepts only a Necronomicon, which gates which materializations are shown. */
    private static class NecronomiconSlot extends Slot {

        NecronomiconSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.is(ACItems.necronomicon.get())
                    || stack.is(ACItems.abyssal_wasteland_necronomicon.get())
                    || stack.is(ACItems.dreadlands_necronomicon.get())
                    || stack.is(ACItems.omothol_necronomicon.get())
                    || stack.is(ACItems.abyssalnomicon.get());
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    /**
     * A previewed result. Taking it charges the crystal bag; it can never be inserted into, since
     * its contents are derived rather than stored.
     */
    private static class ResultSlot extends Slot {

        ResultSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }

        @Override
        public void onTake(Player player, ItemStack stack) {
            if (container instanceof MaterializerBlockEntity materializer) {
                materializer.payFor(stack);
            }
            super.onTake(player, stack);
        }
    }
}
