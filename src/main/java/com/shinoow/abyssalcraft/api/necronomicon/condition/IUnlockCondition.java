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
package com.shinoow.abyssalcraft.api.necronomicon.condition;

/**
 * A condition gating a piece of Necronomicon knowledge.
 * <p>
 * The numeric type IDs are unchanged from 1.12.2, because saved player progress keys off them:
 * -1 none, 0 biome, 1 entity, 2 dimension, 3 multi-biome, 4 multi-entity, 5 biome predicate,
 * 6 entity predicate, 7 artifact, 8 page, 9 whisper, 10 misc.
 *
 * @author shinoow
 */
public interface IUnlockCondition {

    boolean areConditionObjectsEqual(Object stuff);

    Object getConditionObject();

    int getType();
}
