package com.phantomwing.calamari.fabric;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.fabric.condition.ConfigBooleanResourceCondition;
import com.phantomwing.calamari.fabric.config.CalamariFabricConfig;
import com.phantomwing.calamari.fabric.loot.CalamariLootTableId;
import com.phantomwing.calamari.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Fabric entrypoint for Calamari. Registers the AutoConfig holder first (the loot
 * injection reads config at datapack load), then delegates the loader-agnostic
 * bootstrap to {@link CalamariCommon#init()}.
 */
public final class CalamariFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // MUST be first: the loot injection reads config very early (datapack load).
        CalamariFabricConfig.register();

        CalamariCommon.init();

        // Handler for the calamari:config_boolean load condition carried by the generated
        // villager-trade JSON (26.1 made trades datapack entries).
        ResourceConditions.register(ConfigBooleanResourceCondition.TYPE);

        // Inject Calamari + Cooked Calamari into the vanilla Food & Drinks tab,
        // right after Cooked Rabbit (and so before Raw Cod).
        // 26.1: fabric-item-group-api-v1 was replaced by fabric-creative-tab-api-v1 —
        // ItemGroupEvents#modifyEntriesEvent -> CreativeModeTabEvents#modifyOutputEvent,
        // and the entries' addAfter is now the output's insertAfter.
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output ->
                output.insertAfter(Items.COOKED_RABBIT, ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get()));

        // Vanilla LootTable carries no id; stamp each one once all tables load so
        // the LootTableMixin (Fabric equivalent of the NeoForge GLM) knows which
        // table is rolling. Mirrors the GLM loot_table_id condition.
        LootTableEvents.ALL_LOADED.register((resourceManager, lootRegistry) ->
                lootRegistry.entrySet().forEach(e -> {
                    LootTable table = e.getValue();
                    if (table instanceof CalamariLootTableId holder) {
                        // 1.21.11: ResourceKey#location was renamed to identifier(),
                        // alongside ResourceLocation -> Identifier.
                        holder.calamari$setLootTableId(e.getKey().identifier());
                    }
                }));
    }
}
