"""Correct the evil animals' texture references in write_corrupted_animals.py.

The evil animals reuse vanilla entity textures rather than shipping their own, so their
ResourceLocation must have no namespace and must not sit under the mod's textures/model path.
The demon animals do ship their own sprites. This rewrites the generator to emit the right
constructor for each case, then reruns it.
"""
import pathlib
import subprocess

script = pathlib.Path("plans/260804-0138-abyssalcraft-1201-forge-port/write_corrupted_animals.py")
text = script.read_text(encoding="utf-8")

# Make the texture entry a full ResourceLocation expression rather than a mod-relative path.
text = text.replace(
    '''    private static final ResourceLocation TEXTURE =
            new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/{texture}.png");''',
    "    private static final ResourceLocation TEXTURE = {texture_expr};",
)
text = text.replace(
    "import com.shinoow.abyssalcraft.AbyssalCraft;\nimport com.shinoow.abyssalcraft.common.entity.demon.{name};",
    "{mod_import}import com.shinoow.abyssalcraft.common.entity.demon.{name};",
)

# Demon animals ship their own sprite under the mod namespace; evil animals reuse vanilla ones.
text = text.replace(
    '"texture": "demon/cow"',
    '"texture_expr": \'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_cow.png")\', '
    '"mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\\n"',
)
text = text.replace(
    '"texture": "demon/pig"',
    '"texture_expr": \'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_pig.png")\', '
    '"mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\\n"',
)
text = text.replace(
    '"texture": "demon/chicken"',
    '"texture_expr": \'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_chicken.png")\', '
    '"mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\\n"',
)
text = text.replace(
    '"texture": "demon/sheep"',
    '"texture_expr": \'new ResourceLocation(AbyssalCraft.MOD_ID, "textures/model/demon_sheep.png")\', '
    '"mod_import": "import com.shinoow.abyssalcraft.AbyssalCraft;\\n"',
)
text = text.replace(
    '"texture": "evil/cow"',
    '"texture_expr": \'new ResourceLocation("textures/entity/cow/cow.png")\', "mod_import": ""',
)
text = text.replace(
    '"texture": "evil/pig"',
    '"texture_expr": \'new ResourceLocation("textures/entity/pig/pig.png")\', "mod_import": ""',
)
text = text.replace(
    '"texture": "evil/chicken"',
    '"texture_expr": \'new ResourceLocation("textures/entity/chicken.png")\', "mod_import": ""',
)
text = text.replace(
    '"texture": "evil/sheep"',
    '"texture_expr": \'new ResourceLocation("textures/entity/sheep/sheep.png")\', "mod_import": ""',
)

# The javadoc referenced the mod's own sprites; make it accurate for both families.
text = text.replace(
    " * 1.12.2 drew these with the matching vanilla animal model, so the vanilla layer is reused rather\n"
    " * than duplicating that geometry.",
    " * 1.12.2 drew these with the matching vanilla animal model, so the vanilla layer is reused rather\n"
    " * than duplicating that geometry. The demon animals ship their own sprite; the evil ones reuse\n"
    " * the vanilla animal texture, which is why that reference has no namespace.",
)

script.write_text(text, encoding="utf-8")
print("corrected texture references")

# The generator appends to the registry files, so reset them before rerunning.
subprocess.run(
    ["git", "checkout", "--",
     "src/main/java/com/shinoow/abyssalcraft/init/ACEntities.java",
     "src/main/java/com/shinoow/abyssalcraft/client/ACClientSetup.java"],
    check=True,
)
print("reset registry files")
