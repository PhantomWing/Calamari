package com.phantomwing.calamari.loot;

import com.phantomwing.calamari.item.ModItems;
import com.phantomwing.calamari.platform.CommonConfig;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Cross-platform calamari loot <em>additions</em> (as opposed to the replacements
 * in {@link CalamariLootSpec}). Adds a chance-gated raw-calamari pool to a handful
 * of vanilla loot tables via Architectury's {@link LootEvent#MODIFY_LOOT_TABLE}
 * (the same load-time add-a-pool mechanism as the squid drops). Gated on
 * {@link CommonConfig#generateStructureLoot()}.
 */
public final class CalamariLootAdditions {
    private CalamariLootAdditions() {
    }

    public static void register() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            if (!builtin || !CommonConfig.generateStructureLoot()) {
                return;
            }

            if (EntityType.GUARDIAN.getDefaultLootTable().equals(key)) {
                addCalamariPool(context, 1, 1, 0.25f);
            } else if (EntityType.ELDER_GUARDIAN.getDefaultLootTable().equals(key)) {
                addCalamariPool(context, 1, 2, 1.0f);
            } else if (BuiltInLootTables.CAT_MORNING_GIFT.equals(key)) {
                addCalamariPool(context, 1, 1, 0.25f);
            }
        });
    }

    private static void addCalamariPool(LootEvent.LootTableModificationContext context, int min, int max, float chance) {
        context.addPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(ModItems.CALAMARI.get()))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max))));
    }
}
