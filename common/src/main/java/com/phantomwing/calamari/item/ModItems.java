package com.phantomwing.calamari.item;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.food.ModFoodProperties;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Calamari.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> CALAMARI = ITEMS.register("calamari", () ->
            new Item(new Item.Properties().food(ModFoodProperties.CALAMARI)));
    public static final RegistrySupplier<Item> COOKED_CALAMARI = ITEMS.register("cooked_calamari", () ->
            new Item(new Item.Properties().food(ModFoodProperties.COOKED_CALAMARI)));

    public static void register() {
        ITEMS.register();
    }
}
