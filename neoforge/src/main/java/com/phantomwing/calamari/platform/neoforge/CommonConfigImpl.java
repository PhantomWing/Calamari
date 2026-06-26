package com.phantomwing.calamari.platform.neoforge;

import com.phantomwing.calamari.neoforge.Configuration;

/**
 * NeoForge implementation of {@link com.phantomwing.calamari.platform.CommonConfig}
 * (resolved by Architectury's {@code @ExpectPlatform} transformer). Delegates
 * straight to the {@code ModConfigSpec} values.
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
