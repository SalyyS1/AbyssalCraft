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
package com.shinoow.abyssalcraft.api.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import com.shinoow.abyssalcraft.init.ACRegistries;
import com.shinoow.abyssalcraft.lib.ACLib;
import com.shinoow.abyssalcraft.lib.item.ACArmorMaterial;
import com.shinoow.abyssalcraft.lib.item.ACItemTier;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraftforge.registries.RegistryObject;

/**
 * Contains all items added in AbyssalCraft.
 * <p>
 * These were mutable {@code public static Item} fields assigned during preInit on 1.12.2; they are
 * {@link RegistryObject}s now for the same reason the blocks are. Call {@code .get()} to reach the
 * item.
 * <p>
 * Tool and armor stats live in {@link ACItemTier} and {@link ACArmorMaterial}. Food values are
 * carried over from the 1.12.2 constructor arguments; {@code ItemFood(hunger, saturation, isWolfFood)}
 * became {@link FoodProperties}, where the old "hunger" argument is nutrition and the old
 * "saturation" argument is the saturation *modifier*.
 *
 * @author shinoow
 */
public class ACItems {

    // ---- Materials and ingots ----
    public static final RegistryObject<Item> chunk_of_abyssalnite = simple("chunk_of_abyssalnite");
    public static final RegistryObject<Item> dreaded_shard_of_abyssalnite = simple("dreaded_shard_of_abyssalnite");
    public static final RegistryObject<Item> dreaded_chunk_of_abyssalnite = simple("dreaded_chunk_of_abyssalnite");
    public static final RegistryObject<Item> abyssalnite_ingot = simple("abyssalnite_ingot");
    public static final RegistryObject<Item> coralium_gem = simple("coralium_gem");
    public static final RegistryObject<Item> coralium_pearl = simple("coralium_pearl");
    public static final RegistryObject<Item> chunk_of_coralium = simple("chunk_of_coralium");
    public static final RegistryObject<Item> refined_coralium_ingot = simple("refined_coralium_ingot");
    public static final RegistryObject<Item> coralium_plate = simple("coralium_plate");
    public static final RegistryObject<Item> coralium_brick = simple("coralium_brick");
    public static final RegistryObject<Item> dreadium_ingot = simple("dreadium_ingot");
    public static final RegistryObject<Item> dread_fragment = simple("dread_fragment");
    public static final RegistryObject<Item> dreadium_plate = simple("dreadium_plate");
    public static final RegistryObject<Item> dread_cloth = simple("dread_cloth");
    public static final RegistryObject<Item> shadow_fragment = simple("shadow_fragment");
    public static final RegistryObject<Item> shadow_shard = simple("shadow_shard");
    public static final RegistryObject<Item> shadow_gem = simple("shadow_gem");
    public static final RegistryObject<Item> shard_of_oblivion = simple("shard_of_oblivion");
    /**
     * The crafting ingredient, not the block of the same display name. Registered as "ethbrick"
     * on 1.12.2 precisely because "ethaxium_brick" was already taken by the block, whose BlockItem
     * still claims that name here.
     */
    public static final RegistryObject<Item> ethaxium_brick = simple("ethbrick");
    public static final RegistryObject<Item> ethaxium_ingot = simple("ethaxium_ingot");
    public static final RegistryObject<Item> life_crystal = simple("life_crystal");
    public static final RegistryObject<Item> tin_ingot = simple("tin_ingot");
    public static final RegistryObject<Item> copper_ingot = simple("copper_ingot");
    public static final RegistryObject<Item> iron_plate = simple("iron_plate");
    public static final RegistryObject<Item> nitre = simple("nitre");
    public static final RegistryObject<Item> sulfur = simple("sulfur");
    public static final RegistryObject<Item> methane = simple("methane");
    public static final RegistryObject<Item> carbon_cluster = simple("carbon_cluster");
    public static final RegistryObject<Item> dense_carbon_cluster = simple("dense_carbon_cluster");
    public static final RegistryObject<Item> eldritch_scale = simple("eldritch_scale");
    public static final RegistryObject<Item> ingot_nugget = simple("ingot_nugget");
    public static final RegistryObject<Item> crystal_fragment = simple("crystal_fragment");
    public static final RegistryObject<Item> transmutation_gem = simple("transmutation_gem");

