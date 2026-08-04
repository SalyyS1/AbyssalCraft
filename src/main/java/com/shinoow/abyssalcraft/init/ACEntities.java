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

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.common.entity.AbyssalniteGolem;
import com.shinoow.abyssalcraft.common.entity.DreadGolem;
import com.shinoow.abyssalcraft.common.entity.OmotholGhoul;
import com.shinoow.abyssalcraft.common.entity.GatekeeperMinion;
import com.shinoow.abyssalcraft.common.entity.ShadowBeast;
import com.shinoow.abyssalcraft.common.entity.Dreadguard;
import com.shinoow.abyssalcraft.common.entity.SkeletonGoliath;
import com.shinoow.abyssalcraft.common.entity.anti.AntiZombie;
import com.shinoow.abyssalcraft.common.entity.anti.AntiSkeleton;
import com.shinoow.abyssalcraft.common.entity.anti.AntiCow;
import com.shinoow.abyssalcraft.common.entity.anti.AntiPig;
import com.shinoow.abyssalcraft.common.entity.anti.AntiChicken;
import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;
import com.shinoow.abyssalcraft.common.entity.DepthsGhoul;
import com.shinoow.abyssalcraft.common.entity.DreadSpawn;
import com.shinoow.abyssalcraft.common.entity.Dreadling;
import com.shinoow.abyssalcraft.common.entity.ShadowCreature;
import com.shinoow.abyssalcraft.common.entity.ShadowMonster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.RegistryObject;

/**
 * Entity types.
 * <p>
 * 1.12.2 registered entities by class and a mod-local numeric ID through
 * {@code EntityRegistry.registerModEntity}, with spawn-egg colours passed in at the same time.
 * 1.20.1 needs an {@link EntityType} built with its category and hitbox size, attributes supplied
 * separately via {@link EntityAttributeCreationEvent}, and spawn eggs as ordinary registered items.
 * Hitbox sizes come from the old {@code setSize} calls.
 */
public final class ACEntities {

    public static final RegistryObject<EntityType<DepthsGhoul>> DEPTHS_GHOUL =
            monster("depthsghoul", DepthsGhoul::new, 1.0F, 3.0F);
    public static final RegistryObject<EntityType<AbyssalZombie>> ABYSSAL_ZOMBIE =
            monster("abyssalzombie", AbyssalZombie::new, 0.6F, 1.8F);
    public static final RegistryObject<EntityType<Dreadling>> DREADLING =
            monster("dreadling", Dreadling::new, 0.8F, 1.5F);
    public static final RegistryObject<EntityType<DreadSpawn>> DREAD_SPAWN =
            monster("dreadspawn", DreadSpawn::new, 0.6F, 0.6F);
    public static final RegistryObject<EntityType<ShadowCreature>> SHADOW_CREATURE =
            monster("shadowcreature", ShadowCreature::new, 0.5F, 1.0F);
    /** 1.12.2 never called setSize on the Shadow Monster, leaving the vanilla 0.6x1.8 default. */
    public static final RegistryObject<EntityType<ShadowMonster>> SHADOW_MONSTER =
            monster("shadowmonster", ShadowMonster::new, 0.6F, 1.8F);

    public static final RegistryObject<EntityType<AbyssalniteGolem>> ABYSSALNITE_GOLEM =
            monster("abygolem", AbyssalniteGolem::new, 0.6F, 1.8F);
    public static final RegistryObject<EntityType<DreadGolem>> DREAD_GOLEM =
            monster("dreadgolem", DreadGolem::new, 0.6F, 1.8F);

    public static final RegistryObject<EntityType<OmotholGhoul>> OMOTHOL_GHOUL =
            monster("omotholghoul", OmotholGhoul::new, 1.3F, 3.7F);
    public static final RegistryObject<EntityType<GatekeeperMinion>> GATEKEEPER_MINION =
            monster("gatekeeperminion", GatekeeperMinion::new, 0.8F, 2.7F);
    public static final RegistryObject<EntityType<ShadowBeast>> SHADOW_BEAST =
            monster("shadowbeast", ShadowBeast::new, 1.0F, 2.8F);

