package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.CalamariCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

// GatherDataEvent fires on the MOD bus. Since 1.21.2 @EventBusSubscriber defaults to
// the GAME bus, so the bus must be set explicitly or datagen never runs.
@EventBusSubscriber(modid = CalamariCommon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModRecipeProvider.Runner(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModItemTagsProvider(
                output, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.<Block>empty()), existingFileHelper));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifierProvider(output, lookupProvider));

        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
    }
}
