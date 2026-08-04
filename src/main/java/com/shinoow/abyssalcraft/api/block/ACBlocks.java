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
package com.shinoow.abyssalcraft.api.block;

import java.util.function.Supplier;

import com.shinoow.abyssalcraft.common.blocks.ACBlockProperties;
import com.shinoow.abyssalcraft.common.blocks.CrateBlock;
import com.shinoow.abyssalcraft.common.blocks.EngraverBlock;
import com.shinoow.abyssalcraft.common.blocks.MachineBlock;
import com.shinoow.abyssalcraft.common.blocks.EnergyBlock;
import com.shinoow.abyssalcraft.common.blocks.tile.CrystallizerBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.EnergyCollectorBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.EnergyContainerBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.RitualAltarBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.TransmutatorBlockEntity;
import com.shinoow.abyssalcraft.init.ACBlockEntities;
import com.shinoow.abyssalcraft.init.ACRegistries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraftforge.registries.RegistryObject;

/**
 * Contains all blocks added in AbyssalCraft.
 * <p>
 * These were mutable {@code public static Block} fields assigned during preInit on 1.12.2.
 * Registries freeze during startup on 1.20.1, so each entry is a {@link RegistryObject} declared
 * here and handed to the mod event bus by {@link ACRegistries}. Call {@code .get()} to reach the
 * block itself.
 * <p>
 * Several 1.12.2 entries were metadata blocks holding up to eight variants behind one registry
 * name. Metadata is gone, so every variant is a separate block and the old aggregate fields
 * ({@code stone}, {@code cobblestone}, {@code darkstone_brick}, ...) are replaced by the
 * per-variant fields below. Hardness, resistance and map colours are carried over unchanged from
 * the 1.12.2 enums.
 *
 * @author shinoow
 */
public class ACBlocks {

