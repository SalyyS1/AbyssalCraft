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

import com.shinoow.abyssalcraft.api.item.ItemEngraving;
import com.shinoow.abyssalcraft.api.recipe.EngraverRecipes;
import com.shinoow.abyssalcraft.common.inventory.EngraverMenu;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The Engraver stamps a coin with an engraving.
 * <p>
 * Unlike the Crystallizer and Transmutator it burns no fuel: the engraving stamp itself is the
 * consumable, losing a point of durability per use. That is why this does not extend
 * {@link AbstractMachineBlockEntity} -- the shared base is built around a fuel burn.
 * <p>
 * Slots are carried over from 1.12.2: 0 coin, 1 engraving, 2 output.
 */
public class EngraverBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    /** Ticks a full engraving takes. Unchanged from 1.12.2. */
    public static final int PROCESS_TIME = 200;

    private static final int SLOT_COIN = 0;
    private static final int SLOT_ENGRAVING = 1;
    private static final int SLOT_OUTPUT = 2;

    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    private int processTime;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return index == 0 ? processTime : 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) {
                processTime = value;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public EngraverBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.ENGRAVER.get(), pos, state);
    }

    public ContainerData getData() {
        return data;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EngraverBlockEntity be) {
        if (!be.canEngrave()) {
            if (be.processTime != 0) {
                be.processTime = 0;
                be.setChanged();
            }
            return;
        }
        be.processTime++;
        if (be.processTime >= PROCESS_TIME) {
            be.processTime = 0;
            be.engrave();
        }
        be.setChanged();
    }

    /** True when the coin and engraving form a valid pair and the result fits. */
    public boolean canEngrave() {
        ItemStack result = currentResult();
        if (result.isEmpty()) {
            return false;
        }
        ItemStack output = items.get(SLOT_OUTPUT);
        if (output.isEmpty()) {
            return true;
        }
        if (!ItemStack.isSameItem(output, result)) {
            return false;
        }
        int combined = output.getCount() + result.getCount();
        return combined <= getMaxStackSize() && combined <= output.getMaxStackSize();
    }

    private ItemStack currentResult() {
        ItemStack coin = items.get(SLOT_COIN);
        ItemStack stamp = items.get(SLOT_ENGRAVING);
        if (coin.isEmpty() || !(stamp.getItem() instanceof ItemEngraving engraving)) {
            return ItemStack.EMPTY;
        }
        return EngraverRecipes.instance().getEngravingResult(coin, engraving);
    }

    private void engrave() {
        ItemStack result = currentResult();
        if (result.isEmpty()) {
            return;
        }
        ItemStack output = items.get(SLOT_OUTPUT);
        if (output.isEmpty()) {
            items.set(SLOT_OUTPUT, result.copy());
        } else {
            output.grow(result.getCount());
        }

        items.get(SLOT_COIN).shrink(1);

        // The stamp wears out rather than being consumed outright.
        ItemStack stamp = items.get(SLOT_ENGRAVING);
        stamp.setDamageValue(stamp.getDamageValue() + 1);
        if (stamp.getDamageValue() >= stamp.getMaxDamage()) {
            items.set(SLOT_ENGRAVING, ItemStack.EMPTY);
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
            case UP -> new int[]{SLOT_COIN};
            case DOWN -> new int[]{SLOT_OUTPUT};
            default -> new int[]{SLOT_ENGRAVING};
        };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return switch (slot) {
            case SLOT_COIN -> EngraverRecipes.instance().isCoin(stack);
            case SLOT_ENGRAVING -> stack.getItem() instanceof ItemEngraving;
            default -> false;
        };
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot == SLOT_OUTPUT;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.abyssalcraft.engraver");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new EngraverMenu(id, inventory, this, data);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        processTime = tag.getInt("ProcessTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("ProcessTime", processTime);
        ContainerHelper.saveAllItems(tag, items);
    }
}
