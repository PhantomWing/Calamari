package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.CalamariCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

// GatherDataEvent is a MOD-bus event. 1.21.4 reworked it: there is no more
// includeServer()/includeClient() gating or getExistingFileHelper() — you just
// event.addProvider(provider) and the run args (--all/--client/--server) decide what
// actually emits. The event is also abstract now, so we subscribe to the concrete
// Client subclass (its environment is a full client, so the server-side providers
// added here run fine alongside the model provider).
@EventBusSubscriber(modid = CalamariCommon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new ModRecipeProvider.Runner(output, lookupProvider));
        event.addProvider(new ModItemTagsProvider(
                output, lookupProvider, CompletableFuture.completedFuture(TagsProvider.TagLookup.<Block>empty())));
        event.addProvider(new ModGlobalLootModifierProvider(output, lookupProvider));

        // 1.21.4: block + item models come from a single vanilla-style ModelProvider.
        event.addProvider(new ModModelProvider(output));
    }
}
