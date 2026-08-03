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
package com.shinoow.abyssalcraft.lib;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Config option definitions.
 * <p>
 * The 1.12.2 build read these through {@code net.minecraftforge.common.config.Configuration}
 * and cached the values in plain static fields. That class is gone, so the spec below defines
 * the same options against {@link ForgeConfigSpec} and {@link ACConfig} keeps mirroring them
 * into static fields so the rest of the mod reads them exactly as before.
 * <p>
 * Option paths intentionally reuse the 1.12.2 category and key strings so existing configs
 * keep their meaning. Dimension IDs are gone in 1.20.1 (dimensions are named by
 * {@code ResourceLocation}), so the four numeric dimension ID options are not carried over.
 */
public final class ACConfigSpec {

    public static final ForgeConfigSpec SPEC;
    public static final ACConfigSpec CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        CONFIG = new ACConfigSpec(builder);
        SPEC = builder.build();
    }

    // dimensions
    public final ForgeConfigSpec.BooleanValue keepLoaded1, keepLoaded2, keepLoaded3, keepLoaded4;

    // biome_generation
    public final ForgeConfigSpec.BooleanValue dark1, dark2, dark3, dark4, dark5, coralium1;

    // biome_spawning
    public final ForgeConfigSpec.BooleanValue darkspawn1, darkspawn2, darkspawn3, darkspawn4,
            darkspawn5, coraliumspawn1;

    // biome_weight
    public final ForgeConfigSpec.IntValue darkWeight1, darkWeight2, darkWeight3, darkWeight4,
            darkWeight5, coraliumWeight;

    // shoggoth
    public final ForgeConfigSpec.BooleanValue shoggothOoze, oozeExpire, consumeItems,
            shieldsBlockAcid, shoggothGlowingEyes;
    public final ForgeConfigSpec.DoubleValue acidResistanceHardness;
    public final ForgeConfigSpec.IntValue acidSpitFrequency, monolithBuildingCooldown,
            biomassPlayerDistance, biomassMaxSpawn, biomassCooldown, biomassShoggothDistance;

    // worldgen
    public final ForgeConfigSpec.BooleanValue generateDarklandsStructures, generateShoggothLairs,
            generateAbyssalWastelandPillars, generateAbyssalWastelandRuins, generateAntimatterLake,
            generateCoraliumLake, generateDreadlandsStalagmite, generateOmotholStructures,
            generateCoraliumOre, generateNitreOre, generateAbyssalniteOre, generateAbyssalCoraliumOre,
            generateDreadlandsAbyssalniteOre, generateDreadedAbyssalniteOre, generateAbyssalIronOre,
            generateAbyssalGoldOre, generateAbyssalDiamondOre, generateAbyssalNitreOre,
            generateAbyssalTinOre, generateAbyssalCopperOre, generatePearlescentCoraliumOre,
            generateLiquifiedCoraliumOre, useAmplifiedWorldType, generateStatuesInLairs;
    public final ForgeConfigSpec.IntValue shoggothLairSpawnRate, shoggothLairSpawnRateRivers,
            shoggothLairGenerationDistance;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> oreGenDimBlacklist,
            structureGenDimBlacklist;

    // item_blacklist
    public final ForgeConfigSpec.BooleanValue antiPlayersPickupLoot;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> mobItemPickupBlacklist;

    // silly_settings
    public final ForgeConfigSpec.BooleanValue breakLogic, nuclearAntimatterExplosions,
            jzaharBreaksFourthWall;
    public final ForgeConfigSpec.IntValue odbExplosionSize, antimatterExplosionSize;

    // blocks
    public final ForgeConfigSpec.BooleanValue darkstone_brick_slab, darkstone_cobblestone_slab,
            darkstone_brick_stairs, darkstone_cobblestone_stairs, darkstone_slab, darklands_oak_slab,
            darklands_oak_stairs, abyssal_stone_brick_slab, abyssal_stone_brick_stairs,
            coralium_stone_brick_slab, coralium_stone_brick_stairs, dreadstone_brick_slab,
            dreadstone_brick_stairs, abyssalnite_stone_brick_slab, abyssalnite_stone_brick_stairs,
            ethaxium_brick_slab, ethaxium_brick_stairs, abyssal_cobblestone_slab,
            abyssal_cobblestone_stairs, coralium_cobblestone_slab, coralium_cobblestone_stairs,
            dreadstone_cobblestone_slab, dreadstone_cobblestone_stairs, abyssalnite_cobblestone_slab,
            abyssalnite_cobblestone_stairs, darkstone_cobblestone_wall, abyssal_cobbblestone_wall,
            coralium_cobblestone_wall, dreadstone_cobblestone_wall, abyssalnite_cobblestone_wall;

    // wet_noodle
    public final ForgeConfigSpec.BooleanValue no_dreadlands_spread, no_acid_breaking_blocks,
            no_spectral_dragons, no_projectile_damage_immunity, no_disruptions, no_black_holes,
            no_odb_explosions;

    // mod_compat
    public final ForgeConfigSpec.BooleanValue hcdarkness_aw, hcdarkness_dl, hcdarkness_omt,
            hcdarkness_dr;

    // modules
    public final ForgeConfigSpec.BooleanValue foodstuff, upgrade_kits, plague_enchantments,
            crystal_rework;

    // spells
    public final ForgeConfigSpec.BooleanValue entropy_spell, life_drain_spell, mining_spell,
            grasp_of_cthulhu_spell, invisibility_spell, detachment_spell, steal_vigor_spell,
            sirens_song_spell, undeath_to_dust_spell, ooze_removal_spell, teleport_hostile_spell,
            floating_spell, teleport_home_spell;

    // general
    public final ForgeConfigSpec.BooleanValue shouldSpread, shouldInfect, destroyOcean,
            demonAnimalFire, particleBlock, particleEntity, hardcoreMode, antiItemDisintegration,
            smeltingRecipes, purgeMobSpawns, mimicFire, armorPotionEffects, syncDataOnBookOpening,
            dreadGrassSpread, display_names, portalSpawnsNearPlayer, showBossDialogs,
            lootTableContent, depthsGhoulBiomeDictSpawn, abyssalZombieBiomeDictSpawn, enchantBooks,
            nightVisionEverywhere, demonAnimalsSpawnOnDeath, evilAnimalNewMoonSpawning,
            enchantMergedBooks, no_potion_clouds;
    public final ForgeConfigSpec.IntValue evilAnimalSpawnWeight, portalCooldown,
            demonAnimalSpawnWeight, knowledgeSyncDelay, darkOffspringSpawnWeight,
            corruptionRitualRange, cleansingRitualRange, purgingRitualRange, enchantmentMaxLevel,
            curingRitualRange, jzaharHealingPace, jzaharHealingAmount, chagarothHealingPace,
            chagarothHealingAmount, sacthothHealingPace, sacthothHealingAmount;
    public final ForgeConfigSpec.DoubleValue damageAmpl, depthsHelmetOverlayOpacity;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> interdimensionalCageBlacklist,
            blackHoleBlacklist, dreadPlagueImmunityList, dreadPlagueCarrierList,
            coraliumPlagueImmunityList, coraliumPlagueCarrierList, itemTransportBlacklist,
            dimensionMappings;

    private ACConfigSpec(ForgeConfigSpec.Builder b) {
        b.comment("Dimension configuration (dimension unloading).",
                "Numeric dimension IDs are gone in 1.20.1; dimensions are identified by name.")
                .push("dimensions");
        keepLoaded1 = b.comment("Prevent unloading: The Abyssal Wasteland").define("keepLoadedAbyssalWasteland", false);
        keepLoaded2 = b.comment("Prevent unloading: The Dreadlands").define("keepLoadedDreadlands", false);
        keepLoaded3 = b.comment("Prevent unloading: Omothol").define("keepLoadedOmothol", false);
        keepLoaded4 = b.comment("Prevent unloading: The Dark Realm").define("keepLoadedDarkRealm", false);
        b.pop();

        b.comment("Biome generation configuration (whether a biome should generate).").push("biome_generation");
        dark1 = b.comment("Darklands").define("darklands", true);
        dark2 = b.comment("Darklands Forest").define("darklandsForest", true);
        dark3 = b.comment("Darklands Plains").define("darklandsPlains", true);
        dark4 = b.comment("Darklands Highland").define("darklandsHighland", true);
        dark5 = b.comment("Darklands Mountain").define("darklandsMountain", true);
        coralium1 = b.comment("Coralium Infested Swamp").define("coraliumInfestedSwamp", true);
        b.pop();

        b.comment("Biome spawning configuration (if players have a chance of spawning in the biomes).").push("biome_spawning");
        darkspawn1 = b.comment("Darklands").define("darklands", true);
        darkspawn2 = b.comment("Darklands Forest").define("darklandsForest", true);
        darkspawn3 = b.comment("Darklands Plains").define("darklandsPlains", true);
        darkspawn4 = b.comment("Darklands Highland").define("darklandsHighland", true);
        darkspawn5 = b.comment("Darklands Mountain").define("darklandsMountain", true);
        coraliumspawn1 = b.comment("Coralium Infested Swamp").define("coraliumInfestedSwamp", true);
        b.pop();

        b.comment("Biome weight configuration (the chance n out of 100 that a biome is picked to generate).").push("biome_weight");
        darkWeight1 = b.comment("Darklands").defineInRange("darklands", 4, 0, 100);
        darkWeight2 = b.comment("Darklands Forest").defineInRange("darklandsForest", 4, 0, 100);
        darkWeight3 = b.comment("Darklands Plains").defineInRange("darklandsPlains", 4, 0, 100);
        darkWeight4 = b.comment("Darklands Highland").defineInRange("darklandsHighland", 4, 0, 100);
        darkWeight5 = b.comment("Darklands Mountain").defineInRange("darklandsMountain", 4, 0, 100);
        coraliumWeight = b.comment("Coralium Infested Swamp").defineInRange("coraliumInfestedSwamp", 6, 0, 100);
        b.pop();

        b.comment("Shoggoth Ooze configuration.").push("shoggoth");
        shoggothOoze = b.comment("Shoggoth Ooze Spread").define("shoggothOozeSpread", true);
        oozeExpire = b.comment("Ooze expiration").define("oozeExpiration", true);
        consumeItems = b.comment("Item Consumption").define("itemConsumption", false);
        shieldsBlockAcid = b.comment("Shields Block Acid").define("shieldsBlockAcid", true);
        acidResistanceHardness = b.comment("Acid Resistance Hardness").defineInRange("acidResistanceHardness", 3.0D, 0.0D, 100.0D);
        acidSpitFrequency = b.comment("Acid Spit Frequency").defineInRange("acidSpitFrequency", 120, 20, 1200);
        monolithBuildingCooldown = b.comment("Monolith Building Cooldown").defineInRange("monolithBuildingCooldown", 1500, 100, 24000);
        shoggothGlowingEyes = b.comment("Glowing Eyes").define("glowingEyes", true);
        biomassPlayerDistance = b.comment("Biomass: Player Distance").defineInRange("biomassPlayerDistance", 16, 1, 128);
        biomassMaxSpawn = b.comment("Biomass: Spawn Limit").defineInRange("biomassSpawnLimit", 6, 0, 64);
        biomassCooldown = b.comment("Biomass: Cooldown Time").defineInRange("biomassCooldownTime", 400, 20, 24000);
        biomassShoggothDistance = b.comment("Biomass: Shoggoth Distance").defineInRange("biomassShoggothDistance", 32, 1, 128);
        b.pop();

        b.comment("World generation configuration (things that generate in the world).").push("worldgen");
        generateDarklandsStructures = b.comment("Darklands Structures").define("darklandsStructures", true);
        generateShoggothLairs = b.comment("Shoggoth Lairs").define("shoggothLairs", true);
        generateAbyssalWastelandPillars = b.comment("Abyssal Wasteland Pillars").define("abyssalWastelandPillars", true);
        generateAbyssalWastelandRuins = b.comment("Abyssal Wasteland Ruins").define("abyssalWastelandRuins", true);
        generateAntimatterLake = b.comment("Liquid Antimatter Lakes").define("liquidAntimatterLakes", true);
        generateCoraliumLake = b.comment("Liquid Coralium Lakes").define("liquidCoraliumLakes", true);
        generateDreadlandsStalagmite = b.comment("Dreadlands Stalagmites").define("dreadlandsStalagmites", true);
        generateOmotholStructures = b.comment("Generate Omothol City").define("omotholCity", true);
        generateCoraliumOre = b.comment("Coralium Ore").define("coraliumOre", true);
        generateNitreOre = b.comment("Nitre Ore").define("nitreOre", true);
        generateAbyssalniteOre = b.comment("Abyssalnite Ore").define("abyssalniteOre", true);
        generateAbyssalCoraliumOre = b.comment("Abyssal Coralium Ore").define("abyssalCoraliumOre", true);
        generateDreadlandsAbyssalniteOre = b.comment("Dreadlands Abyssalnite Ore").define("dreadlandsAbyssalniteOre", true);
        generateDreadedAbyssalniteOre = b.comment("Dreaded Abyssalnite Ore").define("dreadedAbyssalniteOre", true);
        generateAbyssalIronOre = b.comment("Abyssal Iron Ore").define("abyssalIronOre", true);
        generateAbyssalGoldOre = b.comment("Abyssal Gold Ore").define("abyssalGoldOre", true);
        generateAbyssalDiamondOre = b.comment("Abyssal Diamond Ore").define("abyssalDiamondOre", true);
        generateAbyssalNitreOre = b.comment("Abyssal Nitre Ore").define("abyssalNitreOre", true);
        generateAbyssalTinOre = b.comment("Abyssal Tin Ore").define("abyssalTinOre", true);
        generateAbyssalCopperOre = b.comment("Abyssal Copper Ore").define("abyssalCopperOre", true);
        generatePearlescentCoraliumOre = b.comment("Pearlescent Coralium Ore").define("pearlescentCoraliumOre", true);
        generateLiquifiedCoraliumOre = b.comment("Liquified Coralium Ore").define("liquifiedCoraliumOre", true);
        shoggothLairSpawnRate = b.comment("Shoggoth Lair Generation Chance: Swamps").defineInRange("shoggothLairChanceSwamps", 35, 0, 100);
        shoggothLairSpawnRateRivers = b.comment("Shoggoth Lair Generation Chance: Rivers").defineInRange("shoggothLairChanceRivers", 30, 0, 100);
        useAmplifiedWorldType = b.comment("Use Amplified World Type").define("useAmplifiedWorldType", true);
        generateStatuesInLairs = b.comment("Generate Statues In Lairs").define("statuesInLairs", true);
        shoggothLairGenerationDistance = b.comment("Shoggoth Lair Generation Distance").defineInRange("shoggothLairDistance", 100, 1, 1000);
        oreGenDimBlacklist = b.comment("Ore Generation Dimension Blacklist. Dimension names, eg. \"minecraft:the_nether\".")
                .defineList("oreGenDimBlacklist", List.of(), o -> o instanceof String);
        structureGenDimBlacklist = b.comment("Structure Generation Dimension Blacklist. Dimension names, eg. \"minecraft:the_nether\".")
                .defineList("structureGenDimBlacklist", List.of(), o -> o instanceof String);
        b.pop();

        b.comment("Entity Item Blacklist (blacklist items/blocks for entities that can pick up things).").push("item_blacklist");
        antiPlayersPickupLoot = b.comment("Anti-Players Can Pick Up Loot").define("antiPlayersCanPickUpLoot", true);
        mobItemPickupBlacklist = b.comment("Mob Pickup Item Blacklist")
                .defineList("mobPickupItemBlacklist", List.of("minecraft:rotten_flesh"), o -> o instanceof String);
        b.pop();

        b.comment("These settings are generally out of place, and don't contribute to the mod experience.").push("silly_settings");
        breakLogic = b.comment("Liquid Coralium Physics").define("liquidCoraliumPhysics", false);
        nuclearAntimatterExplosions = b.comment("Nuclear Antimatter Explosions").define("nuclearAntimatterExplosions", false);
        jzaharBreaksFourthWall = b.comment("J'zahar Can Break The Fourth Wall").define("jzaharCanBreakTheFourthWall", true);
        odbExplosionSize = b.comment("ODB Explosion Size").defineInRange("odbExplosionSize", 160, 1, 1000);
        antimatterExplosionSize = b.comment("Antimatter Explosion Size").defineInRange("antimatterExplosionSize", 80, 1, 1000);
        b.pop();

        b.comment("These settings allow you to disable specific blocks in the mod, mainly slabs, stairs and walls.").push("blocks");
        darkstone_brick_slab = b.comment("Darkstone Brick Slab").define("darkstoneBrickSlab", true);
        darkstone_cobblestone_slab = b.comment("Darkstone Cobblestone Slab").define("darkstoneCobblestoneSlab", true);
        darkstone_brick_stairs = b.comment("Darkstone Brick Stairs").define("darkstoneBrickStairs", true);
        darkstone_cobblestone_stairs = b.comment("Darkstone Cobblestone Stairs").define("darkstoneCobblestoneStairs", true);
        darkstone_slab = b.comment("Darkstone Slab").define("darkstoneSlab", true);
        darklands_oak_slab = b.comment("Darklands Oak Slab").define("darklandsOakSlab", true);
        darklands_oak_stairs = b.comment("Darklands Oak Stairs").define("darklandsOakStairs", true);
        abyssal_stone_brick_slab = b.comment("Abyssal Stone Brick Slab").define("abyssalStoneBrickSlab", true);
        abyssal_stone_brick_stairs = b.comment("Abyssal Stone Brick Stairs").define("abyssalStoneBrickStairs", true);
        coralium_stone_brick_slab = b.comment("Coralium Stone Brick Slab").define("coraliumStoneBrickSlab", true);
        coralium_stone_brick_stairs = b.comment("Coralium Stone Brick Stairs").define("coraliumStoneBrickStairs", true);
        dreadstone_brick_slab = b.comment("Dreadstone Brick Slab").define("dreadstoneBrickSlab", true);
        dreadstone_brick_stairs = b.comment("Dreadstone Brick Stairs").define("dreadstoneBrickStairs", true);
        abyssalnite_stone_brick_slab = b.comment("Abyssalnite Stone Brick Slab").define("abyssalniteStoneBrickSlab", true);
        abyssalnite_stone_brick_stairs = b.comment("Abyssalnite Stone Brick Stairs").define("abyssalniteStoneBrickStairs", true);
        ethaxium_brick_slab = b.comment("Ethaxium Brick Slab").define("ethaxiumBrickSlab", true);
        ethaxium_brick_stairs = b.comment("Ethaxium Brick Stairs").define("ethaxiumBrickStairs", true);
        abyssal_cobblestone_slab = b.comment("Abyssal Cobblestone Slab").define("abyssalCobblestoneSlab", true);
        abyssal_cobblestone_stairs = b.comment("Abyssal Cobblestone Stairs").define("abyssalCobblestoneStairs", true);
        coralium_cobblestone_slab = b.comment("Coralium Cobblestone Slab").define("coraliumCobblestoneSlab", true);
        coralium_cobblestone_stairs = b.comment("Coralium Cobblestone Stairs").define("coraliumCobblestoneStairs", true);
        dreadstone_cobblestone_slab = b.comment("Dreadstone Cobblestone Slab").define("dreadstoneCobblestoneSlab", true);
        dreadstone_cobblestone_stairs = b.comment("Dreadstone Cobblestone Stairs").define("dreadstoneCobblestoneStairs", true);
        abyssalnite_cobblestone_slab = b.comment("Abyssalnite Cobblestone Slab").define("abyssalniteCobblestoneSlab", true);
        abyssalnite_cobblestone_stairs = b.comment("Abyssalnite Cobblestone Stairs").define("abyssalniteCobblestoneStairs", true);
        darkstone_cobblestone_wall = b.comment("Darkstone Cobblestone Wall").define("darkstoneCobblestoneWall", true);
        abyssal_cobbblestone_wall = b.comment("Abyssal Cobblestone Wall").define("abyssalCobblestoneWall", true);
        coralium_cobblestone_wall = b.comment("Coralium Cobblestone Wall").define("coraliumCobblestoneWall", true);
        dreadstone_cobblestone_wall = b.comment("Dreadstone Cobblestone Wall").define("dreadstoneCobblestoneWall", true);
        abyssalnite_cobblestone_wall = b.comment("Abyssalnite Cobblestone Wall").define("abyssalniteCobblestoneWall", true);
        b.pop();

        b.comment("Settings that let you disable parts of the mod that some find punishing.").push("wet_noodle");
        no_dreadlands_spread = b.comment("Disable Dreadlands Spread").define("disableDreadlandsSpread", false);
        no_acid_breaking_blocks = b.comment("Disable Acid Projectiles Breaking Blocks").define("disableAcidBreakingBlocks", false);
        no_spectral_dragons = b.comment("Disable Spectral Dragons").define("disableSpectralDragons", false);
        no_projectile_damage_immunity = b.comment("Disable Projectile Damage Immunity").define("disableProjectileDamageImmunity", false);
        no_disruptions = b.comment("Disable Disruptions").define("disableDisruptions", false);
        no_black_holes = b.comment("Disable Black Holes").define("disableBlackHoles", false);
        no_odb_explosions = b.comment("Disable ODB Explosions").define("disableOdbExplosions", false);
        b.pop();

        b.comment("Compatibility toggles for other mods.").push("mod_compat");
        hcdarkness_aw = b.comment("Hardcore Darkness: Abyssal Wasteland").define("hardcoreDarknessAbyssalWasteland", true);
        hcdarkness_dl = b.comment("Hardcore Darkness: Dreadlands").define("hardcoreDarknessDreadlands", true);
        hcdarkness_omt = b.comment("Hardcore Darkness: Omothol").define("hardcoreDarknessOmothol", true);
        hcdarkness_dr = b.comment("Hardcore Darkness: Dark Realm").define("hardcoreDarknessDarkRealm", true);
        b.pop();

        b.comment("Optional content modules.").push("modules");
        foodstuff = b.comment("Enable Foodstuffs").define("enableFoodstuffs", true);
        upgrade_kits = b.comment("Enable Upgrade Kits").define("enableUpgradeKits", true);
        plague_enchantments = b.comment("Enable Plague Enchantments").define("enablePlagueEnchantments", true);
        crystal_rework = b.comment("Crystal Rework").define("crystalRework", true);
        b.pop();

        b.comment("Toggles for individual spells.").push("spells");
        entropy_spell = b.comment("Entropy").define("entropy", true);
        life_drain_spell = b.comment("Life Drain").define("lifeDrain", true);
        mining_spell = b.comment("Mining").define("mining", true);
        grasp_of_cthulhu_spell = b.comment("Grasp of Cthulhu").define("graspOfCthulhu", true);
        invisibility_spell = b.comment("Hide from the Eye").define("hideFromTheEye", true);
        detachment_spell = b.comment("Detachment").define("detachment", true);
        steal_vigor_spell = b.comment("Steal Vigor").define("stealVigor", true);
        sirens_song_spell = b.comment("Siren's Song").define("sirensSong", true);
        undeath_to_dust_spell = b.comment("Undeath to Dust").define("undeathToDust", true);
        ooze_removal_spell = b.comment("Ooze Removal").define("oozeRemoval", true);
        teleport_hostile_spell = b.comment("Sacrificial Interdiction").define("sacrificialInterdiction", true);
        floating_spell = b.comment("Floating").define("floating", true);
        teleport_home_spell = b.comment("Teleport Home").define("teleportHome", true);
        b.pop();

        b.comment("General configuration (misc things).").push("general");
        shouldSpread = b.comment("Set true for the Liquid Coralium to convert other liquids into itself and transmute blocks into their Abyssal Wasteland counterparts outside of the Abyssal Wasteland.")
                .define("liquidCoraliumTransmutation", true);
        shouldInfect = b.comment("Set true to allow the Coralium Plague to spread outside The Abyssal Wasteland.")
                .define("coraliumPlagueSpreading", false);
        destroyOcean = b.comment("Set true to allow the Liquid Coralium to spread across oceans.")
                .define("oceanicCoraliumPollution", false);
        demonAnimalFire = b.comment("Set to false to prevent Demon Animals (Pigs, Cows, Chickens) from burning in the overworld.")
                .define("demonAnimalBurning", false);
        evilAnimalSpawnWeight = b.comment("Spawn weight for the Evil Animals (Pigs, Cows, Chickens), keep under 35 to avoid complete annihilation.")
                .defineInRange("evilAnimalSpawnWeight", 15, 0, 100);
        particleBlock = b.comment("Toggles whether blocks that emit particles should do so.").define("blockParticles", true);
        particleEntity = b.comment("Toggles whether entities that emit particles should do so.").define("entityParticles", true);
        hardcoreMode = b.comment("Toggles Hardcore mode. If set to true, all mobs (in the mod) will become tougher.")
                .define("hardcoreMode", false);
        antiItemDisintegration = b.comment("Toggles whether or not Liquid Antimatter will disintegrate any items dropped into a pool of it.")
                .define("liquidAntimatterItemDisintegration", true);
        portalCooldown = b.comment("Cooldown after using a portal, measured in ticks (20 ticks = 1 second).")
                .defineInRange("portalCooldown", 100, 10, 300);
        demonAnimalSpawnWeight = b.comment("Spawn weight for the Demon Animals.").defineInRange("demonAnimalSpawnWeight", 15, 0, 100);
        smeltingRecipes = b.comment("Smelting Recipes").define("smeltingRecipes", true);
        purgeMobSpawns = b.comment("Purge Mob Spawns").define("purgeMobSpawns", false);
        interdimensionalCageBlacklist = b.comment("Interdimensional Cage Blacklist")
                .defineList("interdimensionalCageBlacklist", List.of(), o -> o instanceof String);
        damageAmpl = b.comment("Hardcore Mode damage amplifier").defineInRange("hardcoreModeDamageAmplifier", 1.0D, 0.0D, 100.0D);
        depthsHelmetOverlayOpacity = b.comment("Visage of The Depths Overlay Opacity")
                .defineInRange("visageOfTheDepthsOverlayOpacity", 1.0D, 0.0D, 1.0D);
        mimicFire = b.comment("Mimic Fire").define("mimicFire", true);
        armorPotionEffects = b.comment("Armor Potion Effects").define("armorPotionEffects", true);
        syncDataOnBookOpening = b.comment("Necronomicon Data Syncing").define("necronomiconDataSyncing", true);
        dreadGrassSpread = b.comment("Dreadlands Grass Spread").define("dreadlandsGrassSpread", true);
        display_names = b.comment("Display Item Names").define("displayItemNames", false);
        blackHoleBlacklist = b.comment("Reality Maelstrom Blacklist. Dimension names, eg. \"minecraft:the_nether\".")
                .defineList("realityMaelstromBlacklist", List.of(), o -> o instanceof String);
        portalSpawnsNearPlayer = b.comment("Portal Mob Spawning Near Players").define("portalMobSpawningNearPlayers", true);
        showBossDialogs = b.comment("Show Boss Dialogs").define("showBossDialogs", true);
        knowledgeSyncDelay = b.comment("Knowledge Sync Delay").defineInRange("knowledgeSyncDelay", 60, 1, 1200);
        dreadPlagueImmunityList = b.comment("Dread Plague Immunity List")
                .defineList("dreadPlagueImmunityList", List.of(), o -> o instanceof String);
        dreadPlagueCarrierList = b.comment("Dread Plague Carrier List")
                .defineList("dreadPlagueCarrierList", List.of(), o -> o instanceof String);
        coraliumPlagueImmunityList = b.comment("Coralium Plague Immunity List")
                .defineList("coraliumPlagueImmunityList", List.of(), o -> o instanceof String);
        coraliumPlagueCarrierList = b.comment("Coralium Plague Carrier List")
                .defineList("coraliumPlagueCarrierList", List.of(), o -> o instanceof String);
        lootTableContent = b.comment("Loot Table Content").define("lootTableContent", true);
        depthsGhoulBiomeDictSpawn = b.comment("Depths Ghoul Biome Dictionary Spawning").define("depthsGhoulBiomeDictionarySpawning", true);
        abyssalZombieBiomeDictSpawn = b.comment("Abyssal Zombie Biome Dictionary Spawning").define("abyssalZombieBiomeDictionarySpawning", true);
        darkOffspringSpawnWeight = b.comment("Dark Offspring Spawn Weight").defineInRange("darkOffspringSpawnWeight", 5, 0, 100);
        corruptionRitualRange = b.comment("Corruption Ritual Range").defineInRange("corruptionRitualRange", 32, 1, 256);
        cleansingRitualRange = b.comment("Cleansing Ritual Range").defineInRange("cleansingRitualRange", 32, 1, 256);
        purgingRitualRange = b.comment("Purging Ritual Range").defineInRange("purgingRitualRange", 32, 1, 256);
        enchantmentMaxLevel = b.comment("Mass Enchantment Max Level").defineInRange("massEnchantmentMaxLevel", 10, 1, 127);
        enchantBooks = b.comment("Mass Enchantment Books").define("massEnchantmentBooks", true);
        nightVisionEverywhere = b.comment("Plated Coralium Helmet Night Vision Everywhere").define("platedCoraliumHelmetNightVisionEverywhere", true);
        demonAnimalsSpawnOnDeath = b.comment("Demon Animals Spawn on Death").define("demonAnimalsSpawnOnDeath", true);
        evilAnimalNewMoonSpawning = b.comment("Evil Animal New Moon Spawning").define("evilAnimalNewMoonSpawning", true);
        curingRitualRange = b.comment("Curing Ritual Range").defineInRange("curingRitualRange", 32, 1, 256);
        itemTransportBlacklist = b.comment("Item Transportation System Blacklist")
                .defineList("itemTransportationSystemBlacklist", List.of(), o -> o instanceof String);
        enchantMergedBooks = b.comment("Mass Enchantment Merged Books").define("massEnchantmentMergedBooks", true);
        no_potion_clouds = b.comment("No plague Potion Clouds").define("noPlaguePotionClouds", false);
        jzaharHealingPace = b.comment("J'zahar Healing Pace").defineInRange("jzaharHealingPace", 200, 1, 24000);
        jzaharHealingAmount = b.comment("J'zahar Heal Amount").defineInRange("jzaharHealAmount", 1, 0, 1000);
        chagarothHealingPace = b.comment("Cha'garoth Healing Pace").defineInRange("chagarothHealingPace", 200, 1, 24000);
        chagarothHealingAmount = b.comment("Cha'garoth Heal Amount").defineInRange("chagarothHealAmount", 1, 0, 1000);
        sacthothHealingPace = b.comment("Sacthoth Healing Pace").defineInRange("sacthothHealingPace", 200, 1, 24000);
        sacthothHealingAmount = b.comment("Sacthoth Heal Amount").defineInRange("sacthothHealAmount", 1, 0, 1000);
        dimensionMappings = b.comment("Dimension Book Type Mappings")
                .defineList("dimensionBookTypeMappings", List.of(), o -> o instanceof String);
        b.pop();
    }

    /** Splits a comma-separated legacy config entry the way the 1.12.2 parser did. */
    static List<String> split(String value) {
        return Arrays.asList(value.split("\\s*,\\s*"));
    }
}
