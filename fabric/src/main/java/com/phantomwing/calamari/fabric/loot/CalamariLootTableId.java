package com.phantomwing.calamari.fabric.loot;

import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

/**
 * Duck interface mixed onto {@code LootTable} (via {@code LootTableMixin}) so a
 * rolled table can report its own id. Vanilla 1.21.1 {@code LootTable} carries no
 * id; the Fabric loot mixin needs it to know which {@code CalamariLootSpec}
 * entries apply (mirroring the NeoForge GLM {@code loot_table_id} condition). The
 * id is stamped once after all tables load, from {@code CalamariFabric}.
 */
public interface CalamariLootTableId {
    @Nullable
    Identifier calamari$getLootTableId();

    void calamari$setLootTableId(Identifier id);
}
