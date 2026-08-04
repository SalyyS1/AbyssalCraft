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
package com.shinoow.abyssalcraft.common.ritual;

import com.shinoow.abyssalcraft.api.ritual.EnumRitualParticle;
import com.shinoow.abyssalcraft.api.ritual.NecronomiconRitual;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Cycles the weather: clear becomes rain, rain becomes thunder, thunder becomes clear.
 * <p>
 * 1.12.2 wrote through {@code world.getWorldInfo()}; that is server-only state on 1.20.1 and is
 * set through {@link ServerLevel#setWeatherParameters}. The durations below are vanilla's own
 * defaults for a weather command with no time argument.
 */
public class NecronomiconWeatherRitual extends NecronomiconRitual {

    /** Vanilla's default weather duration, in ticks. */
    private static final int WEATHER_DURATION = 6000;

    public NecronomiconWeatherRitual() {
        super("weather", 0, 100F, Items.FEATHER, Items.FEATHER, Items.FEATHER, Items.FEATHER,
                Items.FEATHER, Items.FEATHER, Items.FEATHER, Items.FEATHER);
        setRitualParticle(EnumRitualParticle.PE_STREAM);
    }

    /** 1.12.2 gated on {@code Biome.canRain()}; the equivalent check is now on the biome value. */
    @Override
    public boolean canCompleteRitual(Level level, BlockPos pos, Player player) {
        return level.getBiome(pos).value().hasPrecipitation();
    }

    @Override
    protected void completeRitualClient(Level level, BlockPos pos, Player player) {
        // The weather change is server state; the client sees it through the normal sync.
    }

    @Override
    protected void completeRitualServer(Level level, BlockPos pos, Player player) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        if (!level.isRaining()) {
            serverLevel.setWeatherParameters(0, WEATHER_DURATION, true, false);
        } else if (!level.isThundering()) {
            serverLevel.setWeatherParameters(0, WEATHER_DURATION, true, true);
        } else {
            serverLevel.setWeatherParameters(WEATHER_DURATION, 0, false, false);
        }
    }
}
