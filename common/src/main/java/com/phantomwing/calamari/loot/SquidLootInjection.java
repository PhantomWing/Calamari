package com.phantomwing.calamari.loot;

import com.phantomwing.calamari.item.ModItems;
import com.phantomwing.calamari.platform.CommonConfig;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

/**
 * Cross-platform "squids drop calamari" loot injection, ported from Rustic
 * Delight. NeoForge there used a Global Loot Modifier and Fabric used
 * {@code LootTableEvents.MODIFY}; Architectury's {@link LootEvent#MODIFY_LOOT_TABLE}
 * unifies both into this single implementation.
 *
 * <p>The {@link CommonConfig#squidsDropCalamari()} gate is evaluated at loot-table
 * load time (datapack load / {@code /reload}), so toggling the config and
 * reloading takes effect without a restart. Squid and glow squid each gain a
 * 1–2 calamari drop.</p>
 */
public final class SquidLootInjection {
    private SquidLootInjection() {
    }

    public static void register() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            if (!builtin || !CommonConfig.squidsDropCalamari()) {
                return;
            }

            if (EntityType.SQUID.getDefaultLootTable().equals(key)
                    || EntityType.GLOW_SQUID.getDefaultLootTable().equals(key)) {
                LootPool.Builder pool = LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ModItems.CALAMARI.get()))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f)));
                context.addPool(pool);
            }
        });
    }
}
