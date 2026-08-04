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
import com.shinoow.abyssalcraft.client.model.entity.ShadowMonsterModel;
import com.shinoow.abyssalcraft.client.render.ACModelLayers;
import com.shinoow.abyssalcraft.common.entity.ShadowMonster;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/** Renders the ShadowMonster. */
public class ShadowMonsterRenderer extends MobRenderer<ShadowMonster, ShadowMonsterModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/shadowmonster.png");

    public ShadowMonsterRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowMonsterModel(context.bakeLayer(ACModelLayers.SHADOW_MONSTER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowMonster entity) {
        return TEXTURE;
    }
}
