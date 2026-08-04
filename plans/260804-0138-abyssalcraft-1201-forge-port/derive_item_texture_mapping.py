"""Derive the item texture mapping for the 1.20.1 port.

The 1.12.2 build registered each item under an abbreviated name and shipped a model JSON
under that same name pointing at an equally abbreviated texture. The port uses descriptive
registry names, so a generated item model cannot infer its texture. This reads the legacy
ItemHandler registrations plus the legacy model JSONs and emits the
new-registry-name -> texture-base pairs as Java map entries.

Run once during the port; the output is pasted into ACItemTextures.
"""
import json
import pathlib
import re
import sys

repo = pathlib.Path(sys.argv[1])
legacy_handler = repo / "src/legacy/java/com/shinoow/abyssalcraft/init/ItemHandler.java"
legacy_models = repo / "src/main/resources/assets/abyssalcraft/models/item"
textures = repo / "src/main/resources/assets/abyssalcraft/textures/items"
ported_items = repo / "src/main/java/com/shinoow/abyssalcraft/api/item/ACItems.java"

# new_name -> legacy_registry_name
registrations = dict(
    re.findall(r'registerItem\(ACItems\.([a-z_0-9]+), "([a-z_0-9]+)"', legacy_handler.read_text(encoding="utf-8"))
)

# Which items the port has actually registered so far.
ported = set(
    re.findall(r"RegistryObject<Item> ([a-z_0-9]+) =", ported_items.read_text(encoding="utf-8"))
)


def texture_for(legacy_name):
    """Read layer0 out of the legacy model and strip the namespace prefix."""
    model = legacy_models / f"{legacy_name}.json"
    if not model.exists():
        return None
    try:
        data = json.loads(model.read_text(encoding="utf-8"))
    except json.JSONDecodeError:
        return None
    layer0 = data.get("textures", {}).get("layer0")
    if not layer0:
        return None
    return layer0.split("/")[-1]


resolved, unresolved = {}, []
for new_name in sorted(ported):
    legacy_name = registrations.get(new_name)
    if legacy_name is None:
        unresolved.append((new_name, "not registered in 1.12.2 ItemHandler"))
        continue
    texture = texture_for(legacy_name)
    if texture is None:
        unresolved.append((new_name, f"no layer0 in models/item/{legacy_name}.json"))
        continue
    if not (textures / f"{texture}.png").exists():
        unresolved.append((new_name, f"texture {texture}.png missing"))
        continue
    resolved[new_name] = texture

for new_name, texture in resolved.items():
    print(f'            Map.entry(ACItems.{new_name}, "{texture}"),')

print(f"\n// resolved {len(resolved)}, unresolved {len(unresolved)}", file=sys.stderr)
for new_name, reason in unresolved:
    print(f"//   {new_name}: {reason}", file=sys.stderr)
