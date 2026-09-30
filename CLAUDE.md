@../MinecraftDeveloperPortal/.claude/profiles/architectury.md

# Calamari

**This branch is `version/26.3` — the 26.3 line.** Confirm with `git branch --show-current` before
trusting it; the registry at `MinecraftDeveloperPortal/data/mods.json` lists every line this mod has.

`common` + `fabric` + `neoforge`, on `loom-no-remap` with `shadowJar` as the shipped jar. A `forge`
folder may be left on disk from the 1.20.1 line; this branch neither tracks nor builds it.

All twelve lines sit on `mod_version` 1.0.1, but that is not content parity: the Fisherman
duplicate-trade fix reached only some of them. Check `/mc-parity` before assuming a fix is everywhere,
and propagate a change before starting anything else.

On this line:

- NeoForge's manifest takes its NeoForge, Minecraft and Architectury ranges from `gradle.properties`,
  as `fabric.mod.json` does, so a version bump reaches both.
- Datagen is `:neoforge:runData` and writes `common/src/generated/resources` for both loaders. It does
  not exit when it finishes: stop it once the log says `All providers took`.
- The game tests (nine) live in the NeoForge `test` source set and run with `:neoforge:runGameTest`.
  The JUnit `test` task finds none there and is told not to fail on that.

Publishing is configured per loader in `fabric/build.gradle` and `neoforge/build.gradle`, and this repo
is the reference implementation for the `publishMods` setup. It tags uploads with the single
`minecraft_version` rather than a `supported_minecraft_versions` set — see `publishing.md`. On 26.3
that is the whole rung, since 26.3 covers only itself.
