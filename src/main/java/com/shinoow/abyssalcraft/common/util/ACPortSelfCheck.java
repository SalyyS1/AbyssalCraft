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
import com.shinoow.abyssalcraft.api.item.ACItems;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Startup self-check for the port.
 * <p>
 * Content is migrated subsystem by subsystem, and a {@link RegistryObject} that was declared but
 * never actually bound only fails later, at the point of use. This walks every declared block and
 * item entry once the server is up and reports anything unbound, absent from its registry, or
 * missing an item form, so a broken migration is visible immediately instead of at first use.
 */
public final class ACPortSelfCheck {

    private ACPortSelfCheck() {}

    public static void onServerStarted(ServerStartedEvent event) {
        List<String> problems = new ArrayList<>();
        int blocks = checkBlocks(problems);
        int items = checkItems(problems);

        if (problems.isEmpty()) {
            ACLogger.info("Port self-check: {} blocks and {} items resolved.", blocks, items);
        } else {
            ACLogger.severe("Port self-check: {} problems across {} blocks and {} items:",
                    problems.size(), blocks, items);
            problems.forEach(problem -> ACLogger.severe("  {}", problem));
        }
    }

    private static int checkBlocks(List<String> problems) {
        int checked = 0;
        for (Field field : ACBlocks.class.getDeclaredFields()) {
            RegistryObject<Block> entry = readEntry(field, problems);
            if (entry == null) {
                continue;
            }
            checked++;
            if (!verifyBound(field, entry, problems)) {
                continue;
            }
            if (!BuiltInRegistries.BLOCK.containsKey(entry.getId())) {
                problems.add(field.getName() + ": " + entry.getId() + " absent from the block registry");
            }
            if (new ItemStack(entry.get()).isEmpty()) {
                problems.add(field.getName() + ": " + entry.getId() + " has no item form");
            }
        }
        return checked;
    }

    private static int checkItems(List<String> problems) {
        int checked = 0;
        for (Field field : ACItems.class.getDeclaredFields()) {
            RegistryObject<Item> entry = readEntry(field, problems);
            if (entry == null) {
                continue;
            }
            checked++;
            if (!verifyBound(field, entry, problems)) {
                continue;
            }
            if (!BuiltInRegistries.ITEM.containsKey(entry.getId())) {
                problems.add(field.getName() + ": " + entry.getId() + " absent from the item registry");
            }
        }
        return checked;
    }

    /** Returns the field's registry object, or null when the field is not one. */
    private static <T> RegistryObject<T> readEntry(Field field, List<String> problems) {
        if (!RegistryObject.class.isAssignableFrom(field.getType())) {
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            RegistryObject<T> entry = (RegistryObject<T>) field.get(null);
            return entry;
        } catch (ReflectiveOperationException | RuntimeException e) {
            problems.add(field.getName() + ": " + e);
            return null;
        }
    }

    private static boolean verifyBound(Field field, RegistryObject<?> entry, List<String> problems) {
        if (entry.isPresent()) {
            return true;
        }
        problems.add(field.getName() + ": registry object is unbound");
        return false;
    }
}
