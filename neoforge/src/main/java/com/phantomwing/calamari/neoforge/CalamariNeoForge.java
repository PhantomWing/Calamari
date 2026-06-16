package com.phantomwing.calamari.neoforge;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.neoforge.client.CalamariNeoForgeClient;
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
 * Villager trades are wired separately via {@code @EventBusSubscriber}.
 */
@Mod(CalamariCommon.MOD_ID)
public final class CalamariNeoForge {
    public CalamariNeoForge(IEventBus modEventBus, ModContainer container) {
        CalamariCommon.init();

        // NeoForge Global Loot Modifier serializer (structure-loot replacement).
        ModLootModifiers.register(modEventBus);

        container.registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        // Inject Calamari + Cooked Calamari into the vanilla Food & Drinks tab.
        ModCreativeTabs.register(modEventBus);

        // Client-only: register the in-game config screen. Isolated in a separate
        // class so the dedicated server never loads the referenced client types.
        if (FMLEnvironment.dist.isClient()) {
            CalamariNeoForgeClient.init(container);
        }
    }
}
