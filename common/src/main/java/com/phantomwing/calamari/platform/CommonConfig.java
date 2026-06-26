package com.phantomwing.calamari.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;

/**
 * {@code @ExpectPlatform} bridge exposing the config booleans the loader-agnostic
 * code (loot drops, villager trades) needs.
 *
 * <p>The config system itself stays loader-specific: NeoForge {@code ModConfigSpec},
 * Fabric Cloth {@code AutoConfig}. The option ids and {@code true} defaults are kept
 * 1:1 across both, so behaviour is identical. Implemented per loader at
 * {@code com.phantomwing.calamari.platform.<loader>.CommonConfigImpl} (Architectury
 * rewrites the call sites at build time).</p>
 */
public final class CommonConfig {
    private CommonConfig() {
    }

    /** Gate for replacing items in generated structure/chest loot with calamari. */
    @ExpectPlatform
    public static boolean generateStructureLoot() {
        throw new AssertionError("@ExpectPlatform stub – replaced per loader at build time");
    }

    /** Gate for the Fisherman villager calamari trades. */
    @ExpectPlatform
    public static boolean enableVillagerTrades() {
        throw new AssertionError("@ExpectPlatform stub – replaced per loader at build time");
    }
}
