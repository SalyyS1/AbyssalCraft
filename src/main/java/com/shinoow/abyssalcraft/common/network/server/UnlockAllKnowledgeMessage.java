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
package com.shinoow.abyssalcraft.common.network.server;

import java.util.function.Supplier;

import com.shinoow.abyssalcraft.common.util.ACKnowledge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

/**
 * Asks the server to flip the unlock-all-knowledge flag for the sending player.
 * <p>
 * Only operators may use this: the flag reveals the entire Necronomicon, so a client that sends the
 * packet unprompted must not be trusted.
 */
public record UnlockAllKnowledgeMessage(boolean unlock) {

    /** Permission level required, matching a vanilla cheat-level command. */
    private static final int REQUIRED_PERMISSION_LEVEL = 2;

    public static void encode(UnlockAllKnowledgeMessage message, FriendlyByteBuf buffer) {
        buffer.writeBoolean(message.unlock());
    }

    public static UnlockAllKnowledgeMessage decode(FriendlyByteBuf buffer) {
        return new UnlockAllKnowledgeMessage(buffer.readBoolean());
    }

    public static void handle(UnlockAllKnowledgeMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            if (sender == null || !sender.hasPermissions(REQUIRED_PERMISSION_LEVEL)) {
                return;
            }
            ACKnowledge.get(sender).ifPresent(progress -> progress.unlockAllKnowledge(message.unlock()));
        });
        context.get().setPacketHandled(true);
    }
}
