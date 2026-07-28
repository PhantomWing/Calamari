package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.trading.MerchantOffer;

/**
 * Guards against the calamari fisherman trades being registered more than once.
 *
 * <p>A trade pool holding two listings that yield the same offer lets a single
 * villager roll that trade twice, which is not intended. NeoForge rebuilds each
 * profession's pool from a pristine vanilla snapshot before firing
 * {@code VillagerTradesEvent}, so a duplicate here means the mod added it twice.</p>
 */
public class CalamariVillagerTradeGameTest {

    public static void fishermanHasNoDuplicateCalamariTrades(GameTestHelper helper) {
        Entity villager = helper.spawn(EntityType.VILLAGER, BlockPos.ZERO);
        RandomSource random = helper.getLevel().getRandom();

        Int2ObjectMap<VillagerTrades.ItemListing[]> byLevel = VillagerTrades.TRADES.get(VillagerProfession.FISHERMAN);
        if (byLevel == null) {
            helper.fail(Component.literal("No fisherman trades registered at all"));
            return;
        }

        // Scan EVERY level, not just the one each trade is meant to live in — a stray
        // copy in another level's pool would let a villager roll the trade a second time.
        int sellsCookedCalamari = 0;
        int buysCalamari = 0;
        for (int level = 1; level <= 5; level++) {
            VillagerTrades.ItemListing[] pool = byLevel.get(level);
            if (pool == null) {
                continue;
            }
            for (VillagerTrades.ItemListing listing : pool) {
                // 1.21.11: getOffer takes the ServerLevel first.
                MerchantOffer offer = listing.getOffer(helper.getLevel(), villager, random);
                if (offer == null) {
                    continue;
                }
                if (offer.getResult().is(ModItems.COOKED_CALAMARI.get())) {
                    sellsCookedCalamari++;
                }
                if (offer.getBaseCostA().is(ModItems.CALAMARI.get())) {
                    buysCalamari++;
                }
            }
        }

        if (!assertExactlyOne(helper, sellsCookedCalamari, "listings selling cooked calamari")) return;
        if (!assertExactlyOne(helper, buysCalamari, "listings buying raw calamari")) return;

        helper.succeed();
    }

    private static boolean assertExactlyOne(GameTestHelper helper, int count, String what) {
        if (count != 1) {
            helper.fail(Component.literal("Fisherman has " + count + " " + what + " across all levels (expected 1)"));
            return false;
        }
        return true;
    }
}
