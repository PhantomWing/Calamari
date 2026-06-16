# Calamari

A small, vanilla-friendly NeoForge + Fabric mod for Minecraft 1.21.1. Squids and
glow squids drop **Calamari**, a seafood you can cook, trade, and eat.

## Features

- **Calamari** (raw) and **Cooked Calamari** food items.
- Squids and glow squids drop 1–2 calamari (toggleable).
- Cook calamari in a furnace, smoker, or over a campfire.
- Loot replacement (toggleable, extensible spec): raw cod becomes raw calamari with a chance, in village fisher chests (1–3) and Hero of the Village fisherman gifts (1).
- Fisherman villager trades (toggleable): buys raw calamari, sells cooked calamari.
- Cross-platform: one Architectury codebase ships both NeoForge and Fabric jars.

## Configuration

Three options, identical on both loaders (NeoForge: `config/calamari-common.toml`;
Fabric: `config/calamari.json`, with an in-game Cloth Config / Mod Menu screen):

| Option | Default | Effect |
| --- | --- | --- |
| `squids_drop_calamari` | `true` | Squids / glow squids drop calamari |
| `generate_structure_loot` | `true` | Replace some chest loot with calamari (e.g. raw cod → raw calamari in village fisher chests) |
| `enable_villager_trades` | `true` | Fisherman calamari trades |

## Building

Requires JDK 21. The committed `gradle.properties` pins a local JDK 21 path via
`org.gradle.java.home` — adjust it for your machine or override on the command line.

```bash
./gradlew build              # build both loaders -> */build/libs/
./gradlew :neoforge:runData  # regenerate assets/data into common/src/generated/resources
./gradlew :fabric:runClient  # or :neoforge:runClient
```

## Tech stack

Minecraft 1.21.1 · Java 21 · Architectury 13.0.8 (Loom 1.7.435) · NeoForge 21.1.218 ·
Fabric Loader 0.16.14 + Fabric API · Cloth Config (Fabric) · Parchment mappings.

## Releasing

Deployment is manual: run the **Release** GitHub Action (`workflow_dispatch`) to
build and publish one version per platform to Modrinth and CurseForge. Set the
`MODRINTH_TOKEN`, `MODRINTH_PROJECT_ID`, `CURSEFORGE_TOKEN`, and
`CURSEFORGE_PROJECT_ID` repository secrets first; until then the publish step runs
in dry-run mode.

## License

MIT
