/*
 * Copyright 2026 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen.impl;

/**
 * Reed-Solomon error correction codeword generation for QR codes.
 * Uses GF(256) with the QR code primitive polynomial x^8+x^4+x^3+x^2+1 (0x11D).
 *
 * See ISO/IEC 18004:2015 Annex A.
 */
public class ReedSolomon {

    // GF(256) log and antilog tables (primitive polynomial 0x11D)
    private static final int[] LOG = new int[256];
    private static final int[] ALOG = new int[256];

    static {
        int x = 1;
        for (int i = 0; i < 255; i++) {
            ALOG[i] = x;
            LOG[x] = i;
            x <<= 1;
            if ((x & 0x100) != 0) x ^= 0x11D;
        }
        ALOG[255] = ALOG[0];
    }

    /** Multiply two GF(256) elements. */
    static int gfMul(int a, int b) {
        if (a == 0 || b == 0) return 0;
        return ALOG[(LOG[a] + LOG[b]) % 255];
    }

    /**
     * Build the generator polynomial g(x) = (x+a^0)(x+a^1)...(x+a^(n-1)).
     * Coefficients are stored highest-degree first; the array has length n+1
     * with g[0]=1 (leading term).
     */
    static int[] generatorPoly(int n) {
        int[] g = { 1 };
        for (int i = 0; i < n; i++) {
            int[] factor = { 1, ALOG[i] };
            int[] product = new int[g.length + 1];
            for (int j = 0; j < g.length; j++) {
                for (int k = 0; k < factor.length; k++) {
                    product[j + k] ^= gfMul(g[j], factor[k]);
                }
            }
            g = product;
        }
        return g; // length n+1; g[0]=1 is the leading (x^n) coefficient
    }

    /**
     * Compute {@code nECCW} error correction codewords for the given data block.
     * Implements polynomial long division in GF(256).
     *
     * @param data   data codewords (values 0-255)
     * @param nECCW  number of EC codewords to produce
     * @return       array of nECCW EC codewords
     */
    public static int[] computeEC(int[] data, int nECCW) {
        int[] g = generatorPoly(nECCW);
        // Append nECCW zero bytes to the message (multiply by x^nECCW)
        int[] msg = new int[data.length + nECCW];
        System.arraycopy(data, 0, msg, 0, data.length);
        // Polynomial long division
        for (int i = 0; i < data.length; i++) {
            int coeff = msg[i];
            if (coeff == 0) continue;
            for (int j = 1; j <= nECCW; j++) {
                msg[i + j] ^= gfMul(g[j], coeff);
            }
        }
        // Remainder is the EC codewords
        int[] ec = new int[nECCW];
        System.arraycopy(msg, data.length, ec, 0, nECCW);
        return ec;
    }
}
