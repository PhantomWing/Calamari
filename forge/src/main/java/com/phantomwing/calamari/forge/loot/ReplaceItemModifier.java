package com.phantomwing.calamari.forge.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phantomwing.calamari.loot.CalamariLootAlgorithms;
import com.phantomwing.calamari.platform.CommonConfig;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

/**
 * Global Loot Modifier that replaces rolled stacks of {@code removedItems} with a
 * freshly-rolled {@code [minCount, maxCount]} of {@code item}. The
 * {@code loot_table_id} + {@code random_chance} conditions are evaluated by Forge's
 * {@code LootModifier.apply} before {@link #doApply} runs (the datagen provider
 * attaches them). The Fabric twin is {@code LootTableMixin}.
 *
 * <p>1.20.1 Forge GLMs use a plain {@link Codec} (not {@code MapCodec}).</p>
 */
public class ReplaceItemModifier extends LootModifier {
    public static final Supplier<Codec<ReplaceItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
                    .and(ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("removed_item").forGetter(m -> m.removedItems))
                    .and(Codec.INT.fieldOf("min_count").forGetter(m -> m.minCount))
                    .and(Codec.INT.fieldOf("max_count").forGetter(m -> m.maxCount))
                    .apply(inst, ReplaceItemModifier::new)));

    private final Item item;
    private final List<Item> removedItems;
    private final int minCount;
    private final int maxCount;

    public ReplaceItemModifier(LootItemCondition[] conditions, Item item, List<Item> removedItems, int minCount, int maxCount) {
        super(conditions);
        this.item = item;
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
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
