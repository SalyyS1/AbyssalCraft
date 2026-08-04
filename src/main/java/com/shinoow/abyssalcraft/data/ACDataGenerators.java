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

import java.util.Set;

import com.shinoow.abyssalcraft.AbyssalCraft;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Datagen entrypoint.
 * <p>
 * 1.12.2 computed drops and harvest levels in Java. Both are data on 1.20.1, so they are generated
 * here into {@code src/generated/resources} rather than hand-written: a block without a loot table
 * silently drops nothing, and one without tool tags cannot be mined properly.
 */
@Mod.EventBusSubscriber(modid = AbyssalCraft.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ACDataGenerators {

    private ACDataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
                java.util.List.of(new LootTableProvider.SubProviderEntry(
                        ACBlockLootProvider::new, LootContextParamSets.BLOCK))));

        generator.addProvider(event.includeServer(), new ACBlockTagsProvider(output,
                event.getLookupProvider(), event.getExistingFileHelper()));

        generator.addProvider(event.includeClient(),
                new ACBlockModelProvider(event, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(),
                new ACItemModelProvider(event, event.getExistingFileHelper()));
    }
}
