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

import com.shinoow.abyssalcraft.api.necronomicon.condition.IUnlockCondition;

/**
 * A node in the Necronomicon knowledge tree.
 *
 * @author shinoow
 */
public interface INecroData {

    String getTitle();

    /** Index of the icon shown beside this entry. */
    int getDisplayIcon();

    String getText();

    boolean hasText();

    /** Stable identifier; player progress is saved against this. */
    String getIdentifier();

    IUnlockCondition getCondition();
}
