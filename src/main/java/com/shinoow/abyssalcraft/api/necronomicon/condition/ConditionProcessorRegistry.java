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
package com.shinoow.abyssalcraft.api.necronomicon.condition;

import java.util.HashMap;
import java.util.Map;

import com.shinoow.abyssalcraft.common.util.ACLogger;

/**
 * Maps condition type IDs to the processor that evaluates them. Addon mods register their own
 * condition types here, so this stays a runtime registry.
 *
 * @author shinoow
 */
public class ConditionProcessorRegistry {

    private static final ConditionProcessorRegistry INSTANCE = new ConditionProcessorRegistry();

    private final Map<Integer, IConditionProcessor> processors = new HashMap<>();

    public static ConditionProcessorRegistry instance() {
        return INSTANCE;
    }

    private ConditionProcessorRegistry() {}

    public void registerProcessor(int type, IConditionProcessor processor) {
        if (type <= -1) {
            ACLogger.severe("Invalid condition type: {}", type);
            return;
        }
        if (processors.putIfAbsent(type, processor) != null) {
            ACLogger.severe("Processor already registered for condition type {}", type);
        }
    }

    /** Unregistered types deny the unlock rather than throwing. */
    public IConditionProcessor getProcessor(int type) {
        return processors.getOrDefault(type, (condition, cap, player) -> false);
    }

    public int getProcessorCount() {
        return processors.size();
    }
}
