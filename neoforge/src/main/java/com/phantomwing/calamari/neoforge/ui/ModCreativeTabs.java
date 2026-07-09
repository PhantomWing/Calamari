package com.phantomwing.calamari.neoforge.ui;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;

/**
 * Forge creative-tab injection. Places Calamari and Cooked Calamari into the
 * vanilla Food &amp; Drinks tab, right after Cooked Rabbit (and so before Raw Cod).
 * The Fabric twin uses {@code ItemGroupEvents.modifyEntriesEvent}.
 */
public final class ModCreativeTabs {
    private ModCreativeTabs() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModCreativeTabs::onBuildContents);
    }

    private static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.getEntries().putAfter(new ItemStack(Items.COOKED_RABBIT), new ItemStack(ModItems.CALAMARI.get()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.getEntries().putAfter(new ItemStack(ModItems.CALAMARI.get()), new ItemStack(ModItems.COOKED_CALAMARI.get()),
                    CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
