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

import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.common.inventory.StateTransformerMenu;
import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
 * The State Transformer packs a chest-load of items into a single stone tablet, and unpacks them
 * again.
 * <p>
 * Slot 0 holds the tablet; the rest are the payload. In pack mode the payload is written into the
 * tablet's NBT and the slots are emptied; in unpack mode the reverse. A tablet that already holds
 * items cannot be packed into again, and unpacking needs the payload slots empty -- both guards
 * carried over from 1.12.2, and both are what stop the operation destroying items.
 * <p>
 * The stored PE figure weights each item by how unstackable it is, matching 1.12.2 exactly:
 * {@code count * (64 / maxStackSize)}.
 */
public class StateTransformerBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {

    /** Ticks a transform takes. Unchanged from 1.12.2. */
    public static final int PROCESS_TIME = 200;

    /** Writes the payload into the tablet. */
    public static final int MODE_PACK = 0;
    /** Reads the payload back out of the tablet. */
    public static final int MODE_UNPACK = 1;

    private static final int SLOT_TABLET = 0;
    /** Total slots, unchanged from 1.12.2 so existing block entities load. */
    private static final int SIZE = 50;

    private static final String PAYLOAD_KEY = "ItemInventory";
    private static final String ENERGY_KEY = "PotEnergy";

    private NonNullList<ItemStack> items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
    private int mode = MODE_PACK;
    private int processTime;

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> processTime;
                case 1 -> mode;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> processTime = value;
                case 1 -> mode = value;
                default -> { }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public StateTransformerBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.STATE_TRANSFORMER.get(), pos, state);
    }

    public ContainerData getData() {
        return data;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        processTime = 0;
        setChanged();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            StateTransformerBlockEntity be) {
        if (!be.canTransform()) {
            if (be.processTime != 0) {
                be.processTime = 0;
                be.setChanged();
            }
            return;
        }
        be.processTime++;
        if (be.processTime >= PROCESS_TIME) {
            be.processTime = 0;
            be.transform();
        }
        be.setChanged();
    }

    /** Whether the current mode can run without losing anything. */
    public boolean canTransform() {
        ItemStack tablet = items.get(SLOT_TABLET);
        if (tablet.isEmpty()) {
            return false;
        }
        boolean payloadPresent = hasPayloadInSlots();

        if (mode == MODE_PACK) {
            // Refuse to overwrite a tablet that already holds a payload.
            CompoundTag tag = tablet.getTag();
            return payloadPresent && (tag == null || !tag.contains(PAYLOAD_KEY));
        }
        // Unpacking needs somewhere to put the items.
        return !payloadPresent && tablet.getTag() != null && tablet.getTag().contains(PAYLOAD_KEY);
    }

    private boolean hasPayloadInSlots() {
        for (int slot = SLOT_TABLET + 1; slot < items.size(); slot++) {
            if (!items.get(slot).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private void transform() {
        ItemStack tablet = items.get(SLOT_TABLET);
        if (mode == MODE_PACK) {
            pack(tablet);
        } else {
            unpack(tablet);
        }
    }

    private void pack(ItemStack tablet) {
        ListTag payload = new ListTag();
        float energy = 0;

        for (int slot = SLOT_TABLET + 1; slot < items.size(); slot++) {
            ItemStack stored = items.get(slot);
            if (stored.isEmpty()) {
                continue;
            }
            CompoundTag entry = new CompoundTag();
            entry.putInt("Slot", slot);
            stored.save(entry);
            payload.add(entry);
            // Rarer items weigh more: a non-stacking item counts 64 times a fully stacking one.
            energy += stored.getCount() * (64.0F / stored.getMaxStackSize());
            items.set(slot, ItemStack.EMPTY);
        }

        CompoundTag tag = tablet.getOrCreateTag();
        tag.put(PAYLOAD_KEY, payload);
        tag.putFloat(ENERGY_KEY, energy);
    }

    private void unpack(ItemStack tablet) {
        CompoundTag tag = tablet.getTag();
        if (tag == null) {
            return;
        }
        ListTag payload = tag.getList(PAYLOAD_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < payload.size(); i++) {
            CompoundTag entry = payload.getCompound(i);
            int slot = entry.getInt("Slot");
            if (slot > SLOT_TABLET && slot < items.size()) {
                items.set(slot, ItemStack.of(entry));
            }
        }
        tag.remove(PAYLOAD_KEY);
        tag.remove(ENERGY_KEY);
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
        return new int[]{SLOT_TABLET};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return slot != SLOT_TABLET || stack.is(ACItems.stone_tablet.get());
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) {
        return slot == SLOT_TABLET;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.abyssalcraft.statetransformer");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new StateTransformerMenu(id, inventory, this, data);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items);
        mode = tag.getInt("Mode");
        processTime = tag.getInt("ProcessTime");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Mode", mode);
        tag.putInt("ProcessTime", processTime);
        ContainerHelper.saveAllItems(tag, items);
    }
}
