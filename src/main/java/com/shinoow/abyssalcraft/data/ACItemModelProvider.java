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

import java.util.Map;

import com.shinoow.abyssalcraft.AbyssalCraft;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Generates flat item models.
 * <p>
 * Textures come from {@link ACItemTextures} rather than the registry name, because the 1.12.2
 * assets are abbreviated and are left untouched. Handheld tools use the held-item parent so they
 * render in the hand the way they did before.
 */
public class ACItemModelProvider extends ItemModelProvider {

    public ACItemModelProvider(GatherDataEvent event, ExistingFileHelper helper) {
        super(event.getGenerator().getPackOutput(), AbyssalCraft.MOD_ID, helper);
    }

    @Override
    protected void registerModels() {
        for (Map.Entry<RegistryObject<Item>, String> entry : ACItemTextures.ALL.entrySet()) {
            Item item = entry.getKey().get();
            String name = ForgeRegistries.ITEMS.getKey(item).getPath();
            withExistingParent(name, parentFor(item))
                    .texture("layer0", new ResourceLocation(AbyssalCraft.MOD_ID, "items/" + entry.getValue()));
        }
    }

    /** Tools and armour render held; everything else is a flat generated sprite. */
    private static ResourceLocation parentFor(Item item) {
        boolean handheld = item instanceof net.minecraft.world.item.TieredItem;
        return new ResourceLocation("item/" + (handheld ? "handheld" : "generated"));
    }
}
