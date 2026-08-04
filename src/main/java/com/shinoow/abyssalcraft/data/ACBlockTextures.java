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

import java.util.HashMap;
import java.util.Map;

import com.shinoow.abyssalcraft.api.block.ACBlocks;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * Maps each block to the texture file it uses.
 * <p>
 * The 1.12.2 assets are abbreviated ({@code ds.png} for darkstone, {@code asb.png} for abyssal
 * stone brick) and the metadata split renamed most blocks, so a generated model cannot infer its
 * texture from the block's registry name. The textures themselves are All Rights Reserved and are
 * deliberately left untouched, so the mapping lives here instead of renaming files.
 * <p>
 * Every entry was read out of the corresponding 1.12.2 model JSON, so the port keeps the exact
 * texture each block had.
 */
final class ACBlockTextures {

    /** Texture base name per block, relative to {@code textures/blocks/}. */
    static final Map<RegistryObject<Block>, String> ALL = buildCubeTextures();

    /**
     * Blocks whose top and side textures differ; value order is {top, side}. These were separate
     * model files on 1.12.2 rather than cube_all.
     */
    static final Map<RegistryObject<Block>, String[]> COLUMNS = buildColumnTextures();

    /**
     * Built imperatively rather than with {@code Map.ofEntries}: there are more entries than that
     * method's fixed-arity overloads accept.
     */
    private static Map<RegistryObject<Block>, String> buildCubeTextures() {
        Map<RegistryObject<Block>, String> textures = new HashMap<>();
        textures.put(ACBlocks.darkstone, "ds");
        textures.put(ACBlocks.abyssal_stone, "as");
        textures.put(ACBlocks.dreadstone, "drs");
        textures.put(ACBlocks.abyssalnite_stone, "abydrs");
        textures.put(ACBlocks.coralium_stone, "cis");
        textures.put(ACBlocks.ethaxium, "eth");
        textures.put(ACBlocks.omothol_stone, "os");
        textures.put(ACBlocks.monolith_stone, "monolithstone");
        textures.put(ACBlocks.darkstone_cobblestone, "dsc");
        textures.put(ACBlocks.abyssal_cobblestone, "abyssalcobblestone");
        textures.put(ACBlocks.dreadstone_cobblestone, "dreadstonecobblestone");
        textures.put(ACBlocks.abyssalnite_cobblestone, "abyssalnitecobblestone");
        textures.put(ACBlocks.coralium_cobblestone, "coraliumcobblestone");
        textures.put(ACBlocks.darkstone_brick, "dsb");
        textures.put(ACBlocks.darkstone_brick_chiseled, "dsbc");
        textures.put(ACBlocks.darkstone_brick_cracked, "dsbcr");
        textures.put(ACBlocks.abyssal_stone_brick, "asb");
        textures.put(ACBlocks.abyssal_stone_brick_chiseled, "asbc");
        textures.put(ACBlocks.abyssal_stone_brick_cracked, "asbcr");
        textures.put(ACBlocks.dreadstone_brick, "drsb");
        textures.put(ACBlocks.dreadstone_brick_chiseled, "drsbc");
        textures.put(ACBlocks.dreadstone_brick_cracked, "drsbcr");
        textures.put(ACBlocks.abyssalnite_stone_brick, "abydrsb");
        textures.put(ACBlocks.abyssalnite_stone_brick_chiseled, "abydrsbc");
        textures.put(ACBlocks.abyssalnite_stone_brick_cracked, "abydrsbcr");
        textures.put(ACBlocks.coralium_stone_brick, "cstonebrick");
        textures.put(ACBlocks.coralium_stone_brick_chiseled, "cstonebrickc");
        textures.put(ACBlocks.coralium_stone_brick_cracked, "cstonebrickcr");
        textures.put(ACBlocks.ethaxium_brick, "eb");
        textures.put(ACBlocks.dark_ethaxium_brick, "deb");
        textures.put(ACBlocks.glowing_darkstone_bricks, "dsglow");
        textures.put(ACBlocks.coralium_ore, "co");
        textures.put(ACBlocks.abyssalnite_ore, "ano");
        textures.put(ACBlocks.abyssal_coralium_ore, "acoro");
        textures.put(ACBlocks.dreadlands_abyssalnite_ore, "drso");
        textures.put(ACBlocks.dreaded_abyssalnite_ore, "abydrso");
        textures.put(ACBlocks.nitre_ore, "no");
        textures.put(ACBlocks.abyssal_iron_ore, "aio");
        textures.put(ACBlocks.abyssal_gold_ore, "ago");
        textures.put(ACBlocks.abyssal_diamond_ore, "ado");
        textures.put(ACBlocks.abyssal_nitre_ore, "ao");
        textures.put(ACBlocks.abyssal_tin_ore, "ato");
        textures.put(ACBlocks.abyssal_copper_ore, "aco");
        textures.put(ACBlocks.pearlescent_coralium_ore, "apcoro");
        textures.put(ACBlocks.liquified_coralium_ore, "alcoro");
        textures.put(ACBlocks.abyssal_sand, "abyssalsand");
        textures.put(ACBlocks.abyssal_sand_glass, "abyssalsandglass");
        textures.put(ACBlocks.dreadlands_dirt, "dreadlandsdirt");
        textures.put(ACBlocks.calcified_stone, "calcifiedstone");
        textures.put(ACBlocks.coralium_infused_stone, "cis");
        textures.put(ACBlocks.dreadlands_infused_powerstone, "psdl");
        textures.put(ACBlocks.energy_collector, "energycollector");
        textures.put(ACBlocks.energy_container, "energycontainer");
        textures.put(ACBlocks.ritual_altar, "altar");
        textures.put(ACBlocks.wooden_crate, "crate");
        return Map.copyOf(textures);
    }

    private static Map<RegistryObject<Block>, String[]> buildColumnTextures() {
        Map<RegistryObject<Block>, String[]> textures = new HashMap<>();
        textures.put(ACBlocks.fused_abyssal_sand, new String[]{"fusedabyssalsand", "fusedabyssalsand_side"});
        textures.put(ACBlocks.ethaxium_pillar, new String[]{"ebp_top", "ebp"});
        textures.put(ACBlocks.dark_ethaxium_pillar, new String[]{"debp_top", "debp"});
        return Map.copyOf(textures);
    }

    private ACBlockTextures() {}
}
