"""Wire the State Transformer into the block, block-entity, menu, screen and asset registries.

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
            raise SystemExit(f"anchor not found in {path}: {old[:70]}")
        text = text.replace(old, new, 1)
    file.write_text(text, encoding="utf-8")


edit(
    "src/main/java/com/shinoow/abyssalcraft/api/block/ACBlocks.java",
    [
        (
            "    // ---- Potential Energy ----",
            "    /** Packs a chest-load of items into a stone tablet and back out again. */\n"
            '    public static final RegistryObject<Block> state_transformer = register("state_transformer",\n'
            "            () -> new StateTransformerBlock(ACBlockProperties.stone(3.5F, 6.0F, MapColor.COLOR_GRAY)));\n\n"
            "    // ---- Potential Energy ----",
        ),
        (
            "import com.shinoow.abyssalcraft.common.blocks.MaterializerBlock;",
            "import com.shinoow.abyssalcraft.common.blocks.MaterializerBlock;\n"
            "import com.shinoow.abyssalcraft.common.blocks.StateTransformerBlock;",
        ),
    ],
)

edit(
    "src/main/java/com/shinoow/abyssalcraft/init/ACBlockEntities.java",
    [
        (
            "    private ACBlockEntities() {}",
            "    public static final RegistryObject<BlockEntityType<StateTransformerBlockEntity>> STATE_TRANSFORMER =\n"
            '            ACRegistries.BLOCK_ENTITIES.register("state_transformer", () -> BlockEntityType.Builder\n'
            "                    .of(StateTransformerBlockEntity::new, ACBlocks.state_transformer.get())\n"
            "                    .build(null));\n\n"
            "    private ACBlockEntities() {}",
        ),
        (
            "import com.shinoow.abyssalcraft.common.blocks.tile.TransmutatorBlockEntity;",
            "import com.shinoow.abyssalcraft.common.blocks.tile.StateTransformerBlockEntity;\n"
            "import com.shinoow.abyssalcraft.common.blocks.tile.TransmutatorBlockEntity;",
        ),
    ],
)

edit(
    "src/main/java/com/shinoow/abyssalcraft/init/ACMenus.java",
    [
        (
            "    private ACMenus() {}",
            "    public static final RegistryObject<MenuType<StateTransformerMenu>> STATE_TRANSFORMER =\n"
            '            ACRegistries.MENUS.register("state_transformer",\n'
            "                    () -> IForgeMenuType.create((IContainerFactory<StateTransformerMenu>)\n"
            "                            (id, inventory, buffer) -> new StateTransformerMenu(id, inventory)));\n\n"
            "    private ACMenus() {}",
        ),
        (
            "import com.shinoow.abyssalcraft.common.inventory.MaterializerMenu;",
            "import com.shinoow.abyssalcraft.common.inventory.MaterializerMenu;\n"
            "import com.shinoow.abyssalcraft.common.inventory.StateTransformerMenu;",
        ),
    ],
)

edit(
    "src/main/java/com/shinoow/abyssalcraft/client/ACClientSetup.java",
    [
        (
            "            MenuScreens.register(ACMenus.MATERIALIZER.get(), MaterializerScreen::new);",
            "            MenuScreens.register(ACMenus.MATERIALIZER.get(), MaterializerScreen::new);\n"
            "            MenuScreens.register(ACMenus.STATE_TRANSFORMER.get(), StateTransformerScreen::new);",
        ),
        (
            "import com.shinoow.abyssalcraft.client.gui.MaterializerScreen;",
            "import com.shinoow.abyssalcraft.client.gui.MaterializerScreen;\n"
            "import com.shinoow.abyssalcraft.client.gui.StateTransformerScreen;",
        ),
    ],
)

# The block reuses the machine model helper: side, front and top sprites all exist.
edit(
    "src/main/java/com/shinoow/abyssalcraft/data/ACBlockModelProvider.java",
    [
        (
            '        machine(ACBlocks.materializer.get(), "materializer", "materializer_front");',
            '        machine(ACBlocks.materializer.get(), "materializer", "materializer_front");\n'
            "        // The State Transformer ships only a side and a front sprite, so the side doubles\n"
            "        // as the top, which is what the 1.12.2 model did.\n"
            "        var transformer = models().orientable(\"state_transformer\",\n"
            '                texture("statetransformer"), texture("statetransformer_front"),\n'
            '                texture("statetransformer"));\n'
            "        horizontalBlock(ACBlocks.state_transformer.get(), transformer);\n"
            "        simpleBlockItem(ACBlocks.state_transformer.get(), transformer);",
        )
    ],
)

lang = pathlib.Path("src/main/resources/assets/abyssalcraft/lang/en_us.json")
entries = json.loads(lang.read_text(encoding="utf-8"))
entries["container.abyssalcraft.statetransformer"] = "State Transformer"
lang.write_text(json.dumps(entries, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")

print("wired the State Transformer")
