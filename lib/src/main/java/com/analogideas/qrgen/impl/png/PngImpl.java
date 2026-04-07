/*
 * Copyright 2026 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen.impl.png;

import com.analogideas.qrgen.api.QrCode;
import com.analogideas.qrgen.api.ReadOnlyBitMatrix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;

/**
 * Writes a QR code to a PNG file.
 * See https://www.libpng.org/pub/png/spec/1.2/PNG-Structure.html
 */
public class PngImpl implements com.analogideas.qrgen.api.BinaryPngWriter {

    static class Chunk {

        private ByteBuffer buffer;

        public int length() {
            return buffer.getInt(0);
        }

        public int type() {
            return buffer.getInt(4);
        }

        public byte[] data() {
            return Arrays.copyOfRange(buffer.array(), 8, 8 + length());
        }

        public int crc() {
            return buffer.getInt(8 + length());
        }

        public Chunk(String type, byte[] data) {
            buffer = ByteBuffer.allocate(12 + data.length);
            buffer.putInt(data.length);
            buffer.put((byte) type.charAt(0));
            buffer.put((byte) type.charAt(1));
            buffer.put((byte) type.charAt(2));
            buffer.put((byte) type.charAt(3));
            buffer.put(data);
            buffer.flip();
            buffer.position(4);
            //calc CRC based on remaining buffer
            int crc = crc32(buffer); // leaves buffer positioned after last data byte
            buffer.limit(buffer.position() + 4);
            buffer.putInt(crc);
            buffer.flip();
        }

        public void writeTo(WritableByteChannel channel) throws IOException {
            // write it
            channel.write(buffer);
            buffer.clear();
        }

        private static int crc32(ByteBuffer bytes) {
            int crc = 0xFFFFFFFF;
            while (bytes.hasRemaining()) {
                int b = bytes.get() & 0xFF;
                crc ^= b;
                for (int i = 0; i < 8; i++) {
                    int bit = crc & 1;
                    crc >>>= 1;
                    if (bit == 1) crc ^= 0xEDB88320;
                }
            }
            return ~crc & 0xFFFFFFFF;
        }
    }

    private static final long pngHeader = 0x89504E470D0A1A0AL;

    /**
     * Writes a QR code as a PNG image with the given module size.
     * @param data the QR code matrix to write
     * @param pixelSize the size of the modules in pixels
     * @param file the file to write to
     * @throws IOException if an I/O error occurs
     */
    public void write(ReadOnlyBitMatrix data, int pixelSize, File file) throws IOException {
        try (var fos = new FileOutputStream(file); var channel = fos.getChannel()) {
            write(data, pixelSize, channel);
        }
    }

    /**
     * Writes a QR code as a PNG image with the given module size to the given channel.
     * @param data the QR code data
     * @param pixelSize the size of the modules in pixels
     * @param channel the channel to write to
     * @throws IOException if an I/O error occurs
     */
    public void write(ReadOnlyBitMatrix data, int pixelSize, WritableByteChannel channel) throws IOException {
        int imageSize = (data.dim() + 8) * pixelSize;
        channel.write(ByteBuffer.allocate(8).putLong(pngHeader).flip());
        ByteBuffer ihdrBuff = ByteBuffer.allocate(13);
        ihdrBuff.putInt(imageSize); // width
        ihdrBuff.putInt(imageSize); // height
        ihdrBuff.put((byte) 1); // bit depth (binary)
        ihdrBuff.put((byte) 0); // color type (grayscale)
        ihdrBuff.put((byte) 0); // compression method (deflate/inflate compression with a sliding window of at most 32768 bytes)
        ihdrBuff.put((byte) 0); // filter method
        ihdrBuff.put((byte) 0); // interlace method
        Chunk ihdr = new Chunk("IHDR", ihdrBuff.array());
        ihdr.writeTo(channel);
        byte[] compressedData = compress(data, imageSize, pixelSize);
        Chunk idat = new Chunk("IDAT", compressedData);
        idat.writeTo(channel);
        new Chunk("IEND", new byte[0]).writeTo(channel);
    }

    private static byte[] compress(ReadOnlyBitMatrix data, int imageSize, int pixelSize) {
        int dim = data.dim();
        int stride = (imageSize + 7) / 8; // bytes per scanline (1 bit per pixel, packed)
        // Build raw scanlines: each row is `stride` bytes, pixels packed MSB-first.
        // Quiet zone (4 modules each side) is white (0). Dark modules are 1.
        byte[] scanlines = new byte[imageSize * stride];
        int quietModules = 4; // quiet zone in modules
        for (int row = 0; row < imageSize; row++) {
            // module row in the QR matrix (may be outside matrix = white)
            int matrixRow = row / pixelSize - quietModules;
            for (int col = 0; col < imageSize; col++) {
                int matrixCol = col / pixelSize - quietModules;
                boolean dark =
                    matrixRow >= 0 &&
                    matrixRow < dim &&
                    matrixCol >= 0 &&
                    matrixCol < dim &&
                    data.get(matrixCol, matrixRow);
                if (dark) {
                    int byteIdx = row * stride + col / 8;
                    scanlines[byteIdx] |= (byte) (0x80 >>> (col % 8));
                }
            }
        }
        for (int i = 0; i < scanlines.length; i++) {
            scanlines[i] = (byte) ~scanlines[i];
        }
        return deflateScanlines(scanlines, imageSize, stride);
    }

    private static byte[] deflateScanlines(byte[] scanlines, int imageSize, int stride) {
        // Each PNG scanline is prefixed with a filter byte (0 = None).
        int filteredRowLen = 1 + stride;
        byte[] filtered = new byte[imageSize * filteredRowLen];
        for (int row = 0; row < imageSize; row++) {
            filtered[row * filteredRowLen] = 0; // filter type None
            System.arraycopy(scanlines, row * stride, filtered, row * filteredRowLen + 1, stride);
        }

        java.util.zip.Deflater deflater = new java.util.zip.Deflater(java.util.zip.Deflater.BEST_COMPRESSION);
        deflater.setInput(filtered);
        deflater.finish();
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(filtered.length);
        byte[] buf = new byte[8192];
        while (!deflater.finished()) {
            baos.write(buf, 0, deflater.deflate(buf));
        }
        deflater.end();
        return baos.toByteArray();
    }
}
