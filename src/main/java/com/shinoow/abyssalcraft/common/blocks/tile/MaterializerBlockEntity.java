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

import java.util.List;

import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.common.inventory.MaterializerMenu;
import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The Materializer turns crystals held in a bag into items.
 * <p>
 * It is a browser rather than a processor: slot 0 holds the crystal bag, slot 1 a Necronomicon, and
 * the remaining slots are filled with every item the bag's crystals can currently pay for. Taking
 * one spends its crystals. There is no fuel and no progress timer.
 * <p>
 * The 1.12.2 inventory was 550 slots so the result list could not overflow it. Only 18 are visible,
 * so the same cap is kept here to stay compatible with saved contents.
 */
public class MaterializerBlockEntity extends BaseContainerBlockEntity {

    private static final int SLOT_BAG = 0;
    private static final int SLOT_BOOK = 1;
    private static final int FIRST_RESULT_SLOT = 2;
    /** Total slots, unchanged from 1.12.2 so existing block entities load. */
    private static final int SIZE = 550;
    /** Results beyond this are dropped, matching the 1.12.2 cap. */
    private static final int MAX_RESULTS = SIZE - FIRST_RESULT_SLOT;

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);

    /** Set when the bag changes, so results are recomputed on the next tick rather than per-change. */
    private boolean resultsStale = true;

    public MaterializerBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.MATERIALIZER.get(), pos, state);
    }

    public static void serverTick(net.minecraft.world.level.Level level, BlockPos pos,
            BlockState state, MaterializerBlockEntity be) {
        if (be.resultsStale) {
            be.refreshResults();
            be.resultsStale = false;
            be.setChanged();
        }
    }

    /**
     * Spends the crystals for one result. Called when a player takes it, so the cost is only paid
     * on an actual withdrawal.
     */
    public void payFor(ItemStack result) {
        MaterializerRecipes.instance().processMaterialization(result, items.get(SLOT_BAG));
        resultsStale = true;
    }

    /** Rebuilds the result slots from what the bag can currently afford. */
    private void refreshResults() {
        for (int slot = FIRST_RESULT_SLOT; slot < items.size(); slot++) {
            items.set(slot, ItemStack.EMPTY);
        }
        ItemStack bag = items.get(SLOT_BAG);
        if (bag.isEmpty()) {
            return;
        }
        List<ItemStack> available = MaterializerRecipes.instance().getMaterializationResult(bag);
        int count = Math.min(available.size(), MAX_RESULTS);
        for (int i = 0; i < count; i++) {
            items.set(FIRST_RESULT_SLOT + i, available.get(i));
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
        ItemStack removed = ContainerHelper.removeItem(items, slot, amount);
        if (slot == SLOT_BAG) {
            resultsStale = true;
        }
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack removed = ContainerHelper.takeItem(items, slot);
        if (slot == SLOT_BAG) {
            resultsStale = true;
        }
        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        if (slot == SLOT_BAG) {
            resultsStale = true;
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
    protected Component getDefaultName() {
        return Component.translatable("container.abyssalcraft.materializer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new MaterializerMenu(id, inventory, this);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        resultsStale = true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        // Only the bag and book are real contents; the result slots are recomputed on load.
        NonNullList<ItemStack> persistent = NonNullList.withSize(items.size(), ItemStack.EMPTY);
        persistent.set(SLOT_BAG, items.get(SLOT_BAG));
        persistent.set(SLOT_BOOK, items.get(SLOT_BOOK));
        ContainerHelper.saveAllItems(tag, persistent);
    }
}
