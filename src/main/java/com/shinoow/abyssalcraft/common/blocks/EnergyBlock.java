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

import java.util.function.BiFunction;
import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * A block whose only job is to host a Potential Energy block entity.
 * <p>
 * 1.12.2 had a separate near-identical block class per PE tile; they differed only in which tile
 * they created, so one parameterised class replaces the lot.
 */
public class EnergyBlock extends BaseEntityBlock {

    private final BiFunction<BlockPos, BlockState, BlockEntity> factory;
    private final Supplier<? extends BlockEntityType<? extends BlockEntity>> expectedType;
    private final BlockEntityTicker<? extends BlockEntity> tickMethod;

    public EnergyBlock(Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> factory) {
        this(properties, factory, null, null);
    }

    /**
     * @param expectedType the block entity type this block ticks; the ticker only runs for a
     *        matching type, which is what {@code createTickerHelper} checks
     * @param tickMethod the per-tick method to call
     */
    public <E extends BlockEntity> EnergyBlock(Properties properties,
            BiFunction<BlockPos, BlockState, BlockEntity> factory,
            Supplier<BlockEntityType<E>> expectedType, BlockEntityTicker<E> tickMethod) {
        super(properties);
        this.factory = factory;
        this.expectedType = expectedType;
        this.tickMethod = tickMethod;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> type) {
        // Only the server ticks; PE amounts reach clients through block updates.
        if (expectedType == null || level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, (BlockEntityType<BlockEntity>) expectedType.get(),
                (BlockEntityTicker<BlockEntity>) tickMethod);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return factory.apply(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
