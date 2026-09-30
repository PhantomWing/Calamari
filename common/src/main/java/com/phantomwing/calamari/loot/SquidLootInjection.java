package com.phantomwing.calamari.loot;

import com.phantomwing.calamari.item.ModItems;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/**
 * Cross-platform "squids drop calamari" loot injection, ported from Rustic
 * Delight. NeoForge there used a Global Loot Modifier and Fabric used
 * {@code LootTableEvents.MODIFY}; Architectury's {@link LootEvent#MODIFY_LOOT_TABLE}
 * unifies both into this single implementation.
 *
 * <p>This is the core premise of the mod, so it has no config toggle — squid and
 * glow squid always gain a 1–2 calamari drop.</p>
 */
public final class SquidLootInjection {
    private SquidLootInjection() {
    }

    public static void register() {
        // Architectury 22 (26.3) hands the registries first.
        LootEvent.MODIFY_LOOT_TABLE.register((registries, key, context, builtin) -> {
            if (!builtin) {
                return;
            }

            // 1.21.2+: EntityType#getDefaultLootTable returns Optional<ResourceKey<LootTable>>.
            if (key.equals(EntityTypes.SQUID.getDefaultLootTable().orElse(null))
                    || key.equals(EntityTypes.GLOW_SQUID.getDefaultLootTable().orElse(null))) {
                // 26.3: loot numbers are Holder<ContextIntProvider>, built by ContextIntProviders.
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(ContextIntProviders.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.CALAMARI.get()))
                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 2)));
                context.addPool(pool);
            }
        });
    }
}
