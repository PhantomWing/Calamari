package com.phantomwing.calamari.platform.forge;

import com.phantomwing.calamari.forge.Configuration;

/**
 * Forge implementation of {@link com.phantomwing.calamari.platform.CommonConfig}
 * (resolved by Architectury's {@code @ExpectPlatform} transformer, which looks the
 * impl up by platform name — {@code forge} — regardless of the module directory
 * name). Delegates straight to the {@code ForgeConfigSpec} values.
 */
public final class CommonConfigImpl {
    private CommonConfigImpl() {
    }

    public static boolean generateStructureLoot() {
        return Configuration.GENERATE_STRUCTURE_LOOT.get();
    }

    public static boolean enableVillagerTrades() {
        return Configuration.ENABLE_VILLAGER_TRADES.get();
    }
}
