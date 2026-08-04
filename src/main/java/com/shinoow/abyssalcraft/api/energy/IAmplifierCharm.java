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

import com.shinoow.abyssalcraft.api.energy.EnergyEnum.AmplifierType;
import com.shinoow.abyssalcraft.api.energy.EnergyEnum.DeityType;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for an item that carries an amplifier and deity attunement.
 *
 * @author shinoow
 *
 * @since 1.5
 */
public interface IAmplifierCharm {

    AmplifierType getAmplifier(ItemStack stack);

    DeityType getDeity(ItemStack stack);
}
