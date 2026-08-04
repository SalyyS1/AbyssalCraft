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
package com.shinoow.abyssalcraft.api.necronomicon.condition.caps;

import java.util.List;

import com.shinoow.abyssalcraft.api.necronomicon.condition.IUnlockCondition;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * A player's Necronomicon progress: which unlock triggers they have satisfied.
 * <p>
 * The trigger lists held plain name strings on 1.12.2, and dimensions held numeric IDs. Both are
 * {@link ResourceLocation}s here, matching the switch in the condition classes.
 *
 * @author shinoow
 */
public interface INecroDataCapability {

    boolean isUnlocked(IUnlockCondition condition, Player player);

    void triggerEntityUnlock(ResourceLocation name);

    void triggerBiomeUnlock(ResourceLocation name);

    void triggerDimensionUnlock(ResourceLocation dimension);

    void triggerArtifactUnlock(ResourceLocation name);

    void triggerPageUnlock(ResourceLocation name);

    void triggerWhisperUnlock(ResourceLocation name);

    void triggerMiscUnlock(ResourceLocation name);

    void unlockAllKnowledge(boolean unlock);

    boolean hasUnlockedAllKnowledge();

    List<ResourceLocation> getBiomeTriggers();

    List<ResourceLocation> getEntityTriggers();

    List<ResourceLocation> getDimensionTriggers();

    List<ResourceLocation> getArtifactTriggers();

    List<ResourceLocation> getPageTriggers();

    List<ResourceLocation> getWhisperTriggers();

    List<ResourceLocation> getMiscTriggers();

    void setLastSyncTime(long time);

    long getLastSyncTime();

    void incrementSyncTimer();

    void resetSyncTimer();

    int getSyncTimer();

    /** Copies another player's progress, used when respawning. */
    void copy(INecroDataCapability other);
}
