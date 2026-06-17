package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.item.ModItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

/**
 * Game tests for the calamari loot integration — both the GLM replacements
 * (cod -> calamari) and the LootEvent additions (guardian drops, cat gift). Run
 * headless with {@code ./gradlew :neoforge:runGameTest}. Uses a tiny empty
 * structure ({@code calamari:empty}) since this NeoForge version has no
 * {@code @EmptyTemplate} helper.
 */
@GameTestHolder("calamari")
@PrefixGameTestTemplate(false)
public class CalamariLootGameTest {

    /** Village fisher chest: raw cod -> raw calamari. */
    @GameTest(template = "empty")
    public static void villageFisherYieldsCalamari(GameTestHelper helper) {
        assertTableYields(helper, "chests/village/village_fisher", LootContextParamSets.CHEST, ModItems.CALAMARI.get());
    }

    /** Hero of the Village fisherman gift: raw cod -> raw calamari. */
    @GameTest(template = "empty")
    public static void fishermanGiftYieldsCalamari(GameTestHelper helper) {
        assertTableYields(helper, "gameplay/hero_of_the_village/fisherman_gift", LootContextParamSets.GIFT, ModItems.CALAMARI.get());
    }

    /** Buried treasure: cooked cod -> cooked calamari. */
    @GameTest(template = "empty")
    public static void buriedTreasureYieldsCookedCalamari(GameTestHelper helper) {
        assertTableYields(helper, "chests/buried_treasure", LootContextParamSets.CHEST, ModItems.COOKED_CALAMARI.get());
    }

    /** Cat morning gift: bonus raw calamari pool (LootEvent add). */
    @GameTest(template = "empty")
    public static void catMorningGiftYieldsCalamari(GameTestHelper helper) {
        assertTableYields(helper, "gameplay/cat_morning_gift", LootContextParamSets.GIFT, ModItems.CALAMARI.get());
    }

    /** Guardian drop: bonus raw calamari pool (LootEvent add). */
    @GameTest(template = "empty")
    public static void guardianDropsCalamari(GameTestHelper helper) {
        assertEntityTableYields(helper, EntityType.GUARDIAN, ModItems.CALAMARI.get());
    }

    /** Rolls an entity's death loot table (ENTITY param set) and asserts {@code expected} appears. */
    private static void assertEntityTableYields(GameTestHelper helper, EntityType<?> type, Item expected) {
        ServerLevel level = helper.getLevel();
        Entity entity = helper.spawn(type, BlockPos.ZERO);
        LootTable table = level.getServer().reloadableRegistries().getLootTable(type.getDefaultLootTable());
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .withParameter(LootContextParams.ORIGIN, helper.absolutePos(BlockPos.ZERO).getCenter())
                .withParameter(LootContextParams.DAMAGE_SOURCE, level.damageSources().generic())
                .create(LootContextParamSets.ENTITY);

        int found = 0;
        for (int i = 0; i < 400; i++) {
            for (ItemStack stack : table.getRandomItems(params)) {
                if (stack.is(expected)) {
                    found += stack.getCount();
                }
            }
        }

        if (found <= 0) {
            helper.fail("Expected " + type + " loot to yield " + expected + " over 400 rolls, got none");
        } else {
            helper.succeed();
        }
    }

    /**
     * Rolls a vanilla loot table many times and asserts the GLM replaces the
     * vanilla item with {@code expected} at least once. At 50% chance the vanilla
     * item still appears in the other rolls — the assertion is simply that the
     * replacement shows up at all, which proves the GLM is wired for this table.
     */
    private static void assertTableYields(GameTestHelper helper, String tablePath, LootContextParamSet paramSet, Item expected) {
        ServerLevel level = helper.getLevel();
        // A THIS_ENTITY is required by the GIFT param set (and allowed by CHEST).
        Entity entity = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        LootTable table = level.getServer().reloadableRegistries().getLootTable(
                ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace(tablePath)));
        LootParams params = new LootParams.Builder(level)
                .withParameter(LootContextParams.ORIGIN, helper.absolutePos(BlockPos.ZERO).getCenter())
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .create(paramSet);

        int found = 0;
        for (int i = 0; i < 400; i++) {
            ObjectArrayList<ItemStack> loot = table.getRandomItems(params);
            for (ItemStack stack : loot) {
                if (stack.is(expected)) {
                    found += stack.getCount();
                }
            }
        }

        if (found <= 0) {
            helper.fail("Expected " + tablePath + " loot to yield " + expected + " over 400 rolls, got none");
        } else {
            helper.succeed();
        }
    }
}
