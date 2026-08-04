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

/**
 * Interface for a block that amplifies a manipulator's output.
 *
 * @author shinoow
 *
 * @since 1.5
 */
public interface IEnergyAmplifier {

    AmplifierType getAmplifierType();
}
