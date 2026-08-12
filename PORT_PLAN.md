# Grey Goo Port Plan

Port of Grey Goo Mod from Minecraft **1.6.4 / legacy Forge** to **1.21 / NeoForge**.

Upstream reference: `source/` (cloned from [StevenRS11/GreyGooMod](https://github.com/StevenRS11/GreyGooMod)).

Documentation in `source/` was written to enable faithful reimplementation. Read these before porting behavior:

- `source/CLAUDE.md` — architecture, packages, legacy API notes
- `source/GOO_SYSTEMS_DOCUMENTATION.md` — SpreadLimiter, Restorer, Destroyer chain
- `source/ALL_GOO_TYPES_DOCUMENTATION.md` — all 26 goo types

---

## Phase 0 — Project scaffold (current)

- [x] NeoForge 1.21 MDK (`greygoo` mod id)
- [x] Java 21 toolchain
- [x] Upstream `source/` reference clone
- [x] Core utilities: `CoordHolder`, `SpreadHelper`, `SpreadLimiter`, `GooSpreadCategory`
- [x] Config for spread scale values (ported from `mod_GreyGoo` config fields)
- [x] Tick handler to reset spread counters each server tick
- [x] Verify `gradlew build` on dev machine

## Phase 1 — Registries and assets

- [x] DeferredRegister for blocks, items, block entities, entities
- [x] Creative tab
- [x] Convert `GooBlockTextures.png` / `GooItemTextures.png` to per-block JSON models (first 3 blocks)
- [x] Lang file from original display names (note naming quirks: `BlockGreyEater` = "GreyGoo", `BlockGreyGoo` = "PurpleGoo")

## Phase 2 — Core goo infrastructure

- [x] `GooProtection` — `gooNeverEatThese`, `cleanerList`, `mineTheseOnly`, `NeverRestoreThese` (partial — grows with blocks)
- [ ] `GooActivation` — global flags for activation-required goo (Bubble, Freezer, Orange-*, Rapid-*)
- [x] `GooWorldData` — NBT persistence (`TGDbloom`, `GooActive`, `FreezerTexture`) via `SavedData`
- [x] `isGooActive()` — EMP array gating (EMP stub pending)

## Phase 3 — Autonomous goo blocks

Port in order of dependency / testability:

1. [x] Green Goo (Inert) — anchor, no spread
2. [x] Orange Wall — barrier spread from anchors
3. [x] Red Goo (Cleaner) — counter to all goo
4. [x] Purple Goo (`BlockGreyGoo`) — basic spread + column collapse
5. Grey Goo (`BlockGreyEater`) — main spreader, metadata tints, Cancer mutation
6. [x] Blue / White / Miner goo — specialized eaters (Yellow still pending)
7. Destroyer chain: Cancer → Black → Cancer 2 → TGD → TGD Inert

## Phase 4 — Activation-required goo

- Bubble, Freezer, Orange-Red, Orange-Purple, Orange-White
- Rapid Eater, Rapid Miner, Rapid Water Eater

## Phase 5 — Utility and special

- Rainbow Goo (Restorer) + backup dimension / fresh chunk comparison
- Elevator Goo, Substrate, Data Storage
- Gravity goo falling entity
- EMP Array

## Phase 6 — Machines and crafting

Each machine = Block + BlockEntity + Menu + Screen + Recipes:

- Assembler, Compiler, Programmer, Homogenizer, Coprocessor

## Phase 7 — Dimension and entities

- Goo dimension (WorldProvider, ChunkProvider, portal, teleporter)
- TGD Golem + AI replicate
- Worldgen (`GreyGooTGDworldgen`)

## Phase 8 — Items

- 9 Modifiers, 15+ Matrices, NanoLathe, Nanolens
- Recipe string-key systems → modern recipe codecs / datagen

---

## API mapping cheat sheet

| 1.6.4 | 1.21 NeoForge |
|-------|----------------|
| `world.setBlockWithNotify(x,y,z,id)` | `level.setBlock(pos, state, flags)` |
| Block integer IDs | `DeferredRegister` + `ResourceLocation` |
| Metadata | `BlockState` properties |
| `updateTick()` | `randomTick()` + `BlockBehaviour.Properties.randomTicks()` |
| `world.isRemote` | `level.isClientSide` |
| `TileEntity` | `BlockEntity` |
| `Container` / `Gui` | `AbstractContainerMenu` / `Screen` |
| `Configuration` (Forge) | `ModConfigSpec` |
| `TickRegistry` | `NeoForge.EVENT_BUS` + `TickEvent` |
| `getBlockId()` | `level.getBlockState(pos).getBlock()` |

---

## Critical behavior quirks (do not "fix")

Documented in `GOO_SYSTEMS_DOCUMENTATION.md`:

- **Class vs display name:** `BlockGreyEater` is in-game "GreyGoo" (Destroyer chain start); `BlockGreyGoo` is "PurpleGoo"
- **hasTicked toggle** on Cancer / Cancer 2 / Black — transformation takes minimum 2 ticks
- **TGD** only grows upward (`Math.abs(yOffset)` in scan)
- **Cancer density cap** — max 3 Cancer in 5×5×5 around target
- **Black Goo** — 1/60 spread chance, density cap 100
- **Cancer 2** — surface-only (air above), player proximity affects spread rate
