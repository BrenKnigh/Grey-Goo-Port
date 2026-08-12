package com.brenknigh.greygoo.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.brenknigh.greygoo.GreyGooConfig;

/**
 * Throttles goo propagation to prevent server lag. Faithful port of the original
 * {@code SpreadLimiter} probability formula documented in {@code source/GOO_SYSTEMS_DOCUMENTATION.md}.
 */
public final class SpreadLimiter {
    private static final Random RANDOM = new Random();

    private final int[] spreadThisTick = new int[GooSpreadCategory.values().length];

    /**
     * @param category spread category to check or increment
     * @param increment if {@code true}, increment the counter after allowing a spread
     * @return {@code true} if spreading is allowed this tick
     */
    public boolean trySpread(GooSpreadCategory category, boolean increment) {
        int index = category.ordinal();
        int maxSpread = category.maxSpreadPerTick();
        int current = spreadThisTick[index];

        int base = (current - maxSpread) / maxSpread;
        int scale = configScale(category);

        boolean allowed = RANDOM.nextInt(100) > (base + scale);
        if (allowed && increment) {
            spreadThisTick[index]++;
        }
        return allowed;
    }

    public void resetCounters() {
        for (int i = 0; i < spreadThisTick.length; i++) {
            spreadThisTick[i] = 0;
        }
    }

    private static int configScale(GooSpreadCategory category) {
        return switch (category) {
            case GENERAL -> GreyGooConfig.GENERAL_SPREAD_SCALE.get();
            case DESTROYER -> GreyGooConfig.DESTROYER_SPREAD_SCALE.get();
            case TGD -> GreyGooConfig.TGD_SPREAD_SCALE.get();
            case RESTORER -> GreyGooConfig.RESTORER_SPREAD_SCALE.get();
            case FALLING -> GreyGooConfig.FALLING_SPREAD_SCALE.get();
        };
    }
}
