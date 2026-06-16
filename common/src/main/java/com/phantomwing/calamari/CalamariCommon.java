package com.phantomwing.calamari;

import com.phantomwing.calamari.item.ModItems;
import com.phantomwing.calamari.loot.SquidLootInjection;

/**
 * Common (loader-agnostic) entrypoint for Calamari.
 *
 * <p>Registers the Architectury {@code DeferredRegister}s and wires the
 * loader-agnostic squid loot drops. Loader-specific bootstrap (NeoForge config +
 * datagen, Fabric AutoConfig, villager trades, and creative-tab injection) is
 * performed by the per-loader entrypoints.</p>
 */
public final class CalamariCommon {
    public static final String MOD_ID = Calamari.MOD_ID;

    private CalamariCommon() {
    }

    public static void init() {
        ModItems.register();

        // Squid / glow-squid calamari drops (gated on config, applied via the
        // cross-platform Architectury LootEvent).
        SquidLootInjection.register();
    }
}
