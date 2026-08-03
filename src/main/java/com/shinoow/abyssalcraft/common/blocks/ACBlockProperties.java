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
package com.shinoow.abyssalcraft.common.blocks;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * Block property presets.
 * <p>
 * On 1.12.2 a block's hardness, resistance, sound, map colour and harvest level were set with
 * individual setters after construction, and {@code Material} implied several of them. Both
 * {@code Material} and the harvest-level system are gone in 1.20.1: behaviour comes from an
 * immutable {@link BlockBehaviour.Properties} passed to the constructor, and what used to be a
 * harvest level is now a mineable/tool-tier tag applied during datagen.
 * <p>
 * These helpers keep the call sites in the block registry as terse as the old setter chains were.
 */
public final class ACBlockProperties {

    private ACBlockProperties() {}

    /** Stone-like block that needs the correct tool to drop. */
    public static BlockBehaviour.Properties stone(float hardness, float resistance, MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hardness, resistance)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops();
    }

    /** Stone-like block that emits light. */
    public static BlockBehaviour.Properties glowingStone(float hardness, float resistance, MapColor color, int light) {
        return stone(hardness, resistance, color).lightLevel(state -> light);
    }

    /** Wood-like block; drops without a specific tool, as on 1.12.2. */
    public static BlockBehaviour.Properties wood(float hardness, float resistance, MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hardness, resistance)
                .sound(SoundType.WOOD);
    }

    /** Metal/ore block. */
    public static BlockBehaviour.Properties metal(float hardness, float resistance, MapColor color) {
        return BlockBehaviour.Properties.of()
                .mapColor(color)
                .strength(hardness, resistance)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops();
    }
}
