package com.phantomwing.calamari.forge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Calamari.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.CALAMARI);
        simpleItem(ModItems.COOKED_CALAMARI);
    }

    private void simpleItem(RegistrySupplier<Item> item) {
        String path = item.getId().getPath();
        withExistingParent(path, new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(Calamari.MOD_ID, "item/" + path));
    }
}
