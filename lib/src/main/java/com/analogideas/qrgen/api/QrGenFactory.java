package com.analogideas.qrgen.api;

import com.analogideas.qrgen.impl.QrGenFactoryImpl;

/**
 * Factory interface for creating QR code generators and writers.
 */
public interface QrGenFactory {
    /**
     * Returns a new {@link QrGenFactory} instance.
     * @return a new {@link QrGenFactory} instance
     */
    static QrGenFactory newFactoryInstance() {
        return new QrGenFactoryImpl();
    }

    /**
     * The singleton {@link QrGenFactory} instance.
     */
    static final QrGenFactory instance = newFactoryInstance();

    /**
     * Returns a new {@link QrCodeGenerator} instance.
     * @return a new {@link QrCodeGenerator} instance
     */
    QrCodeGenerator qrCodeGenerator();
    /**
     * Returns a new {@link BinaryPngWriter} instance.
     * @return a new {@link BinaryPngWriter} instance
     */
    BinaryPngWriter pngWriter();
    /**
     * Returns a new {@link BinarySvgWriter} instance.
     * @return a new {@link BinarySvgWriter} instance
     */
    BinarySvgWriter svgWriter();
}
