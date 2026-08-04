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
package com.shinoow.abyssalcraft.api.ritual;

/**
 * Particle effect played while a ritual runs. Ordinals are sent over the wire, so the order is
 * unchanged from 1.12.2.
 *
 * @author shinoow
 */
public enum EnumRitualParticle {

    NONE, ITEM, SMOKE, ITEM_SMOKE_COMBO, SMOKE_PILLARS, SPRINKLER, PE_STREAM, GLYPHS;

    /** Out-of-range ids fall back to NONE rather than throwing on a malformed packet. */
    public static EnumRitualParticle fromId(int id) {
        return id < 0 || id >= values().length ? NONE : values()[id];
    }
}
