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

import com.shinoow.abyssalcraft.lib.ACConfig;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/**
 * Shared base for AbyssalCraft hostile mobs.
 * <p>
 * On 1.12.2 every mob checked {@code ACConfig.hardcoreMode} inside its own
 * {@code applyEntityAttributes} and picked one of two stat pairs. Attributes are baked into the
 * {@link EntityType} on 1.20.1 and so are fixed before any config-dependent choice can be made;
 * the hardcore values are therefore applied to the instance on spawn. Subclasses declare their
 * hardcore stats by overriding {@link #getHardcoreHealth()} and {@link #getHardcoreDamage()},
 * which keeps that branch in one place instead of repeated across every mob.
 */
public abstract class ACMonster extends Monster {

    protected ACMonster(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    /** Max health in hardcore mode; NaN means the mob's health is unchanged by it. */
    protected double getHardcoreHealth() {
        return Double.NaN;
    }

    /** Attack damage in hardcore mode; NaN means the mob's damage is unchanged by it. */
    protected double getHardcoreDamage() {
        return Double.NaN;
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (level().isClientSide() || !ACConfig.hardcoreMode) {
            return;
        }
        boolean healthChanged = setAttributeBase(Attributes.MAX_HEALTH, getHardcoreHealth());
        setAttributeBase(Attributes.ATTACK_DAMAGE, getHardcoreDamage());
        if (healthChanged) {
            setHealth(getMaxHealth());
        }
    }

    /** Returns whether the attribute was actually changed. */
    private boolean setAttributeBase(Attribute attribute, double value) {
        if (Double.isNaN(value)) {
            return false;
        }
        AttributeInstance instance = getAttribute(attribute);
        if (instance == null) {
            return false;
        }
        instance.setBaseValue(value);
        return true;
    }
}
