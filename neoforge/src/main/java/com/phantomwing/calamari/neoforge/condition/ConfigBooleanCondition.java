package com.phantomwing.calamari.neoforge.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phantomwing.calamari.neoforge.Configuration;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

/**
 * NeoForge load condition gating a datapack entry on one of the mod's config booleans.
 *
 * <p>Added for 26.1: the villager trades became datapack entries, so the config gate that
 * used to be an {@code if} in the trade event has to travel with the data. The Fabric twin
 * is {@code com.phantomwing.calamari.fabric.condition.ConfigBooleanResourceCondition}, and
 * {@code ModVillagerTradeProvider} writes both forms into the same file.</p>
 */
public record ConfigBooleanCondition(String settingId) implements ICondition {
    public static final MapCodec<ConfigBooleanCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("settingId").forGetter(ConfigBooleanCondition::settingId)
    ).apply(inst, ConfigBooleanCondition::new));

    @Override
    public boolean test(ICondition.@NotNull IContext context) {
        return Configuration.getBooleanConfigurationValue(settingId);
    }

    @Override
    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }
}
