package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
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
    }
}
