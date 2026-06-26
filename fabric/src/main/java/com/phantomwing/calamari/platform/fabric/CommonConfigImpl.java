package com.phantomwing.calamari.platform.fabric;

import com.phantomwing.calamari.fabric.config.CalamariFabricConfig;

/**
 * Fabric implementation of {@link com.phantomwing.calamari.platform.CommonConfig}
 * (resolved by Architectury's {@code @ExpectPlatform} transformer). Delegates to
 * the Cloth/AutoConfig-backed {@link CalamariFabricConfig}.
 */
public final class CommonConfigImpl {
    private CommonConfigImpl() {
    }

    public static boolean generateStructureLoot() {
        return CalamariFabricConfig.getBooleanConfigurationValue(
                CalamariFabricConfig.GENERATE_STRUCTURE_LOOT_ID);
    }

    public static boolean enableVillagerTrades() {
        return CalamariFabricConfig.getBooleanConfigurationValue(
                CalamariFabricConfig.ENABLE_VILLAGER_TRADES_ID);
    }
}
