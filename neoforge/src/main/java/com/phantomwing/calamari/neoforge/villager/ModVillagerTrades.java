package com.phantomwing.calamari.neoforge.villager;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.neoforge.Configuration;
import com.phantomwing.calamari.villager.CalamariVillagerTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;

/**
 * NeoForge trade registrant. Trade content is defined once in the shared
 * {@link CalamariVillagerTrades}; this class applies it through the NeoForge
 * village events, re-checking the config every rebuild. The Fabric twin lives at
 * {@code com.phantomwing.calamari.fabric.villager.ModVillagerTrades}.
 */
@EventBusSubscriber(modid = CalamariCommon.MOD_ID)
public class ModVillagerTrades {
    @SubscribeEvent
    public static void addVillagerTrades(VillagerTradesEvent event) {
        if (!Configuration.ENABLE_VILLAGER_TRADES.get()) {
            return;
        }

        if (event.getType() == VillagerProfession.FISHERMAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add(CalamariVillagerTrades.fishermanCookedCalamari());
            trades.get(2).add(CalamariVillagerTrades.fishermanCalamariForEmerald());
        }
    }
}
