package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    // Conventional seafood food tags (the `c:` namespace), as used by Rustic
    // Delight and other food mods.
    private static final TagKey<Item> C_FOODS_RAW_FISH =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/raw_fish"));
    private static final TagKey<Item> C_FOODS_COOKED_FISH =
            TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "foods/cooked_fish"));

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Calamari.MOD_ID, existingFileHelper);
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

        // Conventional seafood food tags for cross-mod integration.
        tag(C_FOODS_RAW_FISH).add(ModItems.CALAMARI.get());
        tag(C_FOODS_COOKED_FISH).add(ModItems.COOKED_CALAMARI.get());
    }
}
