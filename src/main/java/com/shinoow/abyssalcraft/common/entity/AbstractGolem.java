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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
 * Shared base for the two golems.
 * <p>
 * The Abyssalnite and Dread golems had identical AI and stats on 1.12.2, differing only in which
 * rival golem they hunt, so everything but that target lives here. Outside hardcore mode neither
 * set a health value, leaving the vanilla mob default of 20, which is stated explicitly below.
 */
public abstract class AbstractGolem extends ACMonster {

    protected AbstractGolem(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createGolemAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 40.0D;
    }

    @Override
    protected double getHardcoreDamage() {
        return 8.0D;
    }

    /** The rival golem this one hunts. */
    protected abstract Class<? extends Monster> getRivalGolem();

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.35D, true));
        goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35D));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.35D));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, getRivalGolem(), true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
