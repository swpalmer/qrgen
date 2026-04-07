package com.analogideas.qrgen.api;

import com.analogideas.qrgen.impl.QrGenFactoryImpl;

public interface QrGenFactory {
    static QrGenFactory newFactoryInstance() {
        return new QrGenFactoryImpl();
    }

    static final QrGenFactory instance = newFactoryInstance();

    static QrGenFactory factory() {
        return instance;
    }

    QrCodeGenerator qrCodeGenerator();
    BinaryPngWriter pngWriter();
    BinarySvgWriter svgWriter();
}
