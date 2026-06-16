package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.loot.CalamariLootSpec;
import com.phantomwing.calamari.neoforge.loot.ReplaceItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Generates the calamari Global Loot Modifier JSON by iterating the shared
 * {@link CalamariLootSpec} (one GLM per entry, gated on
 * {@code loot_table_id} + {@code random_chance}).
 */
public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, CalamariCommon.MOD_ID);
    }

    @Override
    protected void start() {
        for (CalamariLootSpec.Entry entry : CalamariLootSpec.entries()) {
            LootItemCondition[] conditions = {
                    new LootTableIdCondition.Builder(entry.targetLootTable()).build(),
                    LootItemRandomChanceCondition.randomChance(entry.chance()).build()
            };
            add(entry.id(), new ReplaceItemModifier(
                    conditions,
                    entry.item().get(),
                    entry.removedItems().stream().map(Supplier::get).toList(),
                    entry.minCount(), entry.maxCount()));
        }
    }
}
