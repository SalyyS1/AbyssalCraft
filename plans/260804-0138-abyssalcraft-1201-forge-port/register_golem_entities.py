"""Register the two golems: entity types, attributes, renderers and spawn-egg-free wiring.

Kept as a script rather than an inline heredoc: the Java snippets contain nested quotes that a
shell heredoc mangles.
"""
import pathlib

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
import com.shinoow.abyssalcraft.common.entity.{entity};

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the {display}.
 * <p>
 * 1.12.2 drew this with the vanilla biped model, so the vanilla player layers are reused rather
 * than duplicating that geometry.
 */
public class {entity}Renderer extends MobRenderer<{entity}, HumanoidModel<{entity}>> {{

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/{texture}.png");

    public {entity}Renderer(EntityRendererProvider.Context context) {{
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
    }}

    @Override
    public ResourceLocation getTextureLocation({entity} entity) {{
        return TEXTURE;
    }}
}}
'''

GOLEMS = [
    {"entity": "AbyssalniteGolem", "display": "Abyssalnite Golem", "texture": "aby_warden",
     "id": "abygolem", "constant": "ABYSSALNITE_GOLEM"},
    {"entity": "DreadGolem", "display": "Dread Golem", "texture": "dread_warden",
     "id": "dreadgolem", "constant": "DREAD_GOLEM"},
]

render_dir = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/client/render/entity")
for golem in GOLEMS:
    path = render_dir / f"{golem['entity']}Renderer.java"
    path.write_text(RENDERER.format(**golem), encoding="utf-8")
    print(f"wrote {path.name}")


def edit(path, replacements):
    file = pathlib.Path(path)
    text = file.read_text(encoding="utf-8")
    for old, new in replacements:
        if old not in text:
            raise SystemExit(f"anchor not found in {path}: {old[:70]}")
        text = text.replace(old, new, 1)
    file.write_text(text, encoding="utf-8")


types = "".join(
    f'    public static final RegistryObject<EntityType<{g["entity"]}>> {g["constant"]} =\n'
    f'            monster("{g["id"]}", {g["entity"]}::new, 0.6F, 1.8F);\n'
    for g in GOLEMS
)
attributes = "".join(
    f'        event.put({g["constant"]}.get(), {g["entity"]}.createGolemAttributes().build());\n'
    for g in GOLEMS
)
imports = "".join(
    f"import com.shinoow.abyssalcraft.common.entity.{g['entity']};\n" for g in GOLEMS
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
    f'        event.registerEntityRenderer(ACEntities.{g["constant"]}.get(), {g["entity"]}Renderer::new);\n'
    for g in GOLEMS
)
render_imports = "".join(
    f"import com.shinoow.abyssalcraft.client.render.entity.{g['entity']}Renderer;\n" for g in GOLEMS
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

print("registered both golems")
