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
 * Unlocks knowledge once the player has killed a given entity type.
 * <p>
 * 1.12.2 keyed this off a raw name string; a {@link ResourceLocation} is used here so the
 * reference is namespaced and validated.
 */
public class EntityCondition implements IUnlockCondition {

    private final ResourceLocation entity;

    public EntityCondition(ResourceLocation entity) {
        this.entity = entity;
    }

    @Override
    public boolean areConditionObjectsEqual(Object stuff) {
        return entity.equals(stuff);
    }

    @Override
    public Object getConditionObject() {
        return entity;
    }

    @Override
    public int getType() {
        return 1;
    }
}
