package com.phantomwing.calamari.fabric.mixin;

import com.phantomwing.calamari.fabric.loot.CalamariLootTableId;
import com.phantomwing.calamari.loot.CalamariLootAlgorithms;
import com.phantomwing.calamari.loot.CalamariLootSpec;
import com.phantomwing.calamari.platform.CommonConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Supplier;

/**
 * Fabric parity for the NeoForge {@code ReplaceItemModifier} GLM. Fabric has no
 * post-roll loot API, so this mixin reproduces the GLM behaviour at the loot-roll
 * site: it injects at {@code RETURN} of the private {@code getRandomItems(LootContext)}
 * (the {@code ObjectArrayList} every chest/container roll funnels through) and
 * mutates it exactly like {@code doApply}.
 *
 * <p>Per spec entry the GLM conditions ({@code loot_table_id} + {@code random_chance})
 * and the config gate are evaluated here, then the shared
 * {@link CalamariLootAlgorithms} runs — so the result matches NeoForge.</p>
 */
@Mixin(LootTable.class)
public abstract class LootTableMixin implements CalamariLootTableId {
    @Unique
    @Nullable
    private Identifier calamari$lootTableId;

    @Override
    @Nullable
    public Identifier calamari$getLootTableId() {
        return this.calamari$lootTableId;
    }

    @Override
    public void calamari$setLootTableId(Identifier id) {
        this.calamari$lootTableId = id;
    }

    @Inject(
            method = "getRandomItems(Lnet/minecraft/world/level/storage/loot/LootContext;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
            at = @At("RETURN")
    )
    private void calamari$applyLoot(LootContext context, CallbackInfoReturnable<ObjectArrayList<ItemStack>> cir) {
        Identifier tableId = this.calamari$lootTableId;
        ObjectArrayList<ItemStack> generatedLoot = cir.getReturnValue();
        if (tableId == null || generatedLoot == null || !CommonConfig.generateStructureLoot()) {
            return;
        }

        RandomSource random = context.getRandom();
        for (CalamariLootSpec.Entry entry : CalamariLootSpec.entries()) {
            // GLM condition: loot_table_id — only entries for this table.
            if (!entry.targetLootTable().equals(tableId)) {
                continue;
            }
            // GLM condition: random_chance(chance) — same draw as LootItemRandomChanceCondition.
            if (random.nextFloat() >= entry.chance()) {
                continue;
            }
            List<Item> removed = entry.removedItems().stream().map(Supplier::get).toList();
            CalamariLootAlgorithms.applyReplaceItem(generatedLoot, context, entry.item().get(), removed,
                    entry.minCount(), entry.maxCount());
        }
    }
}
