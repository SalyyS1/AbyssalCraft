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

import com.shinoow.abyssalcraft.common.blocks.tile.AbstractMachineBlockEntity;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

/**
 * Shared menu behaviour for the fuel-burning machines.
 * <p>
 * The 1.12.2 containers pushed progress to clients through {@code IContainerListener}; 1.20.1 syncs
 * it through {@link ContainerData}. Quick-move rules were identical across these machines -- fuel to
 * the fuel slot, anything with a recipe to the input, outputs only outward -- so they live here.
 */
public abstract class AbstractMachineMenu extends AbstractContainerMenu {

    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_FUEL = 1;
    protected static final int SLOT_OUTPUT = 2;

    private final Container container;
    private final ContainerData data;
    private final int machineSlots;
    private final int inventoryEnd;
    private final int hotbarEnd;

    protected AbstractMachineMenu(MenuType<?> type, int id, Inventory inventory, Container container,
            ContainerData data, int machineSlots) {
        super(type, id);
        checkContainerSize(container, machineSlots);
        checkContainerDataCount(data, 3);
        this.container = container;
        this.data = data;
        this.machineSlots = machineSlots;
        inventoryEnd = machineSlots + 27;
        hotbarEnd = inventoryEnd + 9;

        addMachineSlots(container);

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

    /** Adds the machine's own slots, in registry order. */
    protected abstract void addMachineSlots(Container container);

    /** Whether the machine has a recipe for this stack, deciding where a shift-click sends it. */
    protected abstract boolean hasRecipeFor(ItemStack stack);

    public boolean isBurning() {
        return data.get(0) > 0;
    }

    /** Flame gauge height in pixels. */
    public int getBurnProgress(int pixels) {
        int total = data.get(1);
        if (total == 0) {
            total = AbstractMachineBlockEntity.PROCESS_TIME;
        }
        return data.get(0) * pixels / total;
    }

    /** Arrow progress width in pixels. */
    public int getProcessProgress(int pixels) {
        return data.get(2) * pixels / AbstractMachineBlockEntity.PROCESS_TIME;
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

        if (index >= SLOT_OUTPUT && index < machineSlots) {
            if (!moveItemStackTo(stack, machineSlots, hotbarEnd, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (index >= machineSlots) {
            if (ForgeHooks.getBurnTime(stack, null) > 0) {
                if (!moveItemStackTo(stack, SLOT_FUEL, SLOT_FUEL + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (hasRecipeFor(stack)) {
                if (!moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < inventoryEnd) {
                if (!moveItemStackTo(stack, inventoryEnd, hotbarEnd, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(stack, machineSlots, inventoryEnd, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, machineSlots, hotbarEnd, false)) {
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

    /** Accepts only items that actually burn. */
    protected static class FuelSlot extends Slot {

        public FuelSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, null) > 0;
        }
    }

    /** Results can be taken out but never put in. */
    protected static class OutputSlot extends Slot {

        public OutputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false;
        }
    }
}
