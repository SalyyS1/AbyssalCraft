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

import java.util.function.Supplier;

import com.shinoow.abyssalcraft.init.ACRegistries;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

/**
 * Creative Tab references.
 * <p>
 * {@code CreativeTabs} was a class you subclassed on 1.12.2; on 1.20.1 tabs are registry entries
 * built through {@link CreativeModeTab#builder()}. Tab identifiers are unchanged so the existing
 * {@code itemGroup.*} lang keys still apply, and contents are filled in by the block/item phases.
 */
public class ACTabs {

    public static final RegistryObject<CreativeModeTab> tabBlock =
            tab("acblocks", () -> new ItemStack(Items.STONE));
    public static final RegistryObject<CreativeModeTab> tabItems =
            tab("acitems", () -> new ItemStack(Items.BOOK));
    public static final RegistryObject<CreativeModeTab> tabTools =
            tab("actools", () -> new ItemStack(Items.STONE_AXE));
    public static final RegistryObject<CreativeModeTab> tabCombat =
            tab("acctools", () -> new ItemStack(Items.STONE_SWORD));
    public static final RegistryObject<CreativeModeTab> tabFood =
            tab("acfood", () -> new ItemStack(Items.APPLE));
    public static final RegistryObject<CreativeModeTab> tabDecoration =
            tab("acdblocks", () -> new ItemStack(Items.STONE_BRICKS));
    public static final RegistryObject<CreativeModeTab> tabCrystals =
            tab("accrystals", () -> new ItemStack(Items.QUARTZ));
    public static final RegistryObject<CreativeModeTab> tabCoins =
            tab("accoins", () -> new ItemStack(Items.GOLD_NUGGET));
    public static final RegistryObject<CreativeModeTab> tabSpells =
            tab("acspells", () -> new ItemStack(Items.PAPER));

    private static RegistryObject<CreativeModeTab> tab(String name, Supplier<ItemStack> icon) {
        return ACRegistries.CREATIVE_TABS.register(name, () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + name))
                .icon(icon)
                .build());
    }

    /** Forces class initialisation so the tabs above are handed to the DeferredRegister. */
    public static void register() {}
}
