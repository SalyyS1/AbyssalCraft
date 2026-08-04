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
package com.shinoow.abyssalcraft.api.ritual;

import com.shinoow.abyssalcraft.api.necronomicon.condition.DefaultCondition;
import com.shinoow.abyssalcraft.api.necronomicon.condition.IUnlockCondition;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Base class for Necronomicon rituals.
 * <p>
 * A ritual consumes Potential Energy and up to eight offerings placed on pedestals, optionally
 * requires a sacrifice, and may be restricted to one dimension and to a Necronomicon tier.
 * <p>
 * Two things changed shape in the port. The dimension restriction was a numeric ID with -1 meaning
 * "any"; it is now a nullable {@link ResourceKey}, where null means any. And the client half of
 * completion no longer round-trips through an internal method handler: {@link #completeRitual}
 * runs the server half on the server and the client half on the client, each on its own side.
 *
 * @author shinoow
 */
public abstract class NecronomiconRitual {

    /** Maximum offerings a ritual can require, one per pedestal. */
    public static final int MAX_OFFERINGS = 8;

    private final Object[] offerings;
    private final String unlocalizedName;
    private final int bookType;
    private final ResourceKey<Level> dimension;
    private final float requiredEnergy;
    private final boolean requiresSacrifice;

    protected Object sacrifice;

    private boolean nbtSensitive;
    private boolean nbtSensitiveSacrifice;
    private IUnlockCondition condition = new DefaultCondition();
    private EnumRitualParticle particle = EnumRitualParticle.ITEM;

    protected NecronomiconRitual(String unlocalizedName, int bookType, ResourceKey<Level> dimension,
            float requiredEnergy, boolean requiresSacrifice, Object... offerings) {
        if (offerings.length > MAX_OFFERINGS) {
            throw new IllegalArgumentException(
                    "Ritual " + unlocalizedName + " declares " + offerings.length
                            + " offerings; at most " + MAX_OFFERINGS + " fit on the pedestals");
        }
        this.unlocalizedName = unlocalizedName;
        this.bookType = bookType;
        this.dimension = dimension;
        this.requiredEnergy = requiredEnergy;
        this.requiresSacrifice = requiresSacrifice;
        this.offerings = offerings.clone();
    }

    protected NecronomiconRitual(String unlocalizedName, int bookType, ResourceKey<Level> dimension,
            float requiredEnergy, Object... offerings) {
        this(unlocalizedName, bookType, dimension, requiredEnergy, false, offerings);
    }

    /** A ritual with no dimension restriction. */
    protected NecronomiconRitual(String unlocalizedName, int bookType, float requiredEnergy,
            Object... offerings) {
        this(unlocalizedName, bookType, null, requiredEnergy, offerings);
    }

    public NecronomiconRitual setNBTSensitive() {
        nbtSensitive = true;
        return this;
    }

    public NecronomiconRitual setNBTSensitiveSacrifice() {
        nbtSensitiveSacrifice = true;
        return this;
    }

    public NecronomiconRitual setUnlockCondition(IUnlockCondition condition) {
        this.condition = condition;
        return this;
    }

    public NecronomiconRitual setRitualParticle(EnumRitualParticle particle) {
        this.particle = particle;
        return this;
    }

    public Object[] getOfferings() {
        return offerings.clone();
    }

    public boolean requiresSacrifice() {
        return requiresSacrifice;
    }

    /** Minimum Necronomicon tier needed to perform this ritual. */
    public int getBookType() {
        return bookType;
    }

    /** The dimension this ritual is restricted to, or null when it works anywhere. */
    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public float getReqEnergy() {
        return requiredEnergy;
    }

    public String getUnlocalizedName() {
        return "ac.ritual." + unlocalizedName;
    }

    public Component getDisplayName() {
        return Component.translatable(getUnlocalizedName());
    }

    public Component getDescription() {
        return Component.translatable(getUnlocalizedName() + ".desc");
    }

    public boolean requiresItemSacrifice() {
        return sacrifice != null;
    }

    public boolean isNBTSensitive() {
        return nbtSensitive;
    }

    public boolean isSacrificeNBTSensitive() {
        return nbtSensitiveSacrifice;
    }

    public IUnlockCondition getUnlockCondition() {
        return condition;
    }

    public EnumRitualParticle getRitualParticle() {
        return particle;
    }

    public Object getSacrifice() {
        return sacrifice;
    }

    /** Whether the ritual's own preconditions are met, beyond offerings and energy. */
    public abstract boolean canCompleteRitual(Level level, BlockPos pos, Player player);

    /** Runs the appropriate half of the ritual for the side it is called on. */
    public void completeRitual(Level level, BlockPos pos, Player player) {
        if (level.isClientSide()) {
            completeRitualClient(level, pos, player);
        } else {
            completeRitualServer(level, pos, player);
        }
    }

    /** Override to do something client-side when the ritual completes. */
    protected abstract void completeRitualClient(Level level, BlockPos pos, Player player);

    /** Override to do something server-side when the ritual completes. */
    protected abstract void completeRitualServer(Level level, BlockPos pos, Player player);
}
