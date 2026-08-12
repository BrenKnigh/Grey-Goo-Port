package com.brenknigh.greygoo;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Spread scale config values from the original {@code mod_GreyGoo} config fields.
 * 0–100; higher values further throttle spreading beyond the per-tick soft cap.
 */
public final class GreyGooConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue GENERAL_SPREAD_SCALE = BUILDER
            .comment("Additional throttle for general goo spread (0-100)")
            .defineInRange("generalSpreadScale", 0, 0, 100);

    public static final ModConfigSpec.IntValue DESTROYER_SPREAD_SCALE = BUILDER
            .comment("Additional throttle for Destroyer-chain goo (0-100)")
            .defineInRange("destroyerSpreadScale", 0, 0, 100);

    public static final ModConfigSpec.IntValue TGD_SPREAD_SCALE = BUILDER
            .comment("Additional throttle for The Great Destroyer (0-100)")
            .defineInRange("tgdSpreadScale", 0, 0, 100);

    public static final ModConfigSpec.IntValue RESTORER_SPREAD_SCALE = BUILDER
            .comment("Additional throttle for Rainbow Goo / Restorer (0-100)")
            .defineInRange("restorerSpreadScale", 0, 0, 100);

    public static final ModConfigSpec.IntValue FALLING_SPREAD_SCALE = BUILDER
            .comment("Additional throttle for falling gravity goo (0-100)")
            .defineInRange("fallingSpreadScale", 0, 0, 100);

    public static final ModConfigSpec.IntValue TGD_BLOOM_HEIGHT = BUILDER
            .comment("Y level where TGD enters bloom phase (original default: 146)")
            .defineInRange("tgdBloomHeight", 146, 1, 319);

    public static final ModConfigSpec.IntValue MAX_TGD_GOLEMS = BUILDER
            .comment("Maximum TGD golems allowed in a world (original default: 80)")
            .defineInRange("maxTgdGolems", 80, 0, 1000);

    static final ModConfigSpec SPEC = BUILDER.build();

    private GreyGooConfig() {}
}
