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

import com.shinoow.abyssalcraft.api.APIUtils;

import net.minecraft.world.item.ItemStack;

/**
 * A Materializer recipe: up to five crystals in, one item out.
 *
 * @author shinoow
 */
public class Materialization {

    /** Maximum crystals a materialization can require. */
    public static final int MAX_INPUTS = 5;

    public final ItemStack output;
    public final ItemStack[] input;

    public Materialization(ItemStack[] input, ItemStack output) {
        if (input.length > MAX_INPUTS) {
            throw new IllegalArgumentException("Materialization takes at most " + MAX_INPUTS
                    + " crystals, got " + input.length);
        }
        for (ItemStack stack : input) {
            if (!APIUtils.isCrystal(stack)) {
                throw new IllegalArgumentException("All materialization inputs must be crystals, got "
                        + stack.getItem());
            }
        }
        this.output = output;
        this.input = input.clone();
    }
}
