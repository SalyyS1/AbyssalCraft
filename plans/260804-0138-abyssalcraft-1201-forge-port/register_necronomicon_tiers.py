"""Register the five Necronomicon tiers and map their textures.

Kept as a script rather than an inline heredoc: the Java contains nested quotes a shell heredoc
mangles.
"""
import pathlib

# new registry name -> texture base, from the 1.12.2 registerItem calls and item models.
BOOKS = [
    ("necronomicon", "necronomicon"),
    ("abyssal_wasteland_necronomicon", "necronomicon_cor"),
    ("dreadlands_necronomicon", "necronomicon_dre"),
    ("omothol_necronomicon", "necronomicon_omt"),
    ("abyssalnomicon", "abyssalnomicon"),
]

items = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/api/item/ACItems.java")
text = items.read_text(encoding="utf-8")

declarations = [
    "    // ---- Necronomicon tiers. The book's tier gates rituals and knowledge. ----"
]
for name, _ in BOOKS:
    declarations.append(
        f'    public static final RegistryObject<Item> {name} = book("{name}");'
    )
declarations.append("")

anchor = "    // ---- Crystal bags."
text = text.replace(anchor, "\n".join(declarations) + "\n" + anchor, 1)

# Books never stack: 1.12.2 set max stack size 1 on them.
text = text.replace(
    "    private static RegistryObject<Item> simple(String name) {",
    "    /** Necronomicons never stack, matching 1.12.2. */\n"
    "    private static RegistryObject<Item> book(String name) {\n"
    "        return ACRegistries.ITEMS.register(name,\n"
    "                () -> new Item(new Item.Properties().stacksTo(1)));\n"
    "    }\n\n"
    "    private static RegistryObject<Item> simple(String name) {",
    1,
)
items.write_text(text, encoding="utf-8")

textures = pathlib.Path("src/main/java/com/shinoow/abyssalcraft/data/ACItemTextures.java")
text = textures.read_text(encoding="utf-8")
lines = [
    f'        textures.put(ACItems.{name}, "{texture}");' for name, texture in BOOKS
]
lines.append("        return Map.copyOf(textures);")
text = text.replace("        return Map.copyOf(textures);", "\n".join(lines), 1)
textures.write_text(text, encoding="utf-8")

print(f"registered {len(BOOKS)} Necronomicon tiers")
