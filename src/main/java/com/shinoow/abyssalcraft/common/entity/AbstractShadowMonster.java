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

import com.shinoow.abyssalcraft.api.entity.IOmotholEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Shared base for the shadow mobs.
 * <p>
 * The Shadow Creature and Shadow Monster had identical AI lists on 1.12.2 and both carried 0.2
 * knockback resistance, so all of that lives here; the subclasses differ only in size and stats.
 */
public abstract class AbstractShadowMonster extends ACMonster implements IOmotholEntity {

    /** Knockback resistance shared by the shadow mobs on 1.12.2. */
    public static final double KNOCKBACK_RESISTANCE = 0.2D;

    protected AbstractShadowMonster(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.35D, true));
        goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        goalSelector.addGoal(5, new RestrictSunGoal(this));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
