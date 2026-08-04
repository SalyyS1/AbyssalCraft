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
package com.shinoow.abyssalcraft.client.render;

import com.shinoow.abyssalcraft.AbyssalCraft;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

/**
 * Model layer identifiers.
 * <p>
 * 1.12.2 renderers instantiated their model directly. 1.20.1 bakes geometry once per layer and
 * hands renderers the baked result, so each model needs a registered {@link ModelLayerLocation}.
 */
public final class ACModelLayers {

    public static final ModelLayerLocation DREADLING = layer("dreadling");
    public static final ModelLayerLocation SHADOW_CREATURE = layer("shadow_creature");
    public static final ModelLayerLocation SHADOW_MONSTER = layer("shadow_monster");
    public static final ModelLayerLocation DREAD_SPAWN = layer("dread_spawn");

    private ACModelLayers() {}

    private static ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(new ResourceLocation(AbyssalCraft.MOD_ID, name), "main");
    }
}
