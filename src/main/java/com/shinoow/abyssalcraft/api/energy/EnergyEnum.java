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

import net.minecraft.network.chat.Component;

/**
 * Collection of Enums used by various parts of the Potential Energy system.
 * <p>
 * The 1.12.2 version exposed {@code addAmplifierType} and {@code addDeityType}, which used
 * {@code EnumHelper} to splice new constants into these enums at runtime. That mechanism relied on
 * reflective final-field rewriting and no longer works on Java 17, so those two methods are gone.
 * Localisation also moves from the removed server-side {@code I18n} to {@link Component}.
 *
 * @author shinoow
 *
 * @since 1.5
 */
public class EnergyEnum {

    private EnergyEnum() {}

    /**
     * Amplifier Types<br>
     * RANGE - Range at which the amplified thing can transfer<br>
     * DURATION - Amount of time it takes to transfer<br>
     * POWER - Boost in amount of power transferred
     *
     * @since 1.5
     */
    public enum AmplifierType {

        RANGE("range"), DURATION("duration"), POWER("power");

        private final String unlocalizedName;

        AmplifierType(String name) {
            unlocalizedName = name;
        }

        /** Getter for the Amplifier's translation key. */
        public String getUnlocalizedName() {
            return "ac.amplifier." + unlocalizedName;
        }

        /** Getter for the Amplifier's display name. */
        public Component getDisplayName() {
            return Component.translatable(getUnlocalizedName());
        }
    }

    /**
     * Deity Types. The seven deities the Potential Energy system can be attuned to.
     *
     * @since 1.5
     */
    public enum DeityType {

        CTHULHU("Cthulhu"), HASTUR("Hastur"), JZAHAR("J'zahar"),
        AZATHOTH("Azathoth"), NYARLATHOTEP("Nyarlathotep"),
        SHUBNIGGURATH("Shub-Niggurath"), YOGSOTHOTH("Yog-Sothoth");

        private final String name;

        DeityType(String name) {
            this.name = name;
        }

        /** Getter for the Deity's name. Deity names are proper nouns and are not translated. */
        public String getName() {
            return name;
        }
    }

    /**
     * Fetches the display name of an AmplifierType.
     *
     * @param type Type to fetch the name from
     * @return The amplifier's name, or a localized "None" when the type is null
     *
     * @since 1.5
     */
    public static Component getAmplifierName(AmplifierType type) {
        return type == null ? Component.translatable("ac.text.none") : type.getDisplayName();
    }

    /**
     * Fetches the name of a DeityType.
     *
     * @param type Type to fetch the name from
     * @return The deity's name, or a localized "None" when the type is null
     *
     * @since 1.5
     */
    public static Component getDeityName(DeityType type) {
        return type == null ? Component.translatable("ac.text.none") : Component.literal(type.getName());
    }
}
