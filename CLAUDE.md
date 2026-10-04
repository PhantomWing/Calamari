@~/Documents/Projects/Minecraft/MinecraftDeveloperPortal/.claude/profiles/architectury.md

# Calamari - `version/26.1`

**This file describes the `version/26.1` line**: Minecraft 26.1.2 (the jar covers 26.1, 26.1.1 and 26.1.2), Fabric and NeoForge.
Built with Architectury Loom without remapping (`loom-no-remap`): Minecraft 26.x ships unobfuscated, so there are no mappings and `shadowJar` is the shipped jar.

It belongs to whichever folder has this branch checked out - the main `Calamari` folder or a worktree
under `Calamari/.worktrees/`. A session started in a worktree also loads the main folder's CLAUDE.md,
which describes another line; for this folder, this file is the one that applies. Confirm with
`git branch --show-current`. `MinecraftDeveloperPortal/data/mods.json` lists every line of the mod.

Squids and glow squids drop calamari, a vanilla-friendly seafood you can cook, trade and eat.
Mod ID `calamari`.

A uniform `mod_version` across the lines is not content parity: a fix released as the same patch
number reached some lines and not others. Check `/mc-parity` before assuming a fix is everywhere.

Publishing is configured per loader, in each loader's `build.gradle`, and this repo is the reference
implementation for the `publishMods` setup.

## This line

- Java 25, no mappings and no Parchment (Mojang names are the runtime names). `org.gradle.java.home` is pinned in `gradle.properties` to a JDK on this machine; if the daemon fails to start, check that path first.
- Datagen: `:neoforge:runData`. Output: `common/src/generated/resources`, never hand-edited.
- Game tests in `neoforge/src/test`, run with `:neoforge:runGameTest`.
- Published with `publishMods` from `fabric/build.gradle` and `neoforge/build.gradle`, with the `-PpublishDryRun` flag. Uploads are tagged `26.1`, `26.1.1` and `26.1.2` in the build script, the whole rung.
- GitHub Actions: `release.yml`.
