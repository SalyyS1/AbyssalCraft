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
package com.shinoow.abyssalcraft.common.handlers;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.api.necronomicon.condition.caps.INecroDataCapability;
import com.shinoow.abyssalcraft.common.util.ACKnowledge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * Attaches the Necronomicon progress store to players and carries it across death.
 * <p>
 * 1.12.2 copied progress in a clone handler for the same reason: capabilities are recreated on
 * respawn, so without this the player would lose everything they had unlocked.
 */
public final class ACKnowledgeHandler {

    private static final ResourceLocation ID = new ResourceLocation(AbyssalCraft.MOD_ID, "knowledge");

    private ACKnowledgeHandler() {}

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(INecroDataCapability.class);
    }

    public static void attach(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(ID, new ACKnowledge.Provider());
        }
    }

    /** Carries progress over when the player respawns after dying. */
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) {
            return;
        }
        event.getOriginal().reviveCaps();
        ACKnowledge.get(event.getOriginal()).ifPresent(
                original -> ACKnowledge.get(event.getEntity()).ifPresent(fresh -> fresh.copy(original)));
        event.getOriginal().invalidateCaps();
    }
}
