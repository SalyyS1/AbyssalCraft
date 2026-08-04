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
package com.shinoow.abyssalcraft.api.transfer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * One item-transfer route: an ordered list of positions, the faces items enter and leave by, and a
 * filter of up to five stacks.
 * <p>
 * The 1.12.2 version kept a parallel "subtype filter" whose stacks had their damage set to
 * {@code OreDictionary.WILDCARD_VALUE} so a filter entry could match every metadata variant of an
 * item. Variants are separate items on 1.20.1 and damage is only durability, so that second list
 * and the flag guarding it are gone; a filter entry now matches by item, and the NBT flag is still
 * read so existing configurations load without complaint.
 *
 * @author shinoow
 */
public class ItemTransferConfiguration implements INBTSerializable<CompoundTag> {

    /** Maximum filter entries, unchanged from 1.12.2. */
    public static final int FILTER_SIZE = 5;

    private BlockPos[] route = new BlockPos[0];
    private NonNullList<ItemStack> filter = NonNullList.withSize(FILTER_SIZE, ItemStack.EMPTY);
    private Direction exitFacing = Direction.NORTH;
    private Direction entryFacing = Direction.NORTH;
    private boolean filterNBT;

    public ItemTransferConfiguration() {}

    public ItemTransferConfiguration(BlockPos[] route) {
        this.route = route.clone();
    }

    public ItemTransferConfiguration setFilter(NonNullList<ItemStack> filter) {
        this.filter = filter;
        return this;
    }

    public ItemTransferConfiguration setEntryFacing(Direction facing) {
        entryFacing = facing;
        return this;
    }

    public ItemTransferConfiguration setExitFacing(Direction facing) {
        exitFacing = facing;
        return this;
    }

    public ItemTransferConfiguration setFilterNBT(boolean filterNBT) {
        this.filterNBT = filterNBT;
        return this;
    }

    public BlockPos[] getRoute() {
        return route.clone();
    }

    public NonNullList<ItemStack> getFilter() {
        return filter;
    }

    public Direction getExitFacing() {
        return exitFacing;
    }

    public Direction getEntryFacing() {
        return entryFacing;
    }

    public boolean filterByNBT() {
        return filterNBT;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("exitFacing", exitFacing.get3DDataValue());
        tag.putInt("entryFacing", entryFacing.get3DDataValue());

        ListTag positions = new ListTag();
        Arrays.stream(route).map(pos -> LongTag.valueOf(pos.asLong())).forEach(positions::add);
        tag.put("route", positions);

        ContainerHelper.saveAllItems(tag, filter);
        tag.putBoolean("FilterNBT", filterNBT);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        exitFacing = Direction.from3DDataValue(tag.getInt("exitFacing"));
        entryFacing = Direction.from3DDataValue(tag.getInt("entryFacing"));

        ListTag positions = tag.getList("route", Tag.TAG_LONG);
        List<BlockPos> parsed = new ArrayList<>(positions.size());
        for (int i = 0; i < positions.size(); i++) {
            parsed.add(BlockPos.of(((LongTag) positions.get(i)).getAsLong()));
        }
        route = parsed.toArray(new BlockPos[0]);

        filter = NonNullList.withSize(FILTER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, filter);
        filterNBT = tag.getBoolean("FilterNBT");
    }

    /**
     * Two configurations are equal when they serialise identically. The 1.12.2 version compared
     * the NBT's string form; comparing the tags directly is the same test without the formatting
     * round trip.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ItemTransferConfiguration other
                && serializeNBT().equals(other.serializeNBT());
    }

    @Override
    public int hashCode() {
        return serializeNBT().hashCode();
    }
}
