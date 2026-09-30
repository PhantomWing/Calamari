@../MinecraftDeveloperPortal/.claude/profiles/architectury.md

# Calamari

**This working copy is on `version/1.21.11` — the 1.21.11 line.** Confirm with
`git branch --show-current` before trusting it; the registry at
`MinecraftDeveloperPortal/data/mods.json` lists every line this mod has.

`common` + `fabric` + `neoforge`, plus a `forge` folder used by the 1.20.1 line.

The healthiest fan-out in the workspace: all eleven lines sit on the same `mod_version`. Keep it that way — when a change lands here, propagate it before starting anything else.

Publishing is configured per loader in `fabric/build.gradle` and `neoforge/build.gradle`, and this repo is the reference implementation for the `publishMods` setup. Note it still tags uploads with a single `minecraft_version` rather than a `supported_minecraft_versions` set — see `publishing.md`.
