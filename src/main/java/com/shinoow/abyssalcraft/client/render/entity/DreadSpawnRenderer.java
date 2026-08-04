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
package com.shinoow.abyssalcraft.client.render.entity;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.client.model.entity.DreadSpawnModel;
import com.shinoow.abyssalcraft.client.render.ACModelLayers;
import com.shinoow.abyssalcraft.common.entity.DreadSpawn;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders the DreadSpawn. */
public class DreadSpawnRenderer extends MobRenderer<DreadSpawn, DreadSpawnModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/dread_spawn.png");

    public DreadSpawnRenderer(EntityRendererProvider.Context context) {
        super(context, new DreadSpawnModel(context.bakeLayer(ACModelLayers.DREAD_SPAWN)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(DreadSpawn entity) {
        return TEXTURE;
    }
}
