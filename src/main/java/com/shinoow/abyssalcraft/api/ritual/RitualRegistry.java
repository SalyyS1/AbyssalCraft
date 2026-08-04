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
package com.shinoow.abyssalcraft.api.ritual;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shinoow.abyssalcraft.common.util.ACLogger;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/**
 * Registry of Necronomicon rituals, and of which Necronomicon tier each dimension requires.
 * <p>
 * Dimensions are keyed by {@link ResourceKey} rather than the numeric IDs 1.12.2 used. The split
 * between built-in and config-supplied mappings is kept: config entries act as a fallback, so a
 * pack can add dimensions without overriding what the mod itself declares.
 *
 * @author shinoow
 */
public class RitualRegistry {

    /** Highest valid Necronomicon tier. */
    public static final int MAX_BOOK_TYPE = 4;

    private static final RitualRegistry INSTANCE = new RitualRegistry();

    private final List<NecronomiconRitual> rituals = new ArrayList<>();
    private final Map<ResourceKey<Level>, Integer> dimToBookType = new HashMap<>();
    private final Map<ResourceKey<Level>, Integer> configDimToBookType = new HashMap<>();

    public static RitualRegistry instance() {
        return INSTANCE;
    }

    private RitualRegistry() {}

    public void addDimensionToBookType(ResourceKey<Level> dimension, int bookType) {
        dimToBookType.put(dimension, bookType);
    }

    public void addDimensionToBookTypeConfig(ResourceKey<Level> dimension, int bookType) {
        configDimToBookType.put(dimension, bookType);
    }

    public void wipeConfig() {
        configDimToBookType.clear();
    }

    /** Whether a book of the given tier can perform rituals in the given dimension. */
    public boolean canPerformAction(ResourceKey<Level> dimension, int bookType) {
        Integer required = dimToBookType.get(dimension);
        if (required == null) {
            required = configDimToBookType.get(dimension);
        }
        return required != null && bookType >= required;
    }

    /** Whether the given tier is exactly the one the dimension is mapped to. */
    public boolean sameBookType(ResourceKey<Level> dimension, int bookType) {
        Integer required = dimToBookType.get(dimension);
        if (required == null) {
            required = configDimToBookType.get(dimension);
        }
        return required != null && required == bookType;
    }

    public void registerRitual(NecronomiconRitual ritual) {
        if (ritual.getBookType() < 0 || ritual.getBookType() > MAX_BOOK_TYPE) {
            ACLogger.severe("Necronomicon book type does not exist: {}", ritual.getBookType());
            return;
        }
        for (NecronomiconRitual entry : rituals) {
            if (ritual.getUnlocalizedName().equals(entry.getUnlocalizedName())) {
                ACLogger.severe("Necronomicon Ritual already registered: {}", ritual.getUnlocalizedName());
                return;
            }
        }
        rituals.add(ritual);
    }

    public List<NecronomiconRitual> getRituals() {
        return List.copyOf(rituals);
    }
}
