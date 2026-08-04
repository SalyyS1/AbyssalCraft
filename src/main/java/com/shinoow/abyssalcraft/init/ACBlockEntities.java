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
package com.shinoow.abyssalcraft.init;

import com.shinoow.abyssalcraft.api.block.ACBlocks;
import com.shinoow.abyssalcraft.common.blocks.tile.CrystallizerBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.EnergyCollectorBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.EngraverBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.EnergyContainerBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.RitualAltarBlockEntity;
import com.shinoow.abyssalcraft.common.blocks.tile.TransmutatorBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

/**
 * Block entity types.
 * <p>
 * 1.12.2 registered tile entities by name with {@code GameRegistry.registerTileEntity} and looked
 * them up reflectively. 1.20.1 requires a {@link BlockEntityType} that declares which blocks it may
 * attach to, which is what the {@code Builder.of} calls below provide.
 */
public final class ACBlockEntities {

    public static final RegistryObject<BlockEntityType<CrystallizerBlockEntity>> CRYSTALLIZER =
            ACRegistries.BLOCK_ENTITIES.register("crystallizer", () -> BlockEntityType.Builder
                    .of(CrystallizerBlockEntity::new,
                            ACBlocks.crystallizer_idle.get(),
                            ACBlocks.crystallizer_active.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<EnergyCollectorBlockEntity>> ENERGY_COLLECTOR =
            ACRegistries.BLOCK_ENTITIES.register("energy_collector", () -> BlockEntityType.Builder
                    .of(EnergyCollectorBlockEntity::new, ACBlocks.energy_collector.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<EnergyContainerBlockEntity>> ENERGY_CONTAINER =
            ACRegistries.BLOCK_ENTITIES.register("energy_container", () -> BlockEntityType.Builder
                    .of(EnergyContainerBlockEntity::new, ACBlocks.energy_container.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<RitualAltarBlockEntity>> RITUAL_ALTAR =
            ACRegistries.BLOCK_ENTITIES.register("ritual_altar", () -> BlockEntityType.Builder
                    .of(RitualAltarBlockEntity::new, ACBlocks.ritual_altar.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<TransmutatorBlockEntity>> TRANSMUTATOR =
            ACRegistries.BLOCK_ENTITIES.register("transmutator", () -> BlockEntityType.Builder
                    .of(TransmutatorBlockEntity::new,
                            ACBlocks.transmutator_idle.get(), ACBlocks.transmutator_active.get())
                    .build(null));

    public static final RegistryObject<BlockEntityType<EngraverBlockEntity>> ENGRAVER =
            ACRegistries.BLOCK_ENTITIES.register("engraver", () -> BlockEntityType.Builder
                    .of(EngraverBlockEntity::new, ACBlocks.engraver.get())
                    .build(null));

    private ACBlockEntities() {}

    /** Forces class initialisation so the fields above reach the DeferredRegister. */
    public static void register() {}
}
