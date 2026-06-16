package com.phantomwing.calamari.loot;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
                int count = Math.max(1, UniformGenerator.between(minCount, maxCount).getInt(context));
                generatedLoot.set(i, new ItemStack(item, count));
            }
        }
    }
}
