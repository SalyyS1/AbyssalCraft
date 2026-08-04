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

import java.util.ArrayList;
import java.util.List;

import com.shinoow.abyssalcraft.api.APIUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

/**
 * The list of Materializer results.
 * <p>
 * A materialization turns up to five crystals into one item. The crystals are not placed in slots:
 * they are drawn from a crystal bag held in the machine, which is why this registry also owns the
 * bag's NBT layout.
 * <p>
 * The 1.12.2 {@code consumeCrystals} had a bug worth not reproducing: it assigned its working copy
 * back to the {@code inventory} parameter, which does nothing to the caller's array, so a partial
 * consumption could leave crystals spent with no output. Here consumption is computed on a copy and
 * only written back once the whole recipe is satisfied.
 *
 * @author shinoow
 */
public class MaterializerRecipes {

    private static final MaterializerRecipes materializationBase = new MaterializerRecipes();

    /** NBT key the crystal bag stores its contents under; unchanged so existing bags still load. */
    private static final String BAG_INVENTORY_KEY = "ItemInventory";

    private final List<Materialization> materializations = new ArrayList<>();

    public static MaterializerRecipes instance() {
        return materializationBase;
    }

    private MaterializerRecipes() {}

    public void materialize(ItemStack[] input, ItemStack output) {
        materialize(new Materialization(input, output));
    }

    public void materialize(Materialization materialization) {
        materializations.add(materialization);
    }

    /** The materialization producing this output, or null when there is none. */
    public Materialization getMaterializationFor(ItemStack output) {
        return materializations.stream()
                .filter(entry -> APIUtils.areStacksEqual(entry.output, output))
                .findFirst()
                .orElse(null);
    }

    /** Every output the crystals currently in the bag can produce. */
    public List<ItemStack> getMaterializationResult(ItemStack bag) {
        ItemStack[] crystals = extractItemsFromBag(bag);
        if (crystals == null) {
            return List.of();
        }
        List<ItemStack> results = new ArrayList<>();
        for (Materialization entry : materializations) {
            if (canConsume(crystals, entry.input)) {
                results.add(entry.output.copy());
            }
        }
        return results;
    }

    /**
     * Spends the crystals for {@code output} out of {@code bag}, repeating for a stacked output.
     * Nothing is spent unless the full amount can be paid for.
     */
    public void processMaterialization(ItemStack output, ItemStack bag) {
        ItemStack[] crystals = extractItemsFromBag(bag);
        Materialization materialization = getMaterializationFor(output);
        if (crystals == null || materialization == null) {
            return;
        }

        ItemStack[] working = copyOf(crystals);
        int wanted = Math.max(1, output.getCount());
        for (int paid = 0; paid < wanted; paid++) {
            if (!consume(working, materialization.input)) {
                // Cannot pay for the whole output, so spend nothing.
                return;
            }
        }
        replaceBagContents(bag, working);
    }

    /** Whether these crystals could pay for the recipe, without spending them. */
    public boolean canConsume(ItemStack[] crystals, ItemStack[] recipe) {
        return consume(copyOf(crystals), recipe);
    }

    /**
     * Spends the recipe out of {@code crystals} in place, returning whether it was fully paid.
     * When it returns false the array may be partially spent, so callers work on a copy.
     */
    private boolean consume(ItemStack[] crystals, ItemStack[] recipe) {
        List<ItemStack> owed = new ArrayList<>();
        for (ItemStack required : recipe) {
            owed.add(required.copy());
        }

        for (int i = 0; i < crystals.length && !owed.isEmpty(); i++) {
            ItemStack available = crystals[i];
            if (available == null || available.isEmpty()) {
                continue;
            }
            owed.removeIf(required -> {
                if (!APIUtils.areStacksEqual(available, required) || available.isEmpty()) {
                    return false;
                }
                int paid = Math.min(available.getCount(), required.getCount());
                available.shrink(paid);
                required.shrink(paid);
                return required.isEmpty();
            });
            if (available.isEmpty()) {
                crystals[i] = ItemStack.EMPTY;
            }
        }
        return owed.isEmpty();
    }

    /**
     * Reads the crystals out of a bag, or null when the bag holds anything that is not a crystal.
     */
    public ItemStack[] extractItemsFromBag(ItemStack bag) {
        CompoundTag tag = bag.getTag();
        if (tag == null || !tag.contains(BAG_INVENTORY_KEY)) {
            return null;
        }
        ListTag stored = tag.getList(BAG_INVENTORY_KEY, Tag.TAG_COMPOUND);
        ItemStack[] crystals = new ItemStack[stored.size()];
        for (int i = 0; i < stored.size(); i++) {
            crystals[i] = ItemStack.of(stored.getCompound(i));
            if (!APIUtils.isCrystal(crystals[i])) {
                return null;
            }
        }
        return crystals;
    }

    /** Writes crystals back into a bag, dropping emptied entries. */
    public void replaceBagContents(ItemStack bag, ItemStack[] crystals) {
        ListTag stored = new ListTag();
        for (ItemStack crystal : crystals) {
            if (crystal != null && !crystal.isEmpty()) {
                stored.add(crystal.save(new CompoundTag()));
            }
        }
        bag.getOrCreateTag().put(BAG_INVENTORY_KEY, stored);
    }

    private static ItemStack[] copyOf(ItemStack[] crystals) {
        ItemStack[] copy = new ItemStack[crystals.length];
        for (int i = 0; i < crystals.length; i++) {
            copy[i] = crystals[i] == null ? ItemStack.EMPTY : crystals[i].copy();
        }
        return copy;
    }

    public List<Materialization> getMaterializationList() {
        return List.copyOf(materializations);
    }

    /** Clears the registry so a reload can repopulate it. */
    public void clear() {
        materializations.clear();
    }
}
