package com.phantomwing.calamari.fabric.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.phantomwing.calamari.Calamari;
import com.phantomwing.calamari.fabric.config.CalamariFabricConfig;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryOps;

/**
 * Fabric runtime handler for the {@code calamari:config_boolean} load condition — parity
 * with the NeoForge {@code ConfigBooleanCondition}. Both read the same {@code settingId},
 * which is written into the generated JSON by {@code ModVillagerTradeProvider}.
 */
public record ConfigBooleanResourceCondition(String settingId) implements ResourceCondition {
    public static final Identifier ID =
            Identifier.fromNamespaceAndPath(Calamari.MOD_ID, "config_boolean");

    public static final MapCodec<ConfigBooleanResourceCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Codec.STRING.fieldOf("settingId").forGetter(ConfigBooleanResourceCondition::settingId)
    ).apply(inst, ConfigBooleanResourceCondition::new));

    public static final ResourceConditionType<ConfigBooleanResourceCondition> TYPE =
            ResourceConditionType.create(ID, CODEC);

    @Override
    public ResourceConditionType<?> getType() {
        return TYPE;
    }

    @Override
    public boolean test(RegistryOps.RegistryInfoLookup registryLookup) {
        // Unknown ids fail the condition (entry simply absent) rather than throwing and
        // taking datapack loading down with them.
        try {
            return CalamariFabricConfig.getBooleanConfigurationValue(settingId);
        } catch (Error unknownSetting) {
            Calamari.LOGGER.error("Unknown config setting id '{}' in a calamari:config_boolean condition", settingId);
            return false;
        }
    }
}