    // ---- Tools ----
    public static final RegistryObject<Item> darkstone_pickaxe = pickaxe("darkstone_pickaxe", ACItemTier.DARKSTONE);
    public static final RegistryObject<Item> darkstone_axe = axe("darkstone_axe", ACItemTier.DARKSTONE, 6.0F);
    public static final RegistryObject<Item> darkstone_shovel = shovel("darkstone_shovel", ACItemTier.DARKSTONE);
    public static final RegistryObject<Item> darkstone_sword = sword("darkstone_sword", ACItemTier.DARKSTONE);
    public static final RegistryObject<Item> darkstone_hoe = hoe("darkstone_hoe", ACItemTier.DARKSTONE);

    public static final RegistryObject<Item> abyssalnite_pickaxe = pickaxe("abyssalnite_pickaxe", ACItemTier.ABYSSALNITE);
    public static final RegistryObject<Item> abyssalnite_axe = axe("abyssalnite_axe", ACItemTier.ABYSSALNITE, 6.0F);
    public static final RegistryObject<Item> abyssalnite_shovel = shovel("abyssalnite_shovel", ACItemTier.ABYSSALNITE);
    public static final RegistryObject<Item> abyssalnite_sword = sword("abyssalnite_sword", ACItemTier.ABYSSALNITE);
    public static final RegistryObject<Item> abyssalnite_hoe = hoe("abyssalnite_hoe", ACItemTier.ABYSSALNITE);

    public static final RegistryObject<Item> refined_coralium_pickaxe = pickaxe("refined_coralium_pickaxe", ACItemTier.REFINED_CORALIUM);
    public static final RegistryObject<Item> refined_coralium_axe = axe("refined_coralium_axe", ACItemTier.REFINED_CORALIUM, 6.0F);
    public static final RegistryObject<Item> refined_coralium_shovel = shovel("refined_coralium_shovel", ACItemTier.REFINED_CORALIUM);
    public static final RegistryObject<Item> refined_coralium_sword = sword("refined_coralium_sword", ACItemTier.REFINED_CORALIUM);
    public static final RegistryObject<Item> refined_coralium_hoe = hoe("refined_coralium_hoe", ACItemTier.REFINED_CORALIUM);

    public static final RegistryObject<Item> dreadium_pickaxe = pickaxe("dreadium_pickaxe", ACItemTier.DREADIUM);
    public static final RegistryObject<Item> dreadium_axe = axe("dreadium_axe", ACItemTier.DREADIUM, 6.0F);
    public static final RegistryObject<Item> dreadium_shovel = shovel("dreadium_shovel", ACItemTier.DREADIUM);
    public static final RegistryObject<Item> dreadium_sword = sword("dreadium_sword", ACItemTier.DREADIUM);
    public static final RegistryObject<Item> dreadium_hoe = hoe("dreadium_hoe", ACItemTier.DREADIUM);

    public static final RegistryObject<Item> ethaxium_pickaxe = pickaxe("ethaxium_pickaxe", ACItemTier.ETHAXIUM);
    public static final RegistryObject<Item> ethaxium_axe = axe("ethaxium_axe", ACItemTier.ETHAXIUM, 6.0F);
    public static final RegistryObject<Item> ethaxium_shovel = shovel("ethaxium_shovel", ACItemTier.ETHAXIUM);
    public static final RegistryObject<Item> ethaxium_sword = sword("ethaxium_sword", ACItemTier.ETHAXIUM);
    public static final RegistryObject<Item> ethaxium_hoe = hoe("ethaxium_hoe", ACItemTier.ETHAXIUM);

