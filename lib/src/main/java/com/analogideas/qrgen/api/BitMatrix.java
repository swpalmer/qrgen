package com.analogideas.qrgen.api;

import java.util.BitSet;

/**
 * Represents a matrix of bits (modules)used in QR codes.
 */
public interface BitMatrix extends ReadOnlyBitMatrix {
    /**
     * Sets the value of a bit at the specified position.
     *
     * @param x the x-coordinate of the bit
     * @param y the y-coordinate of the bit
     * @param value the value to set
     */
    void set(int x, int y, boolean value);
    /**
     * Returns a copy of this bit matrix.
     * @return a copy of this bit matrix
     */
    BitMatrix copy();
}
