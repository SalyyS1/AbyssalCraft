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
package com.shinoow.abyssalcraft;

import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.api.necronomicon.condition.ACConditionProcessors;
import com.shinoow.abyssalcraft.common.AbyssalCrafting;
import com.shinoow.abyssalcraft.common.util.ACPortSelfCheck;
import com.shinoow.abyssalcraft.init.ACBlockEntities;
import com.shinoow.abyssalcraft.init.ACEntities;
import com.shinoow.abyssalcraft.init.ACMenus;
import com.shinoow.abyssalcraft.init.ACRegistries;
import com.shinoow.abyssalcraft.init.ACRituals;
import com.shinoow.abyssalcraft.lib.ACConfig;
import com.shinoow.abyssalcraft.lib.ACConfigSpec;
import com.shinoow.abyssalcraft.lib.ACTabs;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AbyssalCraft.MOD_ID)
public class AbyssalCraft {

    public static final String MOD_ID = "abyssalcraft";
    public static final String NAME = "AbyssalCraft";

    /** Kept for the many read sites that referenced the old lowercase constant. */
    public static final String modid = MOD_ID;

    public AbyssalCraft() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ACRegistries.register(modBus);
        ACBlocks.register();
        ACItems.register();
        ACBlockEntities.register();
        ACMenus.register();
        ACEntities.register();
        ACTabs.register();

        modBus.addListener(ACConfig::onConfigLoad);
        modBus.addListener(this::commonSetup);
        modBus.addListener(ACEntities::registerAttributes);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ACConfigSpec.SPEC);

        MinecraftForge.EVENT_BUS.addListener(ACPortSelfCheck::onServerStarted);
    }

    /**
     * Machine recipes are registered here rather than in the constructor: they build ItemStacks,
     * which requires the item registry to have been populated already.
     */
    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(AbyssalCrafting::init);
        event.enqueueWork(ACConditionProcessors::register);
        event.enqueueWork(ACRituals::register);
    }

    /** Replaces the 1.12.2 {@code @SidedProxy} split. */
    public static boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }
}
