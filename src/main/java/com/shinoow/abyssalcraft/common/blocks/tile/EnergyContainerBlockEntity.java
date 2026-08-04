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

import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** The Energy Container, the large-capacity Potential Energy store. */
public class EnergyContainerBlockEntity extends AbstractEnergyBlockEntity {

    /** Capacity, unchanged from 1.12.2. */
    public static final int CAPACITY = 10000;

    public EnergyContainerBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.ENERGY_CONTAINER.get(), pos, state);
    }

    @Override
    public int getMaxEnergy() {
        return CAPACITY;
    }
}
