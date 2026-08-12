package com.brenknigh.greygoo.core;

/**
 * Integer block coordinates used by {@link SpreadHelper}.
 * Ported from the original {@code CoordHolder}.
 */
public record CoordHolder(int x, int y, int z) {
    public static CoordHolder of(int x, int y, int z) {
        return new CoordHolder(x, y, z);
    }
}
