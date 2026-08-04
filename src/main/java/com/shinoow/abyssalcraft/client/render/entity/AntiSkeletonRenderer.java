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
import com.shinoow.abyssalcraft.common.entity.anti.AntiSkeleton;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the AntiSkeleton.
 * <p>
 * 1.12.2 drew the anti-mobs with the vanilla biped model, so the vanilla layers are reused.
 */
public class AntiSkeletonRenderer extends MobRenderer<AntiSkeleton, HumanoidModel<AntiSkeleton>> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/anti/skeleton.png");

    public AntiSkeletonRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(AntiSkeleton entity) {
        return TEXTURE;
    }
}
