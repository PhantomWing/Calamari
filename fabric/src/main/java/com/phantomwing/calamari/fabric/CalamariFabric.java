package com.phantomwing.calamari.fabric;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.fabric.config.CalamariFabricConfig;
import com.phantomwing.calamari.fabric.loot.CalamariLootTableId;
import com.phantomwing.calamari.fabric.villager.ModVillagerTrades;
import com.phantomwing.calamari.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Fabric entrypoint for Calamari. Registers the AutoConfig holder first (the loot
 * injection reads config at datapack load), delegates the loader-agnostic
 * bootstrap to {@link CalamariCommon#init()}, then registers the Fabric villager
 * trades.
 */
public final class CalamariFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // MUST be first: the loot injection reads config very early (datapack load).
        CalamariFabricConfig.register();

        CalamariCommon.init();

        ModVillagerTrades.register();

        // Inject Calamari + Cooked Calamari into the vanilla Food & Drinks tab,
        // right after Cooked Rabbit (and so before Raw Cod).
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(entries ->
                entries.addAfter(Items.COOKED_RABBIT, ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get()));

        // Vanilla LootTable carries no id; stamp each one once all tables load so
        // the LootTableMixin (Fabric equivalent of the NeoForge GLM) knows which
        // table is rolling. Mirrors the GLM loot_table_id condition.
        LootTableEvents.ALL_LOADED.register((resourceManager, lootRegistry) ->
                lootRegistry.entrySet().forEach(e -> {
                    LootTable table = e.getValue();
                    if (table instanceof CalamariLootTableId holder) {
                        holder.calamari$setLootTableId(e.getKey().location());
                    }
                }));
    }
}
