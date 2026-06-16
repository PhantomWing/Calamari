package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(net.minecraft.data.PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Calamari.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.CALAMARI);
        simpleItem(ModItems.COOKED_CALAMARI);
    }

    private void simpleItem(RegistrySupplier<Item> item) {
        String path = item.getId().getPath();
        withExistingParent(path, ResourceLocation.withDefaultNamespace("item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(Calamari.MOD_ID, "item/" + path));
    }
}
