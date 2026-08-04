"""Write and register the Dreadguard and Skeleton Goliath.

Kept as a script rather than inline heredocs: the Java contains nested quotes a shell heredoc
mangles.
"""
import pathlib

DREADGUARD = '''/*******************************************************************************
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
package com.shinoow.abyssalcraft.common.entity;

import com.shinoow.abyssalcraft.api.entity.IDreadEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** The Dreadguard, a tall Dreadlands elite. Moves at normal speed rather than the shamble. */
public class Dreadguard extends ACMonster implements IDreadEntity {

    public Dreadguard(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.MAX_HEALTH, 120.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 240.0D;
    }

    @Override
    protected double getHardcoreDamage() {
        return 20.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
'''

GOLIATH = '''/*******************************************************************************
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
package com.shinoow.abyssalcraft.common.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Skeleton Goliath: a 4.5-block-tall undead that flees daylight and hits harder than anything
 * else at its tier.
 */
public class SkeletonGoliath extends ACMonster {

    public SkeletonGoliath(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 200.0D;
    }

    @Override
    protected double getHardcoreDamage() {
        return 40.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(5, new RestrictSunGoal(this));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, AbyssalZombie.class, 8.0F));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Zombie.class, 8.0F));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, DepthsGhoul.class, 8.0F));
        goalSelector.addGoal(7, new LookAtPlayerGoal(this, Skeleton.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
'''

RENDERER = '''/*******************************************************************************
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
package com.shinoow.abyssalcraft.client.render.entity;

import com.shinoow.abyssalcraft.AbyssalCraft;
import com.shinoow.abyssalcraft.common.entity.{name};

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the {name}.
 * <p>
 * 1.12.2 drew this with the vanilla biped model, so the vanilla layers are reused rather than
 * duplicating that geometry. The shadow radius matches the mob's footprint.
 */
public class {name}Renderer extends MobRenderer<{name}, HumanoidModel<{name}>> {{

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/{texture}.png");

    public {name}Renderer(EntityRendererProvider.Context context) {{
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), {shadow}F);
    }}

    @Override
    public ResourceLocation getTextureLocation({name} entity) {{
        return TEXTURE;
    }}
}}
'''

MOBS = [
    {"name": "Dreadguard", "source": DREADGUARD, "id": "dreadguard",
     "constant": "DREADGUARD", "size": "1.0F, 3.0F",
     "texture": "elite/dread_guard", "shadow": "0.8"},
    {"name": "SkeletonGoliath", "source": GOLIATH, "id": "skeletongoliath",
     "constant": "SKELETON_GOLIATH", "size": "1.0F, 4.5F",
     "texture": "elite/skeletongoliath", "shadow": "1.0"},
]

entity_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity")
render_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/client/render/entity")
for mob in MOBS:
    (entity_dir / f"{mob['name']}.java").write_text(mob["source"], encoding="utf-8")
    (render_dir / f"{mob['name']}Renderer.java").write_text(
        RENDERER.format(**mob), encoding="utf-8"
    )
    print(f"wrote {mob['name']} and its renderer")


def edit(path, replacements):
    file = pathlib.Path(path)
    text = file.read_text(encoding="utf-8")
    for old, new in replacements:
        if old not in text:
            raise SystemExit(f"anchor not found in {path}: {old[:70]}")
        text = text.replace(old, new, 1)
    file.write_text(text, encoding="utf-8")


types = "".join(
    f'    public static final RegistryObject<EntityType<{m["name"]}>> {m["constant"]} =\n'
    f'            monster("{m["id"]}", {m["name"]}::new, {m["size"]});\n'
    for m in MOBS
)
attributes = "".join(
    f'        event.put({m["constant"]}.get(), {m["name"]}.createAttributes().build());\n'
    for m in MOBS
)
imports = "".join(f"import com.shinoow.abyssalcraft.common.entity.{m['name']};\n" for m in MOBS)

edit(
    "src/main/java/com/shinoow/abyssalcraft/init/ACEntities.java",
    [
        ("    private ACEntities() {}", types + "\n    private ACEntities() {}"),
        (
            "    public static void registerAttributes(EntityAttributeCreationEvent event) {\n",
            "    public static void registerAttributes(EntityAttributeCreationEvent event) {\n" + attributes,
        ),
        ("import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;", imports + "import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;"),
    ],
)

renderers = "".join(
    f'        event.registerEntityRenderer(ACEntities.{m["constant"]}.get(), {m["name"]}Renderer::new);\n'
    for m in MOBS
)
render_imports = "".join(
    f"import com.shinoow.abyssalcraft.client.render.entity.{m['name']}Renderer;\n" for m in MOBS
)
edit(
    "src/main/java/com/shinoow/abyssalcraft/client/ACClientSetup.java",
    [
        (
            "        event.registerEntityRenderer(ACEntities.DEPTHS_GHOUL.get(), DepthsGhoulRenderer::new);",
            "        event.registerEntityRenderer(ACEntities.DEPTHS_GHOUL.get(), DepthsGhoulRenderer::new);\n"
            + renderers.rstrip("\n"),
        ),
        (
            "import com.shinoow.abyssalcraft.client.render.entity.AbyssalZombieRenderer;",
            render_imports + "import com.shinoow.abyssalcraft.client.render.entity.AbyssalZombieRenderer;",
        ),
    ],
)

print(f"registered {len(MOBS)} elite mobs")
