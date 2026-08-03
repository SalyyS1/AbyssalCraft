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

import java.util.List;

import net.minecraftforge.fml.event.config.ModConfigEvent;

/**
 * Config option references.
 * <p>
 * The mod reads config through these plain static fields in well over a hundred places. The
 * values themselves are defined in {@link ACConfigSpec}; {@link #bake()} copies them across on
 * load and on reload, which keeps the read sites unchanged from the 1.12.2 build while the
 * underlying storage is Forge's modern {@code ForgeConfigSpec}.
 */
public class ACConfig {

    public static boolean keepLoaded1, keepLoaded2, keepLoaded3, keepLoaded4;

    public static boolean shouldSpread, shouldInfect, breakLogic, destroyOcean, demonAnimalFire, particleBlock,
            particleEntity, hardcoreMode, antiItemDisintegration, smeltingRecipes, purgeMobSpawns, mimicFire,
            armorPotionEffects, nuclearAntimatterExplosions, syncDataOnBookOpening, dreadGrassSpread, portalSpawnsNearPlayer,
            showBossDialogs, jzaharBreaksFourthWall, lootTableContent, depthsGhoulBiomeDictSpawn, abyssalZombieBiomeDictSpawn,
            useAmplifiedWorldType, generateStatuesInLairs, enchantBooks, nightVisionEverywhere, antiPlayersPickupLoot,
            demonAnimalsSpawnOnDeath, evilAnimalNewMoonSpawning, enchantMergedBooks, display_names,
            no_potion_clouds;
    public static int evilAnimalSpawnWeight, portalCooldown, demonAnimalSpawnWeight, shoggothLairSpawnRate, acidSpitFrequency,
            knowledgeSyncDelay, shoggothLairSpawnRateRivers, darkOffspringSpawnWeight, monolithBuildingCooldown, corruptionRitualRange,
            cleansingRitualRange, purgingRitualRange, odbExplosionSize, antimatterExplosionSize, enchantmentMaxLevel, curingRitualRange,
            shoggothLairGenerationDistance, jzaharHealingPace, jzaharHealingAmount, chagarothHealingPace, chagarothHealingAmount,
            sacthothHealingPace, sacthothHealingAmount, biomassPlayerDistance, biomassMaxSpawn, biomassCooldown, biomassShoggothDistance;
    public static double damageAmpl, depthsHelmetOverlayOpacity;
    public static boolean shoggothOoze, oozeExpire, consumeItems, shieldsBlockAcid, shoggothGlowingEyes;
    public static double acidResistanceHardness;
    public static boolean generateDarklandsStructures, generateShoggothLairs, generateAbyssalWastelandPillars,
            generateAbyssalWastelandRuins, generateAntimatterLake, generateCoraliumLake, generateDreadlandsStalagmite,
            generateOmotholStructures;
    public static boolean generateCoraliumOre, generateNitreOre, generateAbyssalniteOre, generateAbyssalCoraliumOre,
            generateDreadlandsAbyssalniteOre, generateDreadedAbyssalniteOre, generateAbyssalIronOre, generateAbyssalGoldOre,
            generateAbyssalDiamondOre, generateAbyssalNitreOre, generateAbyssalTinOre, generateAbyssalCopperOre,
            generatePearlescentCoraliumOre, generateLiquifiedCoraliumOre;
    public static boolean hcdarkness_aw, hcdarkness_dl, hcdarkness_omt, hcdarkness_dr;
    public static boolean no_dreadlands_spread, no_acid_breaking_blocks, no_spectral_dragons, no_projectile_damage_immunity,
            no_disruptions, no_black_holes, no_odb_explosions;

    public static boolean darkstone_brick_slab, darkstone_cobblestone_slab, darkstone_brick_stairs, darkstone_cobblestone_stairs, darkstone_slab,
            darklands_oak_slab, darklands_oak_stairs, abyssal_stone_brick_slab, abyssal_stone_brick_stairs, coralium_stone_brick_slab, coralium_stone_brick_stairs,
            dreadstone_brick_slab, dreadstone_brick_stairs, abyssalnite_stone_brick_slab, abyssalnite_stone_brick_stairs, ethaxium_brick_slab, ethaxium_brick_stairs,
            abyssal_cobblestone_slab, abyssal_cobblestone_stairs, coralium_cobblestone_slab, coralium_cobblestone_stairs, dreadstone_cobblestone_slab,
            dreadstone_cobblestone_stairs, abyssalnite_cobblestone_slab, abyssalnite_cobblestone_stairs, darkstone_cobblestone_wall, abyssal_cobbblestone_wall,
            coralium_cobblestone_wall, dreadstone_cobblestone_wall, abyssalnite_cobblestone_wall;

