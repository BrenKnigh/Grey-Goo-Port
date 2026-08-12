# Multi-Agent Workflow — Grey Goo Port

Use this when running parallel Cursor agents on the port. **One lead chat owns architecture**; worker agents get narrow, non-overlapping tasks.

## Rules

1. Do not edit `GreyGooMod.java` registries unless your task says so.
2. Read behavior from `source/ALL_GOO_TYPES_DOCUMENTATION.md` before implementing a block.
3. Run `.\gradlew build` before marking a task done.
4. One block family per agent per PR/session.

## Task queue (recommended order)

| Sprint | Agent task | Files to touch | Done when |
|--------|------------|----------------|-----------|
| **A** (lead) | Registries, `GooBlock`, protection lists, Green/Orange/Red blocks | `registry/`, `block/`, `core/GooProtection.java` | `runClient` — place 3 blocks |
| **B** | Purple Goo + SpreadHelper integration | `PurpleGooBlock.java` | Spreads to stone, collapses column |
| **C** | Blue + White + Miner goo | `block/eater/` | Each eats correct targets |
| **D** | Destroyer chain (Grey → Cancer → Black → C2 → TGD) | `block/destroyer/` | Chain progresses in test world |
| **E** | Textures/models for next batch | `assets/greygoo/` | No missing texture squares |
| **F** | Machines (Assembler first) | `block/machine/` | GUI opens, one recipe works |

## Copy-paste agent prompts

### Worker — port one autonomous goo block

```
Port [BLOCK_NAME] for Grey Goo NeoForge 1.21.
- Read source/ALL_GOO_TYPES_DOCUMENTATION.md section for this block
- Read source/mod_GreyGoo/[OriginalClass].java
- Extend com.brenknigh.greygoo.block.GooBlock
- Register in GreyGooBlocks if not present
- Add blockstate, model, lang entry
- Do not modify unrelated blocks
- Run gradlew build
```

### Worker — assets only

```
Add block models and blockstates for greygoo:[block_ids].
Textures are in assets/greygoo/textures/block/.
Match 16x16 style from source/GooBlockTextures.png.
Run gradlew build.
```

## Automations to add (Cursor)

| Automation | Trigger | Action |
|------------|---------|--------|
| **CI build** | Push to `main` | `gradlew build`, report failure |
| **Port progress** | Weekly cron | Summarize open Phase items from PORT_PLAN.md |

Ask the lead agent to open the Automations editor when ready to configure these.
