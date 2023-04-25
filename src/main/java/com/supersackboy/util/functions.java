package com.supersackboy.util;

public class functions {
    /**
     * Linearly interpolates between two values.
     *
     * @param from
     *            the start value
     * @param to
     *            the end value
     * @param p
     *            the current interpolation position, must be between 0 and 1
     * @return the result of the interpolation
     */
    public static double lerp(double from, double to, double p) {
        assert p >= 0 && p <= 1 : "interpolation position out of range";
        return from + (to - from) * p;
    }
}
