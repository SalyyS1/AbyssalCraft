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

import net.minecraft.client.model.BatModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ambient.Bat;

/**
 * Renders the Anti Bat with the vanilla bat model, as 1.12.2 did.
 * <p>
 * Typed to {@link Bat} rather than {@code AntiBat} because {@link BatModel} is bound to the vanilla
 * class; the Anti Bat extends it, so the renderer still only ever receives one.
 */
public class AntiBatRenderer extends MobRenderer<Bat, BatModel> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/anti/bat.png");

    public AntiBatRenderer(EntityRendererProvider.Context context) {
        super(context, new BatModel(context.bakeLayer(ModelLayers.BAT)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(Bat entity) {
        return TEXTURE;
    }
}
