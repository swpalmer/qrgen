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
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

/**
 * Writes a two color SVG image of a QR code.
 */
public interface BinarySvgWriter {
    /**
     * Writes the QR code to the given file.
     * @param data the QR code data
     * @param file the file to write to
     * @throws IOException if an I/O error occurs
     */
    void write(ReadOnlyBitMatrix data, File file) throws IOException;
    /**
     * Writes the QR code to the given byte channel.
     * @param data the QR code data
     * @param file the byte channel to write to
     * @throws IOException if an I/O error occurs
     */
    void write(ReadOnlyBitMatrix data, WritableByteChannel file) throws IOException;
    /**
     * Writes the QR code to the given output stream.
     * @param data the QR code data
     * @param os the output stream to write to
     * @throws IOException if an I/O error occurs
     */
    void write(ReadOnlyBitMatrix data, OutputStream os) throws IOException;
}
