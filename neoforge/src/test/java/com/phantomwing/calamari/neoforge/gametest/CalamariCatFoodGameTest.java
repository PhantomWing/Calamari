package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.feline.Cat;
import net.minecraft.world.item.ItemStack;

/**
 * Verifies raw calamari is accepted as cat food (the {@code minecraft:cat_food}
 * tag). Registered by {@link CalamariGameTests} — 1.21.5 replaced the game-test
 * annotations with registry-driven test functions, so this is a plain static method
 * used as a {@code Consumer<GameTestHelper>}.
 * Run headless with {@code ./gradlew :neoforge:runGameTest}.
 */
public class CalamariCatFoodGameTest {

    public static void catTreatsCalamariAsFood(GameTestHelper helper) {
        Cat cat = helper.spawn(EntityType.CAT, BlockPos.ZERO);
        if (cat.isFood(new ItemStack(ModItems.CALAMARI.get()))) {
            helper.succeed();
        } else {
            helper.fail(Component.literal("A cat does not treat raw calamari as food (is it in the minecraft:cat_food tag?)"));
        }
    }
}