    public static boolean foodstuff, upgrade_kits, plague_enchantments, crystal_rework;

    public static boolean entropy_spell, life_drain_spell, mining_spell, grasp_of_cthulhu_spell, invisibility_spell,
            detachment_spell, steal_vigor_spell, sirens_song_spell, undeath_to_dust_spell, ooze_removal_spell, teleport_hostile_spell,
            floating_spell, teleport_home_spell;

    // Biome generation/spawning/weight toggles.
    public static boolean dark1, dark2, dark3, dark4, dark5, coralium1;
    public static boolean darkspawn1, darkspawn2, darkspawn3, darkspawn4, darkspawn5, coraliumspawn1;
    public static int darkWeight1, darkWeight2, darkWeight3, darkWeight4, darkWeight5, coraliumWeight;

    /**
     * List options. These held numeric dimension IDs on 1.12.2 and now hold dimension names,
     * so the two dimension blacklists changed from {@code int[]} to string lists.
     */
    public static List<? extends String> oreGenDimBlacklist, structureGenDimBlacklist, mobItemPickupBlacklist,
            interdimensionalCageBlacklist, blackHoleBlacklist, dreadPlagueImmunityList, dreadPlagueCarrierList,
            coraliumPlagueImmunityList, coraliumPlagueCarrierList, itemTransportBlacklist, dimensionMappings;

