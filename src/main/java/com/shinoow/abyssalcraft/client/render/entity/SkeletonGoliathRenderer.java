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
import com.shinoow.abyssalcraft.common.entity.SkeletonGoliath;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the SkeletonGoliath.
 * <p>
 * 1.12.2 drew this with the vanilla biped model, so the vanilla layers are reused rather than
 * duplicating that geometry. The shadow radius matches the mob's footprint.
 */
public class SkeletonGoliathRenderer extends MobRenderer<SkeletonGoliath, HumanoidModel<SkeletonGoliath>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/elite/skeletongoliath.png");

    public SkeletonGoliathRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(SkeletonGoliath entity) {
        return TEXTURE;
    }
}
