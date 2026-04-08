/*
   Copyright 2026 Scott W. Palmer

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
