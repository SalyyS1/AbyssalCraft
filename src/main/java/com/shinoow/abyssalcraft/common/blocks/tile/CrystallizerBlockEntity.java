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

import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.common.inventory.CrystallizerMenu;
import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

/**
 * The Crystallizer splits an input item into up to two crystal outputs, burning fuel the way a
 * furnace does.
 * <p>
 * Slots, timings and output-merging rules are carried over from 1.12.2 unchanged: slot 0 input,
 * slot 1 fuel, slots 2 and 3 outputs, and a fixed 200-tick process once fuel is burning.
 * {@code ITickable.update()} became a static ticker registered on the {@code BlockEntityType},
 * and the four progress fields move into a {@link ContainerData} because 1.20.1 syncs menu
 * integers through that rather than through {@code IContainerListener}.
 */
public class CrystallizerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    /** Ticks a full crystallization takes once fuel is burning. Unchanged from 1.12.2. */
    public static final int PROCESS_TIME = 200;

    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FUEL = 1;
    private static final int SLOT_OUTPUT_PRIMARY = 2;
    private static final int SLOT_OUTPUT_SECONDARY = 3;

    private static final int[] SLOTS_TOP = {SLOT_INPUT};
    private static final int[] SLOTS_BOTTOM = {SLOT_OUTPUT_PRIMARY, SLOT_FUEL, SLOT_OUTPUT_SECONDARY};
    private static final int[] SLOTS_SIDES = {SLOT_FUEL};

    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    /** Ticks of fuel left in the current burn. */
    private int burnTime;
    /** Ticks the current fuel item burns for in total; drives the flame gauge. */
    private int currentItemBurnTime;
    /** Ticks the current input has been processing for. */
    private int processTime;

    private final ContainerData data = new ContainerData() {
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

    public CrystallizerBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.CRYSTALLIZER.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CrystallizerBlockEntity be) {
        boolean wasBurning = be.isBurning();
        boolean changed = false;

        if (be.isBurning()) {
            be.burnTime--;
        }

        ItemStack fuel = be.items.get(SLOT_FUEL);
        if (!be.isBurning() && be.canCrystallize()) {
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

        if (be.isBurning() && be.canCrystallize()) {
            be.processTime++;
            if (be.processTime == PROCESS_TIME) {
                be.processTime = 0;
                be.crystallizeItem();
                changed = true;
            }
        } else {
            be.processTime = 0;
        }

        if (wasBurning != be.isBurning()) {
            changed = true;
        }
        if (changed) {
            be.setChanged();
        }
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    /** True when there is an input with a recipe and room for both of its outputs. */
    private boolean canCrystallize() {
        if (items.get(SLOT_INPUT).isEmpty()) {
            return false;
        }
        ItemStack[] results = CrystallizerRecipes.instance().getCrystallizationResult(items.get(SLOT_INPUT));
        if (results[0].isEmpty()) {
            return false;
        }
        return fits(results[0], SLOT_OUTPUT_PRIMARY) && fits(results[1], SLOT_OUTPUT_SECONDARY);
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

    private void crystallizeItem() {
        if (!canCrystallize()) {
            return;
        }
        ItemStack input = items.get(SLOT_INPUT);
        ItemStack[] results = CrystallizerRecipes.instance().getCrystallizationResult(input);

        merge(results[0], SLOT_OUTPUT_PRIMARY);
        merge(results[1], SLOT_OUTPUT_SECONDARY);

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
        return switch (side) {
            case DOWN -> SLOTS_BOTTOM;
            case UP -> SLOTS_TOP;
            default -> SLOTS_SIDES;
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot == SLOT_OUTPUT_PRIMARY || slot == SLOT_OUTPUT_SECONDARY) {
            return false;
        }
        return slot != SLOT_FUEL || ForgeHooks.getBurnTime(stack, null) > 0;
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return side != Direction.DOWN || slot != SLOT_FUEL || stack.getItem() == Items.BUCKET;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.abyssalcraft.crystallizer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new CrystallizerMenu(id, inventory, this, data);
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
