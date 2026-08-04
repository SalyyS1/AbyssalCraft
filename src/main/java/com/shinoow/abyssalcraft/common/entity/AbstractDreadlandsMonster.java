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

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Shared base for the slow Dreadlands melee mobs.
 * <p>
 * The Dreadling and Dread Spawn had byte-identical AI lists on 1.12.2 — the same five goals at the
 * same priorities, all at the 0.35 speed modifier that makes them shamble. That list lives here so
 * the two subclasses only carry their own stats.
 */
public abstract class AbstractDreadlandsMonster extends ACMonster {

    protected AbstractDreadlandsMonster(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.35D, true));
        goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
