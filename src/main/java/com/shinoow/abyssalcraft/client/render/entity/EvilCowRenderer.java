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

import com.shinoow.abyssalcraft.common.entity.demon.EvilCow;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the Evil Cow.
 * <p>
 * 1.12.2 drew these with the matching vanilla animal model, so the vanilla layer is reused rather
 * than duplicating that geometry. The demon animals ship their own sprite; the evil ones reuse
 * the vanilla animal texture, which is why that reference has no namespace.
 */
public class EvilCowRenderer extends MobRenderer<EvilCow, CowModel<EvilCow>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/cow/cow.png");

    public EvilCowRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.COW)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(EvilCow entity) {
        return TEXTURE;
    }
}
