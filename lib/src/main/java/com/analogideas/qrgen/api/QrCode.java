package com.analogideas.qrgen.api;

/**
 * Represents a QR code.
 */
public interface QrCode {
    /**
     * Returns the matrix representation of this QR code.
     *
     * @return the matrix representation of this QR code
     */
    ReadOnlyBitMatrix getMatrix();

    /**
     * Returns the error correction level of this QR code.
     *
     * @return the error correction level of this QR code
     */
    ECL getEcl();
}
