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
package com.shinoow.abyssalcraft.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.api.necronomicon.condition.ConditionProcessorRegistry;
import com.shinoow.abyssalcraft.api.necronomicon.condition.DimensionCondition;
import com.shinoow.abyssalcraft.api.necronomicon.condition.caps.NecroDataCapability;
import com.shinoow.abyssalcraft.api.item.ItemEngraving;
import com.shinoow.abyssalcraft.api.recipe.CrystallizerRecipes;
import com.shinoow.abyssalcraft.api.recipe.EngraverRecipes;
import com.shinoow.abyssalcraft.api.recipe.MaterializerRecipes;
import com.shinoow.abyssalcraft.api.recipe.TransmutatorRecipes;
import com.shinoow.abyssalcraft.api.ritual.NecronomiconRitual;
import com.shinoow.abyssalcraft.api.ritual.RitualRegistry;
import com.shinoow.abyssalcraft.common.blocks.tile.EnergyContainerBlockEntity;
import com.shinoow.abyssalcraft.init.ACEntities;
import com.shinoow.abyssalcraft.lib.ACLib;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Startup self-check for the port.
 * <p>
 * Content is migrated subsystem by subsystem, and a {@link RegistryObject} that was declared but
 * never actually bound only fails later, at the point of use. This walks every declared block and
 * item entry once the server is up and reports anything unbound, absent from its registry, or
 * missing an item form, so a broken migration is visible immediately instead of at first use.
 */
public final class ACPortSelfCheck {

    private ACPortSelfCheck() {}

    public static void onServerStarted(ServerStartedEvent event) {
        List<String> problems = new ArrayList<>();
        int blocks = checkBlocks(problems);
        int items = checkItems(problems);
        int recipes = checkMachineRecipes(problems);
        int entities = checkEntities(event, problems);
        checkEnergyStorage(problems);
        checkKnowledgeGating(problems);
        int rituals = checkRituals(problems);
        checkCapabilityRegistered(problems);
        checkCrystalBag(problems);
        checkBlockLootTables(event, problems);

        if (problems.isEmpty()) {
            ACLogger.info("Port self-check: {} blocks, {} items, {} machine recipes, {} entities and {} rituals resolved; PE storage and Necronomicon gating OK.",
                    blocks, items, recipes, entities, rituals);
        } else {
            ACLogger.severe("Port self-check: {} problems across {} blocks, {} items, {} recipes, {} entities and {} rituals:",
                    problems.size(), blocks, items, recipes, entities, rituals);
            problems.forEach(problem -> ACLogger.severe("  {}", problem));
        }
    }

    /**
     * Checks each registered ritual declares a usable shape, and that the dimension-to-book-tier
     * gate actually refuses a lower-tier book. That gate is what keeps late-game rituals out of
     * reach early, so a permissive bug there collapses the progression.
     */
    private static int checkRituals(List<String> problems) {
        List<NecronomiconRitual> rituals = RitualRegistry.instance().getRituals();

        for (NecronomiconRitual ritual : rituals) {
            if (ritual.getOfferings().length > NecronomiconRitual.MAX_OFFERINGS) {
                problems.add("ritual " + ritual.getUnlocalizedName() + " declares more offerings than pedestals");
            }
            if (ritual.getBookType() < 0 || ritual.getBookType() > RitualRegistry.MAX_BOOK_TYPE) {
                problems.add("ritual " + ritual.getUnlocalizedName() + " has out-of-range book type "
                        + ritual.getBookType());
            }
            if (ritual.getReqEnergy() < 0.0F) {
                problems.add("ritual " + ritual.getUnlocalizedName() + " requires negative energy");
            }
        }

        RitualRegistry registry = RitualRegistry.instance();
        if (!registry.canPerformAction(ACLib.THE_DARK_REALM, 4)) {
            problems.add("rituals: a tier-4 book cannot act in the Dark Realm");
        }
        if (registry.canPerformAction(ACLib.THE_DARK_REALM, 3)) {
            problems.add("rituals: a tier-3 book was allowed to act in the Dark Realm");
        }
        if (!registry.canPerformAction(Level.OVERWORLD, 0)) {
            problems.add("rituals: a tier-0 book cannot act in the Overworld");
        }

        return rituals.size();
    }

