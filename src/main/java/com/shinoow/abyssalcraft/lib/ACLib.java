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

import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import com.shinoow.abyssalcraft.AbyssalCraft;

/**
 * This package exposes a bit more internal things without forcing too much of a hard dependency
 * @author shinoow
 */
public class ACLib {

    /**
     * Dimension keys. 1.12.2 addressed dimensions by mutable numeric ID; 1.20.1 addresses them by
     * {@link ResourceKey}, so the four IDs became the four constants below and the corresponding
     * dimensions are declared as datapack JSON under {@code data/abyssalcraft/dimension}.
     */
    public static final ResourceKey<Level> THE_ABYSSAL_WASTELAND =
            ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    new ResourceLocation(AbyssalCraft.MOD_ID, "the_abyssal_wasteland"));
    public static final ResourceKey<Level> THE_DREADLANDS =
            ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    new ResourceLocation(AbyssalCraft.MOD_ID, "the_dreadlands"));
    public static final ResourceKey<Level> OMOTHOL =
            ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    new ResourceLocation(AbyssalCraft.MOD_ID, "omothol"));
    public static final ResourceKey<Level> THE_DARK_REALM =
            ResourceKey.create(net.minecraft.core.registries.Registries.DIMENSION,
                    new ResourceLocation(AbyssalCraft.MOD_ID, "the_dark_realm"));

    // Crystal stuff
    public static final String[] crystalNames = new String[]{"Iron", "Gold", "Sulfur", "Carbon", "Oxygen", "Hydrogen", "Nitrogen", "Phosphorus",
            "Potassium", "Nitrate", "Methane", "Redstone", "Abyssalnite", "Coralium", "Dreadium", "Blaze", "Tin", "Copper",
            "Silicon", "Magnesium", "Aluminium", "Silica", "Alumina", "Magnesia", "Zinc", "Calcium", "Beryllium", "Beryl"};
    public static final String[] crystalAtoms = new String[]{"Fe", "Au", "S", "C", "O", "H", "N", "P", "K", "NO₃", "CH₄", "none", "An",
            "Cor", "Dr", "none", "Sn", "Cu", "Si", "Mg", "Al", "SiO₂", "Al₂O₃", "MgO", "Zn", "Ca", "Be", "Be₃Al₂(SiO₃)₆"};
}
