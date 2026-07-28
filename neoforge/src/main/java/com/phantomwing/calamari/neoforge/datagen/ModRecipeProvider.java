package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

/**
 * 1.21.2 reworked {@link RecipeProvider}: the builder helpers ({@code has},
 * {@code getHasName}, ...) are now instance methods, {@link #buildRecipes()} takes
 * no arguments (the {@link RecipeOutput} is a field), and a nested
 * {@link RecipeProvider.Runner} is what the data generator actually registers.
 */
public class ModRecipeProvider extends RecipeProvider {
    private static final float FOOD_COOKING_EXP = 0.35f;

    private final RecipeOutput output;

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.output = output;
    }

    @Override
    protected void buildRecipes() {
        // Calamari -> Cooked Calamari (furnace / smoker / campfire).
        foodCookingRecipes(ModItems.CALAMARI.get(), ModItems.COOKED_CALAMARI.get(), FOOD_COOKING_EXP);
    }

    private void foodCookingRecipes(ItemLike material, ItemLike result, float experience) {
        String resultName = BuiltInRegistries.ITEM.getKey(result.asItem()).getPath();
        // 26.1: smelting() (unlike smoking()/campfireCooking()) also takes the recipe-book
        // grouping. FOOD keeps these in the same book section as the vanilla cooked fish.
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(material), RecipeCategory.FOOD,
                        CookingBookCategory.FOOD, result, experience, 200)
                .unlockedBy(getHasName(material), has(material))
                .save(this.output);
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(material), RecipeCategory.FOOD, result, experience, 100)
                .unlockedBy(getHasName(material), has(material))
                .save(this.output, Calamari.MOD_ID + ":" + resultName + "_from_smoking");
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(material), RecipeCategory.FOOD, result, experience, 600)
                .unlockedBy(getHasName(material), has(material))
                .save(this.output, Calamari.MOD_ID + ":" + resultName + "_from_campfire_cooking");
    }

    public static final class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Calamari Recipes";
        }
    }
}
