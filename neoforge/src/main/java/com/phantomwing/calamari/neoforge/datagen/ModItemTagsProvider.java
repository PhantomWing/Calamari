package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
// 1.21.6: vanilla net.minecraft.data.tags.ItemTagsProvider was removed; NeoForge's
// replacement drops the block-tag TagLookup ctor param (block->item tag copying is
// separate, and this provider only adds item tags directly).
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    // Conventional seafood food tags (the `c:` namespace), as used by Rustic
    // Delight and other food mods.
    private static final TagKey<Item> C_FOODS_RAW_FISH =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "foods/raw_fish"));
    private static final TagKey<Item> C_FOODS_COOKED_FISH =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "foods/cooked_fish"));
    private static final TagKey<Item> C_FOODS_SEAFOOD =
            TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "foods/seafood"));

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Calamari.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Conventional `c:foods` so other mods recognise the calamari items as food.
        tag(Tags.Items.FOODS)
                .add(ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get());

        // Raw calamari counts as cat/ocelot food, like raw cod and salmon do.
        tag(ItemTags.CAT_FOOD).add(ModItems.CALAMARI.get());
        tag(ItemTags.OCELOT_FOOD).add(ModItems.CALAMARI.get());

        // Wolves accept raw and cooked calamari as food (the vanilla wolf_food tag
        // contains both raw and cooked variants of fish).
        tag(ItemTags.WOLF_FOOD).add(ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get());

        // Dolphins treat fish as feed: feeding an item in minecraft:fishes triggers
        // the lead-to-treasure behaviour (Dolphin#mobInteract checks ItemTags.FISHES).
        // Squid is a dolphin's natural prey, so calamari fits. Vanilla keeps cooked
        // cod/salmon in this tag too, so cooked calamari is included for parity.
        tag(ItemTags.FISHES).add(ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get());

        // Conventional seafood food tags for cross-mod integration.
        tag(C_FOODS_RAW_FISH).add(ModItems.CALAMARI.get());
        tag(C_FOODS_COOKED_FISH).add(ModItems.COOKED_CALAMARI.get());
        tag(C_FOODS_SEAFOOD).add(ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get());
    }
}
