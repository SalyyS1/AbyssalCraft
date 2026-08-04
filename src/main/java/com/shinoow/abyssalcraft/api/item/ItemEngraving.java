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
package com.shinoow.abyssalcraft.api.item;

import net.minecraft.world.item.Item;

/**
 * An engraving stamp used in the Engraver.
 * <p>
 * Engravings wear out with use, so they carry durability and cannot be repaired -- both unchanged
 * from 1.12.2. The 1.12.2 class also set a translation key and an unlock condition; keys come from
 * the registry name on 1.20.1, and the unlock condition is attached where the item is registered.
 *
 * @author shinoow
 */
public class ItemEngraving extends Item {

    public ItemEngraving(int durability) {
        super(new Item.Properties().durability(durability).setNoRepair());
    }
}
