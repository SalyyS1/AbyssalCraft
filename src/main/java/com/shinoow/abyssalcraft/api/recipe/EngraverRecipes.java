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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.shinoow.abyssalcraft.api.APIUtils;
import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.api.item.ItemEngraving;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * The list of Engraver results.
 * <p>
 * The Engraver stamps a blank coin with a deity engraving, or strips an engraved coin back to blank
 * with the blank engraving. That exclusive-or is the whole rule, and it is preserved exactly.
 * <p>
 * Kept as a runtime registry for the same reason as the other machine registries: addon mods
 * register their own coins and engravings through this API.
 *
 * @author shinoow
 */
public class EngraverRecipes {

    private static final EngraverRecipes engravingBase = new EngraverRecipes();

    private final List<ItemStack> coins = new ArrayList<>();
    /** Engraving stamp to the coin it produces. Insertion-ordered so JEI shows a stable list. */
    private final Map<ItemEngraving, ItemStack> engravings = new LinkedHashMap<>();
    private final Map<ItemStack, Float> experienceList = new HashMap<>();

    public static EngraverRecipes instance() {
        return engravingBase;
    }

    private EngraverRecipes() {}

    public void addCoin(ItemLike coin) {
        addCoin(new ItemStack(coin));
    }

    public void addCoin(ItemStack coin) {
        coins.add(coin);
    }

    public void addEngraving(ItemStack coin, ItemEngraving engraving, float xp) {
        engravings.put(engraving, coin);
        experienceList.put(coin, xp);
    }

    /** Whether the stack is a registered coin. */
    public boolean isCoin(ItemStack stack) {
        return coins.stream().anyMatch(coin -> matchesCoin(stack, coin));
    }

    /**
     * The coin produced by stamping {@code coin} with {@code engraving}, or an empty stack when the
     * pair is invalid.
     * <p>
     * A blank coin only accepts a deity engraving, and an already-engraved coin only accepts the
     * blank engraving, which is what turns it back into a blank. Anything else is refused.
     */
    public ItemStack getEngravingResult(ItemStack coin, ItemEngraving engraving) {
        ItemStack result = engravings.get(engraving);
        if (result == null || !isCoin(coin)) {
            return ItemStack.EMPTY;
        }
        boolean coinIsBlank = coin.getItem() == ACItems.coin.get();
        boolean engravingIsBlank = engraving == ACItems.blank_engraving.get();
        return coinIsBlank != engravingIsBlank ? result : ItemStack.EMPTY;
    }

    private static boolean matchesCoin(ItemStack stack, ItemStack coin) {
        return APIUtils.areStacksEqual(stack, coin) || stack.getItem() == ACItems.coin.get();
    }

    public List<ItemStack> getCoinList() {
        return List.copyOf(coins);
    }

    public Map<ItemEngraving, ItemStack> getEngravings() {
        return engravings;
    }

    public float getExperience(ItemStack stack) {
        return experienceList.entrySet().stream()
                .filter(e -> APIUtils.areStacksEqual(stack, e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0.0F);
    }

    /** Clears the registry so a reload can repopulate it. */
    public void clear() {
        coins.clear();
        engravings.clear();
        experienceList.clear();
    }
}
