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
package com.shinoow.abyssalcraft.api.necronomicon.condition;

import net.minecraft.resources.ResourceLocation;

/**
 * Unlocks knowledge once the player has visited a given biome.
 * <p>
 * 1.12.2 keyed this off a raw name string; a {@link ResourceLocation} is used here so the
 * reference is namespaced and validated.
 */
public class BiomeCondition implements IUnlockCondition {

    private final ResourceLocation biome;

    public BiomeCondition(ResourceLocation biome) {
        this.biome = biome;
    }

    @Override
    public boolean areConditionObjectsEqual(Object stuff) {
        return biome.equals(stuff);
    }

    @Override
    public Object getConditionObject() {
        return biome;
    }

    @Override
    public int getType() {
        return 0;
    }
}
