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

import com.shinoow.abyssalcraft.common.blocks.tile.CrystallizerBlockEntity;
import com.shinoow.abyssalcraft.init.ACBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

/**
 * The Crystallizer block.
 * <p>
 * On 1.12.2 the active and idle forms were two separate blocks that swapped places as the machine
 * ran, and the GUI opened via {@code IGuiHandler}. The two-block split is kept so existing recipes
 * and JEI entries keep working, but the block now carries a {@code FACING} property instead of
 * baking rotation into metadata, and opens its menu through {@link NetworkHooks}.
 */
public class CrystallizerBlock extends BaseEntityBlock {

    private final boolean active;

    public CrystallizerBlock(Properties properties, boolean active) {
        super(properties);
        this.active = active;
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    /** True for the lit variant, which renders the active texture and emits light. */
    public boolean isActive() {
        return active;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING,
                context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CrystallizerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Only the server ticks; the client learns about progress through the menu's ContainerData.
        return level.isClientSide() ? null
                : createTickerHelper(type, ACBlockEntities.CRYSTALLIZER.get(), CrystallizerBlockEntity::serverTick);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
            InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }
        if (level.getBlockEntity(pos) instanceof CrystallizerBlockEntity be) {
            NetworkHooks.openScreen((net.minecraft.server.level.ServerPlayer) player, be, pos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    /** Spills the inventory when broken, as on 1.12.2. */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof CrystallizerBlockEntity be) {
            Containers.dropContents(level, pos, be);
        }
        super.onRemove(state, level, pos, newState, moving);
    }
}
