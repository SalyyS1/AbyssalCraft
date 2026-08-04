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

import com.shinoow.abyssalcraft.api.necronomicon.condition.caps.INecroDataCapability;

import net.minecraft.world.entity.player.Player;

/**
 * Decides whether one kind of unlock condition is satisfied.
 *
 * @author shinoow
 */
public interface IConditionProcessor {

    boolean processUnlock(IUnlockCondition condition, INecroDataCapability cap, Player player);
}
