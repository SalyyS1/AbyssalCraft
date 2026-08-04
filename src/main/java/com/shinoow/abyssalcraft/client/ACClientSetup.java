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
package com.shinoow.abyssalcraft.client;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.client.model.entity.DreadSpawnModel;
import com.shinoow.abyssalcraft.client.model.entity.DreadlingModel;
import com.shinoow.abyssalcraft.client.model.entity.ShadowCreatureModel;
import com.shinoow.abyssalcraft.client.model.entity.ShadowMonsterModel;
import com.shinoow.abyssalcraft.client.gui.CrystallizerScreen;
import com.shinoow.abyssalcraft.client.gui.EngraverScreen;
import com.shinoow.abyssalcraft.client.gui.MaterializerScreen;
import com.shinoow.abyssalcraft.client.gui.StateTransformerScreen;
import com.shinoow.abyssalcraft.client.gui.TransmutatorScreen;
import com.shinoow.abyssalcraft.client.render.ACModelLayers;
import com.shinoow.abyssalcraft.client.render.entity.AbyssalniteGolemRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DreadGolemRenderer;
import com.shinoow.abyssalcraft.client.render.entity.OmotholGhoulRenderer;
import com.shinoow.abyssalcraft.client.render.entity.GatekeeperMinionRenderer;
import com.shinoow.abyssalcraft.client.render.entity.ShadowBeastRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DreadguardRenderer;
import com.shinoow.abyssalcraft.client.render.entity.SkeletonGoliathRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiZombieRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiSkeletonRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiCowRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiPigRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiChickenRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DemonCowRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DemonPigRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DemonChickenRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DemonSheepRenderer;
import com.shinoow.abyssalcraft.client.render.entity.EvilCowRenderer;
import com.shinoow.abyssalcraft.client.render.entity.EvilPigRenderer;
import com.shinoow.abyssalcraft.client.render.entity.EvilChickenRenderer;
import com.shinoow.abyssalcraft.client.render.entity.EvilSheepRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiAbyssalZombieRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiGhoulRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiSpiderRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AntiBatRenderer;
import com.shinoow.abyssalcraft.client.render.entity.RemnantRenderer;
import com.shinoow.abyssalcraft.client.render.entity.AbyssalZombieRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DepthsGhoulRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DreadSpawnRenderer;
import com.shinoow.abyssalcraft.client.render.entity.DreadlingRenderer;
import com.shinoow.abyssalcraft.client.render.entity.ShadowCreatureRenderer;
import com.shinoow.abyssalcraft.client.render.entity.ShadowMonsterRenderer;
import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.init.ACEntities;
import com.shinoow.abyssalcraft.init.ACMenus;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client-side setup.
 * <p>
 * Replaces the 1.12.2 {@code ClientProxy}. Renderers and model layers are registered through mod
 * bus events on 1.20.1, and this class loads only on the client so it can reference client types.
 */
@Mod.EventBusSubscriber(modid = AbyssalCraft.MOD_ID, value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ACClientSetup {

    private ACClientSetup() {}

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ACModelLayers.DREADLING, DreadlingModel::createBodyLayer);
        event.registerLayerDefinition(ACModelLayers.SHADOW_CREATURE, ShadowCreatureModel::createBodyLayer);
        event.registerLayerDefinition(ACModelLayers.SHADOW_MONSTER, ShadowMonsterModel::createBodyLayer);
        event.registerLayerDefinition(ACModelLayers.DREAD_SPAWN, DreadSpawnModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ACEntities.DREADLING.get(), DreadlingRenderer::new);
        event.registerEntityRenderer(ACEntities.SHADOW_CREATURE.get(), ShadowCreatureRenderer::new);
        event.registerEntityRenderer(ACEntities.SHADOW_MONSTER.get(), ShadowMonsterRenderer::new);
        event.registerEntityRenderer(ACEntities.DREAD_SPAWN.get(), DreadSpawnRenderer::new);
        event.registerEntityRenderer(ACEntities.ABYSSAL_ZOMBIE.get(), AbyssalZombieRenderer::new);
        event.registerEntityRenderer(ACEntities.DEPTHS_GHOUL.get(), DepthsGhoulRenderer::new);
        event.registerEntityRenderer(ACEntities.REMNANT.get(), RemnantRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_BAT.get(), AntiBatRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_ABYSSAL_ZOMBIE.get(), AntiAbyssalZombieRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_GHOUL.get(), AntiGhoulRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_SPIDER.get(), AntiSpiderRenderer::new);
        event.registerEntityRenderer(ACEntities.DEMON_COW.get(), DemonCowRenderer::new);
        event.registerEntityRenderer(ACEntities.DEMON_PIG.get(), DemonPigRenderer::new);
        event.registerEntityRenderer(ACEntities.DEMON_CHICKEN.get(), DemonChickenRenderer::new);
        event.registerEntityRenderer(ACEntities.DEMON_SHEEP.get(), DemonSheepRenderer::new);
        event.registerEntityRenderer(ACEntities.EVIL_COW.get(), EvilCowRenderer::new);
        event.registerEntityRenderer(ACEntities.EVIL_PIG.get(), EvilPigRenderer::new);
        event.registerEntityRenderer(ACEntities.EVIL_CHICKEN.get(), EvilChickenRenderer::new);
        event.registerEntityRenderer(ACEntities.EVIL_SHEEP.get(), EvilSheepRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_COW.get(), AntiCowRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_PIG.get(), AntiPigRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_CHICKEN.get(), AntiChickenRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_ZOMBIE.get(), AntiZombieRenderer::new);
        event.registerEntityRenderer(ACEntities.ANTI_SKELETON.get(), AntiSkeletonRenderer::new);
        event.registerEntityRenderer(ACEntities.DREADGUARD.get(), DreadguardRenderer::new);
        event.registerEntityRenderer(ACEntities.SKELETON_GOLIATH.get(), SkeletonGoliathRenderer::new);
        event.registerEntityRenderer(ACEntities.OMOTHOL_GHOUL.get(), OmotholGhoulRenderer::new);
        event.registerEntityRenderer(ACEntities.GATEKEEPER_MINION.get(), GatekeeperMinionRenderer::new);
        event.registerEntityRenderer(ACEntities.SHADOW_BEAST.get(), ShadowBeastRenderer::new);
        event.registerEntityRenderer(ACEntities.ABYSSALNITE_GOLEM.get(), AbyssalniteGolemRenderer::new);
        event.registerEntityRenderer(ACEntities.DREAD_GOLEM.get(), DreadGolemRenderer::new);
    }

    /** Binds each menu type to the screen that draws it. */
    @SubscribeEvent
    public static void registerScreens(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ACMenus.CRYSTALLIZER.get(), CrystallizerScreen::new);
            MenuScreens.register(ACMenus.TRANSMUTATOR.get(), TransmutatorScreen::new);
            MenuScreens.register(ACMenus.ENGRAVER.get(), EngraverScreen::new);
            MenuScreens.register(ACMenus.MATERIALIZER.get(), MaterializerScreen::new);
            MenuScreens.register(ACMenus.STATE_TRANSFORMER.get(), StateTransformerScreen::new);
        });
    }

    /**
     * All 28 crystal types share one greyscale sprite and are told apart by tint, as on 1.12.2, so
     * each is registered with the colour for its index.
     */
    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        for (int type = 0; type < ACItems.crystals.size(); type++) {
            int color = ACClientVars.getCrystalColor(type);
            event.register((stack, layer) -> color,
                    ACItems.crystals.get(type).get(), ACItems.crystal_shards.get(type).get());
        }
    }
}
