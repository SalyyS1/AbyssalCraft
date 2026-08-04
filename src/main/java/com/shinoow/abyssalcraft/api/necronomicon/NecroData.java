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
package com.shinoow.abyssalcraft.api.necronomicon;

import java.util.ArrayList;
import java.util.List;

import com.shinoow.abyssalcraft.api.necronomicon.condition.DefaultCondition;
import com.shinoow.abyssalcraft.api.necronomicon.condition.IUnlockCondition;
import com.shinoow.abyssalcraft.common.util.ACLogger;

/**
 * A section of the Necronomicon: a titled node holding up to fourteen child entries, or seven when
 * it also carries introductory text.
 * <p>
 * The page-count limits and the replace-by-identifier behaviour of {@link #addData} are unchanged
 * from 1.12.2, since the book's layout depends on them.
 *
 * @author shinoow
 */
public class NecroData implements INecroData {

    /** Maximum child entries when this node has no introductory text of its own. */
    private static final int MAX_ENTRIES = 14;

    /** Maximum child entries when introductory text takes up half the spread. */
    private static final int MAX_ENTRIES_WITH_TEXT = 7;

    private final String identifier;
    private final String title;
    private final String information;
    private final IUnlockCondition condition;
    private final int displayIcon;
    private final List<INecroData> containedData = new ArrayList<>();

    public NecroData(String identifier, String title, int displayIcon, String info,
            IUnlockCondition condition, INecroData... data) {
        this.identifier = identifier;
        this.title = title;
        this.displayIcon = displayIcon;
        this.information = info;
        this.condition = condition;
        for (INecroData entry : data) {
            addData(entry);
        }
    }

    public NecroData(String identifier, String title, int displayIcon, IUnlockCondition condition,
            INecroData... data) {
        this(identifier, title, displayIcon, null, condition, data);
    }

    public NecroData(String identifier, String title, int displayIcon, String info, INecroData... data) {
        this(identifier, title, displayIcon, info, new DefaultCondition(), data);
    }

    public NecroData(String identifier, String title, int displayIcon, INecroData... data) {
        this(identifier, title, displayIcon, null, new DefaultCondition(), data);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getDisplayIcon() {
        return displayIcon;
    }

    @Override
    public String getText() {
        return information;
    }

    @Override
    public boolean hasText() {
        return information != null && !information.isEmpty();
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public IUnlockCondition getCondition() {
        return condition;
    }

    public List<INecroData> getContainedData() {
        return List.copyOf(containedData);
    }

    /** Adds an entry, replacing any existing one that shares its identifier. */
    public void addData(INecroData data) {
        for (int i = 0; i < containedData.size(); i++) {
            if (containedData.get(i).getIdentifier().equals(data.getIdentifier())) {
                containedData.set(i, data);
                return;
            }
        }
        if (containedData.size() < (hasText() ? MAX_ENTRIES_WITH_TEXT : MAX_ENTRIES)) {
            containedData.add(data);
        } else {
            ACLogger.severe("NecroData instance {} is already full, can't add more data!", identifier);
        }
    }

    public void removeData(String identifier) {
        containedData.removeIf(data -> data.getIdentifier().equals(identifier));
    }
}
