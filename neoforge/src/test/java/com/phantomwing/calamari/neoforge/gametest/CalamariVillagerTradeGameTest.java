package com.phantomwing.calamari.neoforge.gametest;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.villager.CalamariVillagerTrades;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.trading.VillagerTrade;

/**
 * Verifies the two generated fisherman trades reach the game intact.
 *
 * <p>26.1 made villager trades datapack entries, so the failure modes moved: instead of a
 * listing being registered twice (the pre-26.1 Fabric bug this test was written for), the
 * risks are now a trade whose JSON never loads (wrong path or namespace), one that loads but
 * is missing from its pool tag, and generated data that has drifted from the code spec
 * because datagen was not re-run. All three are checked here, plus the original
 * duplicate guard: a trade must appear in exactly one fisherman pool.</p>
 */
public class CalamariVillagerTradeGameTest {
    /** Vanilla's fisherman pools, tagged in the {@code villager_trade} registry. */
    private static final int MAX_PROFESSION_LEVEL = 5;

    public static void fishermanHasNoDuplicateCalamariTrades(GameTestHelper helper) {
        Registry<VillagerTrade> registry = helper.getLevel().registryAccess()
                .lookupOrThrow(Registries.VILLAGER_TRADE);
        RegistryOps<JsonElement> ops = helper.getLevel().registryAccess()
                .createSerializationContext(JsonOps.INSTANCE);

        if (!check(helper, registry, ops, CalamariVillagerTrades.FISHERMAN_L1_ID, 1,
                CalamariVillagerTrades.fishermanCookedCalamari())) {
            return;
        }
        if (!check(helper, registry, ops, CalamariVillagerTrades.FISHERMAN_L2_ID, 2,
                CalamariVillagerTrades.fishermanCalamariForEmerald())) {
            return;
        }

        helper.succeed();
    }

    /** Asserts one trade is loaded, matches the code spec, and sits in exactly its own pool. */
    private static boolean check(GameTestHelper helper, Registry<VillagerTrade> registry,
                                 RegistryOps<JsonElement> ops, String tradeId, int expectedLevel,
                                 VillagerTrade expected) {
        Identifier id = Calamari.resourceLocation(tradeId);
        ResourceKey<VillagerTrade> key = ResourceKey.create(Registries.VILLAGER_TRADE, id);

        VillagerTrade loaded = registry.getValue(key);
        if (loaded == null) {
            helper.fail(Component.literal("Trade " + id + " is not loaded (did datagen run, "
                    + "and did the enable_villager_trades condition pass?)"));
            return false;
        }

        // Re-encoding both through the same codec turns "did the shipped JSON drift from the
        // shared spec?" into a plain equality check — a stale generated tree fails here.
        JsonElement loadedJson = VillagerTrade.CODEC.encodeStart(ops, loaded).getOrThrow();
        JsonElement expectedJson = VillagerTrade.CODEC.encodeStart(ops, expected).getOrThrow();
        if (!loadedJson.equals(expectedJson)) {
            helper.fail(Component.literal("Trade " + id + " does not match CalamariVillagerTrades"
                    + " (stale generated data?). Loaded " + loadedJson + ", expected " + expectedJson));
            return false;
        }

        // Scan EVERY fisherman pool, not just the intended one — a stray copy elsewhere would
        // let a villager roll the same trade twice.
        for (int level = 1; level <= MAX_PROFESSION_LEVEL; level++) {
            int occurrences = 0;
            for (Holder<VillagerTrade> holder : registry.getTagOrEmpty(poolTag(level))) {
                if (holder.is(key)) {
                    occurrences++;
                }
            }

            int wanted = level == expectedLevel ? 1 : 0;
            if (occurrences != wanted) {
                helper.fail(Component.literal("Trade " + id + " appears " + occurrences
                        + " time(s) in the fisherman level " + level + " pool (expected " + wanted + ")"));
                return false;
            }
        }

        return true;
    }

    /** {@code #minecraft:fisherman/level_N} — the pool the vanilla fisherman trade_set draws from. */
    private static TagKey<VillagerTrade> poolTag(int level) {
        return TagKey.create(Registries.VILLAGER_TRADE,
                Identifier.withDefaultNamespace("fisherman/level_" + level));
    }
}
