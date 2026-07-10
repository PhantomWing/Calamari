package com.phantomwing.calamari.forge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    private static final float FOOD_COOKING_EXP = 0.35f;

    public ModRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        // Calamari -> Cooked Calamari (furnace / smoker / campfire).
        foodCookingRecipes(writer, ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get(), FOOD_COOKING_EXP);
    }

    private static void foodCookingRecipes(Consumer<FinishedRecipe> writer, ItemLike material, ItemLike result, float experience) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(material), RecipeCategory.FOOD, result, experience, 200)
                .unlockedBy(getHasName(material), has(material))
                .save(writer);
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(material), RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(getHasName(material), has(material))
                .save(writer, Calamari.MOD_ID + ":" + getItemName(result) + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(material), RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(getHasName(material), has(material))
                .save(writer, Calamari.MOD_ID + ":" + getItemName(result) + "_from_campfire_cooking");
    }
}
