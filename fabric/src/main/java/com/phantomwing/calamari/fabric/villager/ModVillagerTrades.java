package com.phantomwing.calamari.fabric.villager;

import com.phantomwing.calamari.fabric.config.CalamariFabricConfig;
import com.phantomwing.calamari.villager.CalamariVillagerTrades;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

/**
 * Fabric parity for the NeoForge {@code ModVillagerTrades} village-event handler.
 *
 * <p>{@link TradeOfferHelper} registers factories <b>once</b> at mod-init, so the
 * config gate is pushed <em>into</em> each listing: when the option is off the
 * factory returns {@code null}, which vanilla's trade assembly skips — giving the
 * same live-toggleable behaviour as NeoForge's {@code VillagerTradesEvent}. Trade
 * content comes from the shared {@link CalamariVillagerTrades}.</p>
 *
 * <p><b>Why the two-argument adder.</b> The {@code Consumer}-based overload of
 * {@code registerVillagerOffers} documents that it "adds the same trade offers to
 * current and rebalanced trades": it runs the callback twice, once against
 * {@link VillagerTrades#TRADES} and once against {@code VillagerTrades.EXPERIMENTAL_TRADES}.
 * Vanilla builds the experimental map by copying the normal one and replacing only the
 * professions it actually rebalanced — the fisherman is not one of them, so both maps
 * hold the <em>same</em> per-profession object. Both passes therefore mutated a single
 * array, leaving two copies of each listing and letting one villager roll the same
 * calamari trade twice.</p>
 *
 * <p>The {@code VillagerOffersAdder} overload exposes the {@code rebalanced} flag, so the
 * second pass can be skipped — but only when it would actually write to the same array.
 * For a profession vanilla DID rebalance (librarian, armorer, wandering trader, ...) the
 * two pools are separate objects, and skipping would leave the trade missing from
 * rebalanced worlds. {@code sharesRebalancedPool} therefore compares the two pools by
 * identity and skips only when they are the same object, which is correct for every
 * profession.</p>
 */
public final class ModVillagerTrades {
    private ModVillagerTrades() {
    }

    public static void register() {
        VillagerTrades.ItemListing fishermanL1 = CalamariVillagerTrades.fishermanCookedCalamari();
        VillagerTrades.ItemListing fishermanL2 = CalamariVillagerTrades.fishermanCalamariForEmerald();

        registerGated(VillagerProfession.FISHERMAN, 1, fishermanL1);
        registerGated(VillagerProfession.FISHERMAN, 2, fishermanL2);
    }

    /**
     * Adds {@code listing} to {@code profession}'s pool for {@code level} exactly once,
     * for both normal and rebalanced worlds.
     *
     * <p>Works for any profession: the rebalanced pass is skipped only when that
     * profession's two pools are literally the same object (writing to it twice would
     * duplicate). When vanilla gives the profession its own rebalanced pool, the listing
     * is added to both, so the trade is never missing from a rebalanced world.</p>
     */
    private static void registerGated(ResourceKey<VillagerProfession> profession, int level,
                                      VillagerTrades.ItemListing listing) {
        TradeOfferHelper.registerVillagerOffers(profession, level, (factories, rebalanced) -> {
            // Checked INSIDE the callback: TradeOfferHelper only sets up its trade maps
            // when registerVillagerOffers is first called, so testing beforehand would
            // read an uninitialised state and wrongly report the pools as distinct.
            if (rebalanced && sharesRebalancedPool(profession)) {
                return;
            }
            factories.add((trader, random) ->
                    CalamariFabricConfig.get().enable_villager_trades
                            ? listing.getOffer(trader, random)
                            : null);
        });
    }

    /** True when the profession's normal and rebalanced trade pools are the same object. */
    private static boolean sharesRebalancedPool(ResourceKey<VillagerProfession> profession) {
        var normal = VillagerTrades.TRADES.get(profession);
        var rebalanced = VillagerTrades.EXPERIMENTAL_TRADES.get(profession);
        // A null rebalanced entry means Fabric will create a fresh map for it, so the
        // two are distinct and the listing must be added on both passes.
        return normal != null && normal == rebalanced;
    }
}
