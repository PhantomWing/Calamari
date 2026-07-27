package com.phantomwing.calamari.fabric.villager;

import com.phantomwing.calamari.fabric.config.CalamariFabricConfig;
import com.phantomwing.calamari.villager.CalamariVillagerTrades;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
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
 * current and rebalanced trades" — it runs the callback twice, and both passes land
 * in {@link VillagerTrades#TRADES}. That put two copies of each listing in the
 * fisherman's pool, letting a single villager roll the same calamari trade twice.
 * Using the {@code VillagerOffersAdder} overload exposes the {@code rebalanced} flag
 * so the listing is added on the normal pass only, giving exactly one copy.</p>
 */
public final class ModVillagerTrades {
    private ModVillagerTrades() {
    }

    public static void register() {
        VillagerTrades.ItemListing fishermanL1 = CalamariVillagerTrades.fishermanCookedCalamari();
        VillagerTrades.ItemListing fishermanL2 = CalamariVillagerTrades.fishermanCalamariForEmerald();

        registerGated(1, fishermanL1);
        registerGated(2, fishermanL2);
    }

    /** Adds {@code listing} to the fisherman's pool for {@code level}, exactly once. */
    private static void registerGated(int level, VillagerTrades.ItemListing listing) {
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, level, (factories, rebalanced) -> {
            if (rebalanced) {
                // Same destination pool as the normal pass — adding here too would duplicate.
                return;
            }
            factories.add((trader, random) ->
                    CalamariFabricConfig.get().enable_villager_trades
                            ? listing.getOffer(trader, random)
                            : null);
        });
    }
}
