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
package com.shinoow.abyssalcraft.common.entity;

import com.shinoow.abyssalcraft.api.entity.IDreadEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/** The Dread Golem. It hunts Abyssalnite Golems on sight, as it did on 1.12.2. */
public class DreadGolem extends AbstractGolem implements IDreadEntity {

    public DreadGolem(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected Class<? extends Monster> getRivalGolem() {
        return AbyssalniteGolem.class;
    }
}
