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
package com.shinoow.abyssalcraft.common.network.client;

import com.shinoow.abyssalcraft.common.util.ACKnowledge;

import net.minecraft.client.Minecraft;

/**
 * Client half of {@link KnowledgeUnlockMessage}. Split out so the dedicated server never loads a
 * class that references {@code Minecraft}.
 */
final class ClientKnowledgeUnlockHandler {

    private ClientKnowledgeUnlockHandler() {}

    static void apply(KnowledgeUnlockMessage message) {
        ACKnowledge.get(Minecraft.getInstance().player)
                .ifPresent(progress -> ACKnowledge.trigger(progress, message.type(), message.trigger()));
    }
}
