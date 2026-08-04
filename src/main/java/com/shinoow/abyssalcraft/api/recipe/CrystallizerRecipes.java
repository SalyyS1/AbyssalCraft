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
 * The list of Crystallizer results.
 * <p>
 * These stay runtime registries rather than becoming datapack JSON: addon mods call
 * {@code crystallize(...)} against this API, and moving to a {@code RecipeSerializer} would break
 * every one of them. The 1.12.2 wildcard-metadata inputs collapse to plain item comparisons,
 * because variants are separate items on 1.20.1.
 *
 * @author shinoow
 */
public class CrystallizerRecipes {

    private static final CrystallizerRecipes crystallizationBase = new CrystallizerRecipes();

    /** The list of crystallization results. Insertion-ordered so JEI shows a stable list. */
    private final Map<ItemStack, ItemStack[]> crystallizationList = new LinkedHashMap<>();
    private final Map<ItemStack, Float> experienceList = new HashMap<>();

    public static CrystallizerRecipes instance() {
        return crystallizationBase;
    }

    private CrystallizerRecipes() {}

    public void crystallize(ItemLike input, ItemStack output1, ItemStack output2, float xp) {
        crystallize(new ItemStack(input), output1, output2, xp);
    }

    public void crystallize(ItemStack input, ItemStack output1, ItemStack output2, float xp) {
        crystallizationList.put(input, new ItemStack[]{output1, output2});
        experienceList.put(output1, xp);
    }

    /** Returns the crystallization result of an item, or two empty stacks when there is none. */
    public ItemStack[] getCrystallizationResult(ItemStack stack) {
        return crystallizationList.entrySet().stream()
                .filter(e -> APIUtils.areStacksEqual(stack, e.getKey()))
                .map(Entry::getValue)
                .findFirst()
                .orElse(new ItemStack[]{ItemStack.EMPTY, ItemStack.EMPTY});
    }

    public Map<ItemStack, ItemStack[]> getCrystallizationList() {
        return crystallizationList;
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
        crystallizationList.clear();
        experienceList.clear();
    }
}
