package com.phantomwing.calamari.neoforge;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.neoforge.client.CalamariNeoForgeClient;
import com.phantomwing.calamari.neoforge.condition.ModConditions;
import com.phantomwing.calamari.neoforge.loot.ModLootModifiers;
import com.phantomwing.calamari.neoforge.ui.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;

/**
 * NeoForge entrypoint for Calamari. Performs the loader-agnostic bootstrap via
 * {@link CalamariCommon#init()}, then registers the NeoForge {@code ModConfigSpec}.
 * Villager trades are pure data since 26.1 and need no registration here.
 */
@Mod(CalamariCommon.MOD_ID)
public final class CalamariNeoForge {
    public CalamariNeoForge(IEventBus modEventBus, ModContainer container) {
        CalamariCommon.init();

        // NeoForge Global Loot Modifier serializer (structure-loot replacement).
        ModLootModifiers.register(modEventBus);

        // Datapack load conditions — the generated villager trades are gated on the config.
        ModConditions.register(modEventBus);

        container.registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        // Inject Calamari + Cooked Calamari into the vanilla Food & Drinks tab.
        ModCreativeTabs.register(modEventBus);

        // Client-only: register the in-game config screen. Isolated in a separate
        // class so the dedicated server never loads the referenced client types.
        // 1.21.9/NeoForge 21.9: the public `dist` field became the getDist() accessor.
        if (FMLEnvironment.getDist().isClient()) {
            CalamariNeoForgeClient.init(container);
        }
    }
}
