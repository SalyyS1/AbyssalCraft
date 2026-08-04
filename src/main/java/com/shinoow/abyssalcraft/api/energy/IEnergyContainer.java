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
package com.shinoow.abyssalcraft.api.energy;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Interface for anything that can hold Potential Energy.
 *
 * @author shinoow
 *
 * @since 1.5
 */
public interface IEnergyContainer {

    float getContainedEnergy();

    int getMaxEnergy();

    void addEnergy(float energy);

    /** Consumes up to the requested amount, returning how much was actually taken. */
    float consumeEnergy(float energy);

    boolean canAcceptPE();

    boolean canTransferPE();

    BlockEntity getContainerTile();
}
