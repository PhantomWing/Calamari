package com.phantomwing.calamari.neoforge;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * NeoForge config ({@code ModConfigSpec}), persisted to
 * {@code config/calamari-common.toml}. The option ids and {@code true} defaults
 * are kept 1:1 with the Fabric {@code CalamariFabricConfig}.
 */
public class Configuration {
    public static final ModConfigSpec COMMON_CONFIG;

    public static final String SQUIDS_DROP_CALAMARI_ID = "squids_drop_calamari";
    public static final ModConfigSpec.BooleanValue SQUIDS_DROP_CALAMARI;

    public static final String GENERATE_STRUCTURE_LOOT_ID = "generate_structure_loot";
    public static final ModConfigSpec.BooleanValue GENERATE_STRUCTURE_LOOT;

    public static final String ENABLE_VILLAGER_TRADES_ID = "enable_villager_trades";
    public static final ModConfigSpec.BooleanValue ENABLE_VILLAGER_TRADES;

    public static boolean getBooleanConfigurationValue(String id) {
        return switch (id) {
            case SQUIDS_DROP_CALAMARI_ID -> SQUIDS_DROP_CALAMARI.get();
            case GENERATE_STRUCTURE_LOOT_ID -> GENERATE_STRUCTURE_LOOT.get();
            case ENABLE_VILLAGER_TRADES_ID -> ENABLE_VILLAGER_TRADES.get();
            default -> throw new Error("Invalid setting ID: " + id);
        };
    }

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        SQUIDS_DROP_CALAMARI = builder
                .comment("Should squids and glow squids drop a Calamari item?")
                .translation("text.autoconfig.calamari.option.squids_drop_calamari")
                .define(SQUIDS_DROP_CALAMARI_ID, true);
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
