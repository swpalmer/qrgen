package com.analogideas.qrgen.api;

public interface ReadOnlyBitMatrix {
    /**
     * Returns the dimension of the matrix (width and height).
     *
     * @return the dimension of the matrix
     */
    int dim();

    /**
     * Returns the value of a bit at the specified position.
     *
     * @param x the x-coordinate of the bit
     * @param y the y-coordinate of the bit
     * @return the value of the bit
     */
    boolean get(int x, int y);
}
