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
import com.shinoow.abyssalcraft.client.model.entity.ShadowCreatureModel;
import com.shinoow.abyssalcraft.client.render.ACModelLayers;
import com.shinoow.abyssalcraft.common.entity.ShadowCreature;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders the Shadow Creature. */
public class ShadowCreatureRenderer extends MobRenderer<ShadowCreature, ShadowCreatureModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/shadowcreature.png");

    public ShadowCreatureRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowCreatureModel(context.bakeLayer(ACModelLayers.SHADOW_CREATURE)), 0.3F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowCreature entity) {
        return TEXTURE;
    }
}
