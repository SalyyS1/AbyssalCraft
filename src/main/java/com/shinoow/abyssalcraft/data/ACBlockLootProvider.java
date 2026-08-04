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
package com.shinoow.abyssalcraft.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.shinoow.abyssalcraft.api.block.ACBlocks;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * Generates block loot tables.
 * <p>
 * 1.12.2 derived drops from {@code Block.getItemDropped}; 1.20.1 requires a loot table per block,
 * and a block without one silently drops nothing. Every registered block therefore gets at least a
 * self-drop here.
 * <p>
 * Ores drop themselves rather than a raw material, matching 1.12.2, where the ore blocks dropped
 * their own item form and smelting produced the ingot.
 */
public class ACBlockLootProvider extends BlockLootSubProvider {

    public ACBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        registeredBlocks().forEach(this::dropSelf);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return registeredBlocks();
    }

    /** Every block declared on {@link ACBlocks}, read reflectively so new blocks are never missed. */
    private static List<Block> registeredBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (Field field : ACBlocks.class.getDeclaredFields()) {
            if (!RegistryObject.class.isAssignableFrom(field.getType())) {
                continue;
            }
            try {
                @SuppressWarnings("unchecked")
                RegistryObject<Block> entry = (RegistryObject<Block>) field.get(null);
                if (entry.isPresent()) {
                    blocks.add(entry.get());
                }
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Could not read " + field.getName(), e);
            }
        }
        return blocks.stream().distinct().collect(Collectors.toList());
    }
}
