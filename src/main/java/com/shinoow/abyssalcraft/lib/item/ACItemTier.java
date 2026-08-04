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
package com.shinoow.abyssalcraft.lib.item;

import java.util.function.Supplier;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

/**
 * Tool tiers for AbyssalCraft materials.
 * <p>
 * 1.12.2 created these with {@code EnumHelper.addToolMaterial}, which spliced new constants into
 * the vanilla {@code ToolMaterial} enum at runtime. That enum is gone; 1.20.1 uses the {@link Tier}
 * interface, and what used to be a numeric harvest level is now an incorrect-for-drops block tag.
 * Durability, speed, damage bonus and enchantability are carried over unchanged.
 */
public enum ACItemTier implements Tier {

    /** 1.12.2: level 1, 180 uses, 5.0 speed, +1 damage, 5 enchantability. */
    DARKSTONE(1, 180, 5.0F, 1.0F, 5, BlockTags.NEEDS_STONE_TOOL),
    /** 1.12.2: level 4, 1261 uses, 10.0 speed, +4 damage, 12 enchantability. */
    ABYSSALNITE(4, 1261, 10.0F, 4.0F, 12, BlockTags.NEEDS_DIAMOND_TOOL),
    /** 1.12.2: level 5, 1800 uses, 12.0 speed, +5 damage, 13 enchantability. */
    REFINED_CORALIUM(5, 1800, 12.0F, 5.0F, 13, BlockTags.NEEDS_DIAMOND_TOOL),
    /** 1.12.2: level 6, 2300 uses, 14.0 speed, +6 damage, 14 enchantability. */
    DREADIUM(6, 2300, 14.0F, 6.0F, 14, BlockTags.NEEDS_DIAMOND_TOOL),
    /** 1.12.2: level 8, 2800 uses, 16.0 speed, +8 damage, 20 enchantability. */
    ETHAXIUM(8, 2800, 16.0F, 8.0F, 20, BlockTags.NEEDS_DIAMOND_TOOL);

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final TagKey<Block> incorrectForDrops;
    private Supplier<Ingredient> repairIngredient = Ingredient::of;

    ACItemTier(int level, int uses, float speed, float damage, int enchantmentValue,
            TagKey<Block> incorrectForDrops) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.incorrectForDrops = incorrectForDrops;
    }

    /**
     * Repair material is supplied after the fact because the items themselves are registered
     * later than these constants are initialised.
     */
    public void setRepairIngredient(Supplier<Ingredient> ingredient) {
        repairIngredient = ingredient;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public TagKey<Block> getTag() {
        return incorrectForDrops;
    }
}
