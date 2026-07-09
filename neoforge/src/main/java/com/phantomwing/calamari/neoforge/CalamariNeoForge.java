package com.phantomwing.calamari.neoforge;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.food.CalamariPetFood;
import com.phantomwing.calamari.neoforge.loot.ModLootModifiers;
import com.phantomwing.calamari.neoforge.ui.ModCreativeTabs;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Forge entrypoint for Calamari on 1.20.1 (this jar also runs on NeoForge 1.20.1,
 * which is a Forge-compatible soft-fork). Performs the loader-agnostic bootstrap via
 * {@link CalamariCommon#init()}, then registers the Forge {@code ForgeConfigSpec} and
 * the Global Loot Modifier serializer. Villager trades are wired separately via
 * {@code @Mod.EventBusSubscriber}.
 */
@Mod(CalamariCommon.MOD_ID)
public final class CalamariNeoForge {
    public CalamariNeoForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Hand the mod event bus to Architectury so its DeferredRegisters (invoked
        // in CalamariCommon.init below) can register on the Forge platform.
        EventBuses.registerModEventBus(CalamariCommon.MOD_ID, modEventBus);

        CalamariCommon.init();

        // Forge Global Loot Modifier serializer (structure-loot replacement).
        ModLootModifiers.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Configuration.COMMON_CONFIG);

        // Inject Calamari + Cooked Calamari into the vanilla Food & Drinks tab.
        ModCreativeTabs.register(modEventBus);

        // Append calamari to the hardcoded cat/ocelot tempt ingredients (after
        // registration, on the main thread).
        modEventBus.addListener((FMLCommonSetupEvent event) -> event.enqueueWork(CalamariPetFood::register));
    }
}
