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

import java.util.List;
import java.util.function.Function;

import com.shinoow.abyssalcraft.api.necronomicon.condition.caps.INecroDataCapability;

import net.minecraft.resources.ResourceLocation;

/**
 * Registers the built-in unlock condition processors.
 * <p>
 * Every built-in condition resolves the same way: look for the condition's object in the matching
 * trigger list on the player's progress. One shared processor covers all of them, keyed by which
 * list to consult, rather than the seven near-identical classes 1.12.2 had.
 */
public final class ACConditionProcessors {

    private ACConditionProcessors() {}

    public static void register() {
        ConditionProcessorRegistry registry = ConditionProcessorRegistry.instance();

        registry.registerProcessor(0, triggerListProcessor(INecroDataCapability::getBiomeTriggers));
        registry.registerProcessor(1, triggerListProcessor(INecroDataCapability::getEntityTriggers));
        registry.registerProcessor(2, triggerListProcessor(INecroDataCapability::getDimensionTriggers));
        registry.registerProcessor(7, triggerListProcessor(INecroDataCapability::getArtifactTriggers));
        registry.registerProcessor(8, triggerListProcessor(INecroDataCapability::getPageTriggers));
        registry.registerProcessor(9, triggerListProcessor(INecroDataCapability::getWhisperTriggers));
        registry.registerProcessor(10, triggerListProcessor(INecroDataCapability::getMiscTriggers));
    }

    /** Satisfied when the condition's object appears in the chosen trigger list. */
    private static IConditionProcessor triggerListProcessor(
            Function<INecroDataCapability, List<ResourceLocation>> triggers) {
        return (condition, cap, player) -> {
            Object required = condition.getConditionObject();
            return required != null && triggers.apply(cap).stream()
                    .anyMatch(condition::areConditionObjectsEqual);
        };
    }
}
