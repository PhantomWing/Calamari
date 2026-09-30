package com.phantomwing.calamari.loot;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.List;

/**
 * Loader-agnostic implementation of the calamari loot replacement. Both the
 * NeoForge {@code ReplaceItemModifier} GLM and the Fabric loot mixin delegate
 * here, so behaviour is identical on both loaders.
 *
 * <p>The per-loader caller handles the config gate and the loot-table-id /
 * random-chance checks (mirroring the GLM {@code conditions(...)}); this method
 * implements only the post-roll mutation.</p>
 */
public final class CalamariLootAlgorithms {
    private CalamariLootAlgorithms() {
    }

    /**
     * Replaces every rolled stack whose item is in {@code removedItems} with a
     * freshly-rolled {@code [minCount, maxCount]} of {@code item}, in place.
     */
    public static void applyReplaceItem(ObjectArrayList<ItemStack> generatedLoot, LootContext context,
                                        Item item, List<Item> removedItems, int minCount, int maxCount) {
        for (int i = 0; i < generatedLoot.size(); i++) {
            ItemStack stack = generatedLoot.get(i);
            if (removedItems.stream().anyMatch(stack::is)) {
                // 26.3 moved UniformGenerator under providers.number.ints and made it Holder-based; a
                // uniform roll over the loot context's own random is the same thing without the wrapper.
                int count = Math.max(1, Mth.nextInt(context.getRandom(), minCount, maxCount));
                generatedLoot.set(i, new ItemStack(item, count));
            }
        }
    }
}
