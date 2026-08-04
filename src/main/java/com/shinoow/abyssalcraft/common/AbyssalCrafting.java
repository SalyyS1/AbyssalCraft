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
package com.shinoow.abyssalcraft.common;

import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.api.item.ItemEngraving;
import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.EngraverRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.shinoow.abyssalcraft.lib.ACConfig;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

/**
 * Machine recipe registration.
 * <p>
 * Crystallizer and Transmutator recipes stay code-registered, matching 1.12.2, because the API
 * lets addon mods contribute to the same registries. What did change: 1.12.2 identified crystals by
 * item metadata, so a recipe output was {@code new ItemStack(crystal_shard, 4, 15)}; each crystal
 * type is a separate item now, so the same output is written through
 * {@link ACItems#crystalShard(int)} with the old metadata value as the index.
 * <p>
 * Ore-dictionary recipes from 1.12.2 are not reproduced here. The ore dictionary was replaced by
 * item tags, so those entries belong with the tag datagen phase rather than in this list.
 */
public final class AbyssalCrafting {

    private AbyssalCrafting() {}

    public static void init() {
        addCrystallization();
        addTransmutation();
        addEngravings();
    }

    /**
     * The Engraver stamps a blank coin with a deity engraving, or strips an engraved coin back to
     * blank. Both directions come from the same table: the blank engraving maps to the plain coin.
     */
    private static void addEngravings() {
        EngraverRecipes recipes = EngraverRecipes.instance();

        recipes.addCoin(ACItems.coin.get());
        recipes.addCoin(ACItems.cthulhu_engraved_coin.get());
        recipes.addCoin(ACItems.elder_engraved_coin.get());
        recipes.addCoin(ACItems.jzahar_engraved_coin.get());
        recipes.addCoin(ACItems.hastur_engraved_coin.get());
        recipes.addCoin(ACItems.azathoth_engraved_coin.get());
        recipes.addCoin(ACItems.nyarlathotep_engraved_coin.get());
        recipes.addCoin(ACItems.yog_sothoth_engraved_coin.get());
        recipes.addCoin(ACItems.shub_niggurath_engraved_coin.get());

        engraving(ACItems.coin, ACItems.blank_engraving, 0.0F);
        engraving(ACItems.cthulhu_engraved_coin, ACItems.cthulhu_engraving, 0.5F);
        engraving(ACItems.elder_engraved_coin, ACItems.elder_engraving, 0.5F);
        engraving(ACItems.jzahar_engraved_coin, ACItems.jzahar_engraving, 0.5F);
        engraving(ACItems.hastur_engraved_coin, ACItems.hastur_engraving, 0.5F);
        engraving(ACItems.azathoth_engraved_coin, ACItems.azathoth_engraving, 0.5F);
        engraving(ACItems.nyarlathotep_engraved_coin, ACItems.nyarlathotep_engraving, 0.5F);
        engraving(ACItems.yog_sothoth_engraved_coin, ACItems.yog_sothoth_engraving, 0.5F);
        engraving(ACItems.shub_niggurath_engraved_coin, ACItems.shub_niggurath_engraving, 0.5F);
    }

    private static void engraving(RegistryObject<Item> coin, RegistryObject<Item> stamp, float xp) {
        EngraverRecipes.instance().addEngraving(new ItemStack(coin.get()),
                (ItemEngraving) stamp.get(), xp);
    }

