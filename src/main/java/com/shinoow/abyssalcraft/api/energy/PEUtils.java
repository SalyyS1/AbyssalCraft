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
package com.shinoow.abyssalcraft.api.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.shinoow.abyssalcraft.api.energy.EnergyEnum.AmplifierType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Utility methods for the Potential Energy system.
 * <p>
 * Transfer rules, ranges and probabilities are carried over from 1.12.2 unchanged: manipulators
 * reach a 3-block radius plus amplifier boost, skip the inner 5x5 column, cap at 20 collectors,
 * and each eligible collector has a 1-in-(120 - duration discount) chance per tick of receiving a
 * quanta. Tolerance still rises by 2 while attuned and 1 otherwise.
 * <p>
 * {@code World} became {@link Level}, {@code TileEntity} became {@link BlockEntity},
 * {@code EnumFacing} became {@link Direction}, and {@code world.rand} became
 * {@code level.getRandom()}.
 *
 * @author shinoow
 *
 * @since 1.5
 */
public class PEUtils {

    /** Manipulators never feed more than this many collectors at once. */
    private static final int MAX_COLLECTORS = 20;

    /** Base transfer radius before amplifiers. */
    private static final int BASE_RANGE = 3;

    /** Collectors inside this radius of the manipulator are ignored. */
    private static final int INNER_EXCLUSION = 2;

    private PEUtils() {}

    /**
     * Attempts to transfer PE from a Manipulator to nearby Collectors, scanning for them each call.
     *
     * @param level Current Level
     * @param pos Current BlockPos
     * @param manipulator PE Manipulator
     * @param boost Extra horizontal range
     */
    public static void transferPEToCollectors(Level level, BlockPos pos, IEnergyManipulator manipulator, int boost) {
        List<BlockEntity> collectors = new ArrayList<>();
        int verticalRange = getRangeAmplifiers(level, pos, manipulator);
        int horizontalRange = BASE_RANGE + boost;

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        outer:
        for (int x = -horizontalRange; x <= horizontalRange; x++) {
            for (int y = 0; y <= verticalRange; y++) {
                for (int z = -horizontalRange; z <= horizontalRange; z++) {
                    if (isInsideExclusion(x, z)) {
                        continue;
                    }
                    if (collectors.size() == MAX_COLLECTORS) {
                        break outer;
                    }
                    cursor.set(pos.getX() + x, pos.getY() - y, pos.getZ() + z);
                    BlockEntity be = level.getBlockEntity(cursor);
                    if (isCollector(be)) {
                        collectors.add(be);
                    }
                }
            }
        }

        int timeDiscount = (int) (20 * manipulator.getAmplifier(AmplifierType.DURATION));
        for (BlockEntity collector : collectors) {
            tryTransfer(level, collector, manipulator, timeDiscount);
        }
    }

    /**
     * Attempts to transfer PE from a Manipulator to the Collectors it has already located.
     *
     * @param level Current Level
     * @param pos Current BlockPos
     * @param manipulator PE Manipulator
     */
    public static void transferPEToCollectors(Level level, BlockPos pos, IEnergyManipulator manipulator) {
        Set<BlockPos> positions = manipulator.getEnergyCollectors();
        if (positions == null) {
            transferPEToCollectors(level, pos, manipulator, 0);
            return;
        }
        int timeDiscount = (int) (20 * manipulator.getAmplifier(AmplifierType.DURATION));
        for (BlockPos collectorPos : positions) {
            tryTransfer(level, level.getBlockEntity(collectorPos), manipulator, timeDiscount);
        }
    }

    /**
     * One transfer attempt against a single collector. Isolated so both transfer paths share the
     * exact same eligibility rules and probability.
     */
    private static void tryTransfer(Level level, BlockEntity collector, IEnergyManipulator manipulator, int timeDiscount) {
        if (!isCollector(collector) || !checkForAdjacentCollectors(level, collector.getBlockPos())) {
            return;
        }
        if (level.getRandom().nextInt(120 - timeDiscount) != 0) {
            return;
        }
        IEnergyCollector target = (IEnergyCollector) collector;
        if (target.canAcceptPE() && manipulator.canTransferPE()) {
            target.addEnergy(manipulator.getEnergyQuanta());
            manipulator.addTolerance(manipulator.isActive() ? 2 : 1);
        }
    }