    /**
     * Actually constructs each entity type in the overworld and checks that its attributes were
     * registered. A missing {@code EntityAttributeCreationEvent} entry only throws when something
     * first tries to spawn the mob, so this creates one and discards it.
     */
    private static int checkEntities(ServerStartedEvent event, List<String> problems) {
        ServerLevel level = event.getServer().overworld();
        int checked = 0;

        for (Field field : ACEntities.class.getDeclaredFields()) {
            RegistryObject<EntityType<?>> entry = readEntry(field, problems);
            if (entry == null) {
                continue;
            }
            checked++;
            if (!verifyBound(field, entry, problems)) {
                continue;
            }
            EntityType<?> type = entry.get();
            if (!BuiltInRegistries.ENTITY_TYPE.containsKey(entry.getId())) {
                problems.add(field.getName() + ": " + entry.getId() + " absent from the entity registry");
                continue;
            }
            try {
                Entity entity = type.create(level);
                if (entity == null) {
                    problems.add(field.getName() + ": " + entry.getId() + " could not be constructed");
                } else {
                    if (entity instanceof LivingEntity living && living.getMaxHealth() <= 0.0F) {
                        problems.add(field.getName() + ": " + entry.getId() + " has no max health attribute");
                    }
                    entity.discard();
                }
            } catch (RuntimeException e) {
                problems.add(field.getName() + ": " + entry.getId() + " failed to construct: " + e);
            }
        }
        return checked;
    }

    /**
     * Exercises the machine recipe registries rather than merely counting them: a recipe whose
     * output failed to resolve would still be present in the map, so each entry is looked up the
     * same way the machine looks it up and the result is compared against what was registered.
     */
    private static int checkMachineRecipes(List<String> problems) {
        int checked = 0;

        for (Map.Entry<ItemStack, ItemStack[]> entry : CrystallizerRecipes.instance().getCrystallizationList().entrySet()) {
            checked++;
            ItemStack input = entry.getKey();
            ItemStack[] expected = entry.getValue();
            if (input.isEmpty()) {
                problems.add("crystallizer recipe has an empty input");
                continue;
            }
            if (expected[0].isEmpty()) {
                problems.add("crystallizer recipe for " + input.getItem() + " has an empty primary output");
                continue;
            }
            ItemStack[] resolved = CrystallizerRecipes.instance().getCrystallizationResult(input);
            if (!ItemStack.isSameItem(resolved[0], expected[0])) {
                problems.add("crystallizer recipe for " + input.getItem() + " does not resolve to its own output");
            }
        }

        for (Map.Entry<ItemEngraving, ItemStack> entry : EngraverRecipes.instance().getEngravings().entrySet()) {
            checked++;
            ItemEngraving stamp = entry.getKey();
            ItemStack result = entry.getValue();
            if (result.isEmpty()) {
                problems.add("engraver recipe for " + stamp + " has an empty result");
                continue;
            }
            // The Engraver is deliberately asymmetric: a deity stamp only works on a blank coin,
            // and the blank stamp only works on an already-engraved one. So each recipe is checked
            // against whichever coin it is actually meant to accept.
            boolean strips = stamp == ACItems.blank_engraving.get();
            ItemStack input = strips
                    ? new ItemStack(ACItems.cthulhu_engraved_coin.get())
                    : new ItemStack(ACItems.coin.get());
            if (!ItemStack.isSameItem(EngraverRecipes.instance().getEngravingResult(input, stamp), result)) {
                problems.add("engraver recipe for " + stamp + " does not accept the coin it should");
            }
        }

        for (Map.Entry<ItemStack, ItemStack> entry : TransmutatorRecipes.instance().getTransmutationList().entrySet()) {
            checked++;
            ItemStack input = entry.getKey();
            if (input.isEmpty() || entry.getValue().isEmpty()) {
                problems.add("transmutator recipe has an empty input or output");
                continue;
            }
            if (!ItemStack.isSameItem(TransmutatorRecipes.instance().getTransmutationResult(input), entry.getValue())) {
                problems.add("transmutator recipe for " + input.getItem() + " does not resolve to its own output");
            }
        }

        return checked;
    }

