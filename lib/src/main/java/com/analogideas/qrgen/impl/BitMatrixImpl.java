/*
   Copyright 2021, 2026 Scott W. Palmer

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.analogideas.qrgen.impl;

import com.analogideas.qrgen.api.BitMatrix;
import java.util.Arrays;

/**
 * Represents a square matrix of bits.
 */
public class BitMatrixImpl implements BitMatrix {

    byte[][] bits;
    int dim; // length of side - matrix is always square

    /**
     * Creates a new BitMatrix with the given dimension.
     * @param dim the dimension of the matrix
     */
    public BitMatrixImpl(int dim) {
        this.dim = dim;
        bits = new byte[dim][(dim + 7) / 8];
    }

    private BitMatrixImpl(BitMatrixImpl other) {
        this.dim = other.dim;
        this.bits = new byte[dim][];
        for (int i = 0; i < dim; i++) {
            this.bits[i] = other.bits[i].clone();
        }
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
        return (bits[y][x / 8] & (0x80 >>> (x & 7))) != 0;
    }

    /**
     * Sets the value of the bit at the given coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param b the value to set
     */
    public void set(int x, int y, boolean b) {
        assert x >= 0 && x < dim && y >= 0 && y < dim;
        byte[] row = bits[y];
        int col = x / 8;
        byte v = row[col];
        if (b) {
            v |= 0x80 >>> (x & 7);
        } else {
            v &= ~(0x80 >>> (x & 7));
        }
        row[col] = v;
    }

    /**
     * Returns a copy of this BitMatrix.
     * @return a copy of this BitMatrix
     */
    public BitMatrix copy() {
        return new BitMatrixImpl(this);
    }
}
