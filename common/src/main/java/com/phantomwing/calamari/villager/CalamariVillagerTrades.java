package com.phantomwing.calamari.villager;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.TradeCost;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Optional;

/**
 * Loader-agnostic source of truth for the calamari villager trades.
 *
 * <p><b>26.1: villager trades are DATA-DRIVEN.</b> 26.1 replaced the code-defined
 * {@code VillagerTrades.ItemListing} interface with a datapack {@code villager_trade}
 * registry plus {@code tags/villager_trade/<profession>/level_N} pool tags, and removed
 * NeoForge's {@code VillagerTradesEvent} along with the Fabric {@code TradeOfferHelper}
 * path. Both loaders now read the same generated JSON, so there is no per-loader trade
 * registration code left — and the Fabric double-registration bug the old
 * {@code TradeOfferHelper} workaround guarded against cannot recur.</p>
 *
 * <p>The trade content still lives here once. {@code ModVillagerTradeProvider} (NeoForge
 * datagen) encodes these two {@link VillagerTrade}s through {@link VillagerTrade#CODEC}
 * into the shared generated resource tree, and gates each one on
 * {@code enable_villager_trades} via {@code neoforge:conditions} +
 * {@code fabric:load_conditions}.</p>
 */
public final class CalamariVillagerTrades {
    /** Vanilla's standard fisherman price multiplier; mirrored by the data's {@code reputation_discount}. */
    public static final float PRICE_MULTIPLIER = 0.05f;

    /**
     * Registry id of the level-1 trade. The {@code villager_trade} directory is implied,
     * so {@code data/calamari/villager_trade/fisherman/1/x.json} is {@code calamari:fisherman/1/x}.
     */
    public static final String FISHERMAN_L1_ID = "fisherman/1/raw_calamari_and_emerald_cooked_calamari";
    /** Registry id of the level-2 trade. */
    public static final String FISHERMAN_L2_ID = "fisherman/2/calamari_emerald";

    private CalamariVillagerTrades() {
    }

    /**
     * Fisherman, level 1: buy 6 Raw Calamari + 1 Emerald, sell 6 Cooked Calamari.
     *
     * <p>Cost order matches vanilla's {@code raw_cod_and_emerald_cooked_cod}: the fish is the
     * primary cost and the emerald the additional one, so the trade reads the same way round
     * as every other cook-my-fish trade in the game. The primary cost is also the one the
     * reputation discount scales, which is again what vanilla does.</p>
     */
    public static VillagerTrade fishermanCookedCalamari() {
        return new VillagerTrade(
                new TradeCost(ModItems.CALAMARI.get(), 6),
                Optional.of(new TradeCost(Items.EMERALD, 1)),
                new ItemStackTemplate(ModItems.COOKED_CALAMARI.get(), 6),
                16, 1, PRICE_MULTIPLIER,
                Optional.empty(), List.of());
    }

    /** Fisherman, level 2: buy 15 Calamari, sell 1 Emerald. */
    public static VillagerTrade fishermanCalamariForEmerald() {
        return new VillagerTrade(
                new TradeCost(ModItems.CALAMARI.get(), 15),
                new ItemStackTemplate(Items.EMERALD),
                16, 10, PRICE_MULTIPLIER,
                Optional.empty(), List.of());
    }
}
