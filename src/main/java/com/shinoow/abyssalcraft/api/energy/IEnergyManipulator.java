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

import java.util.Set;

import com.shinoow.abyssalcraft.api.energy.EnergyEnum.AmplifierType;
import com.shinoow.abyssalcraft.api.energy.EnergyEnum.DeityType;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Interface for anything that generates and pushes out Potential Energy.
 *
 * @author shinoow
 *
 * @since 1.5
 */
public interface IEnergyManipulator {

    /** Amount of PE produced per transfer. */
    float getEnergyQuanta();

    void setActive(AmplifierType amp, DeityType deity);

    boolean isActive();

    DeityType getDeity(BlockState state);

    float getAmplifier(AmplifierType type);

    DeityType getActiveDeity();

    AmplifierType getActiveAmplifier();

    void setActiveDeity(DeityType deity);

    void setActiveAmplifier(AmplifierType amplifier);

    /**
     * Raises the tolerance counter. Tolerance builds up as the manipulator runs and drives the
     * chance of a disruption.
     */
    void addTolerance(int num);

    int getTolerance();

    boolean canTransferPE();

    /** Fires a disruption event, the penalty for accumulated tolerance. */
    void disrupt();

    /** Cached positions of the collectors this manipulator feeds, or null when not cached. */
    default Set<BlockPos> getEnergyCollectors() {
        return null;
    }
}
