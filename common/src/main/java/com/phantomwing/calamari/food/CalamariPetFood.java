package com.phantomwing.calamari.food;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Makes raw calamari a valid cat/ocelot food on 1.20.1.
 *
 * <p>On 1.21 this is done with the {@code cat_food}/{@code ocelot_food} item tags,
 * but those only exist from 1.20.5 — on 1.20.1 the tempt/breed/trust ingredient is
 * a hardcoded {@code Ingredient} field on {@code Cat}/{@code Ocelot}. The common
 * access widener opens those fields; this appends calamari to the vanilla
 * cod+salmon ingredient. Must run after item registration (wired from each loader's
 * setup phase).</p>
 */
public final class CalamariPetFood {
    private CalamariPetFood() {
    }

    public static void register() {
        Ingredient withCalamari = Ingredient.of(Items.COD, Items.SALMON, ModItems.CALAMARI.get());
        Cat.TEMPT_INGREDIENT = withCalamari;
        Ocelot.TEMPT_INGREDIENT = withCalamari;
    }
}
