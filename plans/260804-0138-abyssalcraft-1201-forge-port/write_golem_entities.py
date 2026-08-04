"""Write the two golem entity classes.

Kept as a script rather than an inline heredoc: the Java contains nested quotes a shell heredoc
mangles, and the two classes are near-identical apart from their rival and marker interface.
"""
import pathlib

TEMPLATE = '''/*******************************************************************************
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

{marker_import}
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

/** {javadoc} */
public class {name} extends AbstractGolem{implements_clause} {{

    public {name}(EntityType<? extends Monster> type, Level level) {{
        super(type, level);
    }}

    @Override
    protected Class<? extends Monster> getRivalGolem() {{
        return {rival}.class;
    }}
}}
'''

GOLEMS = [
    {
        "name": "Abyssalnite Golem".replace(" ", ""),
        "rival": "DreadGolem",
        "marker_import": "",
        "implements_clause": "",
        "javadoc": "The Abyssalnite Golem. It hunts Dread Golems on sight, as it did on 1.12.2.",
    },
    {
        "name": "DreadGolem",
        "rival": "AbyssalniteGolem",
        "marker_import": "import com.shinoow.abyssalcraft.api.entity.IDreadEntity;\n",
        "implements_clause": " implements IDreadEntity",
        "javadoc": "The Dread Golem. It hunts Abyssalnite Golems on sight, as it did on 1.12.2.",
    },
]

target = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/common/entity")
for golem in GOLEMS:
    path = target / f"{golem['name']}.java"
    path.write_text(TEMPLATE.format(**golem), encoding="utf-8")
    print(f"wrote {path.name}")
