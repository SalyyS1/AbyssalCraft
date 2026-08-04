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

import com.shinoow.abyssalcraft.common.blocks.tile.AbstractMachineBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
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
 * A fuel-burning machine block.
 * <p>
 * 1.12.2 had a near-identical block class per machine, each split into an idle and an active form
 * that swapped places as the machine ran. The idle/active split is kept so existing recipes and JEI
 * entries still resolve, but one parameterised class replaces the per-machine duplicates. Rotation
 * moves from metadata to a {@code FACING} property, and the GUI opens through {@link NetworkHooks}
 * instead of the removed {@code IGuiHandler}.
 */
public class MachineBlock extends BaseEntityBlock {

    private final BiFunction<BlockPos, BlockState, BlockEntity> factory;
    private final Supplier<BlockEntityType<? extends AbstractMachineBlockEntity>> type;
    private final boolean active;

    public MachineBlock(Properties properties, BiFunction<BlockPos, BlockState, BlockEntity> factory,
            Supplier<BlockEntityType<? extends AbstractMachineBlockEntity>> type, boolean active) {
        super(properties);
        this.factory = factory;
        this.type = type;
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
        return factory.apply(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
            BlockEntityType<T> blockEntityType) {
        // Only the server ticks; the client learns about progress through the menu's ContainerData.
        return level.isClientSide() ? null
                : createTickerHelper(blockEntityType, type.get(), AbstractMachineBlockEntity::serverTick);
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
        if (level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity machine
                && player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, machine, pos);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    /** Spills the inventory when broken, as on 1.12.2. */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (!state.is(newState.getBlock())
                && level.getBlockEntity(pos) instanceof AbstractMachineBlockEntity machine) {
            Containers.dropContents(level, pos, machine);
        }
        super.onRemove(state, level, pos, newState, moving);
    }
}
