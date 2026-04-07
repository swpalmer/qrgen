/*
 * Copyright 2021,2026 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen;

import java.util.BitSet;

/**
 * Represents a square matrix of bits.
 */
public class BitMatrix {

    BitSet bits = new BitSet();
    int dim; // length of side - matrix is always square

    /**
     * Creates a new BitMatrix with the given dimension.
     * @param dim the dimension of the matrix
     */
    public BitMatrix(int dim) {
        this.dim = dim;
    }

    /**
     * Returns the dimension of the matrix.
     * @return the dimension of the matrix
     */
    public int dim() {
        return dim;
    }

    /**
     * Returns the value of the bit at the given coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the value of the bit at the given coordinates
     */
    public boolean get(int x, int y) {
        assert x >= 0 && x < dim && y >= 0 && y < dim;
        return bits.get(x * dim + y);
    }

    /**
     * Sets the value of the bit at the given coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param b the value to set
     */
    public void set(int x, int y, boolean b) {
        assert x >= 0 && x < dim && y >= 0 && y < dim;
        bits.set(x * dim + y, b);
    }

    /**
     * Returns a copy of this BitMatrix.
     * @return a copy of this BitMatrix
     */
    public BitMatrix copy() {
        BitMatrix c = new BitMatrix(dim);
        c.bits.or(bits);
        return c;
    }
}
