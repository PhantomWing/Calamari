package com.phantomwing.calamari.food;

import net.minecraft.world.food.FoodProperties;

/**
 * Food values for the calamari items. Carried over from Rustic Delight so the
 * standalone mod eats identically.
 */
public final class ModFoodProperties {
    public static final FoodProperties CALAMARI = new FoodProperties.Builder()
            .nutrition(2).saturationMod(0.2F).build();

    public static final FoodProperties COOKED_CALAMARI = new FoodProperties.Builder()
            .nutrition(5).saturationMod(0.6F).build();

    private ModFoodProperties() {
    }
}
