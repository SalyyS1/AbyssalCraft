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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.shinoow.abyssalcraft.api.block.ACBlocks;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Startup self-check for the port.
 * <p>
 * Content is migrated subsystem by subsystem, and a {@link RegistryObject} that was declared but
 * never actually bound only fails later, at the point of use. This walks every declared block
 * entry once the server is up and reports anything unbound, missing an item form, or missing its
 * translation key, so a broken migration is visible immediately instead of at first placement.
 */
public final class ACPortSelfCheck {

    private ACPortSelfCheck() {}

    public static void onServerStarted(ServerStartedEvent event) {
        List<String> problems = new ArrayList<>();
        int checked = 0;

        for (Field field : ACBlocks.class.getDeclaredFields()) {
            if (!RegistryObject.class.isAssignableFrom(field.getType())) {
                continue;
            }
            checked++;
            try {
                @SuppressWarnings("unchecked")
                RegistryObject<Block> entry = (RegistryObject<Block>) field.get(null);

                if (!entry.isPresent()) {
                    problems.add(field.getName() + ": registry object is unbound");
                    continue;
                }
                Block block = entry.get();
                if (!BuiltInRegistries.BLOCK.containsKey(entry.getId())) {
                    problems.add(field.getName() + ": " + entry.getId() + " absent from the block registry");
                }
                if (new ItemStack(block).isEmpty()) {
                    problems.add(field.getName() + ": " + entry.getId() + " has no item form");
                }
            } catch (ReflectiveOperationException | RuntimeException e) {
                problems.add(field.getName() + ": " + e);
            }
        }

        if (problems.isEmpty()) {
            ACLogger.info("Port self-check: all {} registered blocks resolved.", checked);
        } else {
            ACLogger.severe("Port self-check: {} of {} block entries have problems:", problems.size(), checked);
            problems.forEach(problem -> ACLogger.severe("  {}", problem));
        }
    }
}
