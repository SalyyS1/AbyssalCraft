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

import com.shinoow.abyssalcraft.api.energy.IEnergyContainer;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared storage behaviour for Potential Energy block entities.
 * <p>
 * The 1.12.2 tiles each repeated the same clamped add, drain-to-empty consume, and the
 * {@code PotEnergy} NBT float. That is centralised here so the collector, container, relay and
 * pedestal only declare their capacity.
 */
public abstract class AbstractEnergyBlockEntity extends BlockEntity implements IEnergyContainer {

    private float energy;

    protected AbstractEnergyBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public float getContainedEnergy() {
        return energy;
    }

    @Override
    public void addEnergy(float amount) {
        energy = Math.min(energy + amount, getMaxEnergy());
        setChanged();
    }

    /** Drains up to the requested amount, returning what was actually available. */
    @Override
    public float consumeEnergy(float amount) {
        float taken = Math.min(amount, energy);
        energy -= taken;
        setChanged();
        return taken;
    }

    @Override
    public boolean canAcceptPE() {
        return getContainedEnergy() < getMaxEnergy();
    }

    @Override
    public boolean canTransferPE() {
        return getContainedEnergy() > 0;
    }

    @Override
    public BlockEntity getContainerTile() {
        return this;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        energy = tag.getFloat("PotEnergy");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putFloat("PotEnergy", energy);
    }
}
