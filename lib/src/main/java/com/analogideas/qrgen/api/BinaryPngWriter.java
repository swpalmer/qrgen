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

import java.io.File;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * Writes 1-bit-per-pixel PNG images from a {@link ReadOnlyBitMatrix}.
 */
public interface BinaryPngWriter {
    /**
     * Writes a 1-bit PNG image to a file.
     *
     * @param data the bit matrix to write
     * @param pixelSize the size of each pixel in the image
     * @param file the file to write to
     * @throws IOException if an I/O error occurs
     */
    void write(ReadOnlyBitMatrix data, int pixelSize, File file) throws IOException;

    /**
     * Writes a 1-bit PNG image to a {@link WritableByteChannel}.
     *
     * @param data the bit matrix to write
     * @param pixelSize the size of each pixel in the image
     * @param outputStream the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    void write(ReadOnlyBitMatrix data, int pixelSize, WritableByteChannel outputStream) throws IOException;
}
