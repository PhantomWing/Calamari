package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.Optional;

/**
 * Verifies the three calamari cooking recipes (furnace / smoker / campfire) are
 * actually loaded by the server. Registered by {@link CalamariGameTests}.
 *
 * <p>Recipes are datapack-driven, so a wrong output folder or a stale JSON shape
 * fails silently: the game simply loads fewer recipes and the item becomes
 * uncraftable, with nothing in the log. This test pins that down.</p>
 */
public class CalamariRecipeGameTest {

    public static void cookingRecipesAreLoaded(GameTestHelper helper) {
        RecipeManager recipes = helper.getLevel().getServer().getRecipeManager();

        for (String id : new String[]{
                "cooked_calamari",
                "cooked_calamari_from_smoking",
                "cooked_calamari_from_campfire_cooking"}) {
            ResourceKey<Recipe<?>> key = ResourceKey.create(Registries.RECIPE, Calamari.resourceLocation(id));
            Optional<RecipeHolder<?>> holder = recipes.byKey(key);

            if (holder.isEmpty()) {
                helper.fail(Component.literal("Recipe " + key.location() + " is not loaded"));
                return;
            }
        }

        helper.succeed();
    }
}
