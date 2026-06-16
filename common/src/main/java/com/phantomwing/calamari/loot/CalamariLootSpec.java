package com.phantomwing.calamari.loot;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Supplier;

/**
 * Single, loader-agnostic source of truth for every calamari structure-loot
 * replacement. Both the NeoForge Global Loot Modifier datagen provider and the
 * Fabric loot mixin iterate this list, so the two loaders behave identically.
 *
 * <p>Each {@link Entry} replaces — with probability {@code chance} — any rolled
 * stack of one of {@code removedItems} with {@code [minCount, maxCount]} of
 * {@code item}. (Unlike a transmute, the new count is rolled fresh, so e.g. one
 * raw cod becomes 1–3 calamari.)</p>
 */
public final class CalamariLootSpec {
    private CalamariLootSpec() {
    }

    /**
     * @param id              GLM datagen JSON file name.
     * @param targetLootTable vanilla loot table this applies to ({@code minecraft:...}).
     * @param chance          {@code random_chance} probability.
     * @param item            the replacement item.
     * @param minCount        min replacement count.
     * @param maxCount        max replacement count.
     * @param removedItems    items eligible to be replaced.
     */
    public record Entry(String id, ResourceLocation targetLootTable, float chance,
                        Supplier<Item> item, int minCount, int maxCount, List<Supplier<Item>> removedItems) {
    }

    private static ResourceLocation mc(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    /** The complete ordered list of loot replacements. */
    public static List<Entry> entries() {
        return List.of(
                // Village fisher chest: raw cod -> 1–3 raw calamari (50% chance).
                new Entry("calamari_from_village_fisher", mc("chests/village/village_fisher"), 0.5f,
                        ModItems.CALAMARI::get, 1, 3, List.of(() -> Items.COD)),

                // Hero of the Village fisherman gift: raw cod -> 1 raw calamari
                // (50% chance), so fishermen can gift calamari alongside cod/salmon.
                new Entry("calamari_from_fisherman_gift", mc("gameplay/hero_of_the_village/fisherman_gift"), 0.5f,
                        ModItems.CALAMARI::get, 1, 1, List.of(() -> Items.COD)),

                // Buried treasure: cooked cod -> 2–4 cooked calamari (50% chance),
                // matching the vanilla cooked-cod stack size.
                new Entry("cooked_calamari_from_buried_treasure", mc("chests/buried_treasure"), 0.5f,
                        ModItems.COOKED_CALAMARI::get, 2, 4, List.of(() -> Items.COOKED_COD))
        );
    }
}
