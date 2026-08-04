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
package com.shinoow.abyssalcraft.common.entity.demon;

import com.shinoow.abyssalcraft.common.entity.ACMonster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Shared base for the corrupted farm animals.
 * <p>
 * Despite looking like animals these are hostile mobs, which is why they extend Monster rather than
 * Animal: on 1.12.2 they extended EntityMob for the same reason, and they neither breed nor follow
 * a parent. All of them shamble at the 0.35 speed modifier and hit for 4 in hardcore mode; only
 * their health differs, so subclasses declare just that.
 */
public abstract class AbstractCorruptedAnimal extends ACMonster {

    /** Movement and attack speed shared by every corrupted animal. */
    protected static final double SPEED = 0.35D;

    protected AbstractCorruptedAnimal(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    /** Damage in hardcore mode is 4 for every one of them. */
    @Override
    protected double getHardcoreDamage() {
        return 4.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, SPEED, true));
        goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, SPEED));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
