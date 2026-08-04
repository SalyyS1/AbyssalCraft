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

import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.common.blocks.tile.CrystallizerBlockEntity;
import com.shinoow.abyssalcraft.init.ACMenus;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

/**
 * Menu for the Crystallizer.
 * <p>
 * The 1.12.2 {@code ContainerCrystallizer} pushed its progress integers to clients through
 * {@code IContainerListener}; 1.20.1 syncs them through a {@link ContainerData} instead. Slot
 * positions and the quick-move rules are otherwise unchanged.
 */
public class CrystallizerMenu extends AbstractContainerMenu {

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FUEL = 1;
    private static final int SLOT_OUTPUT_PRIMARY = 2;
    private static final int SLOT_OUTPUT_SECONDARY = 3;
    private static final int MACHINE_SLOTS = 4;
    private static final int INVENTORY_END = MACHINE_SLOTS + 27;
    private static final int HOTBAR_END = INVENTORY_END + 9;

    private final Container container;
    private final ContainerData data;

    /** Client-side constructor; the menu type resolves the block entity by position. */
    public CrystallizerMenu(int id, Inventory inventory) {
        this(id, inventory, new SimpleContainer(MACHINE_SLOTS), new SimpleContainerData(3));
    }

    public CrystallizerMenu(int id, Inventory inventory, Container container, ContainerData data) {
        super(ACMenus.CRYSTALLIZER.get(), id);
        checkContainerSize(container, MACHINE_SLOTS);
        checkContainerDataCount(data, 3);
        this.container = container;
        this.data = data;

        addSlot(new Slot(container, SLOT_INPUT, 56, 17));
        addSlot(new FuelSlot(container, SLOT_FUEL, 56, 53));
        addSlot(new OutputSlot(container, SLOT_OUTPUT_PRIMARY, 116, 26));
        addSlot(new OutputSlot(container, SLOT_OUTPUT_SECONDARY, 116, 44));

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

    public boolean isBurning() {
        return data.get(0) > 0;
    }

    /** Flame gauge height in pixels. */
    public int getBurnProgress(int pixels) {
        int total = data.get(1);
        if (total == 0) {
            total = CrystallizerBlockEntity.PROCESS_TIME;
        }
        return data.get(0) * pixels / total;
    }

    /** Arrow progress width in pixels. */
    public int getCrystallizationProgress(int pixels) {
        return data.get(2) * pixels / CrystallizerBlockEntity.PROCESS_TIME;
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

        if (index == SLOT_OUTPUT_PRIMARY || index == SLOT_OUTPUT_SECONDARY) {
            if (!moveItemStackTo(stack, MACHINE_SLOTS, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
            slot.onQuickCraft(stack, original);
        } else if (index >= MACHINE_SLOTS) {
            // From the player inventory: fuel goes to the fuel slot, everything else to the input.
            if (ForgeHooks.getBurnTime(stack, null) > 0) {
                if (!moveItemStackTo(stack, SLOT_FUEL, SLOT_FUEL + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!CrystallizerRecipes.instance().getCrystallizationResult(stack)[0].isEmpty()) {
                if (!moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT + 1, false)) {
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

    /** Accepts only items that actually burn. */
    private static class FuelSlot extends Slot {

        FuelSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return ForgeHooks.getBurnTime(stack, null) > 0;
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
