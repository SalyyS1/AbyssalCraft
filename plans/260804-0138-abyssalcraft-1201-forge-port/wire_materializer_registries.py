"""Wire the Materializer into the block, block-entity, menu, screen and asset registries.

Kept as a script rather than an inline heredoc: the Java snippets contain nested quotes that a
shell heredoc mangles.
"""
import json
import pathlib


def edit(path, replacements):
    file = pathlib.Path(path)
    text = file.read_text(encoding="utf-8")
    for old, new in replacements:
        if old not in text:
            raise SystemExit(f"anchor not found in {path}: {old[:60]}")
        text = text.replace(old, new, 1)
    file.write_text(text, encoding="utf-8")


edit(
    "src/main/java/com/shinoow/abyssalcraft/api/block/ACBlocks.java",
    [
        (
            "    // ---- Potential Energy ----",
            "    /** The Materializer previews what a crystal bag can afford; it has no lit variant. */\n"
            '    public static final RegistryObject<Block> materializer = register("materializer",\n'
            "            () -> new MaterializerBlock(ACBlockProperties.stone(3.5F, 6.0F, MapColor.COLOR_PURPLE)));\n\n"
            "    // ---- Potential Energy ----",
        ),
        (
            "import com.shinoow.abyssalcraft.common.blocks.MachineBlock;",
            "import com.shinoow.abyssalcraft.common.blocks.MachineBlock;\n"
            "import com.shinoow.abyssalcraft.common.blocks.MaterializerBlock;",
        ),
    ],
)

edit(
    "src/main/java/com/shinoow/abyssalcraft/init/ACBlockEntities.java",
    [
        (
            "    private ACBlockEntities() {}",
            "    public static final RegistryObject<BlockEntityType<MaterializerBlockEntity>> MATERIALIZER =\n"
            '            ACRegistries.BLOCK_ENTITIES.register("materializer", () -> BlockEntityType.Builder\n'
            "                    .of(MaterializerBlockEntity::new, ACBlocks.materializer.get())\n"
            "                    .build(null));\n\n"
            "    private ACBlockEntities() {}",
        ),
        (
            "import com.shinoow.abyssalcraft.common.blocks.tile.RitualAltarBlockEntity;",
            "import com.shinoow.abyssalcraft.common.blocks.tile.MaterializerBlockEntity;\n"
            "import com.shinoow.abyssalcraft.common.blocks.tile.RitualAltarBlockEntity;",
        ),
    ],
)

edit(
    "src/main/java/com/shinoow/abyssalcraft/init/ACMenus.java",
    [
        (
            "    private ACMenus() {}",
            "    public static final RegistryObject<MenuType<MaterializerMenu>> MATERIALIZER =\n"
            '            ACRegistries.MENUS.register("materializer",\n'
            "                    () -> IForgeMenuType.create((IContainerFactory<MaterializerMenu>)\n"
            "                            (id, inventory, buffer) -> new MaterializerMenu(id, inventory)));\n\n"
            "    private ACMenus() {}",
        ),
        (
            "import com.shinoow.abyssalcraft.common.inventory.EngraverMenu;",
            "import com.shinoow.abyssalcraft.common.inventory.EngraverMenu;\n"
            "import com.shinoow.abyssalcraft.common.inventory.MaterializerMenu;",
        ),
    ],
)

edit(
    "src/main/java/com/shinoow/abyssalcraft/client/ACClientSetup.java",
    [
        (
            "            MenuScreens.register(ACMenus.ENGRAVER.get(), EngraverScreen::new);",
            "            MenuScreens.register(ACMenus.ENGRAVER.get(), EngraverScreen::new);\n"
            "            MenuScreens.register(ACMenus.MATERIALIZER.get(), MaterializerScreen::new);",
        ),
        (
            "import com.shinoow.abyssalcraft.client.gui.EngraverScreen;",
            "import com.shinoow.abyssalcraft.client.gui.EngraverScreen;\n"
            "import com.shinoow.abyssalcraft.client.gui.MaterializerScreen;",
        ),
    ],
)

lang = pathlib.Path("src/main/resources/assets/abyssalcraft/lang/en_us.json")
entries = json.loads(lang.read_text(encoding="utf-8"))
entries["container.abyssalcraft.materializer"] = "Materializer"
lang.write_text(json.dumps(entries, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

print("wired the Materializer")
