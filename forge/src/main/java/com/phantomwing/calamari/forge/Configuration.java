package com.phantomwing.calamari.forge;

import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Forge config ({@code ForgeConfigSpec}), persisted to
 * {@code config/calamari-common.toml}. The option ids and {@code true} defaults
 * are kept 1:1 with the Fabric {@code CalamariFabricConfig}.
 */
public class Configuration {
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final String GENERATE_STRUCTURE_LOOT_ID = "generate_structure_loot";
    public static final ForgeConfigSpec.BooleanValue GENERATE_STRUCTURE_LOOT;

    public static final String ENABLE_VILLAGER_TRADES_ID = "enable_villager_trades";
    public static final ForgeConfigSpec.BooleanValue ENABLE_VILLAGER_TRADES;

    public static boolean getBooleanConfigurationValue(String id) {
        return switch (id) {
            case GENERATE_STRUCTURE_LOOT_ID -> GENERATE_STRUCTURE_LOOT.get();
            case ENABLE_VILLAGER_TRADES_ID -> ENABLE_VILLAGER_TRADES.get();
            default -> throw new Error("Invalid setting ID: " + id);
        };
    }

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        GENERATE_STRUCTURE_LOOT = builder
                .comment("Add or replace calamari in generated loot: village fisher chests, buried treasure, guardian/elder guardian drops, and Hero of the Village & cat morning gifts.")
                .translation("text.autoconfig.calamari.option.generate_structure_loot")
                .define(GENERATE_STRUCTURE_LOOT_ID, true);
        ENABLE_VILLAGER_TRADES = builder
                .comment("Enable the Fisherman villager calamari trades.")
                .translation("text.autoconfig.calamari.option.enable_villager_trades")
                .define(ENABLE_VILLAGER_TRADES_ID, true);

        COMMON_CONFIG = builder.build();
    }
}
