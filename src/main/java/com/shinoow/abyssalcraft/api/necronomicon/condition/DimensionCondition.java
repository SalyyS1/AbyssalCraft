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
 * Unlocks knowledge once the player has visited a given dimension.
 * <p>
 * 1.12.2 identified the dimension by its numeric ID. Those are gone on 1.20.1, so the condition
 * object is the dimension's {@link ResourceLocation} instead.
 */
public class DimensionCondition implements IUnlockCondition {

    private final ResourceLocation dimension;

    public DimensionCondition(ResourceLocation dimension) {
        this.dimension = dimension;
    }

    @Override
    public boolean areConditionObjectsEqual(Object stuff) {
        return dimension.equals(stuff);
    }

    @Override
    public Object getConditionObject() {
        return dimension;
    }

    @Override
    public int getType() {
        return 2;
    }
}
