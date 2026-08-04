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
package com.shinoow.abyssalcraft.api;

import net.minecraft.world.item.ItemStack;

/**
 * Utility methods shared by the AbyssalCraft API.
 *
 * @author shinoow
 */
public class APIUtils {

    /** Mirrors {@code ACConfig.display_names}; set during config bake. */
    public static boolean display_names;

    private APIUtils() {}

    /**
     * Compares two ItemStacks.
     * <p>
     * The 1.12.2 version also compared item damage, treating
     * {@code OreDictionary.WILDCARD_VALUE} as "any variant", because variants were encoded in
     * metadata. On 1.20.1 damage is only durability and every variant is a distinct item, so
     * comparing the item alone is both correct and what the old wildcard case effectively meant.
     *
     * @param stack1 First ItemStack to compare (the input Item Stack)
     * @param stack2 Second ItemStack to compare (the recipe Item Stack)
     * @return True if both stacks hold the same item, otherwise false
     */
    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }
        return stack1.getItem() == stack2.getItem();
    }

    /**
     * Compares two ItemStacks, optionally requiring equal NBT.
     *
     * @param stack1 First ItemStack to compare (the input Item Stack)
     * @param stack2 Second ItemStack to compare (the recipe Item Stack)
     * @param nbt Whether the stacks must also carry equal tags
     */
    public static boolean areStacksEqual(ItemStack stack1, ItemStack stack2, boolean nbt) {
        if (!areStacksEqual(stack1, stack2)) {
            return false;
        }
        return !nbt || areItemStackTagsEqual(stack1, stack2);
    }

    /** Checks whether two ItemStacks carry equal NBT tags. */
    public static boolean areItemStackTagsEqual(ItemStack stackA, ItemStack stackB) {
        if (stackA.isEmpty() || stackB.isEmpty()) {
            return false;
        }
        if (stackA.getTag() == null) {
            return stackB.getTag() == null;
        }
        return stackA.getTag().equals(stackB.getTag());
    }
}
