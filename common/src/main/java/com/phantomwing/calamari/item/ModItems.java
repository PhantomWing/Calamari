package com.phantomwing.calamari.item;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.food.ModFoodProperties;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Calamari.MOD_ID, Registries.ITEM);

    // 1.21.2+: an item's registry key must be set on its Properties (Item.Properties#setId).
    public static final RegistrySupplier<Item> CALAMARI = ITEMS.register("calamari", () ->
            new Item(new Item.Properties().food(ModFoodProperties.CALAMARI).setId(itemKey("calamari"))));
    public static final RegistrySupplier<Item> COOKED_CALAMARI = ITEMS.register("cooked_calamari", () ->
            new Item(new Item.Properties().food(ModFoodProperties.COOKED_CALAMARI).setId(itemKey("cooked_calamari"))));

    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Calamari.resourceLocation(name));
    }

    public static void register() {
        ITEMS.register();
    }
}
