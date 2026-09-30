package com.phantomwing.calamari.loot;

import com.phantomwing.calamari.item.ModItems;
import com.phantomwing.calamari.platform.CommonConfig;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

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
        // Architectury 22 (26.3) hands the registries first.
        LootEvent.MODIFY_LOOT_TABLE.register((registries, key, context, builtin) -> {
            if (!builtin || !CommonConfig.generateStructureLoot()) {
                return;
            }

            // 1.21.2+: EntityType#getDefaultLootTable returns Optional<ResourceKey<LootTable>>.
            if (key.equals(EntityTypes.GUARDIAN.getDefaultLootTable().orElse(null))) {
                addCalamariPool(context, 1, 1, 0.25f);
            } else if (key.equals(EntityTypes.ELDER_GUARDIAN.getDefaultLootTable().orElse(null))) {
                addCalamariPool(context, 1, 2, 1.0f);
            } else if (key.equals(EntityTypes.DOLPHIN.getDefaultLootTable().orElse(null))) {
                // Dolphins actually do hunt cephalopods, so a small calamari drop fits
                // alongside the vanilla cod drop.
                addCalamariPool(context, 1, 1, 0.25f);
            } else if (BuiltInLootTables.CAT_MORNING_GIFT.equals(key)) {
                addCalamariPool(context, 1, 1, 0.25f);
            }
        });
    }

    private static void addCalamariPool(LootEvent.LootTableModificationContext context, int min, int max, float chance) {
        // 26.3: loot numbers are Holder<ContextIntProvider>, built by ContextIntProviders.
        context.addPool(LootPool.lootPool()
                .setRolls(ContextIntProviders.exactly(1))
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .add(LootItem.lootTableItem(ModItems.CALAMARI.get()))
                .apply(SetItemCountFunction.setCount(ContextIntProviders.between(min, max))));
    }
}
