package com.phantomwing.calamari.forge.datagen;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.loot.CalamariLootSpec;
import com.phantomwing.calamari.forge.loot.ReplaceItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.function.Supplier;

/**
 * Generates the calamari Global Loot Modifier JSON by iterating the shared
 * {@link CalamariLootSpec} (one GLM per entry, gated on
 * {@code loot_table_id} + {@code random_chance}).
 */
public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, CalamariCommon.MOD_ID);
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
