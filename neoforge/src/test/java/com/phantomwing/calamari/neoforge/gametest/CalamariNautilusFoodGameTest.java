package com.phantomwing.calamari.neoforge.gametest;

import com.phantomwing.calamari.item.ModItems;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

/**
 * Verifies calamari feeds (and so heals) a tamed nautilus, added in 1.21.11.
 *
 * <p>This works without any extra registration: vanilla's {@code nautilus_food} tag
 * includes {@code #minecraft:fishes}, and calamari joined that tag for dolphin
 * feeding. The test pins the behaviour so a future vanilla change to either tag
 * doesn't silently drop the integration.</p>
 *
 * <p>Taming is deliberately NOT covered: {@code nautilus_taming_items} is pufferfish
 * only, so calamari should not tame a nautilus — same as vanilla cod/salmon.</p>
 */
public class CalamariNautilusFoodGameTest {

    public static void nautilusEatsCalamari(GameTestHelper helper) {
        // Tamed/adult nautilus feeding reads ItemTags.NAUTILUS_FOOD (AbstractNautilus#isFood).
        if (!new ItemStack(ModItems.CALAMARI.get()).is(ItemTags.NAUTILUS_FOOD)) {
            helper.fail(Component.literal("Raw calamari is not nautilus food (is it still in minecraft:fishes?)"));
            return;
        }
        if (!new ItemStack(ModItems.COOKED_CALAMARI.get()).is(ItemTags.NAUTILUS_FOOD)) {
            helper.fail(Component.literal("Cooked calamari is not nautilus food (is it still in minecraft:fishes?)"));
            return;
        }

        // Taming is pufferfish-only in vanilla; calamari must NOT qualify.
        if (new ItemStack(ModItems.CALAMARI.get()).is(ItemTags.NAUTILUS_TAMING_ITEMS)) {
            helper.fail(Component.literal("Calamari should not be a nautilus taming item (vanilla uses pufferfish)"));
            return;
        }

        helper.succeed();
    }
}
