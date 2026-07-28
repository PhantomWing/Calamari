package com.phantomwing.calamari.neoforge.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.neoforge.Configuration;
import com.phantomwing.calamari.villager.CalamariVillagerTrades;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.trading.VillagerTrade;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Writes the two fisherman {@code villager_trade} entries and adds them to the vanilla
 * fisherman level-1 / level-2 pool tags.
 *
 * <p>26.1 moved villager trades out of code and into datapacks, so this replaces the old
 * per-loader registration entirely — the generated files live in the shared
 * {@code common/src/generated/resources} tree and are read by both loaders.</p>
 *
 * <p>Each trade is encoded through {@link VillagerTrade#CODEC} (so the JSON can never drift
 * from the shared definition in {@link CalamariVillagerTrades}) and then gated on the
 * {@code enable_villager_trades} config with BOTH loaders' load-condition dialects. The pool
 * tag entries are {@code required: false}, so when the config gates a trade out the tag
 * simply skips the missing id instead of erroring.</p>
 */
public class ModVillagerTradeProvider implements DataProvider {
    /** Vanilla fisherman pools these trades join. */
    private static final String L1_TAG_PATH = "minecraft/tags/villager_trade/fisherman/level_1.json";
    private static final String L2_TAG_PATH = "minecraft/tags/villager_trade/fisherman/level_2.json";

    private final PackOutput packOutput;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public ModVillagerTradeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        this.packOutput = packOutput;
        this.registries = registries;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        return registries.thenCompose(provider -> {
            Path data = packOutput.getOutputFolder(PackOutput.Target.DATA_PACK);
            RegistryOps<JsonElement> ops = provider.createSerializationContext(JsonOps.INSTANCE);
            List<CompletableFuture<?>> futures = new ArrayList<>();

            futures.add(saveTrade(cache, ops, data,
                    CalamariVillagerTrades.FISHERMAN_L1_ID, CalamariVillagerTrades.fishermanCookedCalamari()));
            futures.add(saveTrade(cache, ops, data,
                    CalamariVillagerTrades.FISHERMAN_L2_ID, CalamariVillagerTrades.fishermanCalamariForEmerald()));

            futures.add(DataProvider.saveStable(cache,
                    poolTag(CalamariVillagerTrades.FISHERMAN_L1_ID), data.resolve(L1_TAG_PATH)));
            futures.add(DataProvider.saveStable(cache,
                    poolTag(CalamariVillagerTrades.FISHERMAN_L2_ID), data.resolve(L2_TAG_PATH)));

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    /** Encodes one trade, attaches the config gate, and writes it under this mod's namespace. */
    private static CompletableFuture<?> saveTrade(CachedOutput cache, RegistryOps<JsonElement> ops,
                                                  Path data, String tradeId, VillagerTrade trade) {
        JsonObject json = VillagerTrade.CODEC.encodeStart(ops, trade).getOrThrow().getAsJsonObject();
        addConfigGate(json, Configuration.ENABLE_VILLAGER_TRADES_ID);
        return DataProvider.saveStable(cache, json,
                data.resolve(Calamari.MOD_ID + "/villager_trade/" + tradeId + ".json"));
    }

    /**
     * Adds the {@code enable_villager_trades} gate in both loaders' dialects: NeoForge reads
     * {@code neoforge:conditions} (handled by {@code ConfigBooleanCondition}), Fabric reads
     * {@code fabric:load_conditions} (handled by {@code ConfigBooleanResourceCondition}).
     * Each ignores the other's key, so one file serves both.
     */
    private static void addConfigGate(JsonObject json, String settingId) {
        JsonObject neoforge = new JsonObject();
        neoforge.addProperty("type", Calamari.MOD_ID + ":config_boolean");
        neoforge.addProperty("settingId", settingId);
        JsonArray neoforgeConditions = new JsonArray();
        neoforgeConditions.add(neoforge);
        json.add("neoforge:conditions", neoforgeConditions);

        JsonObject fabric = new JsonObject();
        fabric.addProperty("condition", Calamari.MOD_ID + ":config_boolean");
        fabric.addProperty("settingId", settingId);
        JsonArray fabricConditions = new JsonArray();
        fabricConditions.add(fabric);
        json.add("fabric:load_conditions", fabricConditions);
    }

    /** Additive pool tag adding one trade as an OPTIONAL entry (absent when the config gates it out). */
    private static JsonObject poolTag(String tradeId) {
        JsonObject entry = new JsonObject();
        entry.addProperty("id", Calamari.MOD_ID + ":" + tradeId);
        entry.addProperty("required", false);
        JsonArray values = new JsonArray();
        values.add(entry);
        JsonObject tag = new JsonObject();
        tag.add("values", values);
        return tag;
    }

    @Override
    public @NotNull String getName() {
        return "Calamari Villager Trades";
    }
}
