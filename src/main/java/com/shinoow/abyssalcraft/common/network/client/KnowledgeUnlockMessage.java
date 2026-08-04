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

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

/**
 * Tells a client that one of its Necronomicon unlock triggers has fired, so the book reflects the
 * new knowledge without waiting for a full progress sync.
 * <p>
 * 1.12.2 sent the payload as either a varint (dimension IDs) or a string, branching on the type.
 * Every trigger is a {@link ResourceLocation} now, so the wire format no longer needs that branch.
 */
public record KnowledgeUnlockMessage(int type, ResourceLocation trigger) {

    public static void encode(KnowledgeUnlockMessage message, FriendlyByteBuf buffer) {
        buffer.writeVarInt(message.type());
        buffer.writeResourceLocation(message.trigger());
    }

    public static KnowledgeUnlockMessage decode(FriendlyByteBuf buffer) {
        return new KnowledgeUnlockMessage(buffer.readVarInt(), buffer.readResourceLocation());
    }

    /**
     * The handler body lives in a client-only class: touching {@code Minecraft} from a class the
     * dedicated server loads would crash it, even on a code path the server never takes.
     */
    public static void handle(KnowledgeUnlockMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientKnowledgeUnlockHandler.apply(message)));
        context.get().setPacketHandled(true);
    }
}
