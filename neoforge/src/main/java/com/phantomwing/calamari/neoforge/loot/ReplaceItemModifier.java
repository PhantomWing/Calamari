package com.phantomwing.calamari.neoforge.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phantomwing.calamari.loot.CalamariLootAlgorithms;
import com.phantomwing.calamari.platform.CommonConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * Global Loot Modifier that replaces rolled stacks of {@code removedItems} with a
 * freshly-rolled {@code [minCount, maxCount]} of {@code item}. The
 * {@code loot_table_id} + {@code random_chance} conditions are evaluated by
 * NeoForge's {@code LootModifier.apply} before {@link #doApply} runs (the datagen
 * provider attaches them). The Fabric twin is {@code LootTableMixin}.
 */
public class ReplaceItemModifier extends LootModifier {
    public static final Supplier<MapCodec<ReplaceItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst).and(
                    inst.group(
                            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter((m) -> m.item),
                            BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("removed_item").forGetter((m) -> m.removedItems),
                            Codec.INT.fieldOf("min_count").forGetter((m) -> m.minCount),
                            Codec.INT.fieldOf("max_count").forGetter((m) -> m.maxCount)
                    )
            ).apply(inst, ReplaceItemModifier::new)));

    private final Item item;
    private final List<Item> removedItems;
    private final int minCount;
    private final int maxCount;

    public ReplaceItemModifier(LootItemCondition[] conditions, ItemLike item, List<Item> removedItems, int minCount, int maxCount) {
        super(conditions);
        this.item = item.asItem();
        this.removedItems = removedItems;
        this.minCount = minCount;
        this.maxCount = maxCount;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(@NotNull ObjectArrayList<ItemStack> generatedLoot, @NotNull LootContext context) {
        if (!CommonConfig.generateStructureLoot()) {
            return generatedLoot;
        }

        CalamariLootAlgorithms.applyReplaceItem(generatedLoot, context, this.item, this.removedItems,
                this.minCount, this.maxCount);
        return generatedLoot;
    }

    @Override
    public @NotNull MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
