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
package com.shinoow.abyssalcraft.init;

import com.shinoow.abyssalcraft.api.item.ACItems;
import com.shinoow.abyssalcraft.api.ritual.NecronomiconCreationRitual;
import com.shinoow.abyssalcraft.api.ritual.RitualRegistry;
import com.shinoow.abyssalcraft.common.ritual.NecronomiconWeatherRitual;
import com.shinoow.abyssalcraft.lib.ACLib;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Ritual registration and the Necronomicon tier each dimension requires.
 * <p>
 * The tier mapping is what stops a player performing Dreadlands rituals with an Overworld-tier
 * book, so it is registered alongside the rituals themselves.
 */
public final class ACRituals {

    private ACRituals() {}

    public static void register() {
        RitualRegistry registry = RitualRegistry.instance();

        // Book tier required per dimension, unchanged from 1.12.2.
        registry.addDimensionToBookType(Level.OVERWORLD, 0);
        registry.addDimensionToBookType(ACLib.THE_ABYSSAL_WASTELAND, 1);
        registry.addDimensionToBookType(ACLib.THE_DREADLANDS, 2);
        registry.addDimensionToBookType(ACLib.OMOTHOL, 3);
        registry.addDimensionToBookType(ACLib.THE_DARK_REALM, 4);

        registry.registerRitual(new NecronomiconWeatherRitual());
        registry.registerRitual(new NecronomiconCreationRitual("transmutationgem", 1, 500F,
                new ItemStack(ACItems.transmutation_gem.get()),
                ACItems.coralium_gem.get(), Items.DIAMOND, ACItems.coralium_gem.get(), Items.DIAMOND,
                ACItems.coralium_gem.get(), Items.DIAMOND, ACItems.coralium_gem.get(), Items.DIAMOND));
    }
}
