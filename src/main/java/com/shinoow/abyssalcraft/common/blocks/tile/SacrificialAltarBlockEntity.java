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
package com.shinoow.abyssalcraft.common.blocks.tile;

import java.util.List;

import com.shinoow.abyssalcraft.api.energy.IEnergyCollector;
import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * The Sacrificial Altar collects Potential Energy from mobs that die nearby.
 * <p>
 * It marks one eligible mob at a time with glowing, and banks that mob's max health as PE when it
 * dies. To stop it being farmed, it goes on cooldown once a fifth of its capacity has been
 * collected in one stretch. All of those numbers are carried over from 1.12.2.
 * <p>
 * Undead and shadow mobs are excluded, as are players, armour stands and babies -- the same filter
 * the 1.12.2 tile applied. The 1.12.2 build also had a custom SHADOW creature attribute; that maps
 * to no vanilla {@link MobType}, so only the undead exclusion is expressible until the mod's own
 * mob types land.
 */
public class SacrificialAltarBlockEntity extends AbstractEnergyBlockEntity implements IEnergyCollector {

    /** Capacity, unchanged from 1.12.2. */
    public static final int CAPACITY = 5000;

    /** Ticks the altar sits idle after collecting a fifth of its capacity. */
    private static final int COOLDOWN_TICKS = 1200;

    /** Horizontal and vertical reach when looking for a mob to mark. */
    private static final int SEARCH_RADIUS_HORIZONTAL = 8;
    private static final int SEARCH_RADIUS_VERTICAL = 3;

    private LivingEntity marked;
    private float collectedSinceCooldown;
    private int coolDown;

    public SacrificialAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ACBlockEntities.SACRIFICIAL_ALTAR.get(), pos, state);
    }

    @Override
    public int getMaxEnergy() {
        return CAPACITY;
    }

    public boolean isCoolingDown() {
        return coolDown > 0;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state,
            SacrificialAltarBlockEntity be) {
        if (be.isCoolingDown()) {
            be.coolDown--;
        }

        if (be.marked == null) {
            be.marked = be.findSacrifice(level, pos);
        }

        if (be.marked != null) {
            if (be.canAcceptPE()) {
                // Glowing marks which mob the altar has claimed.
                be.marked.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20, 0, false, false));
            }
            if (!be.marked.isAlive()) {
                boolean killed = be.marked.getLastDamageSource() != null;
                if (killed && !be.isCoolingDown() && be.canAcceptPE()) {
                    float gained = be.marked.getMaxHealth();
                    be.addEnergy(gained);
                    be.collectedSinceCooldown += gained;
                }
                be.marked = null;
            }
        }

        if (be.collectedSinceCooldown >= be.getMaxEnergy() / 5.0F) {
            be.collectedSinceCooldown = 0;
            be.coolDown = COOLDOWN_TICKS;
            be.setChanged();
        }
    }

    /** The nearest living mob the altar is willing to claim, or null when there is none. */
    private LivingEntity findSacrifice(Level level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(SEARCH_RADIUS_HORIZONTAL, SEARCH_RADIUS_VERTICAL,
                SEARCH_RADIUS_HORIZONTAL);
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, area);

        for (LivingEntity candidate : candidates) {
            if (candidate instanceof Player || candidate instanceof ArmorStand) {
                continue;
            }
            if (candidate.getMobType() == MobType.UNDEAD) {
                continue;
            }
            if (candidate.isAlive() && !candidate.isBaby()) {
                return candidate;
            }
        }
        return null;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        coolDown = tag.getInt("CoolDown");
        collectedSinceCooldown = tag.getFloat("CollectionLimit");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("CoolDown", coolDown);
        tag.putFloat("CollectionLimit", collectedSinceCooldown);
    }
}
