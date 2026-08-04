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
package com.shinoow.abyssalcraft.api.recipe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.shinoow.abyssalcraft.api.APIUtils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * The list of Transmutator results.
 * <p>
 * Kept as a runtime registry for the same reason as {@link CrystallizerRecipes}: addon mods
 * register into it directly.
 *
 * @author shinoow
 */
public class TransmutatorRecipes {

    private static final TransmutatorRecipes transmutationBase = new TransmutatorRecipes();

    /** The list of transmutation results. Insertion-ordered so JEI shows a stable list. */
    private final Map<ItemStack, ItemStack> transmutationList = new LinkedHashMap<>();
    private final Map<ItemStack, Float> experienceList = new HashMap<>();

    public static TransmutatorRecipes instance() {
        return transmutationBase;
    }

    private TransmutatorRecipes() {}

    public void transmute(ItemLike input, ItemStack output, float xp) {
        transmute(new ItemStack(input), output, xp);
    }

    public void transmute(ItemStack input, ItemStack output, float xp) {
        transmutationList.put(input, output);
        experienceList.put(output, xp);
    }

    /** Returns the transmutation result of an item, or an empty stack when there is none. */
    public ItemStack getTransmutationResult(ItemStack stack) {
        return transmutationList.entrySet().stream()
                .filter(e -> APIUtils.areStacksEqual(stack, e.getKey()))
                .map(Entry::getValue)
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    public Map<ItemStack, ItemStack> getTransmutationList() {
        return transmutationList;
    }

    public float getExperience(ItemStack stack) {
        return experienceList.entrySet().stream()
                .filter(e -> APIUtils.areStacksEqual(stack, e.getKey()))
                .map(Entry::getValue)
                .findFirst()
                .orElse(0.0F);
    }

    /** Clears the registry so a datapack reload can repopulate it. */
    public void clear() {
        transmutationList.clear();
        experienceList.clear();
    }
}
