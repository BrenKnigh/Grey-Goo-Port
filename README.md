# Grey-Goo-Port

NeoForge port of [StevenRS11/GreyGooMod](https://github.com/StevenRS11/GreyGooMod) (Minecraft 1.6.4) to **Minecraft 1.21 / NeoForge**.

## Layout

| Path | Purpose |
|------|---------|
| `src/main/java/` | NeoForge 1.21 port code |
| `source/` | Upstream reference clone (read-only) |
| `PORT_PLAN.md` | Phased port plan from original documentation |
| `source/GOO_SYSTEMS_DOCUMENTATION.md` | Detailed behavior specs (Destroyer chain, Restorer) |
| `source/ALL_GOO_TYPES_DOCUMENTATION.md` | All 26 goo block behaviors |
| `source/CLAUDE.md` | Original mod architecture overview |

## Requirements

- Java 21
- Gradle (wrapper included)

## Build

```powershell
.\gradlew build
```

## Run client

```powershell
.\gradlew runClient
```

## Upstream

Original mod by StevenRS11. This repository is a community port; see upstream for historical context.
