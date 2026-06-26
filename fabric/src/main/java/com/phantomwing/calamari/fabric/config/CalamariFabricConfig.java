package com.phantomwing.calamari.fabric.config;

import com.phantomwing.calamari.Calamari;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

/**
 * Fabric config, backed by Cloth Config's AutoConfig. Persists to
 * {@code config/calamari.json}.
 *
 * <p>The option ids and their {@code true} defaults are kept 1:1 with the
 * NeoForge {@code Configuration} ({@code ModConfigSpec}) so a user sees the same
 * switches with identical semantics on both loaders. Cross-loader code reaches
 * these gates through the {@code @ExpectPlatform CommonConfig} bridge, whose
 * Fabric impl delegates here.</p>
 */
@Config(name = Calamari.MOD_ID)
public class CalamariFabricConfig implements ConfigData {
    public static final String GENERATE_STRUCTURE_LOOT_ID = "generate_structure_loot";
    public boolean generate_structure_loot = true;

    public static final String ENABLE_VILLAGER_TRADES_ID = "enable_villager_trades";
    public boolean enable_villager_trades = true;

    public static CalamariFabricConfig get() {
        return AutoConfig.getConfigHolder(CalamariFabricConfig.class).getConfig();
    }

    /**
     * Registers the config holder + serializer. MUST be called before the first
     * {@link #get()} — i.e. at the very start of the Fabric entrypoint, ahead of
     * the loot injection (which reads config at datapack load).
     */
    public static void register() {
        AutoConfig.register(CalamariFabricConfig.class, GsonConfigSerializer::new);
    }

    /** Mirrors NeoForge {@code Configuration.getBooleanConfigurationValue}. */
    public static boolean getBooleanConfigurationValue(String id) {
        CalamariFabricConfig config = get();
        return switch (id) {
            case GENERATE_STRUCTURE_LOOT_ID -> config.generate_structure_loot;
            case ENABLE_VILLAGER_TRADES_ID -> config.enable_villager_trades;
            default -> throw new Error("Invalid setting ID: " + id);
        };
    }
}
