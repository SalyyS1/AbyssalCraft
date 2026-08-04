"""Write and register the Anti Zombie and Anti Skeleton.

Kept as a script rather than inline heredocs: the Java contains nested quotes a shell heredoc
mangles.
"""
import pathlib

ANTI_ZOMBIE = '''/*******************************************************************************
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
package com.shinoow.abyssalcraft.common.entity.anti;

import com.shinoow.abyssalcraft.api.entity.IAntiEntity;
import com.shinoow.abyssalcraft.common.entity.ACMonster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Anti Zombie: matter's opposite number to the vanilla zombie, which it hunts.
 * <p>
 * 1.12.2 also gave it the vanilla zombie reinforcement attribute, seeded randomly per spawn. That
 * attribute belongs to Zombie rather than Monster on 1.20.1 and this extends Monster, so
 * reinforcements are absent; the mob is otherwise unchanged, including the follow range that
 * doubles in hardcore mode.
 */
public class AntiZombie extends ACMonster implements IAntiEntity {

    public AntiZombie(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected double getHardcoreDamage() {
        return 6.0D;
    }

    @Override
    protected double getHardcoreFollowRange() {
        return 80.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, false, 4, () -> false));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Zombie.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
'''

ANTI_SKELETON = '''/*******************************************************************************
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
package com.shinoow.abyssalcraft.common.entity.anti;

import com.shinoow.abyssalcraft.api.entity.IAntiEntity;
import com.shinoow.abyssalcraft.common.entity.ACMonster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Anti Skeleton: matter's opposite number to the vanilla skeleton, which it hunts.
 * <p>
 * It has no melee goal, matching 1.12.2, where it relied on its bow; the ranged attack lands with
 * the projectile phase. Hardcore mode doubles its health rather than its damage.
 */
public class AntiSkeleton extends ACMonster implements IAntiEntity {

    public AntiSkeleton(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.MAX_HEALTH, 40.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 80.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(2, new RestrictSunGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Skeleton.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
import com.shinoow.abyssalcraft.common.entity.anti.{name};

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the {name}.
 * <p>
 * 1.12.2 drew the anti-mobs with the vanilla biped model, so the vanilla layers are reused.
 */
public class {name}Renderer extends MobRenderer<{name}, HumanoidModel<{name}>> {{

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/{texture}.png");

    public {name}Renderer(EntityRendererProvider.Context context) {{
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }}

    @Override
    public ResourceLocation getTextureLocation({name} entity) {{
        return TEXTURE;
    }}
}}
'''

MOBS = [
    {"name": "AntiZombie", "source": ANTI_ZOMBIE, "id": "antizombie",
     "constant": "ANTI_ZOMBIE", "size": "0.6F, 1.8F", "texture": "anti/zombie"},
    {"name": "AntiSkeleton", "source": ANTI_SKELETON, "id": "antiskeleton",
     "constant": "ANTI_SKELETON", "size": "0.6F, 1.99F", "texture": "anti/skeleton"},
]

entity_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity/anti")
entity_dir.mkdir(parents=True, exist_ok=True)
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
imports = "".join(
    f"import com.shinoow.abyssalcraft.common.entity.anti.{m['name']};\n" for m in MOBS
)

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

print(f"registered {len(MOBS)} anti-mobs")
