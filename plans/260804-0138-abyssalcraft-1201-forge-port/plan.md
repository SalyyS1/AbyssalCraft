# AbyssalCraft 1.12.2 → 1.20.1 Forge Port

Status: **planning — implementation not started**
Branch: `port/1.20.1-forge`
Target: Minecraft 1.20.1 / Forge 47.3.12 / Java 17

## Scope reality

This is not a version bump. It is a rewrite of every Minecraft-facing layer.

| Metric | Value |
| --- | --- |
| Java files | 763 (`src/main/java`: 749, `src/api/java`: 14) |
| Lines of Java | ~109,700 |
| Files already on 1.20.1 APIs | **0** |
| Files importing removed 1.12.2 APIs | 347 direct, ~600 transitively |
| Registry entries | 151 blocks, 195 items, 48 entities, 33 tile entities |
| Dimensions | 4 (Abyssal Wasteland, Dreadlands, Omothol, Dark Realm) |
| Biomes | 15 |
| Asset JSONs | 1340 |

Eight major versions separate the source from the target. Every one of the
following subsystems was **removed or redesigned** between them, so the port
touches essentially all code:

- `FMLPreInitializationEvent` lifecycle → `DeferredRegister` + mod event bus
- `GameRegistry.register` → `RegistryObject`, registries frozen at startup
- `Block` + metadata + `IBlockState` → `Block` + `BlockState` + `Property`
- `WorldProvider` + `DimensionType.register` → datapack JSON dimensions + `ChunkGenerator`
- `Biome` subclassing → datapack JSON biomes + `BiomeModifier`
- `TileEntity` → `BlockEntity` with `BlockEntityType` + codec-based NBT
- `NBTTagCompound` → `CompoundTag`, plus `DataComponents`-style access patterns
- `GuiContainer` / `IGuiHandler` → `AbstractContainerScreen` + `MenuType` + `NetworkHooks`
- `ModelBase` → `EntityModel` + `LayerDefinition` + `ModelPart`
- `SimpleNetworkWrapper` → `SimpleChannel` with explicit registration
- `IWorldGenerator` → `PlacedFeature` / `ConfiguredFeature` JSON
- `IRecipe` + hardcoded Java recipes → datapack JSON + `RecipeSerializer`
- Structures → `Structure` + `StructurePiece` + template pools
- `mcmod.info` → `mods.toml`
- Forge config annotations → `ForgeConfigSpec`
- `CreativeTabs` → `CreativeModeTab` via registry event
- Potions/enchants/particles/sounds → all now registry objects
- `net.minecraft.*` package layout entirely reorganized, unmapped → official mappings

**Honest estimate: this is multi-week-to-multi-month engineering work, not a
single autonomous session.** No amount of automation collapses 110k LOC of
cross-version API rewriting into one run. Any claim of a finished port after one
pass would be a stub with the features hollowed out — the opposite of the
"keep the core features intact" requirement.

## Core features that must survive (derived from code, since the wiki is empty)

The GitHub wiki has only 4 pages, all for resourcepack authors, and the Fandom
wiki is paywalled (HTTP 402). Feature inventory below is taken from the source,
which is authoritative.

1. **Progression spine** — Necronomicon tiers (`api/necronomicon`, `NecroData`,
   unlock conditions in `api/necronomicon/condition`, 17 condition types) gating
   recipes and knowledge.
2. **4 dimensions** — Abyssal Wasteland, Dreadlands, Omothol, Dark Realm, each
   with its own chunk generator and biome set.
3. **Potential Energy (PE) system** — `api/energy`: collectors, containers,
   relays, pedestals, transporters, amplifiers, deity-tier variants,
   disruption events (13 disruption types).
4. **Ritual system** — `api/ritual` + 11 concrete rituals (breeding, cleansing,
   corruption, curing, dread spawn, house, mass enchant, purging, Jzahar
   respawn, resurrection, weather), ritual altar/pedestal multiblocks.
5. **Machines** — Crystallizer, Transmutator, Engraver, Materializer,
   State Transformer, Rending Pedestal, Sacrificial Altar, Spirit Altar, Crate,
   each with recipe registry + GUI.
6. **Spells** — 10 spells + spellbook GUI (`common/spells`, `api/spell`).
7. **Bosses & mobs** — 48 entities incl. Chagaroth, Jzahar, Sacthoth,
   Shadow Titan, Dreadguard, Omothol Warden, Skeleton Goliath, Shoggoths,
   demon/evil animal variants.
