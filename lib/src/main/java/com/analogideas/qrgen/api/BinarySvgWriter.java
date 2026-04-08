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
