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
 */
public final class ModVillagerTrades {
    private ModVillagerTrades() {
    }

    public static void register() {
        VillagerTrades.ItemListing fishermanL1 = CalamariVillagerTrades.fishermanCookedCalamari();
        VillagerTrades.ItemListing fishermanL2 = CalamariVillagerTrades.fishermanCalamariForEmerald();

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 1, factories ->
                factories.add((trader, random) ->
                        CalamariFabricConfig.get().enable_villager_trades
                                ? fishermanL1.getOffer(trader, random)
                                : null));

        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FISHERMAN, 2, factories ->
                factories.add((trader, random) ->
                        CalamariFabricConfig.get().enable_villager_trades
                                ? fishermanL2.getOffer(trader, random)
                                : null));
    }
}