    public static final RegistryObject<EntityType<Dreadguard>> DREADGUARD =
            monster("dreadguard", Dreadguard::new, 1.0F, 3.0F);
    public static final RegistryObject<EntityType<SkeletonGoliath>> SKELETON_GOLIATH =
            monster("skeletongoliath", SkeletonGoliath::new, 1.0F, 4.5F);

    public static final RegistryObject<EntityType<AntiZombie>> ANTI_ZOMBIE =
            monster("antizombie", AntiZombie::new, 0.6F, 1.8F);
    public static final RegistryObject<EntityType<AntiSkeleton>> ANTI_SKELETON =
            monster("antiskeleton", AntiSkeleton::new, 0.6F, 1.99F);

    public static final RegistryObject<EntityType<AntiCow>> ANTI_COW =
            animal("anticow", AntiCow::new, 0.9F, 1.3F);
    public static final RegistryObject<EntityType<AntiPig>> ANTI_PIG =
            animal("antipig", AntiPig::new, 0.9F, 0.9F);
    public static final RegistryObject<EntityType<AntiChicken>> ANTI_CHICKEN =
            animal("antichicken", AntiChicken::new, 0.3F, 0.7F);

    private ACEntities() {}

    private static <T extends Monster> RegistryObject<EntityType<T>> monster(String name,
            EntityType.EntityFactory<T> factory, float width, float height) {
        return ACRegistries.ENTITIES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.MONSTER)
                .sized(width, height)
                .clientTrackingRange(4)
                .build(AbyssalCraft.MOD_ID + ":" + name));
    }


    private static <T extends Animal> RegistryObject<EntityType<T>> animal(String name,
            EntityType.EntityFactory<T> factory, float width, float height) {
        return ACRegistries.ENTITIES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.CREATURE)
                .sized(width, height)
                .clientTrackingRange(4)
                .build(AbyssalCraft.MOD_ID + ":" + name));
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ANTI_COW.get(), AntiCow.createAttributes().build());
        event.put(ANTI_PIG.get(), AntiPig.createAttributes().build());
        event.put(ANTI_CHICKEN.get(), AntiChicken.createAttributes().build());
        event.put(ANTI_ZOMBIE.get(), AntiZombie.createAttributes().build());
        event.put(ANTI_SKELETON.get(), AntiSkeleton.createAttributes().build());
        event.put(DREADGUARD.get(), Dreadguard.createAttributes().build());
        event.put(SKELETON_GOLIATH.get(), SkeletonGoliath.createAttributes().build());
        event.put(OMOTHOL_GHOUL.get(), OmotholGhoul.createAttributes().build());
        event.put(GATEKEEPER_MINION.get(), GatekeeperMinion.createAttributes().build());
        event.put(SHADOW_BEAST.get(), ShadowBeast.createAttributes().build());
        event.put(ABYSSALNITE_GOLEM.get(), AbyssalniteGolem.createGolemAttributes().build());
        event.put(DREAD_GOLEM.get(), DreadGolem.createGolemAttributes().build());
        event.put(DEPTHS_GHOUL.get(), DepthsGhoul.createAttributes().build());
        event.put(ABYSSAL_ZOMBIE.get(), AbyssalZombie.createAttributes().build());
        event.put(DREADLING.get(), Dreadling.createAttributes().build());
        event.put(DREAD_SPAWN.get(), DreadSpawn.createAttributes().build());
        event.put(SHADOW_CREATURE.get(), ShadowCreature.createAttributes().build());
        event.put(SHADOW_MONSTER.get(), ShadowMonster.createAttributes().build());
    }

    /** Forces class initialisation so the fields above reach the DeferredRegister. */
    public static void register() {}
}
