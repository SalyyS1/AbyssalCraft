"""Write and register three Omothol mobs: Omothol Ghoul, Gatekeeper Minion and Shadow Beast.

Kept as a script rather than inline heredocs: the Java contains nested quotes a shell heredoc
mangles, and the three classes share one shape apart from their stats and AI priorities.
"""
import pathlib

ENTITY = '''/*******************************************************************************
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

import com.shinoow.abyssalcraft.api.entity.IOmotholEntity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
{sun_import}import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/** {javadoc} */
public class {name} extends ACMonster implements IOmotholEntity {{

    public {name}(EntityType<? extends Monster> type, Level level) {{
        super(type, level);
    }}

    public static AttributeSupplier.Builder createAttributes() {{
        return Monster.createMonsterAttributes()
{attributes}    }}

    @Override
    protected double getHardcoreHealth() {{
        return {hardcore_health}D;
    }}

    @Override
    protected double getHardcoreDamage() {{
        return {hardcore_damage}D;
    }}

    @Override
    protected void registerGoals() {{
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.35D, {melee_pursuit}));
        goalSelector.addGoal(3, new MoveTowardsRestrictionGoal(this, 0.35D));
        goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.35D));
{extra_goals}        goalSelector.addGoal({look_priority}, new RandomLookAroundGoal(this));
        goalSelector.addGoal({look_priority}, new LookAtPlayerGoal(this, Player.class, 8.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }}
}}
'''

MOBS = [
    {
        "name": "OmotholGhoul",
        "javadoc": "The Omothol Ghoul, a heavy melee mob from Omothol.",
        "attributes": "                .add(Attributes.FOLLOW_RANGE, 64.0D)\n"
                      "                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)\n"
                      "                .add(Attributes.MAX_HEALTH, 150.0D)\n"
                      "                .add(Attributes.ATTACK_DAMAGE, 15.0D);\n",
        "hardcore_health": "300.0",
        "hardcore_damage": "30.0",
        "melee_pursuit": "false",
        "extra_goals": "",
        "look_priority": "7",
        "sun_import": "",
        "id": "omotholghoul",
        "constant": "OMOTHOL_GHOUL",
        "size": "1.3F, 3.7F",
        "texture": "omothol_ghoul",
    },
    {
        "name": "GatekeeperMinion",
        "javadoc": "The Minion of the Gatekeeper, the toughest of the regular Omothol mobs.",
        "attributes": "                .add(Attributes.FOLLOW_RANGE, 64.0D)\n"
                      "                .add(Attributes.KNOCKBACK_RESISTANCE, 0.2D)\n"
                      "                .add(Attributes.MAX_HEALTH, 250.0D)\n"
                      "                .add(Attributes.ATTACK_DAMAGE, 18.0D);\n",
        "hardcore_health": "500.0",
        "hardcore_damage": "36.0",
        "melee_pursuit": "false",
        "extra_goals": "",
        "look_priority": "7",
        "sun_import": "",
        "id": "gatekeeperminion",
        "constant": "GATEKEEPER_MINION",
        "size": "0.8F, 2.7F",
        "texture": "elite/gatekeeperminion",
    },
    {
        "name": "ShadowBeast",
        "javadoc": "The Shadow Beast, the largest of the shadow mobs. It flees daylight.",
        "attributes": "                .add(Attributes.KNOCKBACK_RESISTANCE, 0.3D)\n"
                      "                .add(Attributes.MAX_HEALTH, 100.0D)\n"
                      "                .add(Attributes.ATTACK_DAMAGE, 10.0D);\n",
        "hardcore_health": "200.0",
        "hardcore_damage": "20.0",
        "melee_pursuit": "true",
        "extra_goals": "        goalSelector.addGoal(5, new RestrictSunGoal(this));\n",
        "look_priority": "6",
        "sun_import": "import net.minecraft.world.entity.ai.goal.RestrictSunGoal;\n",
        "id": "shadowbeast",
        "constant": "SHADOW_BEAST",
        "size": "1.0F, 2.8F",
        "texture": "elite/shadowbeast",
    },
]

entity_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity")
for mob in MOBS:
    (entity_dir / f"{mob['name']}.java").write_text(ENTITY.format(**mob), encoding="utf-8")
    print(f"wrote {mob['name']}.java")

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
 * 1.12.2 drew these Omothol mobs with the vanilla biped model, so the vanilla layers are reused
 * rather than duplicating that geometry.
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

render_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/client/render/entity")
for mob in MOBS:
    (render_dir / f"{mob['name']}Renderer.java").write_text(RENDERER.format(**mob), encoding="utf-8")
    print(f"wrote {mob['name']}Renderer.java")


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

print(f"registered {len(MOBS)} Omothol mobs")