    /**
     * Exercises the Potential Energy storage contract on a detached container: fill past capacity,
     * over-drain, and confirm the clamping and drain-to-empty behaviour still match 1.12.2. The PE
     * system is the backbone of the rituals and machines, so a silent regression here would be
     * expensive to trace later.
     */
    private static void checkEnergyStorage(List<String> problems) {
        EnergyContainerBlockEntity container =
                new EnergyContainerBlockEntity(BlockPos.ZERO, ACBlocks.energy_container.get().defaultBlockState());

        if (container.getContainedEnergy() != 0.0F) {
            problems.add("PE container does not start empty");
        }
        if (container.canTransferPE()) {
            problems.add("PE container claims it can transfer while empty");
        }

        container.addEnergy(EnergyContainerBlockEntity.CAPACITY + 500.0F);
        if (container.getContainedEnergy() != EnergyContainerBlockEntity.CAPACITY) {
            problems.add("PE container did not clamp to capacity, holds " + container.getContainedEnergy());
        }
        if (container.canAcceptPE()) {
            problems.add("PE container claims it can accept more while full");
        }

        float drained = container.consumeEnergy(EnergyContainerBlockEntity.CAPACITY + 500.0F);
        if (drained != EnergyContainerBlockEntity.CAPACITY) {
            problems.add("PE container over-drain returned " + drained
                    + " instead of " + EnergyContainerBlockEntity.CAPACITY);
        }
        if (container.getContainedEnergy() != 0.0F) {
            problems.add("PE container did not empty, holds " + container.getContainedEnergy());
        }
    }

    /**
     * Exercises the Necronomicon gating end to end: a condition must stay locked until its trigger
     * fires, must survive a save/load round trip, and the unlock-all cheat must override it. This
     * is the progression spine of the mod, so a condition that silently reads as always-unlocked
     * would hand the player the whole book.
     */
    private static void checkKnowledgeGating(List<String> problems) {
        NecroDataCapability progress = new NecroDataCapability();
        ResourceLocation dimension = new ResourceLocation("abyssalcraft", "the_abyssal_wasteland");
        DimensionCondition condition = new DimensionCondition(dimension);

        if (progress.isUnlocked(condition, null)) {
            problems.add("Necronomicon: dimension knowledge is unlocked before its trigger fired");
        }

        progress.triggerDimensionUnlock(dimension);
        if (!progress.isUnlocked(condition, null)) {
            problems.add("Necronomicon: dimension knowledge stayed locked after its trigger fired");
        }

        NecroDataCapability reloaded = new NecroDataCapability();
        reloaded.deserializeNBT(progress.serializeNBT());
        if (!reloaded.isUnlocked(condition, null)) {
            problems.add("Necronomicon: progress did not survive a save/load round trip");
        }

        NecroDataCapability cheated = new NecroDataCapability();
        cheated.unlockAllKnowledge(true);
        if (!cheated.isUnlocked(condition, null)) {
            problems.add("Necronomicon: unlock-all did not override an unmet condition");
        }

        if (ConditionProcessorRegistry.instance().getProcessorCount() != 7) {
            problems.add("Necronomicon: expected 7 condition processors, found "
                    + ConditionProcessorRegistry.instance().getProcessorCount());
        }
    }

    /**
     * Confirms the knowledge capability was actually registered. An unregistered capability yields
     * a token that silently resolves empty for every player, which would look like a progression
     * reset rather than a missing registration.
     */
    private static void checkCapabilityRegistered(List<String> problems) {
        if (!ACKnowledge.CAPABILITY.isRegistered()) {
            problems.add("Necronomicon progress capability was never registered");
        }
    }

    /**
     * Confirms every block resolves a real loot table. A block with no table quietly drops nothing
     * when broken, which reads as a gameplay bug rather than missing data, so it is worth catching
     * at startup.
     */
    private static void checkBlockLootTables(ServerStartedEvent event, List<String> problems) {
        LootDataManager lootData = event.getServer().getLootData();
        int missing = 0;

        for (Field field : ACBlocks.class.getDeclaredFields()) {
            RegistryObject<Block> entry = readEntry(field, problems);
            if (entry == null || !entry.isPresent()) {
                continue;
            }
            ResourceLocation table = entry.get().getLootTable();
            if (lootData.getLootTable(table) == LootTable.EMPTY) {
                problems.add(field.getName() + ": no loot table at " + table);
                missing++;
            }
        }
        if (missing == 0) {
            ACLogger.fine("Port self-check: every registered block resolves a loot table.");
        }
    }

