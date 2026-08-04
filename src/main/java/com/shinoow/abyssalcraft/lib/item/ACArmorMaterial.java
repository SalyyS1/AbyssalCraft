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

import java.util.EnumMap;
import java.util.function.Supplier;

import com.shinoow.abyssalcraft.AbyssalCraft;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Armor materials for AbyssalCraft sets.
 * <p>
 * 1.12.2 created these with {@code EnumHelper.addArmorMaterial}, splicing constants into the
 * vanilla {@code ArmorMaterial} enum. 1.20.1 replaces that enum with the {@link ArmorMaterial}
 * interface and asks for durability/defense per {@link ArmorItem.Type} rather than per array index.
 * <p>
 * The 1.12.2 defense arrays were ordered {boots, leggings, chestplate, helmet}; the per-type
 * mapping below preserves those values. Durability is the base multiplier from the old call, and
 * vanilla's per-slot factors {13, 15, 16, 11} apply the same way they did before.
 */
public enum ACArmorMaterial implements ArmorMaterial {

    /** 1.12.2: "Abyssalnite", 35 durability, {3,6,8,3} defense, 13 enchantability, 1.0 toughness. */
    ABYSSALNITE("abyssalnite", 35, 3, 6, 8, 3, 13, 1.0F),
    /** 1.12.2: "Dread", 36 durability, {3,6,8,3} defense, 15 enchantability, 1.0 toughness. */
    DREADED_ABYSSALNITE("dread", 36, 3, 6, 8, 3, 15, 1.0F),
    /** 1.12.2: "Coralium", 37 durability, {3,6,8,3} defense, 14 enchantability, 1.0 toughness. */
    REFINED_CORALIUM("coralium", 37, 3, 6, 8, 3, 14, 1.0F),
    /** 1.12.2: "CoraliumP", 55 durability, {4,7,9,4} defense, 14 enchantability, 3.0 toughness. */
    PLATED_CORALIUM("coraliump", 55, 4, 7, 9, 4, 14, 3.0F),
    /** 1.12.2: "Depths", 33 durability, {3,6,8,3} defense, 25 enchantability, 1.5 toughness. */
    DEPTHS("depths", 33, 3, 6, 8, 3, 25, 1.5F),
    /** 1.12.2: "Dreadium", 40 durability, {3,6,8,3} defense, 15 enchantability, 1.0 toughness. */
    DREADIUM("dreadium", 40, 3, 6, 8, 3, 15, 1.0F),
    /** 1.12.2: "DreadiumS", 45 durability, {3,6,8,3} defense, 20 enchantability, 1.5 toughness. */
    DREADIUM_SAMURAI("dreadiums", 45, 3, 6, 8, 3, 20, 1.5F),
    /** 1.12.2: "Ethaxium", 50 durability, {3,6,8,3} defense, 25 enchantability, 2.0 toughness. */
    ETHAXIUM("ethaxium", 50, 3, 6, 8, 3, 25, 2.0F);

    /** Vanilla's per-slot durability factors, unchanged from 1.12.2. */
    private static final EnumMap<ArmorItem.Type, Integer> DURABILITY_PER_TYPE = new EnumMap<>(ArmorItem.Type.class);

    static {
        DURABILITY_PER_TYPE.put(ArmorItem.Type.BOOTS, 13);
        DURABILITY_PER_TYPE.put(ArmorItem.Type.LEGGINGS, 15);
        DURABILITY_PER_TYPE.put(ArmorItem.Type.CHESTPLATE, 16);
        DURABILITY_PER_TYPE.put(ArmorItem.Type.HELMET, 11);
    }

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> defense = new EnumMap<>(ArmorItem.Type.class);
    private final int enchantmentValue;
    private final float toughness;
    private Supplier<Ingredient> repairIngredient = Ingredient::of;

    ACArmorMaterial(String name, int durabilityMultiplier, int boots, int leggings, int chestplate,
            int helmet, int enchantmentValue, float toughness) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.enchantmentValue = enchantmentValue;
        this.toughness = toughness;
        defense.put(ArmorItem.Type.BOOTS, boots);
        defense.put(ArmorItem.Type.LEGGINGS, leggings);
        defense.put(ArmorItem.Type.CHESTPLATE, chestplate);
        defense.put(ArmorItem.Type.HELMET, helmet);
    }

    /** Set after the fact, because the repair items register later than these constants load. */
    public void setRepairIngredient(Supplier<Ingredient> ingredient) {
        repairIngredient = ingredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return DURABILITY_PER_TYPE.get(type) * durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return defense.get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ARMOR_EQUIP_IRON;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }

    @Override
    public String getName() {
        return AbyssalCraft.MOD_ID + ":" + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    /** 1.12.2 had no knockback resistance on these sets. */
    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