    // ---- Armor ----
    public static final RegistryObject<Item> abyssalnite_helmet = armor("abyssalnite_helmet", ACArmorMaterial.ABYSSALNITE, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> abyssalnite_chestplate = armor("abyssalnite_chestplate", ACArmorMaterial.ABYSSALNITE, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> abyssalnite_leggings = armor("abyssalnite_leggings", ACArmorMaterial.ABYSSALNITE, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> abyssalnite_boots = armor("abyssalnite_boots", ACArmorMaterial.ABYSSALNITE, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> dreaded_abyssalnite_helmet = armor("dreaded_abyssalnite_helmet", ACArmorMaterial.DREADED_ABYSSALNITE, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> dreaded_abyssalnite_chestplate = armor("dreaded_abyssalnite_chestplate", ACArmorMaterial.DREADED_ABYSSALNITE, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> dreaded_abyssalnite_leggings = armor("dreaded_abyssalnite_leggings", ACArmorMaterial.DREADED_ABYSSALNITE, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> dreaded_abyssalnite_boots = armor("dreaded_abyssalnite_boots", ACArmorMaterial.DREADED_ABYSSALNITE, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> refined_coralium_helmet = armor("refined_coralium_helmet", ACArmorMaterial.REFINED_CORALIUM, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> refined_coralium_chestplate = armor("refined_coralium_chestplate", ACArmorMaterial.REFINED_CORALIUM, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> refined_coralium_leggings = armor("refined_coralium_leggings", ACArmorMaterial.REFINED_CORALIUM, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> refined_coralium_boots = armor("refined_coralium_boots", ACArmorMaterial.REFINED_CORALIUM, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> plated_coralium_helmet = armor("plated_coralium_helmet", ACArmorMaterial.PLATED_CORALIUM, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> plated_coralium_chestplate = armor("plated_coralium_chestplate", ACArmorMaterial.PLATED_CORALIUM, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> plated_coralium_leggings = armor("plated_coralium_leggings", ACArmorMaterial.PLATED_CORALIUM, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> plated_coralium_boots = armor("plated_coralium_boots", ACArmorMaterial.PLATED_CORALIUM, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> depths_helmet = armor("depths_helmet", ACArmorMaterial.DEPTHS, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> depths_chestplate = armor("depths_chestplate", ACArmorMaterial.DEPTHS, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> depths_leggings = armor("depths_leggings", ACArmorMaterial.DEPTHS, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> depths_boots = armor("depths_boots", ACArmorMaterial.DEPTHS, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> dreadium_helmet = armor("dreadium_helmet", ACArmorMaterial.DREADIUM, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> dreadium_chestplate = armor("dreadium_chestplate", ACArmorMaterial.DREADIUM, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> dreadium_leggings = armor("dreadium_leggings", ACArmorMaterial.DREADIUM, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> dreadium_boots = armor("dreadium_boots", ACArmorMaterial.DREADIUM, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> dreadium_samurai_helmet = armor("dreadium_samurai_helmet", ACArmorMaterial.DREADIUM_SAMURAI, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> dreadium_samurai_chestplate = armor("dreadium_samurai_chestplate", ACArmorMaterial.DREADIUM_SAMURAI, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> dreadium_samurai_leggings = armor("dreadium_samurai_leggings", ACArmorMaterial.DREADIUM_SAMURAI, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> dreadium_samurai_boots = armor("dreadium_samurai_boots", ACArmorMaterial.DREADIUM_SAMURAI, ArmorItem.Type.BOOTS);

    public static final RegistryObject<Item> ethaxium_helmet = armor("ethaxium_helmet", ACArmorMaterial.ETHAXIUM, ArmorItem.Type.HELMET);
    public static final RegistryObject<Item> ethaxium_chestplate = armor("ethaxium_chestplate", ACArmorMaterial.ETHAXIUM, ArmorItem.Type.CHESTPLATE);
    public static final RegistryObject<Item> ethaxium_leggings = armor("ethaxium_leggings", ACArmorMaterial.ETHAXIUM, ArmorItem.Type.LEGGINGS);
    public static final RegistryObject<Item> ethaxium_boots = armor("ethaxium_boots", ACArmorMaterial.ETHAXIUM, ArmorItem.Type.BOOTS);

    // ---- Upgrade kits ----
    public static final RegistryObject<Item> cobblestone_upgrade_kit = simple("cobblestone_upgrade_kit");
    public static final RegistryObject<Item> iron_upgrade_kit = simple("iron_upgrade_kit");
    public static final RegistryObject<Item> gold_upgrade_kit = simple("gold_upgrade_kit");
    public static final RegistryObject<Item> diamond_upgrade_kit = simple("diamond_upgrade_kit");
    public static final RegistryObject<Item> abyssalnite_upgrade_kit = simple("abyssalnite_upgrade_kit");
    public static final RegistryObject<Item> coralium_upgrade_kit = simple("coralium_upgrade_kit");
    public static final RegistryObject<Item> dreadium_upgrade_kit = simple("dreadium_upgrade_kit");
    public static final RegistryObject<Item> ethaxium_upgrade_kit = simple("ethaxium_upgrade_kit");

    // ---- Food. Values carried over from the 1.12.2 ItemFood/ItemPlatefood arguments. ----
    public static final RegistryObject<Item> mre = food("mre", 20, 1.0F);
    public static final RegistryObject<Item> chicken_on_a_plate = food("chicken_on_a_plate", 9, 0.9F);
    public static final RegistryObject<Item> pork_on_a_plate = food("pork_on_a_plate", 12, 1.2F);
    public static final RegistryObject<Item> beef_on_a_plate = food("beef_on_a_plate", 12, 1.2F);
    public static final RegistryObject<Item> fish_on_a_plate = food("fish_on_a_plate", 8, 0.9F);
    public static final RegistryObject<Item> fried_egg = food("fried_egg", 5, 0.6F);
    public static final RegistryObject<Item> fried_egg_on_a_plate = food("fried_egg_on_a_plate", 8, 0.9F);
    public static final RegistryObject<Item> coralium_plagued_flesh = food("coralium_plagued_flesh", 2, 0.1F);
    public static final RegistryObject<Item> omothol_flesh = food("omothol_flesh", 3, 0.3F);
    public static final RegistryObject<Item> dirty_plate = simple("dirty_plate");
    public static final RegistryObject<Item> washcloth = simple("washcloth");
    public static final RegistryObject<Item> shoggoth_flesh = simple("shoggoth_flesh");

    // ---- Coins and engravings ----
    public static final RegistryObject<Item> coin = simple("coin");
    public static final RegistryObject<Item> blank_engraving = simple("blank_engraving");
    public static final RegistryObject<Item> cthulhu_engraved_coin = simple("cthulhu_engraved_coin");
    public static final RegistryObject<Item> elder_engraved_coin = simple("elder_engraved_coin");
    public static final RegistryObject<Item> jzahar_engraved_coin = simple("jzahar_engraved_coin");
    public static final RegistryObject<Item> hastur_engraved_coin = simple("hastur_engraved_coin");
    public static final RegistryObject<Item> azathoth_engraved_coin = simple("azathoth_engraved_coin");
    public static final RegistryObject<Item> nyarlathotep_engraved_coin = simple("nyarlathotep_engraved_coin");
    public static final RegistryObject<Item> yog_sothoth_engraved_coin = simple("yog_sothoth_engraved_coin");
    public static final RegistryObject<Item> shub_niggurath_engraved_coin = simple("shub_niggurath_engraved_coin");
    public static final RegistryObject<Item> cthulhu_engraving = simple("cthulhu_engraving");
    public static final RegistryObject<Item> elder_engraving = simple("elder_engraving");
    public static final RegistryObject<Item> jzahar_engraving = simple("jzahar_engraving");
    public static final RegistryObject<Item> hastur_engraving = simple("hastur_engraving");
    public static final RegistryObject<Item> azathoth_engraving = simple("azathoth_engraving");
    public static final RegistryObject<Item> nyarlathotep_engraving = simple("nyarlathotep_engraving");
    public static final RegistryObject<Item> yog_sothoth_engraving = simple("yog_sothoth_engraving");
    public static final RegistryObject<Item> shub_niggurath_engraving = simple("shub_niggurath_engraving");

    // ---- Ritual charms. Gate the deity rituals. ----
    public static final RegistryObject<Item> ritual_charm = simple("ritual_charm");
    public static final RegistryObject<Item> cthulhu_charm = simple("cthulhu_charm");
    public static final RegistryObject<Item> hastur_charm = simple("hastur_charm");
    public static final RegistryObject<Item> jzahar_charm = simple("jzahar_charm");
    public static final RegistryObject<Item> azathoth_charm = simple("azathoth_charm");
    public static final RegistryObject<Item> nyarlathotep_charm = simple("nyarlathotep_charm");
    public static final RegistryObject<Item> yog_sothoth_charm = simple("yog_sothoth_charm");
    public static final RegistryObject<Item> shub_niggurath_charm = simple("shub_niggurath_charm");

    // ---- Miscellaneous ----
    public static final RegistryObject<Item> essence = simple("essence");
    public static final RegistryObject<Item> skin = simple("skin");
    public static final RegistryObject<Item> stone_tablet = simple("stone_tablet");
    public static final RegistryObject<Item> scroll = simple("scroll");
    public static final RegistryObject<Item> unique_scroll = simple("unique_scroll");
    public static final RegistryObject<Item> antidote = simple("antidote");
    public static final RegistryObject<Item> crystal_shard = simple("crystal_shard");
    public static final RegistryObject<Item> configurator_shard = simple("configurator_shard");
    public static final RegistryObject<Item> shadow_titan_armor_plate = simple("shadow_titan_armor_plate");
    public static final RegistryObject<Item> powerstone_tracker = simple("powerstone_tracker");
    public static final RegistryObject<Item> eye_of_the_abyss = simple("eye_of_the_abyss");
    public static final RegistryObject<Item> oblivion_catalyst = simple("oblivion_catalyst");
    public static final RegistryObject<Item> essence_of_the_gatekeeper = simple("essence_of_the_gatekeeper");

    /**
     * Crystals and crystal shards, one item per type.
     * <p>
     * 1.12.2 stored the crystal type in item metadata, so {@code crystal_shard} damage 15 was the
     * Blaze shard. Metadata is gone, so each of the 28 types in {@link ACLib#crystalNames} becomes
     * its own item, indexed here in the same order the old metadata used.
     */
    public static final List<RegistryObject<Item>> crystals = registerCrystalSet("crystal");
    public static final List<RegistryObject<Item>> crystal_shards = registerCrystalSet("crystal_shard");

    private ACItems() {}

    /** Registers one item per crystal type, in the order the 1.12.2 metadata used. */
    private static List<RegistryObject<Item>> registerCrystalSet(String prefix) {
        List<RegistryObject<Item>> entries = new ArrayList<>(ACLib.crystalNames.length);
        for (String crystal : ACLib.crystalNames) {
            entries.add(simple(prefix + "_" + crystal.toLowerCase(Locale.ROOT)));
        }
        return List.copyOf(entries);
    }

    /** Looks up a crystal by its 1.12.2 metadata index. */
    public static Item crystal(int type) {
        return crystals.get(type).get();
    }

    /** Looks up a crystal shard by its 1.12.2 metadata index. */
    public static Item crystalShard(int type) {
        return crystal_shards.get(type).get();
    }

    private static RegistryObject<Item> simple(String name) {
        return ACRegistries.ITEMS.register(name, () -> new Item(new Item.Properties()));
    }

    private static RegistryObject<Item> food(String name, int nutrition, float saturationModifier) {
        return ACRegistries.ITEMS.register(name, () -> new Item(new Item.Properties().food(
                new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturationModifier).build())));
    }

    private static RegistryObject<Item> pickaxe(String name, ACItemTier tier) {
        return register(name, () -> new PickaxeItem(tier, 1, -2.8F, new Item.Properties()));
    }

    private static RegistryObject<Item> axe(String name, ACItemTier tier, float damage) {
        return register(name, () -> new AxeItem(tier, damage, -3.1F, new Item.Properties()));
    }

    private static RegistryObject<Item> shovel(String name, ACItemTier tier) {
        return register(name, () -> new ShovelItem(tier, 1.5F, -3.0F, new Item.Properties()));
    }

    private static RegistryObject<Item> sword(String name, ACItemTier tier) {
        return register(name, () -> new SwordItem(tier, 3, -2.4F, new Item.Properties()));
    }

    private static RegistryObject<Item> hoe(String name, ACItemTier tier) {
        return register(name, () -> new HoeItem(tier, -tier.getLevel(), -1.0F, new Item.Properties()));
    }

    private static RegistryObject<Item> armor(String name, ACArmorMaterial material, ArmorItem.Type type) {
        return register(name, () -> new ArmorItem(material, type, new Item.Properties()));
    }

    private static RegistryObject<Item> register(String name, Supplier<Item> item) {
        return ACRegistries.ITEMS.register(name, item);
    }

    /** Forces class initialisation so the fields above reach the DeferredRegister. */
    public static void register() {}
}
