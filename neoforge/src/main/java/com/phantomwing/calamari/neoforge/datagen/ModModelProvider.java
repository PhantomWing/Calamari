package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;

/**
 * Item model datagen for 1.21.4. The old NeoForge {@code ItemModelProvider} (and its
 * {@code BlockStateProvider} sibling) are gone — 1.21.4 generates block and item models
 * through a single vanilla {@link ModelProvider}, and item models now emit to
 * {@code assets/<namespace>/items/*.json}.
 *
 * <p>Calamari only has flat (generated) items, so both are emitted with
 * {@link ItemModelGenerators#generateFlatItem}.</p>
 */
public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Calamari.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        flatItem(itemModels, ModItems.CALAMARI);
        flatItem(itemModels, ModItems.COOKED_CALAMARI);
    }

    private static void flatItem(ItemModelGenerators itemModels, RegistrySupplier<Item> item) {
        itemModels.generateFlatItem(item.get(), ModelTemplates.FLAT_ITEM);
    }
}
