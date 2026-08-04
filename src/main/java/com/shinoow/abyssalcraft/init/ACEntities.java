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
package com.shinoow.abyssalcraft.init;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.common.entity.DepthsGhoul;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Entity types.
 * <p>
 * 1.12.2 registered entities by class and a mod-local numeric ID through
 * {@code EntityRegistry.registerModEntity}, with spawn-egg colours passed in at the same time.
 * 1.20.1 needs an {@link EntityType} built with its category and hitbox size, attributes supplied
 * separately via {@link EntityAttributeCreationEvent}, and spawn eggs as ordinary registered items.
 * Hitbox sizes come from the old {@code setSize} calls.
 */
public final class ACEntities {

    public static final RegistryObject<EntityType<DepthsGhoul>> DEPTHS_GHOUL =
            ACRegistries.ENTITIES.register("depthsghoul", () -> EntityType.Builder
                    .of(DepthsGhoul::new, MobCategory.MONSTER)
                    .sized(1.0F, 3.0F)
                    .clientTrackingRange(4)
                    .build(AbyssalCraft.MOD_ID + ":depthsghoul"));

    private ACEntities() {}

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(DEPTHS_GHOUL.get(), DepthsGhoul.createAttributes().build());
    }

    /** Forces class initialisation so the fields above reach the DeferredRegister. */
    public static void register() {}
}