    // Stone family. Was the metadata block "stone" (0-7).
    public static final RegistryObject<Block> darkstone = stone("darkstone", 1.65F, 12.0F, MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> abyssal_stone = stone("abyssal_stone", 1.8F, 12.0F, MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> dreadstone = stone("dreadstone", 2.5F, 20.0F, MapColor.COLOR_RED);
    public static final RegistryObject<Block> abyssalnite_stone = stone("abyssalnite_stone", 2.5F, 20.0F, MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> coralium_stone = stone("coralium_stone", 1.5F, 10.0F, MapColor.COLOR_CYAN);
    public static final RegistryObject<Block> ethaxium = stone("ethaxium", 100.0F, Float.MAX_VALUE, MapColor.WOOL);
    public static final RegistryObject<Block> omothol_stone = stone("omothol_stone", 10.0F, 12.0F, MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> monolith_stone = stone("monolith_stone", 6.0F, 24.0F, MapColor.COLOR_BLACK);

    // Cobblestone family. Was the metadata block "cobblestone" (0-4).
    public static final RegistryObject<Block> darkstone_cobblestone = stone("darkstone_cobblestone", 2.2F, 12.0F, MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> abyssal_cobblestone = stone("abyssal_cobblestone", 2.6F, 12.0F, MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> dreadstone_cobblestone = stone("dreadstone_cobblestone", 3.3F, 20.0F, MapColor.COLOR_RED);
    public static final RegistryObject<Block> abyssalnite_cobblestone = stone("abyssalnite_cobblestone", 3.3F, 20.0F, MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> coralium_cobblestone = stone("coralium_cobblestone", 2.0F, 10.0F, MapColor.COLOR_CYAN);

    // Brick families. Each was a metadata block with normal/chiseled/cracked variants.
    public static final RegistryObject<Block> darkstone_brick = stone("darkstone_brick", 1.65F, 12.0F, MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> darkstone_brick_chiseled = stone("chiseled_darkstone_brick", 1.65F, 12.0F, MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> darkstone_brick_cracked = stone("cracked_darkstone_brick", 1.65F, 12.0F, MapColor.COLOR_BLACK);
    public static final RegistryObject<Block> abyssal_stone_brick = stone("abyssal_stone_brick", 1.8F, 12.0F, MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> abyssal_stone_brick_chiseled = stone("chiseled_abyssal_stone_brick", 1.8F, 12.0F, MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> abyssal_stone_brick_cracked = stone("cracked_abyssal_stone_brick", 1.8F, 12.0F, MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> dreadstone_brick = stone("dreadstone_brick", 2.5F, 20.0F, MapColor.COLOR_RED);
    public static final RegistryObject<Block> dreadstone_brick_chiseled = stone("chiseled_dreadstone_brick", 2.5F, 20.0F, MapColor.COLOR_RED);
    public static final RegistryObject<Block> dreadstone_brick_cracked = stone("cracked_dreadstone_brick", 2.5F, 20.0F, MapColor.COLOR_RED);
    public static final RegistryObject<Block> abyssalnite_stone_brick = stone("abyssalnite_stone_brick", 2.5F, 20.0F, MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> abyssalnite_stone_brick_chiseled = stone("chiseled_abyssalnite_stone_brick", 2.5F, 20.0F, MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> abyssalnite_stone_brick_cracked = stone("cracked_abyssalnite_stone_brick", 2.5F, 20.0F, MapColor.COLOR_PURPLE);
    public static final RegistryObject<Block> coralium_stone_brick = stone("coralium_stone_brick", 1.5F, 10.0F, MapColor.COLOR_CYAN);
    public static final RegistryObject<Block> coralium_stone_brick_chiseled = stone("chiseled_coralium_stone_brick", 1.5F, 10.0F, MapColor.COLOR_CYAN);
    public static final RegistryObject<Block> coralium_stone_brick_cracked = stone("cracked_coralium_stone_brick", 1.5F, 10.0F, MapColor.COLOR_CYAN);
    public static final RegistryObject<Block> ethaxium_brick = stone("ethaxium_brick", 100.0F, Float.MAX_VALUE, MapColor.WOOL);
    public static final RegistryObject<Block> dark_ethaxium_brick = stone("dark_ethaxium_brick", 100.0F, Float.MAX_VALUE, MapColor.COLOR_BLACK);

    public static final RegistryObject<Block> glowing_darkstone_bricks = register("glowing_darkstone_bricks",
            () -> new Block(ACBlockProperties.glowingStone(55.0F, 3000.0F, MapColor.COLOR_BLACK, 15)));
    public static final RegistryObject<Block> ethaxium_pillar = pillar("ethaxium_pillar", 100.0F, Float.MAX_VALUE, MapColor.WOOL);
    public static final RegistryObject<Block> dark_ethaxium_pillar = pillar("dark_ethaxium_pillar", 100.0F, Float.MAX_VALUE, MapColor.COLOR_BLACK);

    // Ores. Experience drops match the 1.12.2 values.
    public static final RegistryObject<Block> coralium_ore = ore("coralium_ore", 3.0F, 6.0F, MapColor.COLOR_CYAN, 2, 5);
    public static final RegistryObject<Block> abyssalnite_ore = ore("abyssalnite_ore", 3.0F, 6.0F, MapColor.COLOR_PURPLE, 2, 5);
    public static final RegistryObject<Block> abyssal_coralium_ore = ore("abyssal_coralium_ore", 3.0F, 6.0F, MapColor.COLOR_CYAN, 2, 5);
    public static final RegistryObject<Block> dreadlands_abyssalnite_ore = ore("dreadlands_abyssalnite_ore", 3.0F, 6.0F, MapColor.COLOR_RED, 2, 5);
    public static final RegistryObject<Block> dreaded_abyssalnite_ore = ore("dreaded_abyssalnite_ore", 3.0F, 6.0F, MapColor.COLOR_RED, 2, 5);
    public static final RegistryObject<Block> nitre_ore = ore("nitre_ore", 3.0F, 6.0F, MapColor.STONE, 0, 2);
    public static final RegistryObject<Block> abyssal_iron_ore = ore("abyssal_iron_ore", 3.0F, 6.0F, MapColor.COLOR_GREEN, 0, 0);
    public static final RegistryObject<Block> abyssal_gold_ore = ore("abyssal_gold_ore", 3.0F, 6.0F, MapColor.COLOR_GREEN, 0, 0);
    public static final RegistryObject<Block> abyssal_diamond_ore = ore("abyssal_diamond_ore", 3.0F, 6.0F, MapColor.COLOR_GREEN, 3, 7);
    public static final RegistryObject<Block> abyssal_nitre_ore = ore("abyssal_nitre_ore", 3.0F, 6.0F, MapColor.COLOR_GREEN, 0, 2);
    public static final RegistryObject<Block> abyssal_tin_ore = ore("abyssal_tin_ore", 3.0F, 6.0F, MapColor.COLOR_GREEN, 0, 0);
    public static final RegistryObject<Block> abyssal_copper_ore = ore("abyssal_copper_ore", 3.0F, 6.0F, MapColor.COLOR_GREEN, 0, 0);
    public static final RegistryObject<Block> pearlescent_coralium_ore = ore("pearlescent_coralium_ore", 3.0F, 6.0F, MapColor.COLOR_CYAN, 2, 5);
    public static final RegistryObject<Block> liquified_coralium_ore = ore("liquified_coralium_ore", 3.0F, 6.0F, MapColor.COLOR_CYAN, 2, 5);

    // Terrain.
    public static final RegistryObject<Block> abyssal_sand = register("abyssal_sand",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> fused_abyssal_sand = stone("fused_abyssal_sand", 1.5F, 10.0F, MapColor.COLOR_GREEN);
    public static final RegistryObject<Block> abyssal_sand_glass = register("abyssal_sand_glass",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistryObject<Block> dreadlands_dirt = register("dreadlands_dirt",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).strength(0.5F).sound(SoundType.GRAVEL)));
    public static final RegistryObject<Block> solid_lava = stone("solid_lava", 1.5F, 10.0F, MapColor.COLOR_ORANGE);
    public static final RegistryObject<Block> calcified_stone = stone("calcified_stone", 1.5F, 10.0F, MapColor.SAND);
    public static final RegistryObject<Block> coralium_infused_stone = stone("coralium_infused_stone", 1.5F, 10.0F, MapColor.COLOR_CYAN);
    public static final RegistryObject<Block> dreadlands_infused_powerstone = stone("dreadlands_infused_powerstone", 2.5F, 20.0F, MapColor.COLOR_RED);

    // ---- Machines ----
    /**
     * The machines keep their 1.12.2 idle/active block split so recipes and JEI entries that
     * reference either form keep resolving.
     */
    public static final RegistryObject<Block> crystallizer_idle = register("crystallizer_idle",
            () -> new MachineBlock(ACBlockProperties.stone(3.5F, 6.0F, MapColor.COLOR_CYAN),
                    CrystallizerBlockEntity::new, () -> ACBlockEntities.CRYSTALLIZER.get(), false));
    public static final RegistryObject<Block> crystallizer_active = register("crystallizer_active",
            () -> new MachineBlock(ACBlockProperties.glowingStone(3.5F, 6.0F, MapColor.COLOR_CYAN, 13),
                    CrystallizerBlockEntity::new, () -> ACBlockEntities.CRYSTALLIZER.get(), true));
    public static final RegistryObject<Block> transmutator_idle = register("transmutator_idle",
            () -> new MachineBlock(ACBlockProperties.stone(3.5F, 6.0F, MapColor.COLOR_PURPLE),
                    TransmutatorBlockEntity::new, () -> ACBlockEntities.TRANSMUTATOR.get(), false));
    public static final RegistryObject<Block> transmutator_active = register("transmutator_active",
            () -> new MachineBlock(ACBlockProperties.glowingStone(3.5F, 6.0F, MapColor.COLOR_PURPLE, 13),
                    TransmutatorBlockEntity::new, () -> ACBlockEntities.TRANSMUTATOR.get(), true));

    /** The Engraver has no lit variant: it stamps coins rather than burning fuel. */
    public static final RegistryObject<Block> engraver = register("engraver",
            () -> new EngraverBlock(ACBlockProperties.stone(3.5F, 6.0F, MapColor.COLOR_GRAY)));

    /** The Wooden Crate: 36 slots of storage, with no facing. */
    public static final RegistryObject<Block> wooden_crate = register("wooden_crate",
            () -> new CrateBlock(ACBlockProperties.wood(3.0F, 6.0F, MapColor.WOOD)));

    // ---- Potential Energy ----
    public static final RegistryObject<Block> energy_collector = register("energy_collector",
            () -> new EnergyBlock(ACBlockProperties.stone(3.0F, 6.0F, MapColor.COLOR_PURPLE),
                    EnergyCollectorBlockEntity::new));
    public static final RegistryObject<Block> energy_container = register("energy_container",
            () -> new EnergyBlock(ACBlockProperties.stone(3.0F, 6.0F, MapColor.COLOR_PURPLE),
                    EnergyContainerBlockEntity::new));

    // ---- Rituals ----
    public static final RegistryObject<Block> ritual_altar = register("ritual_altar",
            () -> new EnergyBlock(ACBlockProperties.stone(3.0F, 6.0F, MapColor.COLOR_BLACK),
                    RitualAltarBlockEntity::new));

    private ACBlocks() {}

    private static RegistryObject<Block> stone(String name, float hardness, float resistance, MapColor color) {
        return register(name, () -> new Block(ACBlockProperties.stone(hardness, resistance, color)));
    }

    private static RegistryObject<Block> pillar(String name, float hardness, float resistance, MapColor color) {
        return register(name, () -> new RotatedPillarBlock(ACBlockProperties.stone(hardness, resistance, color)));
    }

    private static RegistryObject<Block> ore(String name, float hardness, float resistance, MapColor color, int minXp, int maxXp) {
        return register(name, () -> new DropExperienceBlock(
                ACBlockProperties.stone(hardness, resistance, color), UniformInt.of(minXp, maxXp)));
    }

    /** Registers the block and, as on 1.12.2, an accompanying ItemBlock under the same name. */
    private static RegistryObject<Block> register(String name, Supplier<Block> block) {
        RegistryObject<Block> registered = ACRegistries.BLOCKS.register(name, block);
        ACRegistries.ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }

    /** Forces class initialisation so the fields above reach the DeferredRegister. */
    public static void register() {}
}