    public static void onConfigLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() == ACConfigSpec.SPEC) {
            bake();
        }
    }

    private static void bake() {
        ACConfigSpec c = ACConfigSpec.CONFIG;

        keepLoaded1 = c.keepLoaded1.get();
        keepLoaded2 = c.keepLoaded2.get();
        keepLoaded3 = c.keepLoaded3.get();
        keepLoaded4 = c.keepLoaded4.get();

        dark1 = c.dark1.get();
        dark2 = c.dark2.get();
        dark3 = c.dark3.get();
        dark4 = c.dark4.get();
        dark5 = c.dark5.get();
        coralium1 = c.coralium1.get();

        darkspawn1 = c.darkspawn1.get();
        darkspawn2 = c.darkspawn2.get();
        darkspawn3 = c.darkspawn3.get();
        darkspawn4 = c.darkspawn4.get();
        darkspawn5 = c.darkspawn5.get();
        coraliumspawn1 = c.coraliumspawn1.get();

        darkWeight1 = c.darkWeight1.get();
        darkWeight2 = c.darkWeight2.get();
        darkWeight3 = c.darkWeight3.get();
        darkWeight4 = c.darkWeight4.get();
        darkWeight5 = c.darkWeight5.get();
        coraliumWeight = c.coraliumWeight.get();

        shoggothOoze = c.shoggothOoze.get();
        oozeExpire = c.oozeExpire.get();
        consumeItems = c.consumeItems.get();
        shieldsBlockAcid = c.shieldsBlockAcid.get();
        acidResistanceHardness = c.acidResistanceHardness.get();
        acidSpitFrequency = c.acidSpitFrequency.get();
        monolithBuildingCooldown = c.monolithBuildingCooldown.get();
        shoggothGlowingEyes = c.shoggothGlowingEyes.get();
        biomassPlayerDistance = c.biomassPlayerDistance.get();
        biomassMaxSpawn = c.biomassMaxSpawn.get();
        biomassCooldown = c.biomassCooldown.get();
        biomassShoggothDistance = c.biomassShoggothDistance.get();

        generateDarklandsStructures = c.generateDarklandsStructures.get();
        generateShoggothLairs = c.generateShoggothLairs.get();
        generateAbyssalWastelandPillars = c.generateAbyssalWastelandPillars.get();
        generateAbyssalWastelandRuins = c.generateAbyssalWastelandRuins.get();
        generateAntimatterLake = c.generateAntimatterLake.get();
        generateCoraliumLake = c.generateCoraliumLake.get();
        generateDreadlandsStalagmite = c.generateDreadlandsStalagmite.get();
        generateOmotholStructures = c.generateOmotholStructures.get();
        generateCoraliumOre = c.generateCoraliumOre.get();
        generateNitreOre = c.generateNitreOre.get();
        generateAbyssalniteOre = c.generateAbyssalniteOre.get();
        generateAbyssalCoraliumOre = c.generateAbyssalCoraliumOre.get();
        generateDreadlandsAbyssalniteOre = c.generateDreadlandsAbyssalniteOre.get();
        generateDreadedAbyssalniteOre = c.generateDreadedAbyssalniteOre.get();
        generateAbyssalIronOre = c.generateAbyssalIronOre.get();
        generateAbyssalGoldOre = c.generateAbyssalGoldOre.get();
        generateAbyssalDiamondOre = c.generateAbyssalDiamondOre.get();
        generateAbyssalNitreOre = c.generateAbyssalNitreOre.get();
        generateAbyssalTinOre = c.generateAbyssalTinOre.get();
        generateAbyssalCopperOre = c.generateAbyssalCopperOre.get();
        generatePearlescentCoraliumOre = c.generatePearlescentCoraliumOre.get();
        generateLiquifiedCoraliumOre = c.generateLiquifiedCoraliumOre.get();
        shoggothLairSpawnRate = c.shoggothLairSpawnRate.get();
        shoggothLairSpawnRateRivers = c.shoggothLairSpawnRateRivers.get();
        useAmplifiedWorldType = c.useAmplifiedWorldType.get();
        generateStatuesInLairs = c.generateStatuesInLairs.get();
        shoggothLairGenerationDistance = c.shoggothLairGenerationDistance.get();
        oreGenDimBlacklist = c.oreGenDimBlacklist.get();
        structureGenDimBlacklist = c.structureGenDimBlacklist.get();

        antiPlayersPickupLoot = c.antiPlayersPickupLoot.get();
        mobItemPickupBlacklist = c.mobItemPickupBlacklist.get();

        breakLogic = c.breakLogic.get();
        nuclearAntimatterExplosions = c.nuclearAntimatterExplosions.get();
        jzaharBreaksFourthWall = c.jzaharBreaksFourthWall.get();
        odbExplosionSize = c.odbExplosionSize.get();
        antimatterExplosionSize = c.antimatterExplosionSize.get();

        darkstone_brick_slab = c.darkstone_brick_slab.get();
        darkstone_cobblestone_slab = c.darkstone_cobblestone_slab.get();
        darkstone_brick_stairs = c.darkstone_brick_stairs.get();
        darkstone_cobblestone_stairs = c.darkstone_cobblestone_stairs.get();
        darkstone_slab = c.darkstone_slab.get();
        darklands_oak_slab = c.darklands_oak_slab.get();
        darklands_oak_stairs = c.darklands_oak_stairs.get();
        abyssal_stone_brick_slab = c.abyssal_stone_brick_slab.get();
        abyssal_stone_brick_stairs = c.abyssal_stone_brick_stairs.get();
        coralium_stone_brick_slab = c.coralium_stone_brick_slab.get();
        coralium_stone_brick_stairs = c.coralium_stone_brick_stairs.get();
        dreadstone_brick_slab = c.dreadstone_brick_slab.get();
        dreadstone_brick_stairs = c.dreadstone_brick_stairs.get();
        abyssalnite_stone_brick_slab = c.abyssalnite_stone_brick_slab.get();
        abyssalnite_stone_brick_stairs = c.abyssalnite_stone_brick_stairs.get();
        ethaxium_brick_slab = c.ethaxium_brick_slab.get();
        ethaxium_brick_stairs = c.ethaxium_brick_stairs.get();
        abyssal_cobblestone_slab = c.abyssal_cobblestone_slab.get();
        abyssal_cobblestone_stairs = c.abyssal_cobblestone_stairs.get();
        coralium_cobblestone_slab = c.coralium_cobblestone_slab.get();
        coralium_cobblestone_stairs = c.coralium_cobblestone_stairs.get();
        dreadstone_cobblestone_slab = c.dreadstone_cobblestone_slab.get();
        dreadstone_cobblestone_stairs = c.dreadstone_cobblestone_stairs.get();
        abyssalnite_cobblestone_slab = c.abyssalnite_cobblestone_slab.get();
        abyssalnite_cobblestone_stairs = c.abyssalnite_cobblestone_stairs.get();
        darkstone_cobblestone_wall = c.darkstone_cobblestone_wall.get();
        abyssal_cobbblestone_wall = c.abyssal_cobbblestone_wall.get();
        coralium_cobblestone_wall = c.coralium_cobblestone_wall.get();
        dreadstone_cobblestone_wall = c.dreadstone_cobblestone_wall.get();
        abyssalnite_cobblestone_wall = c.abyssalnite_cobblestone_wall.get();

        no_dreadlands_spread = c.no_dreadlands_spread.get();
        no_acid_breaking_blocks = c.no_acid_breaking_blocks.get();
        no_spectral_dragons = c.no_spectral_dragons.get();
        no_projectile_damage_immunity = c.no_projectile_damage_immunity.get();
        no_disruptions = c.no_disruptions.get();
        no_black_holes = c.no_black_holes.get();
        no_odb_explosions = c.no_odb_explosions.get();

        hcdarkness_aw = c.hcdarkness_aw.get();
        hcdarkness_dl = c.hcdarkness_dl.get();
        hcdarkness_omt = c.hcdarkness_omt.get();
        hcdarkness_dr = c.hcdarkness_dr.get();

        foodstuff = c.foodstuff.get();
        upgrade_kits = c.upgrade_kits.get();
        plague_enchantments = c.plague_enchantments.get();
        crystal_rework = c.crystal_rework.get();

        entropy_spell = c.entropy_spell.get();
        life_drain_spell = c.life_drain_spell.get();
        mining_spell = c.mining_spell.get();
        grasp_of_cthulhu_spell = c.grasp_of_cthulhu_spell.get();
        invisibility_spell = c.invisibility_spell.get();
        detachment_spell = c.detachment_spell.get();
        steal_vigor_spell = c.steal_vigor_spell.get();
        sirens_song_spell = c.sirens_song_spell.get();
        undeath_to_dust_spell = c.undeath_to_dust_spell.get();
        ooze_removal_spell = c.ooze_removal_spell.get();
        teleport_hostile_spell = c.teleport_hostile_spell.get();
        floating_spell = c.floating_spell.get();
        teleport_home_spell = c.teleport_home_spell.get();

        shouldSpread = c.shouldSpread.get();
        shouldInfect = c.shouldInfect.get();
        destroyOcean = c.destroyOcean.get();
        demonAnimalFire = c.demonAnimalFire.get();
        evilAnimalSpawnWeight = c.evilAnimalSpawnWeight.get();
        particleBlock = c.particleBlock.get();
        particleEntity = c.particleEntity.get();
        hardcoreMode = c.hardcoreMode.get();
        antiItemDisintegration = c.antiItemDisintegration.get();
        portalCooldown = c.portalCooldown.get();
        demonAnimalSpawnWeight = c.demonAnimalSpawnWeight.get();
        smeltingRecipes = c.smeltingRecipes.get();
        purgeMobSpawns = c.purgeMobSpawns.get();
        interdimensionalCageBlacklist = c.interdimensionalCageBlacklist.get();
        damageAmpl = c.damageAmpl.get();
        depthsHelmetOverlayOpacity = c.depthsHelmetOverlayOpacity.get();
        mimicFire = c.mimicFire.get();
        armorPotionEffects = c.armorPotionEffects.get();
        syncDataOnBookOpening = c.syncDataOnBookOpening.get();
        dreadGrassSpread = c.dreadGrassSpread.get();
        display_names = c.display_names.get();
        blackHoleBlacklist = c.blackHoleBlacklist.get();
        portalSpawnsNearPlayer = c.portalSpawnsNearPlayer.get();
        showBossDialogs = c.showBossDialogs.get();
        knowledgeSyncDelay = c.knowledgeSyncDelay.get();
        dreadPlagueImmunityList = c.dreadPlagueImmunityList.get();
        dreadPlagueCarrierList = c.dreadPlagueCarrierList.get();
        coraliumPlagueImmunityList = c.coraliumPlagueImmunityList.get();
        coraliumPlagueCarrierList = c.coraliumPlagueCarrierList.get();
        lootTableContent = c.lootTableContent.get();
        depthsGhoulBiomeDictSpawn = c.depthsGhoulBiomeDictSpawn.get();
        abyssalZombieBiomeDictSpawn = c.abyssalZombieBiomeDictSpawn.get();
        darkOffspringSpawnWeight = c.darkOffspringSpawnWeight.get();
        corruptionRitualRange = c.corruptionRitualRange.get();
        cleansingRitualRange = c.cleansingRitualRange.get();
        purgingRitualRange = c.purgingRitualRange.get();
        enchantmentMaxLevel = c.enchantmentMaxLevel.get();
        enchantBooks = c.enchantBooks.get();
        nightVisionEverywhere = c.nightVisionEverywhere.get();
        demonAnimalsSpawnOnDeath = c.demonAnimalsSpawnOnDeath.get();
        evilAnimalNewMoonSpawning = c.evilAnimalNewMoonSpawning.get();
        curingRitualRange = c.curingRitualRange.get();
        itemTransportBlacklist = c.itemTransportBlacklist.get();
        enchantMergedBooks = c.enchantMergedBooks.get();
        no_potion_clouds = c.no_potion_clouds.get();
        jzaharHealingPace = c.jzaharHealingPace.get();
        jzaharHealingAmount = c.jzaharHealingAmount.get();
        chagarothHealingPace = c.chagarothHealingPace.get();
        chagarothHealingAmount = c.chagarothHealingAmount.get();
        sacthothHealingPace = c.sacthothHealingPace.get();
        sacthothHealingAmount = c.sacthothHealingAmount.get();
        dimensionMappings = c.dimensionMappings.get();
    }
}
