package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.CalamariCommon;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Consumer;

/**
 * Registration hub for the Calamari game tests.
 *
 * <p>1.21.5 removed the annotation-driven game-test API ({@code @GameTest},
 * {@code @GameTestHolder}, {@code @PrefixGameTestTemplate}). Tests are now data
 * driven and come in two halves:</p>
 * <ol>
 *   <li>the test <em>body</em> is a {@link Consumer}&lt;{@link GameTestHelper}&gt;
 *       registered into the static {@code minecraft:test_function} registry (this
 *       class), and</li>
 *   <li>the test <em>instance</em> — which binds a body to a structure and an
 *       environment — is a datapack entry under
 *       {@code data/calamari/test_instance/}.</li>
 * </ol>
 *
 * <p>The instances are shipped as JSON rather than registered through NeoForge's
 * {@code RegisterGameTestsEvent}: that event hands out the registries mid-load and
 * they are already frozen by the time it dispatches here ("Registry is already
 * frozen"), and it would additionally require a {@code Holder} for the environment.
 * The datapack form references {@code minecraft:default} by id, so it needs neither.</p>
 *
 * <p>The bodies live in {@link CalamariLootGameTest} / {@link CalamariCatFoodGameTest}
 * as plain static methods, referenced here as method handles. Functions are added via
 * {@link RegisterEvent} rather than a {@code DeferredRegister} because this whole
 * source set is test-only — the main {@code @Mod} class must not reference it.</p>
 *
 * <p>Run headless with {@code ./gradlew :neoforge:runGameTest}.</p>
 */
@EventBusSubscriber(modid = CalamariCommon.MOD_ID)
public final class CalamariGameTests {
    private CalamariGameTests() {
    }

    /** Every test: registry id -> body. */
    private static void forEachTest(java.util.function.BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("village_fisher_yields_calamari", CalamariLootGameTest::villageFisherYieldsCalamari);
        out.accept("fisherman_gift_yields_calamari", CalamariLootGameTest::fishermanGiftYieldsCalamari);
        out.accept("buried_treasure_yields_cooked_calamari", CalamariLootGameTest::buriedTreasureYieldsCookedCalamari);
        out.accept("cat_morning_gift_yields_calamari", CalamariLootGameTest::catMorningGiftYieldsCalamari);
        out.accept("guardian_drops_calamari", CalamariLootGameTest::guardianDropsCalamari);
        out.accept("cat_treats_calamari_as_food", CalamariCatFoodGameTest::catTreatsCalamariAsFood);
    }

    /**
     * The test bodies, into the static {@code minecraft:test_function} registry. The
     * matching instances live in {@code data/calamari/test_instance/*.json} and refer
     * back to these ids via their {@code "function"} field.
     */
    @SubscribeEvent
    public static void registerTestFunctions(RegisterEvent event) {
        event.register(Registries.TEST_FUNCTION, helper ->
                forEachTest((name, body) -> helper.register(Calamari.resourceLocation(name), body)));
    }
}
