package com.analogideas.qrgen.api;

/**
 * Generates a QR code.
 */
public interface QrCodeGenerator {
    /**
     * Generates a QR code with the given data and error correction level.
     *
     * @param data the data to encode
     * @param ecl the error correction level
     * @return the generated QR code
     */
    QrCode generate(String data, ECL ecl);
}
