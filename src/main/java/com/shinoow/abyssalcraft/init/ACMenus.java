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

import com.shinoow.abyssalcraft.common.inventory.CrystallizerMenu;
import com.shinoow.abyssalcraft.common.inventory.TransmutatorMenu;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegistryObject;

/**
 * Menu types.
 * <p>
 * 1.12.2 opened GUIs through a single {@code IGuiHandler} keyed by the integer IDs in
 * {@code ACLib}; those IDs are gone. Each screen now needs a registered {@link MenuType}, which
 * {@code NetworkHooks.openScreen} uses to build the client-side menu.
 */
public final class ACMenus {

    public static final RegistryObject<MenuType<CrystallizerMenu>> CRYSTALLIZER =
            ACRegistries.MENUS.register("crystallizer",
                    () -> IForgeMenuType.create((IContainerFactory<CrystallizerMenu>)
                            (id, inventory, buffer) -> new CrystallizerMenu(id, inventory)));

    public static final RegistryObject<MenuType<TransmutatorMenu>> TRANSMUTATOR =
            ACRegistries.MENUS.register("transmutator",
                    () -> IForgeMenuType.create((IContainerFactory<TransmutatorMenu>)
                            (id, inventory, buffer) -> new TransmutatorMenu(id, inventory)));

    private ACMenus() {}

    /** Forces class initialisation so the fields above reach the DeferredRegister. */
    public static void register() {}
}
