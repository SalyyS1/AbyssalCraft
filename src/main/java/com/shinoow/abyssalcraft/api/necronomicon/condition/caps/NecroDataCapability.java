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

import java.util.ArrayList;
import java.util.List;

import com.shinoow.abyssalcraft.api.necronomicon.condition.ConditionProcessorRegistry;
import com.shinoow.abyssalcraft.api.necronomicon.condition.IUnlockCondition;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

/**
 * A player's Necronomicon progress.
 * <p>
 * The 1.12.2 version stored this through a capability with a separate {@code IStorage}
 * implementation. Serialisation is folded into this class here, matching how Forge handles
 * capability NBT on 1.20.1, and each trigger list round-trips as a list of strings.
 *
 * @author shinoow
 */
public class NecroDataCapability implements INecroDataCapability {

    /** Condition type that ignores the unlock-all cheat, as on 1.12.2. */
    private static final int TYPE_EXEMPT_FROM_UNLOCK_ALL = 11;

    private final List<ResourceLocation> biomeTriggers = new ArrayList<>();
    private final List<ResourceLocation> entityTriggers = new ArrayList<>();
    private final List<ResourceLocation> dimensionTriggers = new ArrayList<>();
    private final List<ResourceLocation> artifactTriggers = new ArrayList<>();
    private final List<ResourceLocation> pageTriggers = new ArrayList<>();
    private final List<ResourceLocation> whisperTriggers = new ArrayList<>();
    private final List<ResourceLocation> miscTriggers = new ArrayList<>();

    private boolean hasAllKnowledge;
    private long lastSyncTime;
    private int syncTimer;

    @Override
    public boolean isUnlocked(IUnlockCondition condition, Player player) {
        if (condition.getType() == -1) {
            return true;
        }
        if (hasAllKnowledge && condition.getType() != TYPE_EXEMPT_FROM_UNLOCK_ALL) {
            return true;
        }
        return ConditionProcessorRegistry.instance()
                .getProcessor(condition.getType())
                .processUnlock(condition, this, player);
    }

    @Override
    public void triggerEntityUnlock(ResourceLocation name) {
        addTrigger(entityTriggers, name);
    }

    @Override
    public void triggerBiomeUnlock(ResourceLocation name) {
        addTrigger(biomeTriggers, name);
    }

    @Override
    public void triggerDimensionUnlock(ResourceLocation dimension) {
        addTrigger(dimensionTriggers, dimension);
    }

    @Override
    public void triggerArtifactUnlock(ResourceLocation name) {
        addTrigger(artifactTriggers, name);
    }

    @Override
    public void triggerPageUnlock(ResourceLocation name) {
        addTrigger(pageTriggers, name);
    }

    @Override
    public void triggerWhisperUnlock(ResourceLocation name) {
        addTrigger(whisperTriggers, name);
    }

    @Override
    public void triggerMiscUnlock(ResourceLocation name) {
        addTrigger(miscTriggers, name);
    }

    private static void addTrigger(List<ResourceLocation> triggers, ResourceLocation name) {
        if (name != null && !triggers.contains(name)) {
            triggers.add(name);
        }
    }

    @Override
    public void unlockAllKnowledge(boolean unlock) {
        hasAllKnowledge = unlock;
    }

    @Override
    public boolean hasUnlockedAllKnowledge() {
        return hasAllKnowledge;
    }

    @Override
    public List<ResourceLocation> getBiomeTriggers() {
        return biomeTriggers;
    }

    @Override
    public List<ResourceLocation> getEntityTriggers() {
        return entityTriggers;
    }

    @Override
    public List<ResourceLocation> getDimensionTriggers() {
        return dimensionTriggers;
    }

    @Override
    public List<ResourceLocation> getArtifactTriggers() {
        return artifactTriggers;
    }

    @Override
    public List<ResourceLocation> getPageTriggers() {
        return pageTriggers;
    }

    @Override
    public List<ResourceLocation> getWhisperTriggers() {
        return whisperTriggers;
    }

    @Override
    public List<ResourceLocation> getMiscTriggers() {
        return miscTriggers;
    }

    @Override
    public void setLastSyncTime(long time) {
        lastSyncTime = time;
    }

    @Override
    public long getLastSyncTime() {
        return lastSyncTime;
    }

    @Override
    public void incrementSyncTimer() {
        syncTimer++;
    }

    @Override
    public void resetSyncTimer() {
        syncTimer = 0;
    }

    @Override
    public int getSyncTimer() {
        return syncTimer;
    }

    @Override
    public void copy(INecroDataCapability other) {
        replace(biomeTriggers, other.getBiomeTriggers());
        replace(entityTriggers, other.getEntityTriggers());
        replace(dimensionTriggers, other.getDimensionTriggers());
        replace(artifactTriggers, other.getArtifactTriggers());
        replace(pageTriggers, other.getPageTriggers());
        replace(whisperTriggers, other.getWhisperTriggers());
        replace(miscTriggers, other.getMiscTriggers());
        hasAllKnowledge = other.hasUnlockedAllKnowledge();
        lastSyncTime = other.getLastSyncTime();
    }

    private static void replace(List<ResourceLocation> target, List<ResourceLocation> source) {
        target.clear();
        target.addAll(source);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.put("BiomeTriggers", writeList(biomeTriggers));
        tag.put("EntityTriggers", writeList(entityTriggers));
        tag.put("DimensionTriggers", writeList(dimensionTriggers));
        tag.put("ArtifactTriggers", writeList(artifactTriggers));
        tag.put("PageTriggers", writeList(pageTriggers));
        tag.put("WhisperTriggers", writeList(whisperTriggers));
        tag.put("MiscTriggers", writeList(miscTriggers));
        tag.putBoolean("AllKnowledge", hasAllKnowledge);
        tag.putLong("LastSyncTime", lastSyncTime);
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        readList(tag, "BiomeTriggers", biomeTriggers);
        readList(tag, "EntityTriggers", entityTriggers);
        readList(tag, "DimensionTriggers", dimensionTriggers);
        readList(tag, "ArtifactTriggers", artifactTriggers);
        readList(tag, "PageTriggers", pageTriggers);
        readList(tag, "WhisperTriggers", whisperTriggers);
        readList(tag, "MiscTriggers", miscTriggers);
        hasAllKnowledge = tag.getBoolean("AllKnowledge");
        lastSyncTime = tag.getLong("LastSyncTime");
    }

    private static ListTag writeList(List<ResourceLocation> triggers) {
        ListTag list = new ListTag();
        triggers.forEach(trigger -> list.add(StringTag.valueOf(trigger.toString())));
        return list;
    }

    /** Unparseable entries are dropped rather than failing the whole player's progress load. */
    private static void readList(CompoundTag tag, String key, List<ResourceLocation> target) {
        target.clear();
        ListTag list = tag.getList(key, Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            ResourceLocation parsed = ResourceLocation.tryParse(list.getString(i));
            if (parsed != null) {
                target.add(parsed);
            }
        }
    }
}
