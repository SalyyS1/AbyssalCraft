"""Write and register three more anti-mobs: Anti Abyssal Zombie, Anti Ghoul and Anti Spider.

Kept as a script rather than inline heredocs: the Java contains nested quotes a shell heredoc
mangles.
"""
import pathlib

ANTI_ABYSSAL_ZOMBIE = '''/*******************************************************************************
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
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** The Anti Abyssal Zombie: the antimatter counterpart of the Abyssal Zombie. */
public class AntiAbyssalZombie extends ACMonster implements IAntiEntity {

    public AntiAbyssalZombie(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 42.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 100.0D;
    }

    @Override
    protected double getHardcoreDamage() {
        return 12.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, AntiAbyssalZombie.class, 8.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, AntiZombie.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
'''

ANTI_GHOUL = '''/*******************************************************************************
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
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** The Anti Ghoul: the antimatter counterpart of the Depths Ghoul. */
public class AntiGhoul extends ACMonster implements IAntiEntity {

    public AntiGhoul(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 42.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 120.0D;
    }

    @Override
    protected double getHardcoreDamage() {
        return 10.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, AntiGhoul.class, 8.0F));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, AntiAbyssalZombie.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
'''

ANTI_SPIDER = '''/*******************************************************************************
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
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * The Anti Spider: the antimatter counterpart of the vanilla spider.
 * <p>
 * It leaps at its target rather than using a melee goal, matching 1.12.2, and 1.12.2 set no attack
 * damage on it at all, so it keeps the vanilla monster default.
 */
public class AntiSpider extends ACMonster implements IAntiEntity {

    public AntiSpider(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
                .add(Attributes.MAX_HEALTH, 32.0D);
    }

    @Override
    protected double getHardcoreHealth() {
        return 64.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new FloatGoal(this));
        goalSelector.addGoal(3, new LeapAtTargetGoal(this, 0.4F));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
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
import com.shinoow.abyssalcraft.common.entity.anti.{name};

import net.minecraft.client.model.{model}Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the {name}.
 * <p>
 * 1.12.2 drew this with the matching vanilla model, so the vanilla layer is reused.
 */
public class {name}Renderer extends MobRenderer<{name}, {model}Model<{name}>> {{

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/{texture}.png");

    public {name}Renderer(EntityRendererProvider.Context context) {{
        super(context, new {model}Model<>(context.bakeLayer(ModelLayers.{layer})), {shadow}F);
    }}

    @Override
    public ResourceLocation getTextureLocation({name} entity) {{
        return TEXTURE;
    }}
}}
'''

MOBS = [
    {"name": "AntiAbyssalZombie", "source": ANTI_ABYSSAL_ZOMBIE, "id": "antiabyssalzombie",
     "constant": "ANTI_ABYSSAL_ZOMBIE", "size": "0.6F, 1.8F", "texture": "anti/abyssal_zombie",
     "model": "Humanoid", "layer": "PLAYER", "shadow": "0.5"},
    {"name": "AntiGhoul", "source": ANTI_GHOUL, "id": "antighoul",
     "constant": "ANTI_GHOUL", "size": "1.0F, 3.0F", "texture": "anti/depths_ghoul",
     "model": "Humanoid", "layer": "PLAYER", "shadow": "0.8"},
    {"name": "AntiSpider", "source": ANTI_SPIDER, "id": "antispider",
     "constant": "ANTI_SPIDER", "size": "1.4F, 0.9F", "texture": "anti/spider",
     "model": "Spider", "layer": "SPIDER", "shadow": "0.7"},
]

entity_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity/anti")
render_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/client/render/entity")
for mob in MOBS:
    (entity_dir / f"{mob['name']}.java").write_text(mob["source"], encoding="utf-8")
    (render_dir / f"{mob['name']}Renderer.java").write_text(RENDERER.format(**mob), encoding="utf-8")
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
        (
            "import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;",
            imports + "import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;",
        ),
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
