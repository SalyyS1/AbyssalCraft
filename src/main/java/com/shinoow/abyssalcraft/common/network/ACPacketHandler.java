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
package com.shinoow.abyssalcraft.common.network;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.common.network.client.KnowledgeUnlockMessage;
import com.shinoow.abyssalcraft.common.network.server.UnlockAllKnowledgeMessage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * The mod's network channel.
 * <p>
 * 1.12.2 used {@code SimpleNetworkWrapper} with an auto-incrementing byte id and a separate
 * {@code IMessageHandler} per message. 1.20.1 wants a {@link SimpleChannel} with each message's
 * encoder, decoder, handler and direction declared together, and an explicit protocol version so
 * mismatched clients are rejected at handshake rather than desyncing later.
 * <p>
 * Message ids are assigned explicitly here rather than by a running counter, so adding a message
 * cannot silently renumber the others.
 */
public final class ACPacketHandler {

    /** Bumped whenever a message's wire format changes. */
    private static final String PROTOCOL_VERSION = "1";

    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(AbyssalCraft.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();

    private ACPacketHandler() {}

    public static void register() {
        CHANNEL.messageBuilder(KnowledgeUnlockMessage.class, 0, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(KnowledgeUnlockMessage::encode)
                .decoder(KnowledgeUnlockMessage::decode)
                .consumerMainThread(KnowledgeUnlockMessage::handle)
                .add();

        CHANNEL.messageBuilder(UnlockAllKnowledgeMessage.class, 1, NetworkDirection.PLAY_TO_SERVER)
                .encoder(UnlockAllKnowledgeMessage::encode)
                .decoder(UnlockAllKnowledgeMessage::decode)
                .consumerMainThread(UnlockAllKnowledgeMessage::handle)
                .add();
    }

    public static void sendToPlayer(Object message, ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToServer(Object message) {
        CHANNEL.sendToServer(message);
    }
}
