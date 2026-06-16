package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Verifies raw calamari is accepted as cat food (the {@code minecraft:cat_food}
 * tag). Run headless with {@code ./gradlew :neoforge:runGameTest}.
 */
@GameTestHolder("calamari")
@PrefixGameTestTemplate(false)
public class CalamariCatFoodGameTest {

    @GameTest(template = "empty")
    public static void catTreatsCalamariAsFood(GameTestHelper helper) {
        Cat cat = helper.spawn(EntityType.CAT, BlockPos.ZERO);
        if (cat.isFood(new ItemStack(ModItems.CALAMARI.get()))) {
            helper.succeed();
        } else {
            helper.fail("A cat does not treat raw calamari as food (is it in the minecraft:cat_food tag?)");
        }
    }
}
