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
package com.shinoow.abyssalcraft.common.util;

import java.util.Optional;

import com.shinoow.abyssalcraft.api.necronomicon.condition.caps.INecroDataCapability;
import com.shinoow.abyssalcraft.api.necronomicon.condition.caps.NecroDataCapability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Access to a player's Necronomicon progress.
 * <p>
 * 1.12.2 registered the capability with {@code CapabilityManager.register} plus a separate
 * {@code IStorage}. 1.20.1 uses a {@link CapabilityToken} and folds serialisation into the provider,
 * so both live here alongside the trigger dispatch that the network layer and the game events share.
 */
public final class ACKnowledge {

    public static final Capability<INecroDataCapability> CAPABILITY =
            CapabilityManager.get(new CapabilityToken<>() {});

    private ACKnowledge() {}

    public static Optional<INecroDataCapability> get(Player player) {
        return player == null ? Optional.empty() : player.getCapability(CAPABILITY).resolve();
    }

    /**
     * Fires the trigger matching a condition type. Kept in one place so the client-side network
     * handler and the server-side game events cannot drift on which list a type maps to.
     */
    public static void trigger(INecroDataCapability progress, int type, ResourceLocation name) {
        switch (type) {
            case 0 -> progress.triggerBiomeUnlock(name);
            case 1 -> progress.triggerEntityUnlock(name);
            case 2 -> progress.triggerDimensionUnlock(name);
            case 7 -> progress.triggerArtifactUnlock(name);
            case 8 -> progress.triggerPageUnlock(name);
            case 9 -> progress.triggerWhisperUnlock(name);
            case 10 -> progress.triggerMiscUnlock(name);
            default -> ACLogger.warning("Unknown Necronomicon unlock type {} for {}", type, name);
        }
    }

    /** Attaches the progress store to a player and serialises it with them. */
    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        private final NecroDataCapability progress = new NecroDataCapability();
        private final LazyOptional<INecroDataCapability> holder = LazyOptional.of(() -> progress);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> capability, Direction side) {
            return CAPABILITY.orEmpty(capability, holder);
        }

        @Override
        public CompoundTag serializeNBT() {
            return progress.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            progress.deserializeNBT(tag);
        }
    }
}
