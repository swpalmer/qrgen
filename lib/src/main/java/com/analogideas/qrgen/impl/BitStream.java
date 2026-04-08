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

import java.util.BitSet;

/**
 * Represents a stream of bits.
 */
public class BitStream implements Iterable<Boolean> {

    private int length;
    private BitSet data = new BitSet();

    private class Iterator implements java.util.Iterator<Boolean> {

        int pos;

        @Override
        public boolean hasNext() {
            return pos < length;
        }

        @Override
        public Boolean next() {
            return data.get(pos++);
        }
    }

    @Override
    public java.util.Iterator<Boolean> iterator() {
        return new Iterator();
    }

    /**
     * Adds a single bit to the stream.
     * @param bit the bit to add
     */
    public void add(boolean bit) {
        data.set(length++, bit);
    }

    /**
     * Adds a single byte to the stream. MSB first.
     * @param b the byte to add
     */
    public void addByte(byte b) {
        addBits(b, 8);
    }

    /**
     * Adds a short to the stream. MSB first.
     * @param s the short to add
     */
    public void addShort(short s) {
        addBits(s, 16);
    }

    /**
     * Adds an int to the stream. MSB first.
     * @param i the int to add
     */
    public void addInt(int i) {
        addBits(i, 32);
    }

    /**
     * Adds a long to the stream. MSB first.
     * @param l the long to add
     */
    public void addLong(long l) {
        addBits(l, 64);
    }

    /**
     * Adds a number of bits to the stream.
     * @param i the value to add
     * @param n the number of bits to add, stating with bit n-1 and proceeding to bit 0.
     */
    public void addBits(int i, int n) {
        if (n > 0) {
            int mask = 0x1 << (n - 1);
            while (mask != 0) {
                add((i & mask) != 0);
                mask >>= 1;
            }
        }
    }

    /**
     * Adds a number of bits to the stream. MSB first.
     * @param l the value to add
     * @param n the number of bits to add, stating with bit n-1 and proceeding to bit 0.
     */
    public void addBits(long l, int n) {
        if (n > 0) {
            long mask = 0x1L << (n - 1);
            while (mask != 0) {
                add((l & mask) != 0);
                mask >>= 1;
            }
        }
    }

    /**
     * Returns the number of bits in the stream.
     * @return the number of bits in the stream
     */
    public int length() {
        return length;
    }

    /**
     * Returns a copy of the data in the stream as a {@link BitSet}.
     * @return a copy of the data in the stream as a {@link BitSet}
     */
    public BitSet toBitSet() {
        var bs = new BitSet();
        bs.or(data);
        return bs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(data.get(i) ? '1' : '0');
        }
        return sb.toString();
    }
}