    /**
     * Exercises the crystal bag and the Materializer's crystal accounting.
     * <p>
     * The 1.12.2 consumeCrystals assigned its working copy back to a parameter, which does nothing
     * to the caller, so a recipe that could only be partially paid still spent crystals. This
     * asserts the opposite: a check spends nothing, and an unaffordable materialization leaves the
     * bag untouched.
     */
    private static void checkCrystalBag(List<String> problems) {
        MaterializerRecipes recipes = MaterializerRecipes.instance();

        ItemStack bag = new ItemStack(ACItems.small_crystal_bag.get());
        recipes.replaceBagContents(bag, new ItemStack[]{new ItemStack(ACItems.crystal(0), 4)});

        ItemStack[] contents = recipes.extractItemsFromBag(bag);
        if (contents == null || contents.length != 1 || contents[0].getCount() != 4) {
            problems.add("crystal bag did not round-trip its contents");
            return;
        }

        if (!recipes.canConsume(contents, new ItemStack[]{new ItemStack(ACItems.crystal(0), 3)})) {
            problems.add("crystal bag refused a recipe it can afford");
        }
        if (recipes.canConsume(contents, new ItemStack[]{new ItemStack(ACItems.crystal(0), 9)})) {
            problems.add("crystal bag accepted a recipe it cannot afford");
        }

        ItemStack[] afterChecks = recipes.extractItemsFromBag(bag);
        if (afterChecks == null || afterChecks[0].getCount() != 4) {
            problems.add("crystal bag lost crystals to a non-spending check");
        }

        // A materialization the bag cannot pay for must spend nothing at all.
        recipes.processMaterialization(new ItemStack(ACItems.crystal(1)), bag);
        ItemStack[] afterFailedSpend = recipes.extractItemsFromBag(bag);
        if (afterFailedSpend == null || afterFailedSpend[0].getCount() != 4) {
            problems.add("crystal bag spent crystals on a materialization it could not complete");
        }
    }

    private static int checkBlocks(List<String> problems) {
        int checked = 0;
        for (Field field : ACBlocks.class.getDeclaredFields()) {
            RegistryObject<Block> entry = readEntry(field, problems);
            if (entry == null) {
                continue;
            }
            checked++;
            if (!verifyBound(field, entry, problems)) {
                continue;
            }
            if (!BuiltInRegistries.BLOCK.containsKey(entry.getId())) {
                problems.add(field.getName() + ": " + entry.getId() + " absent from the block registry");
            }
            if (new ItemStack(entry.get()).isEmpty()) {
                problems.add(field.getName() + ": " + entry.getId() + " has no item form");
            }
        }
        return checked;
    }

    private static int checkItems(List<String> problems) {
        int checked = 0;
        for (Field field : ACItems.class.getDeclaredFields()) {
            // The crystal sets are Lists of entries rather than single ones; unpack them.
            if (List.class.isAssignableFrom(field.getType())) {
                checked += checkItemList(field, problems);
                continue;
            }
            RegistryObject<Item> entry = readEntry(field, problems);
            if (entry == null) {
                continue;
            }
            checked++;
            if (verifyBound(field, entry, problems) && !BuiltInRegistries.ITEM.containsKey(entry.getId())) {
                problems.add(field.getName() + ": " + entry.getId() + " absent from the item registry");
            }
        }
        return checked;
    }

    private static int checkItemList(Field field, List<String> problems) {
        Object value;
        try {
            value = field.get(null);
        } catch (ReflectiveOperationException | RuntimeException e) {
            problems.add(field.getName() + ": " + e);
            return 0;
        }
        if (!(value instanceof List<?> entries)) {
            return 0;
        }
        int checked = 0;
        for (Object element : entries) {
            if (!(element instanceof RegistryObject<?> entry)) {
                continue;
            }
            checked++;
            if (!entry.isPresent()) {
                problems.add(field.getName() + ": an entry is unbound");
            } else if (!BuiltInRegistries.ITEM.containsKey(entry.getId())) {
                problems.add(field.getName() + ": " + entry.getId() + " absent from the item registry");
            }
        }
        return checked;
    }

    /** Returns the field's registry object, or null when the field is not one. */
    private static <T> RegistryObject<T> readEntry(Field field, List<String> problems) {
        if (!RegistryObject.class.isAssignableFrom(field.getType())) {
            return null;
        }
        try {
            @SuppressWarnings("unchecked")
            RegistryObject<T> entry = (RegistryObject<T>) field.get(null);
            return entry;
        } catch (ReflectiveOperationException | RuntimeException e) {
            problems.add(field.getName() + ": " + e);
            return null;
        }
    }

    private static boolean verifyBound(Field field, RegistryObject<?> entry, List<String> problems) {
        if (entry.isPresent()) {
            return true;
        }
        problems.add(field.getName() + ": registry object is unbound");
        return false;
    }
}
