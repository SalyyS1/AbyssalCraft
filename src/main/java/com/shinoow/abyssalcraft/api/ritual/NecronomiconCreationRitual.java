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

import com.shinoow.abyssalcraft.common.blocks.tile.RitualAltarBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A ritual that creates an item on the altar.
 * <p>
 * 1.12.2 wrote the result by serialising the altar to NBT, swapping the {@code Item} tag and
 * reading it back. The altar block entity exposes its held stack directly here, so the result is
 * simply set on it.
 *
 * @author shinoow
 */
public class NecronomiconCreationRitual extends NecronomiconRitual {

    private final ItemStack item;

    public NecronomiconCreationRitual(String unlocalizedName, int bookType, ResourceKey<Level> dimension,
            float requiredEnergy, boolean requiresSacrifice, ItemStack item, Object... offerings) {
        super(unlocalizedName, bookType, dimension, requiredEnergy, requiresSacrifice, offerings);
        this.item = item.copyWithCount(1);
    }

    public NecronomiconCreationRitual(String unlocalizedName, int bookType, ResourceKey<Level> dimension,
            float requiredEnergy, ItemStack item, Object... offerings) {
        this(unlocalizedName, bookType, dimension, requiredEnergy, false, item, offerings);
    }

    public NecronomiconCreationRitual(String unlocalizedName, int bookType, float requiredEnergy,
            ItemStack item, Object... offerings) {
        this(unlocalizedName, bookType, null, requiredEnergy, item, offerings);
    }

    public ItemStack getItem() {
        return item.copy();
    }

    @Override
    public boolean canCompleteRitual(Level level, BlockPos pos, Player player) {
        return true;
    }

    @Override
    protected void completeRitualServer(Level level, BlockPos pos, Player player) {
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(pos.getX(), pos.getY() + 1, pos.getZ());
            bolt.setVisualOnly(true);
            level.addFreshEntity(bolt);
        }
        if (level.getBlockEntity(pos) instanceof RitualAltarBlockEntity altar) {
            altar.setItem(getItem());
        }
    }

    @Override
    protected void completeRitualClient(Level level, BlockPos pos, Player player) {
        // The created item and the lightning are both server state and sync normally.
    }
}
