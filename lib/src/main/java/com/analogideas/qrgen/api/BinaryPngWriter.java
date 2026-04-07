package com.analogideas.qrgen.api;

import java.io.File;
import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * Writes 1-bit PNG images from a {@link ReadOnlyBitMatrix}.
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
