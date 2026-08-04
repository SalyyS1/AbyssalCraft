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
package com.shinoow.abyssalcraft.data;

import java.util.Map;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.api.block.ACBlocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Generates blockstates, block models and block item models.
 * <p>
 * The 1.12.2 files cannot be reused: the blockstate format changed, and the metadata split renamed
 * most blocks so their old file names no longer match. Textures are taken from
 * {@link ACBlockTextures} rather than inferred from registry names, because the 1.12.2 assets use
 * abbreviated names and are left untouched.
 */
public class ACBlockModelProvider extends BlockStateProvider {

    public ACBlockModelProvider(GatherDataEvent event, ExistingFileHelper helper) {
        super(event.getGenerator().getPackOutput(), AbyssalCraft.MOD_ID, helper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (Map.Entry<RegistryObject<Block>, String> entry : ACBlockTextures.ALL.entrySet()) {
            Block block = entry.getKey().get();
            simpleBlockWithItem(block, models().cubeAll(name(block), texture(entry.getValue())));
        }

        for (Map.Entry<RegistryObject<Block>, String[]> entry : ACBlockTextures.COLUMNS.entrySet()) {
            Block block = entry.getKey().get();
            String[] textures = entry.getValue();
            var model = models().cubeColumn(name(block), texture(textures[1]), texture(textures[0]));
            if (block instanceof RotatedPillarBlock pillar) {
                axisBlock(pillar, model, model);
            } else {
                simpleBlock(block, model);
            }
            simpleBlockItem(block, model);
        }

        // The machines face the player and show a lit front when running, so each needs an
        // oriented model per state rather than a single cube.
        machine(ACBlocks.crystallizer_idle.get(), "crystallizer", "crystallizer_front_off");
        machine(ACBlocks.crystallizer_active.get(), "crystallizer", "crystallizer_front_on");
        machine(ACBlocks.transmutator_idle.get(), "transmutator", "transmutator_front_off");
        machine(ACBlocks.transmutator_active.get(), "transmutator", "transmutator_front_on");
    }

    private void machine(Block block, String prefix, String frontTexture) {
        var model = models().orientable(name(block), texture(prefix + "_side"),
                texture(frontTexture), texture(prefix + "_top"));
        horizontalBlock(block, model);
        simpleBlockItem(block, model);
    }

    private static String name(Block block) {
        return net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    /** 1.12.2 textures live under {@code textures/blocks/}, not the modern {@code textures/block/}. */
    private static ResourceLocation texture(String base) {
        return new ResourceLocation(AbyssalCraft.MOD_ID, "blocks/" + base);
    }
}
