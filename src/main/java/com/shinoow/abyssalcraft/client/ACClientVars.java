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

/**
 * Client-only display values.
 * <p>
 * Crystal colours, indexed the same way the 1.12.2 metadata was, so entry <i>n</i> is the tint for
 * the crystal type at index <i>n</i> in {@code ACLib.crystalNames}. Every crystal shares one
 * greyscale sprite and is tinted with these, which is why there is no per-type texture.
 */
public final class ACClientVars {

    private static final int[] CRYSTAL_COLORS = {
            0xD9D9D9, 0xF3CC3E, 0xF6FF00, 0x3D3D36, 0xFFFFFF, 0xFFFFFF, 0xFFFFFF, 0x996A18,
            0xD9D9D9, 0x1500FF, 0x19FC00, 0xFF0000, 0x4A1C89, 0x00FFEE, 0x880101, 0xFFCC00,
            0xD9D8D7, 0xE89207, 0xD9D9D9, 0xD9D9D9, 0xD9D9D9, 0xFFFFFF, 0xD9D8D9, 0xFFFFFF,
            0xD7D8D9, 0xD7D8D9, 0xD9D9D9, 0xFFFFFF};

    private ACClientVars() {}

    /** Tint for a crystal type, or white when the index is out of range. */
    public static int getCrystalColor(int type) {
        return type < 0 || type >= CRYSTAL_COLORS.length ? 0xFFFFFF : CRYSTAL_COLORS[type];
    }

    public static int getCrystalColorCount() {
        return CRYSTAL_COLORS.length;
    }
}
