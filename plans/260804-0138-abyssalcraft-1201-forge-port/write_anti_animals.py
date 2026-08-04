"""Write and register the three breedable anti-animals: cow, pig and chicken.

Kept as a script rather than inline heredocs: the Java contains nested quotes a shell heredoc
mangles, and the three classes share one shape apart from stats, breeding item and tempt speed.
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
package com.shinoow.abyssalcraft.common.entity.anti;

import com.shinoow.abyssalcraft.api.entity.IAntiEntity;
import com.shinoow.abyssalcraft.init.ACEntities;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

/**
 * {javadoc}
 * <p>
 * Matter's opposite number to the vanilla animal, and breedable in the same way. Goal priorities,
 * speeds and the tempt item are carried over from 1.12.2 unchanged.
 */
public class {name} extends Animal implements IAntiEntity {{

    private static final Ingredient BREEDING_ITEMS = Ingredient.of({breeding_items});

    public {name}(EntityType<? extends Animal> type, Level level) {{
        super(type, level);
    }}

    public static AttributeSupplier.Builder createAttributes() {{
        // createLivingAttributes omits FOLLOW_RANGE, which the tempt and follow-parent goals
        // need; vanilla animals add it themselves, so the 16-block default is declared here.
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, {health}D)
                .add(Attributes.MOVEMENT_SPEED, {speed}D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }}

    @Override
    protected void registerGoals() {{
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new PanicGoal(this, {panic_speed}D));
        goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        goalSelector.addGoal(3, new TemptGoal(this, {tempt_speed}D, BREEDING_ITEMS, false));
        goalSelector.addGoal(4, new FollowParentGoal(this, {follow_speed}F));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }}

    @Override
    public boolean isFood(ItemStack stack) {{
        return BREEDING_ITEMS.test(stack);
    }}

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {{
        return ACEntities.{constant}.get().create(level);
    }}
}}
'''

ANIMALS = [
    {
        "name": "AntiCow",
        "javadoc": "The Anti Cow.",
        "health": "20.0",
        "speed": "0.20000000298023224",
        "panic_speed": "2.0",
        "tempt_speed": "1.25",
        "follow_speed": "1.25",
        "breeding_items": "net.minecraft.world.item.Items.WHEAT",
        "constant": "ANTI_COW",
        "id": "anticow",
        "size": "0.9F, 1.3F",
        "texture": "anti/cow",
    },
    {
        "name": "AntiPig",
        "javadoc": "The Anti Pig.",
        "health": "20.0",
        "speed": "0.25",
        "panic_speed": "1.25",
        "tempt_speed": "1.2",
        "follow_speed": "1.1",
        "breeding_items": "net.minecraft.world.item.Items.CARROT",
        "constant": "ANTI_PIG",
        "id": "antipig",
        "size": "0.9F, 0.9F",
        "texture": "anti/pig",
    },
    {
        "name": "AntiChicken",
        "javadoc": "The Anti Chicken.",
        "health": "8.0",
        "speed": "0.25",
        "panic_speed": "1.4",
        "tempt_speed": "1.0",
        "follow_speed": "1.1",
        "breeding_items": "net.minecraft.world.item.Items.WHEAT_SEEDS",
        "constant": "ANTI_CHICKEN",
        "id": "antichicken",
        "size": "0.3F, 0.7F",
        "texture": "anti/chicken",
    },
]

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
 * 1.12.2 drew the anti-animals with the matching vanilla animal model, so the vanilla layer is
 * reused rather than duplicating that geometry.
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

RENDER_INFO = {
    "AntiCow": {"model": "Cow", "layer": "COW", "shadow": "0.7"},
    "AntiPig": {"model": "Pig", "layer": "PIG", "shadow": "0.7"},
    "AntiChicken": {"model": "Chicken", "layer": "CHICKEN", "shadow": "0.3"},
}

entity_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity/anti")
render_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/client/render/entity")
for animal in ANIMALS:
    (entity_dir / f"{animal['name']}.java").write_text(ENTITY.format(**animal), encoding="utf-8")
    info = dict(animal, **RENDER_INFO[animal["name"]])
    (render_dir / f"{animal['name']}Renderer.java").write_text(
        RENDERER.format(**info), encoding="utf-8"
    )
    print(f"wrote {animal['name']} and its renderer")


def edit(path, replacements):
    file = pathlib.Path(path)
    text = file.read_text(encoding="utf-8")
    for old, new in replacements:
        if old not in text:
            raise SystemExit(f"anchor not found in {path}: {old[:70]}")
        text = text.replace(old, new, 1)
    file.write_text(text, encoding="utf-8")


types = "".join(
    f'    public static final RegistryObject<EntityType<{a["name"]}>> {a["constant"]} =\n'
    f'            animal("{a["id"]}", {a["name"]}::new, {a["size"]});\n'
    for a in ANIMALS
)
attributes = "".join(
    f'        event.put({a["constant"]}.get(), {a["name"]}.createAttributes().build());\n'
    for a in ANIMALS
)
imports = "".join(
    f"import com.shinoow.abyssalcraft.common.entity.anti.{a['name']};\n" for a in ANIMALS
)

# Animals spawn in the CREATURE category, not MONSTER, so they need their own factory.
animal_factory = '''
    private static <T extends Animal> RegistryObject<EntityType<T>> animal(String name,
            EntityType.EntityFactory<T> factory, float width, float height) {
        return ACRegistries.ENTITIES.register(name, () -> EntityType.Builder
                .of(factory, MobCategory.CREATURE)
                .sized(width, height)
                .clientTrackingRange(4)
                .build(AbyssalCraft.MOD_ID + ":" + name));
    }
'''

edit(
    "src/main/java/com/shinoow/abyssalcraft/init/ACEntities.java",
    [
        ("    private ACEntities() {}", types + "\n    private ACEntities() {}"),
        (
            "    public static void registerAttributes(EntityAttributeCreationEvent event) {\n",
            animal_factory + "\n    public static void registerAttributes(EntityAttributeCreationEvent event) {\n" + attributes,
        ),
        (
            "import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;",
            imports + "import com.shinoow.abyssalcraft.common.entity.AbyssalZombie;",
        ),
        (
            "import net.minecraft.world.entity.monster.Monster;",
            "import net.minecraft.world.entity.animal.Animal;\nimport net.minecraft.world.entity.monster.Monster;",
        ),
    ],
)

renderers = "".join(
    f'        event.registerEntityRenderer(ACEntities.{a["constant"]}.get(), {a["name"]}Renderer::new);\n'
    for a in ANIMALS
)
render_imports = "".join(
    f"import com.shinoow.abyssalcraft.client.render.entity.{a['name']}Renderer;\n" for a in ANIMALS
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

print(f"registered {len(ANIMALS)} anti-animals")
