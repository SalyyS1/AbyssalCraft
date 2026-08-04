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
package com.shinoow.abyssalcraft.common.entity.anti;

import com.shinoow.abyssalcraft.api.entity.IAntiEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;

/**
 * The Anti Bat: matter's opposite number to the vanilla bat.
 * <p>
 * 1.12.2 extended {@code EntityAmbientCreature} and reimplemented the whole of vanilla's bat
 * behaviour -- the hanging flag, the roost search, the erratic flight. Extending {@link Bat}
 * directly inherits all of it, so several hundred lines of duplicated logic are not carried over.
 * Only the health differs from a vanilla bat: 12 rather than 6.
 */
public class AntiBat extends Bat implements IAntiEntity {

    public AntiBat(EntityType<? extends Bat> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Bat.createAttributes().add(Attributes.MAX_HEALTH, 12.0D);
    }
}
