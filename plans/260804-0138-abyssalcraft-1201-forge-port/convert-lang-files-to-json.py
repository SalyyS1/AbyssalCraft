"""Convert 1.12.2 .lang files to the 1.20.1 JSON lang format.

The .lang format is `key=value` with `#` comments. 1.20.1 expects a flat JSON object.
Run once during the port; the .lang files are deleted afterwards.
"""
import json
import pathlib
import sys

lang_dir = pathlib.Path(sys.argv[1])

for src in sorted(lang_dir.glob("*.lang")):
    entries = {}
    duplicates = []
    for raw in src.read_text(encoding="utf-8").splitlines():
        line = raw.strip()
        if not line or line.startswith("#"):
            continue
        if "=" not in line:
            continue
        key, value = line.split("=", 1)
        key = key.strip()
        if key in entries:
            duplicates.append(key)
        entries[key] = value

    dest = src.with_suffix(".json")
    dest.write_text(
        json.dumps(entries, ensure_ascii=False, indent=2) + "\n", encoding="utf-8"
    )
    note = f" ({len(duplicates)} duplicate keys collapsed)" if duplicates else ""
    print(f"{src.name} -> {dest.name}: {len(entries)} keys{note}")