    /**
     * Locates and stores the positions of Energy Collectors in range of the Manipulator.
     *
     * @param level Current Level
     * @param pos Current Position
     * @param manipulator PE Manipulator
     */
    public static void locateCollectors(Level level, BlockPos pos, IEnergyManipulator manipulator) {
        Set<BlockPos> positions = manipulator.getEnergyCollectors();
        if (positions == null) {
            return;
        }
        positions.clear();

        int verticalRange = getRangeAmplifiers(level, pos, manipulator);
        int boost = (int) (verticalRange + manipulator.getAmplifier(AmplifierType.RANGE) / 2);
        int horizontalRange = BASE_RANGE + boost;

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        outer:
        for (int x = -horizontalRange; x <= horizontalRange; x++) {
            for (int y = 0; y <= verticalRange; y++) {
                for (int z = -horizontalRange; z <= horizontalRange; z++) {
                    if (isInsideExclusion(x, z)) {
                        continue;
                    }
                    if (positions.size() == MAX_COLLECTORS) {
                        break outer;
                    }
                    cursor.set(pos.getX() + x, pos.getY() - y, pos.getZ() + z);
                    if (isCollector(level.getBlockEntity(cursor))) {
                        positions.add(cursor.immutable());
                    }
                }
            }
        }
    }

    /** The manipulator ignores the 5x5 column centred on itself. */
    private static boolean isInsideExclusion(int x, int z) {
        return x >= -INNER_EXCLUSION && x <= INNER_EXCLUSION
                && z >= -INNER_EXCLUSION && z <= INNER_EXCLUSION;
    }

    /**
     * Clears the active Deity and Amplifier when a PE Manipulator is no longer active. Call from
     * the block entity ticker whenever the manipulator isn't active, so the data is erased when it
     * should be.
     *
     * @param manipulator PE Manipulator to reset
     */
    public static void clearManipulatorData(IEnergyManipulator manipulator) {
        if (manipulator.getActiveAmplifier() == null && manipulator.getActiveDeity() == null) {
            return;
        }
        manipulator.setActiveDeity(null);
        manipulator.setActiveAmplifier(null);
        if (manipulator instanceof BlockEntity be) {
            be.setChanged();
        }
    }

    /** Reads the active deity and amplifier from saved data. */
    public static void readManipulatorNBT(IEnergyManipulator manipulator, CompoundTag tag) {
        String deity = tag.getString("ActiveDeity");
        String amplifier = tag.getString("ActiveAmplifier");
        manipulator.setActiveDeity(deity.isEmpty() ? null : EnergyEnum.DeityType.valueOf(deity));
        manipulator.setActiveAmplifier(amplifier.isEmpty() ? null : AmplifierType.valueOf(amplifier));
    }

    /** Writes the active deity and amplifier to saved data. */
    public static void writeManipulatorNBT(IEnergyManipulator manipulator, CompoundTag tag) {
        tag.putString("ActiveDeity",
                manipulator.getActiveDeity() == null ? "" : manipulator.getActiveDeity().name());
        tag.putString("ActiveAmplifier",
                manipulator.getActiveAmplifier() == null ? "" : manipulator.getActiveAmplifier().name());
    }

    /**
     * Counts the range amplifier blocks stacked directly beneath the manipulator, up to two.
     * <p>
     * 1.12.2 also added a multiblock structure bonus here when the manipulator was an
     * {@code IStructureComponent}. Multiblocks land in a later phase, so that term is absent for
     * now and this returns only the stacked-amplifier count.
     *
     * @param level Current Level
     * @param pos Current BlockPos
     * @param manipulator PE Manipulator; will select the structure bonus once multiblocks exist
     */
    public static int getRangeAmplifiers(Level level, BlockPos pos, IEnergyManipulator manipulator) {
        int num = 0;
        if (isRangeAmplifier(level, pos.below())) {
            num = 1;
            if (isRangeAmplifier(level, pos.below(2))) {
                num = 2;
            }
        }
        return num;
    }

    private static boolean isRangeAmplifier(Level level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        return block instanceof IEnergyAmplifier amplifier
                && amplifier.getAmplifierType() == AmplifierType.RANGE;
    }

    /** Checks if a BlockEntity is a PE Collector. */
    public static boolean isCollector(BlockEntity be) {
        return be instanceof IEnergyCollector;
    }

    /** Checks if a BlockEntity is a PE Manipulator. */
    public static boolean isManipulator(BlockEntity be) {
        return be instanceof IEnergyManipulator;
    }

    /** Checks if a BlockEntity is a PE Container. */
    public static boolean isContainer(BlockEntity be) {
        return be instanceof IEnergyContainer;
    }

    /** Checks that the BlockPos has no adjacent PE Collectors. */
    public static boolean checkForAdjacentCollectors(Level level, BlockPos pos) {
        for (Direction face : Direction.values()) {
            if (isCollector(level.getBlockEntity(pos.relative(face)))) {
                return false;
            }
        }
        return true;
    }

    /** Checks that the BlockPos has no adjacent PE Manipulators. */
    public static boolean checkForAdjacentManipulators(Level level, BlockPos pos) {
        for (Direction face : Direction.values()) {
            if (isManipulator(level.getBlockEntity(pos.relative(face)))) {
                return false;
            }
        }
        return true;
    }
}
