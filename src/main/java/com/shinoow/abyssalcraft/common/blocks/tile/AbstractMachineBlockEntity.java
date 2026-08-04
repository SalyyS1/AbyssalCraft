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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

/**
 * Shared behaviour for the fuel-burning processing machines.
 * <p>
 * The Crystallizer and Transmutator ran identical loops on 1.12.2 -- burn fuel, count 200 ticks,
 * consume one input, emit results -- differing only in slot count and which recipe registry they
 * consulted. That loop lives here so the two cannot drift apart, and so the remaining machines can
 * reuse it.
 * <p>
 * Progress is exposed through {@link ContainerData} because 1.20.1 syncs menu integers that way
 * rather than through the removed {@code IContainerListener}.
 */
public abstract class AbstractMachineBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    /** Ticks a full process takes once fuel is burning. Unchanged from 1.12.2. */
    public static final int PROCESS_TIME = 200;

    protected static final int SLOT_INPUT = 0;
    protected static final int SLOT_FUEL = 1;
    /** First output slot; machines with a second output use the slot after it. */
    protected static final int SLOT_OUTPUT = 2;

    private NonNullList<ItemStack> items;

    private int burnTime;
    private int currentItemBurnTime;
    private int processTime;

    protected final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> burnTime;
                case 1 -> currentItemBurnTime;
                case 2 -> processTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> burnTime = value;
                case 1 -> currentItemBurnTime = value;
                case 2 -> processTime = value;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    protected AbstractMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
            int slotCount) {
        super(type, pos, state);
        items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }

    /** The results this machine would produce for its current input; empty entries are skipped. */
    protected abstract ItemStack[] resultsFor(ItemStack input);

    /** Output slots, in the same order as {@link #resultsFor}. */
    protected abstract int[] outputSlots();

    public ContainerData getData() {
        return data;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            AbstractMachineBlockEntity be) {
        boolean wasBurning = be.isBurning();
        boolean changed = false;

        if (be.isBurning()) {
            be.burnTime--;
        }

        ItemStack fuel = be.items.get(SLOT_FUEL);
        if (!be.isBurning() && be.canProcess()) {
            be.currentItemBurnTime = be.burnTime = ForgeHooks.getBurnTime(fuel, null);
            if (be.isBurning()) {
                changed = true;
                if (!fuel.isEmpty()) {
                    ItemStack remainder = fuel.getCraftingRemainingItem();
                    fuel.shrink(1);
                    if (fuel.isEmpty()) {
                        be.items.set(SLOT_FUEL, remainder);
                    }
                }
            }
        }

        if (be.isBurning() && be.canProcess()) {
            be.processTime++;
            if (be.processTime == PROCESS_TIME) {
                be.processTime = 0;
                be.process();
                changed = true;
            }
        } else {
            be.processTime = 0;
        }

        if (wasBurning != be.isBurning() || changed) {
            be.setChanged();
        }
    }

    /** True when there is an input with a recipe and room for all of its outputs. */
    public boolean canProcess() {
        ItemStack input = items.get(SLOT_INPUT);
        if (input.isEmpty()) {
            return false;
        }
        ItemStack[] results = resultsFor(input);
        if (results.length == 0 || results[0].isEmpty()) {
            return false;
        }
        int[] slots = outputSlots();
        for (int i = 0; i < results.length && i < slots.length; i++) {
            if (!fits(results[i], slots[i])) {
                return false;
            }
        }
        return true;
    }

    /** An empty result always fits; otherwise the slot must be empty or a matching, unfilled stack. */
    private boolean fits(ItemStack result, int slot) {
        if (result.isEmpty()) {
            return true;
        }
        ItemStack existing = items.get(slot);
        if (existing.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItem(existing, result)) {
            return false;
        }
        int combined = existing.getCount() + result.getCount();
        return combined <= getMaxStackSize() && combined <= existing.getMaxStackSize();
    }

    private void process() {
        ItemStack input = items.get(SLOT_INPUT);
        ItemStack[] results = resultsFor(input);
        int[] slots = outputSlots();

        for (int i = 0; i < results.length && i < slots.length; i++) {
            merge(results[i], slots[i]);
        }

        // Capture before shrinking: an emptied stack reports air.
        boolean wasPotion = input.getItem() == Items.POTION;
        ItemStack remainder = input.getCraftingRemainingItem();
        input.shrink(1);
        if (input.isEmpty()) {
            // Potions leave the bottle behind, as they did on 1.12.2.
            items.set(SLOT_INPUT, wasPotion ? new ItemStack(Items.GLASS_BOTTLE) : remainder);
        }
    }

    private void merge(ItemStack result, int slot) {
        if (result.isEmpty()) {
            return;
        }
        ItemStack existing = items.get(slot);
        if (existing.isEmpty()) {
            items.set(slot, result.copy());
        } else if (ItemStack.isSameItem(existing, result)) {
            existing.grow(result.getCount());
        }
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(items, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return level != null && level.getBlockEntity(worldPosition) == this
                && player.distanceToSqr(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D,
                        worldPosition.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.UP) {
            return new int[]{SLOT_INPUT};
        }
        if (side == Direction.DOWN) {
            int[] outputs = outputSlots();
            int[] bottom = new int[outputs.length + 1];
            System.arraycopy(outputs, 0, bottom, 0, outputs.length);
            bottom[outputs.length] = SLOT_FUEL;
            return bottom;
        }
        return new int[]{SLOT_FUEL};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        for (int output : outputSlots()) {
            if (slot == output) {
                return false;
            }
        }
        return slot != SLOT_FUEL || ForgeHooks.getBurnTime(stack, null) > 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side != Direction.DOWN || slot != SLOT_FUEL || stack.getItem() == Items.BUCKET;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        burnTime = tag.getInt("BurnTime");
        processTime = tag.getInt("ProcessTime");
        currentItemBurnTime = ForgeHooks.getBurnTime(items.get(SLOT_FUEL), null);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", burnTime);
        tag.putInt("ProcessTime", processTime);
        ContainerHelper.saveAllItems(tag, items);
    }
}
