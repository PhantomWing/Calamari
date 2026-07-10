package com.phantomwing.calamari.forge.datagen;

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
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    // Conventional Forge food tags (the `forge:` namespace on 1.20.1), as used by
    // Rustic Delight and other food mods.
    private static final TagKey<Item> FORGE_RAW_FISHES =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge", "raw_fishes"));
    private static final TagKey<Item> FORGE_COOKED_FISHES =
            TagKey.create(Registries.ITEM, new ResourceLocation("forge", "cooked_fishes"));

    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Calamari.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Dolphins treat fish as feed: feeding an item in minecraft:fishes triggers
        // the lead-to-treasure behaviour. Cooked calamari is included for parity
        // with vanilla cooked cod/salmon.
        tag(ItemTags.FISHES).add(ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get());

        // Conventional Forge food tags for cross-mod integration.
        tag(FORGE_RAW_FISHES).add(ModItems.CALAMARI.get());
        tag(FORGE_COOKED_FISHES).add(ModItems.COOKED_CALAMARI.get());
    }
}
