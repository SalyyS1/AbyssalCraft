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

import com.shinoow.abyssalcraft.api.item.ItemEngraving;
import com.shinoow.abyssalcraft.api.recipe.EngraverRecipes;
import com.shinoow.abyssalcraft.common.blocks.tile.EngraverBlockEntity;
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
 * Menu for the Engraver: a coin, an engraving stamp, and the stamped result.
 * <p>
 * This does not share {@link AbstractMachineMenu} because the Engraver has no fuel slot, so the
 * shared quick-move rules, which route burnable items to a fuel slot, do not apply.
 */
public class EngraverMenu extends AbstractContainerMenu {

    private static final int SLOT_COIN = 0;
    private static final int SLOT_ENGRAVING = 1;
    private static final int SLOT_OUTPUT = 2;
    private static final int MACHINE_SLOTS = 3;
    private static final int INVENTORY_END = MACHINE_SLOTS + 27;
    private static final int HOTBAR_END = INVENTORY_END + 9;

    private final Container container;
    private final ContainerData data;

    /** Client-side constructor; the menu type resolves the block entity by position. */
    public EngraverMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(MACHINE_SLOTS), new SimpleContainerData(1));
    }

    public EngraverMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ACMenus.ENGRAVER.get(), id);
        checkContainerSize(container, MACHINE_SLOTS);
        checkContainerDataCount(data, 1);
        this.container = container;
        this.data = data;

        addSlot(new CoinSlot(container, SLOT_COIN, 56, 17));
        addSlot(new EngravingSlot(container, SLOT_ENGRAVING, 56, 53));
        addSlot(new OutputSlot(container, SLOT_OUTPUT, 116, 35));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
            }
        }
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(inventory, column, 8 + column * 18, 142));
        }

        addDataSlots(data);
    }

    /** Arrow progress width in pixels. */
    public int getEngraveProgress(int pixels) {
        return data.get(0) * pixels / EngraverBlockEntity.PROCESS_TIME;
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

        if (index == SLOT_OUTPUT) {
            if (!moveItemStackTo(stack, MACHINE_SLOTS, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (index >= MACHINE_SLOTS) {
            if (stack.getItem() instanceof ItemEngraving) {
                if (!moveItemStackTo(stack, SLOT_ENGRAVING, SLOT_ENGRAVING + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (EngraverRecipes.instance().isCoin(stack)) {
                if (!moveItemStackTo(stack, SLOT_COIN, SLOT_COIN + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < INVENTORY_END) {
                if (!moveItemStackTo(stack, INVENTORY_END, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, MACHINE_SLOTS, INVENTORY_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, MACHINE_SLOTS, HOTBAR_END, false)) {
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

    /** Accepts only registered coins. */
    private static class CoinSlot extends Slot {

        CoinSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return EngraverRecipes.instance().isCoin(stack);
        }
    }

    /** Accepts only engraving stamps. */
    private static class EngravingSlot extends Slot {

        EngravingSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return stack.getItem() instanceof ItemEngraving;
        }
    }

    /** Results can be taken out but never put in. */
    private static class OutputSlot extends Slot {

        OutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
