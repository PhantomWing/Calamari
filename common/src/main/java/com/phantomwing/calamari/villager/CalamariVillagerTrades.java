package com.phantomwing.calamari.villager;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.Optional;

/**
 * Loader-agnostic source of truth for the calamari villager trades, ported from
 * Rustic Delight.
 *
 * <p>Mirrors the "shared spec, per-loader apply" model: the trade content lives
 * here once, and each loader registers it through its own API — NeoForge from
 * {@code VillagerTradesEvent}, Fabric via {@code TradeOfferHelper}. Config gating
 * is applied by each loader's registrant at its idiomatic point.</p>
 */
public final class CalamariVillagerTrades {
    public static final float PRICE_MULTIPLIER = 0.05f;

    private CalamariVillagerTrades() {
    }

    /**
     * Fisherman, level 1: buy 6 Raw Calamari + 1 Emerald, sell 6 Cooked Calamari.
     *
     * <p>Cost order matches vanilla's cooked-cod trade: the fish is the primary cost
     * (and so the one the reputation discount scales) and the emerald the additional
     * one, so the trade reads the same way round as every other cook-my-fish trade.</p>
     */
    public static VillagerTrades.ItemListing fishermanCookedCalamari() {
        // 1.21.11: ItemListing#getOffer takes the ServerLevel as its first argument.
        return (level, trader, random) -> new MerchantOffer(
                new ItemCost(ModItems.CALAMARI.get(), 6),
                Optional.of(new ItemCost(Items.EMERALD, 1)),
                new ItemStack(ModItems.COOKED_CALAMARI.get(), 6),
                16, 1, PRICE_MULTIPLIER
        );
    }

    /** Fisherman, level 2: buy 15 Calamari, sell 1 Emerald. */
    public static VillagerTrades.ItemListing fishermanCalamariForEmerald() {
        return (level, trader, random) -> new MerchantOffer(
                new ItemCost(ModItems.CALAMARI.get(), 15),
                new ItemStack(Items.EMERALD, 1),
                16, 10, PRICE_MULTIPLIER
        );
    }
}
