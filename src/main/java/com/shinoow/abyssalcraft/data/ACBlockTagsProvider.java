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
package com.shinoow.abyssalcraft.data;

import java.util.concurrent.CompletableFuture;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.api.block.ACBlocks;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * Generates block tags.
 * <p>
 * These replace the 1.12.2 {@code setHarvestLevel(tool, level)} calls: on 1.20.1 a block declares
 * which tool mines it and which tier is required through tags, and a block missing them either
 * drops without the right tool or cannot be mined at all.
 * <p>
 * Tiers are mapped from the 1.12.2 harvest levels: 0 to wood, 2 to iron, 3 to diamond, and the
 * higher AbyssalCraft levels to diamond as well, since vanilla has no tier above netherite and the
 * mod's own tiers already exceed it.
 */
public class ACBlockTagsProvider extends BlockTagsProvider {

    public ACBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,
            ExistingFileHelper helper) {
        super(output, lookup, AbyssalCraft.MOD_ID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Everything stone-like is pickaxe-mineable.
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ACBlocks.darkstone.get(), ACBlocks.abyssal_stone.get(), ACBlocks.dreadstone.get(),
                ACBlocks.abyssalnite_stone.get(), ACBlocks.coralium_stone.get(), ACBlocks.ethaxium.get(),
                ACBlocks.omothol_stone.get(), ACBlocks.monolith_stone.get(),
                ACBlocks.darkstone_cobblestone.get(), ACBlocks.abyssal_cobblestone.get(),
                ACBlocks.dreadstone_cobblestone.get(), ACBlocks.abyssalnite_cobblestone.get(),
                ACBlocks.coralium_cobblestone.get(),
                ACBlocks.darkstone_brick.get(), ACBlocks.darkstone_brick_chiseled.get(),
                ACBlocks.darkstone_brick_cracked.get(), ACBlocks.abyssal_stone_brick.get(),
                ACBlocks.abyssal_stone_brick_chiseled.get(), ACBlocks.abyssal_stone_brick_cracked.get(),
                ACBlocks.dreadstone_brick.get(), ACBlocks.dreadstone_brick_chiseled.get(),
                ACBlocks.dreadstone_brick_cracked.get(), ACBlocks.abyssalnite_stone_brick.get(),
                ACBlocks.abyssalnite_stone_brick_chiseled.get(),
                ACBlocks.abyssalnite_stone_brick_cracked.get(), ACBlocks.coralium_stone_brick.get(),
                ACBlocks.coralium_stone_brick_chiseled.get(), ACBlocks.coralium_stone_brick_cracked.get(),
                ACBlocks.ethaxium_brick.get(), ACBlocks.dark_ethaxium_brick.get(),
                ACBlocks.glowing_darkstone_bricks.get(), ACBlocks.ethaxium_pillar.get(),
                ACBlocks.dark_ethaxium_pillar.get(),
                ACBlocks.coralium_ore.get(), ACBlocks.abyssalnite_ore.get(),
                ACBlocks.abyssal_coralium_ore.get(), ACBlocks.dreadlands_abyssalnite_ore.get(),
                ACBlocks.dreaded_abyssalnite_ore.get(), ACBlocks.nitre_ore.get(),
                ACBlocks.abyssal_iron_ore.get(), ACBlocks.abyssal_gold_ore.get(),
                ACBlocks.abyssal_diamond_ore.get(), ACBlocks.abyssal_nitre_ore.get(),
                ACBlocks.abyssal_tin_ore.get(), ACBlocks.abyssal_copper_ore.get(),
                ACBlocks.pearlescent_coralium_ore.get(), ACBlocks.liquified_coralium_ore.get(),
                ACBlocks.fused_abyssal_sand.get(), ACBlocks.solid_lava.get(),
                ACBlocks.calcified_stone.get(), ACBlocks.coralium_infused_stone.get(),
                ACBlocks.dreadlands_infused_powerstone.get(),
                ACBlocks.crystallizer_idle.get(), ACBlocks.crystallizer_active.get(),
                ACBlocks.energy_collector.get(), ACBlocks.energy_container.get(),
                ACBlocks.ritual_altar.get());

        // Harvest level 0 on 1.12.2.
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                ACBlocks.darkstone.get(), ACBlocks.coralium_stone.get(), ACBlocks.monolith_stone.get(),
                ACBlocks.darkstone_cobblestone.get(), ACBlocks.coralium_cobblestone.get(),
                ACBlocks.darkstone_brick.get(), ACBlocks.darkstone_brick_chiseled.get(),
                ACBlocks.darkstone_brick_cracked.get(), ACBlocks.coralium_stone_brick.get(),
                ACBlocks.coralium_stone_brick_chiseled.get(), ACBlocks.coralium_stone_brick_cracked.get(),
                ACBlocks.nitre_ore.get(), ACBlocks.solid_lava.get(), ACBlocks.calcified_stone.get(),
                ACBlocks.coralium_infused_stone.get());

        // Harvest level 2 on 1.12.2.
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                ACBlocks.abyssal_stone.get(), ACBlocks.abyssal_cobblestone.get(),
                ACBlocks.abyssal_stone_brick.get(), ACBlocks.abyssal_stone_brick_chiseled.get(),
                ACBlocks.abyssal_stone_brick_cracked.get(),
                ACBlocks.coralium_ore.get(), ACBlocks.abyssalnite_ore.get(),
                ACBlocks.abyssal_coralium_ore.get(), ACBlocks.abyssal_iron_ore.get(),
                ACBlocks.abyssal_gold_ore.get(), ACBlocks.abyssal_nitre_ore.get(),
                ACBlocks.abyssal_tin_ore.get(), ACBlocks.abyssal_copper_ore.get(),
                ACBlocks.fused_abyssal_sand.get(),
                ACBlocks.crystallizer_idle.get(), ACBlocks.crystallizer_active.get(),
                ACBlocks.energy_collector.get(), ACBlocks.energy_container.get(),
                ACBlocks.ritual_altar.get());

        // Harvest level 3 and above on 1.12.2; diamond is the highest vanilla tier tag.
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                ACBlocks.dreadstone.get(), ACBlocks.abyssalnite_stone.get(), ACBlocks.ethaxium.get(),
                ACBlocks.omothol_stone.get(),
                ACBlocks.dreadstone_cobblestone.get(), ACBlocks.abyssalnite_cobblestone.get(),
                ACBlocks.dreadstone_brick.get(), ACBlocks.dreadstone_brick_chiseled.get(),
                ACBlocks.dreadstone_brick_cracked.get(), ACBlocks.abyssalnite_stone_brick.get(),
                ACBlocks.abyssalnite_stone_brick_chiseled.get(),
                ACBlocks.abyssalnite_stone_brick_cracked.get(),
                ACBlocks.ethaxium_brick.get(), ACBlocks.dark_ethaxium_brick.get(),
                ACBlocks.ethaxium_pillar.get(), ACBlocks.dark_ethaxium_pillar.get(),
                ACBlocks.glowing_darkstone_bricks.get(),
                ACBlocks.dreadlands_abyssalnite_ore.get(), ACBlocks.dreaded_abyssalnite_ore.get(),
                ACBlocks.abyssal_diamond_ore.get(), ACBlocks.pearlescent_coralium_ore.get(),
                ACBlocks.liquified_coralium_ore.get(), ACBlocks.dreadlands_infused_powerstone.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                ACBlocks.abyssal_sand.get(), ACBlocks.dreadlands_dirt.get());
    }
}
