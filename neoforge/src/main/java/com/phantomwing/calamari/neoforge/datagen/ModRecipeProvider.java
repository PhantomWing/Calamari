package com.phantomwing.calamari.neoforge.datagen;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.Set;

/**
 * 1.21.2 reworked {@link RecipeProvider}: the builder helpers ({@code has},
 * {@code getHasName}, ...) are now instance methods and {@link #buildRecipes()} takes
 * no arguments. 26.3 made recipes a reloadable datapack registry: the provider is built
 * over bootstrap contexts rather than run as a DataProvider, and owns its {@code output}.
 */
public class ModRecipeProvider extends RecipeProvider {
    private static final float FOOD_COOKING_EXP = 0.35f;

    protected ModRecipeProvider(BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        super(recipes, advancements);
    }

    /** Recipes also emit their unlock advancements, hence a bootstrap over both registries. */
    public static MultiRegistryBootstrap create() {
        return new MultiRegistryBootstrap() {
            @Override
            public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
                return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
            }

            @Override
            public void run(BootstrapGetter getter) {
                new ModRecipeProvider(getter.get(Registries.RECIPE), getter.get(Registries.ADVANCEMENT))
                        .buildRecipes();
            }
        };
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
}