8. **Materials** — Abyssalnite, Coralium, Dreadium, Ethaxium, Shadow, plus
   ore/tool/armor trees and upgrade kits.
9. **Rending / transmutation registries** — `api/rending`, `api/recipe`.
10. **JEI integration** — 4 recipe categories.

## Phases

Each phase must compile before the next starts. Order is forced by the
dependency graph: nothing compiles until registries and the mod entrypoint exist.

| # | Phase | Files | Gate |
| --- | --- | --- | --- |
| 00 | Toolchain + `mods.toml` + empty mod entrypoint boots | ~10 | `runClient` reaches main menu with empty mod |
| 01 | Config (`ForgeConfigSpec`), `ACLib`, creative tabs | ~15 | compiles |
| 02 | Blocks: `DeferredRegister`, `BlockState` properties, drop metadata variants | ~95 | all blocks placeable |
| 03 | Items, tools, armor, upgrade kits | ~60 | obtainable in creative |
| 04 | Block entities + menus + screens (machines) | ~75 | machines run, GUIs open |
| 05 | Recipe registries + datagen for JSON recipes/loot/tags | ~30 | recipes resolve |
| 06 | Entities + attributes + renderers + models | ~120 | mobs spawn and render |
| 07 | Networking (`SimpleChannel`), capabilities | ~15 | client/server sync |
| 08 | PE energy system + disruptions | ~40 | energy flows, disruptions fire |
| 09 | Rituals + multiblocks | ~30 | all 11 rituals execute |
| 10 | Necronomicon GUI + progression conditions | ~40 | tier gating correct |
| 11 | Spells + spellbook | ~20 | all 10 spells cast |
| 12 | Biomes + worldgen features (datapack JSON) | ~45 | biomes generate |
| 13 | Dimensions + chunk generators | ~25 | all 4 dimensions enterable |
| 14 | Structures | ~30 | structures generate |
| 15 | Potions, enchantments, particles, sounds, commands | ~25 | functional |
| 16 | JEI integration; drop dead Thaumcraft/InvTweaks stubs | ~20 | JEI shows AC recipes |
| 17 | Full playthrough validation against feature list above | — | progression completable |

## Decisions (confirmed 2026-08-04)

1. **Ship target: `SalyyS1/AbyssalCraft` (your fork).** Nothing is posted to
   upstream `Shinoow/AbyssalCraft`.
2. **Delivery: complete the whole port** ("hoàn thiện toàn bộ"). Work the phases
   in order; every phase must compile before the next begins.
3. **Keep Thaumcraft + InvTweaks integration shims** rather than deleting them.
4. **Accept world-save incompatibility** with 1.12.2 — metadata-packed blocks
   become `BlockState` properties, which necessarily changes block IDs.
5. **ProjectE** stays pinned to CurseForge file `4901949`.

## Decisions needed before implementation

1. **Where does this ship?** `gh` resolves this checkout to
   **`Shinoow/AbyssalCraft`** (upstream, the original author's repo), while
   `origin` is your fork `SalyyS1/AbyssalCraft`. I have not created an issue or
   PR, because opening either would post to a third party's public repository.
   Confirm the target before any `gh issue`/`gh pr` step.
2. **Dead integrations** — `src/api/java` holds Thaumcraft and InvTweaks stubs
   for mods with no 1.20.1 equivalent in the same form. Recommend dropping both;
   `SoftDepUtil` references need pruning either way.
3. **Metadata blocks** — 1.12.2 packed variants into metadata (e.g. the
   `*slab2` double-slab pairs). 1.20.1 needs either separate blocks or a
   `BlockState` property. Recommend `SlabType`-style properties, which changes
   block IDs and breaks world compat with 1.12.2 saves (unavoidable).
4. **ProjectE dependency** — pinned to CurseForge file `4901949`; confirm it is
   still the intended integration target.

## Progress so far

- Forge 47.3.12 / MC 1.20.1 / Java 17 toolchain scaffolding in `build.gradle`,
  `settings.gradle`, `gradle.properties` (Gradle 8.1.1 wrapper).
- Fixed: Gradle 8.1.1 cannot parse JDK 21 class files (`major version 65`) and
  `JAVA_HOME` on this machine is JDK 21. Pinned `org.gradle.java.home` to the
  auto-provisioned Temurin 17.
- Verified: `./gradlew :extractSrg` **succeeds** (5m39s) — MCP config downloads
  and official mappings resolve. Toolchain is sound.
- Source tree is still 100% 1.12.2. No Java migrated.

## Unresolved questions

- None. All four open questions were resolved above on 2026-08-04.
