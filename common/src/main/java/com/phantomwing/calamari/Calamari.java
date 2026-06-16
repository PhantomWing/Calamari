package com.phantomwing.calamari;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

/**
 * Shared constants and helpers for Calamari. The actual mod bootstrap lives in
 * {@link CalamariCommon#init()} (called from each loader entrypoint).
 */
public final class Calamari {
    public static final String MOD_ID = "calamari";
    public static final Logger LOGGER = LogUtils.getLogger();

    private Calamari() {
    }

    public static ResourceLocation resourceLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
