package com.phantomwing.calamari.villager;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.world.entity.npc.VillagerTrades;
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

    /** Fisherman, level 1: buy 1 Emerald + 6 Calamari, sell 6 Cooked Calamari. */
    public static VillagerTrades.ItemListing fishermanCookedCalamari() {
        return (trader, random) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                Optional.of(new ItemCost(ModItems.CALAMARI.get(), 6)),
                new ItemStack(ModItems.COOKED_CALAMARI.get(), 6),
                16, 1, PRICE_MULTIPLIER
        );
    }

    /** Fisherman, level 2: buy 15 Calamari, sell 1 Emerald. */
    public static VillagerTrades.ItemListing fishermanCalamariForEmerald() {
        return (trader, random) -> new MerchantOffer(
                new ItemCost(ModItems.CALAMARI.get(), 15),
                new ItemStack(Items.EMERALD, 1),
                16, 10, PRICE_MULTIPLIER
        );
    }
}
