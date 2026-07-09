package com.phantomwing.calamari.neoforge.villager;

import com.phantomwing.calamari.CalamariCommon;
import com.phantomwing.calamari.neoforge.Configuration;
import com.phantomwing.calamari.villager.CalamariVillagerTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * Forge trade registrant. Trade content is defined once in the shared
 * {@link CalamariVillagerTrades}; this class applies it through the Forge village
 * events, re-checking the config every rebuild. The Fabric twin lives at
 * {@code com.phantomwing.calamari.fabric.villager.ModVillagerTrades}.
 */
@Mod.EventBusSubscriber(modid = CalamariCommon.MOD_ID)
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
