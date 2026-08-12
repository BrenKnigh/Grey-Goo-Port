package com.brenknigh.greygoo.core;

/**
 * Spread rate categories from the original mod. Each category has its own per-tick counter
 * and limit; see {@link SpreadLimiter} and {@code source/GOO_SYSTEMS_DOCUMENTATION.md}.
 */
public enum GooSpreadCategory {
    GENERAL(100),
    DESTROYER(70),
    TGD(50),
    RESTORER(100),
    FALLING(25);

    private final int maxSpreadPerTick;

    GooSpreadCategory(int maxSpreadPerTick) {
        this.maxSpreadPerTick = maxSpreadPerTick;
    }

    public int maxSpreadPerTick() {
        return maxSpreadPerTick;
    }
}