    private static void addCrystallization() {
        CrystallizerRecipes recipes = CrystallizerRecipes.instance();

        single(Items.BLAZE_POWDER, shards(15, 4), 0.1F);
        single(Items.BLACK_DYE, shards(7, 4), 0.0F);
        recipes.crystallize(new ItemStack(ACItems.dreaded_chunk_of_abyssalnite.get()),
                shards(12, 4), shards(14, 4), 0.2F);
        recipes.crystallize(new ItemStack(Items.WATER_BUCKET), shards(5, 12), shards(4, 6), 0.1F);
        recipes.crystallize(new ItemStack(ACItems.methane.get()), shards(4, 4), crystals(5, 16), 0.1F);
        recipes.crystallize(new ItemStack(Items.GUNPOWDER), shards(9, 16), shards(2, 4), 0.1F);
        recipes.crystallize(new ItemStack(Blocks.OBSIDIAN), shards(21, 4), shards(23, 4), 0.1F);
        recipes.crystallize(new ItemStack(Blocks.STONE), shards(21, 4), shards(23, 4), 0.1F);

        // Compound crystals split back into their constituents.
        recipes.crystallize(crystals(21, 1), crystals(18, 1), crystals(4, 2), 0.1F);
        recipes.crystallize(shards(21, 1), shards(18, 1), shards(4, 2), 0.1F);
        recipes.crystallize(crystals(22, 1), crystals(20, 2), crystals(4, 3), 0.1F);
        recipes.crystallize(shards(22, 1), shards(20, 2), shards(4, 3), 0.1F);
        recipes.crystallize(crystals(23, 1), crystals(19, 1), crystals(4, 1), 0.1F);
        recipes.crystallize(shards(23, 1), shards(19, 1), shards(4, 1), 0.1F);
        recipes.crystallize(crystals(10, 1), crystals(3, 1), crystals(5, 4), 0.1F);
        recipes.crystallize(shards(10, 1), shards(3, 1), shards(5, 4), 0.1F);
        recipes.crystallize(crystals(9, 1), crystals(6, 1), crystals(4, 3), 0.1F);
        recipes.crystallize(shards(9, 1), shards(6, 1), shards(4, 3), 0.1F);

        single(Items.ROTTEN_FLESH, shards(7, 8), 0.1F);
        single(ACItems.shoggoth_flesh.get(), shards(7, 8), 0.2F);
        recipes.crystallize(new ItemStack(ACItems.coralium_plagued_flesh.get()),
                shards(7, 8), new ItemStack(ACItems.crystal_fragment.get()), 0.2F);
        recipes.crystallize(new ItemStack(ACItems.dreaded_shard_of_abyssalnite.get()),
                shards(12, 1), shards(14, 4), 0.2F);
        single(Items.BONE, shards(25, 4), 0.2F);
        recipes.crystallize(new ItemStack(Items.PRISMARINE_SHARD), shards(21, 4), shards(27, 4), 0.1F);
        recipes.crystallize(new ItemStack(Items.PRISMARINE_CRYSTALS), shards(21, 4), shards(27, 4), 0.1F);
        recipes.crystallize(new ItemStack(Blocks.PRISMARINE), shards(21, 16), shards(27, 16), 0.1F);
        recipes.crystallize(new ItemStack(Blocks.PRISMARINE_BRICKS), crystals(21, 4), crystals(27, 4), 0.1F);
        recipes.crystallize(new ItemStack(Blocks.DARK_PRISMARINE), shards(21, 32), shards(27, 32), 0.1F);
        recipes.crystallize(new ItemStack(Items.EGG), shards(25, 4), shards(7, 4), 0.1F);

        if (ACConfig.crystal_rework) {
            single(ACItems.refined_coralium_ingot.get(), crystals(13, 1), 0.1F);
            single(ACItems.chunk_of_coralium.get(), crystals(13, 1), 0.1F);
            single(ACBlocks.liquified_coralium_ore.get().asItem(), crystals(13, 1), 0.1F);
            single(ACItems.abyssalnite_ingot.get(), crystals(12, 1), 0.1F);
            single(ACItems.chunk_of_abyssalnite.get(), crystals(12, 1), 0.1F);
            single(ACItems.dreadium_ingot.get(), crystals(14, 1), 0.1F);
            single(Items.IRON_INGOT, crystals(0, 1), 0.1F);
            single(Items.GOLD_INGOT, crystals(1, 1), 0.1F);
            single(Items.REDSTONE, crystals(11, 1), 0.1F);
            single(Items.COAL, crystals(3, 1), 0.1F);
            single(Items.CHARCOAL, crystals(3, 1), 0.1F);
            single(Blocks.COAL_ORE.asItem(), crystals(3, 2), 0.1F);
            single(Blocks.REDSTONE_ORE.asItem(), crystals(11, 2), 0.1F);
            recipes.crystallize(new ItemStack(ACBlocks.dreaded_abyssalnite_ore.get()),
                    crystals(12, 2), crystals(14, 2), 0.2F);
        }
    }

    private static void addTransmutation() {
        TransmutatorRecipes recipes = TransmutatorRecipes.instance();

        recipes.transmute(ACItems.chunk_of_abyssalnite.get(),
                new ItemStack(ACItems.abyssalnite_ingot.get()), 0.2F);
        recipes.transmute(ACItems.chunk_of_coralium.get(),
                new ItemStack(ACItems.refined_coralium_ingot.get()), 0.2F);
        recipes.transmute(ACBlocks.abyssalnite_ore.get(),
                new ItemStack(ACItems.chunk_of_abyssalnite.get()), 0.2F);
        recipes.transmute(ACBlocks.coralium_ore.get(),
                new ItemStack(ACItems.coralium_gem.get()), 0.2F);
    }

    private static void single(net.minecraft.world.level.ItemLike input, ItemStack output, float xp) {
        CrystallizerRecipes.instance().crystallize(new ItemStack(input), output, ItemStack.EMPTY, xp);
    }

    /** Builds a crystal stack by its 1.12.2 metadata index. */
    private static ItemStack crystals(int type, int count) {
        return new ItemStack(ACItems.crystal(type), count);
    }

    /** Builds a crystal shard stack by its 1.12.2 metadata index. */
    private static ItemStack shards(int type, int count) {
        return new ItemStack(ACItems.crystalShard(type), count);
    }
}
