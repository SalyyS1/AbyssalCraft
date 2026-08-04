"""Write and register the eight demon and evil animals.

Kept as a script rather than inline heredocs: the Java contains nested quotes a shell heredoc
mangles, and eight near-identical classes are far better generated than hand-copied.
"""
import pathlib

BASE = '''/*******************************************************************************
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
package com.shinoow.abyssalcraft.common.entity.demon;

import com.shinoow.abyssalcraft.common.entity.ACMonster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * Shared base for the corrupted farm animals.
 * <p>
 * Despite looking like animals these are hostile mobs, which is why they extend Monster rather than
 * Animal: on 1.12.2 they extended EntityMob for the same reason, and they neither breed nor follow
 * a parent. All of them shamble at the 0.35 speed modifier and hit for 4 in hardcore mode; only
 * their health differs, so subclasses declare just that.
 */
public abstract class AbstractCorruptedAnimal extends ACMonster {

    /** Movement and attack speed shared by every corrupted animal. */
    protected static final double SPEED = 0.35D;

    protected AbstractCorruptedAnimal(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    /** Damage in hardcore mode is 4 for every one of them. */
    @Override
    protected double getHardcoreDamage() {
        return 4.0D;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, SPEED, true));
        goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, SPEED));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
}
'''

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
package com.shinoow.abyssalcraft.common.entity.demon;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/** The {display}. */
public class {name} extends AbstractCorruptedAnimal {{

    public {name}(EntityType<? extends Monster> type, Level level) {{
        super(type, level);
    }}

    public static AttributeSupplier.Builder createAttributes() {{
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, SPEED)
                .add(Attributes.MAX_HEALTH, {health}D);
    }}

    @Override
    protected double getHardcoreHealth() {{
        return {hardcore_health}D;
    }}
}}
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

{mod_import}import com.shinoow.abyssalcraft.common.entity.demon.{name};

import net.minecraft.client.model.{model}Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the {display}.
 * <p>
 * 1.12.2 drew these with the matching vanilla animal model, so the vanilla layer is reused rather
 * than duplicating that geometry. The demon animals ship their own sprite; the evil ones reuse
 * the vanilla animal texture, which is why that reference has no namespace.
 */
public class {name}Renderer extends MobRenderer<{name}, {model}Model<{name}>> {{

    private static final ResourceLocation TEXTURE = {texture_expr};

    public {name}Renderer(EntityRendererProvider.Context context) {{
        super(context, new {model}Model<>(context.bakeLayer(ModelLayers.{layer})), {shadow}F);
    }}

    @Override
    public ResourceLocation getTextureLocation({name} entity) {{
        return TEXTURE;
    }}
}}
'''

# health is the non-hardcore value; hardcore_health is what 1.12.2 raised it to.
MOBS = [
    {"name": "DemonCow", "display": "Demon Cow", "health": "15.0", "hardcore_health": "30.0",
     "id": "demoncow", "constant": "DEMON_COW", "size": "0.9F, 1.3F",
     "model": "Cow", "layer": "COW", "shadow": "0.7", "texture_expr": 'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_cow.png")', "mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\n"},
    {"name": "DemonPig", "display": "Demon Pig", "health": "15.0", "hardcore_health": "30.0",
     "id": "demonpig", "constant": "DEMON_PIG", "size": "0.9F, 0.9F",
     "model": "Pig", "layer": "PIG", "shadow": "0.7", "texture_expr": 'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_pig.png")', "mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\n"},
    {"name": "DemonChicken", "display": "Demon Chicken", "health": "10.0", "hardcore_health": "20.0",
     "id": "demonchicken", "constant": "DEMON_CHICKEN", "size": "0.3F, 0.7F",
     "model": "Chicken", "layer": "CHICKEN", "shadow": "0.3", "texture_expr": 'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_chicken.png")', "mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\n"},
    {"name": "DemonSheep", "display": "Demon Sheep", "health": "12.0", "hardcore_health": "24.0",
     "id": "demonsheep", "constant": "DEMON_SHEEP", "size": "0.9F, 1.3F",
     "model": "Cow", "layer": "SHEEP", "shadow": "0.7", "texture_expr": 'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_sheep.png")', "mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\n"},
    {"name": "EvilCow", "display": "Evil Cow", "health": "15.0", "hardcore_health": "30.0",
     "id": "evilcow", "constant": "EVIL_COW", "size": "0.9F, 1.3F",
     "model": "Cow", "layer": "COW", "shadow": "0.7", "texture_expr": 'new ResourceLocation("textures/entity/cow/cow.png")', "mod_import": ""},
    {"name": "EvilPig", "display": "Evil Pig", "health": "15.0", "hardcore_health": "30.0",
     "id": "evilpig", "constant": "EVIL_PIG", "size": "0.9F, 0.9F",
     "model": "Pig", "layer": "PIG", "shadow": "0.7", "texture_expr": 'new ResourceLocation("textures/entity/pig/pig.png")', "mod_import": ""},
    {"name": "EvilChicken", "display": "Evil Chicken", "health": "10.0", "hardcore_health": "20.0",
     "id": "evilchicken", "constant": "EVIL_CHICKEN", "size": "0.3F, 0.7F",
     "model": "Chicken", "layer": "CHICKEN", "shadow": "0.3", "texture_expr": 'new ResourceLocation("textures/entity/chicken.png")', "mod_import": ""},
    {"name": "EvilSheep", "display": "Evil Sheep", "health": "12.0", "hardcore_health": "24.0",
     "id": "evilsheep", "constant": "EVIL_SHEEP", "size": "0.9F, 1.3F",
     "model": "Cow", "layer": "SHEEP", "shadow": "0.7", "texture_expr": 'new ResourceLocation("textures/entity/sheep/sheep.png")', "mod_import": ""},
]

entity_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity/demon")
entity_dir.mkdir(parents=True, exist_ok=True)
render_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/client/render/entity")

(entity_dir / "AbstractCorruptedAnimal.java").write_text(BASE, encoding="utf-8")
print("wrote AbstractCorruptedAnimal.java")

for mob in MOBS:
    (entity_dir / f"{mob['name']}.java").write_text(ENTITY.format(**mob), encoding="utf-8")
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
    f"import com.shinoow.abyssalcraft.common.entity.demon.{m['name']};\n" for m in MOBS
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

print(f"registered {len(MOBS)} corrupted animals")
