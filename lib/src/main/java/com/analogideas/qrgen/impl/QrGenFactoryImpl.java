package com.analogideas.qrgen.impl;

import com.analogideas.qrgen.api.BinaryPngWriter;
import com.analogideas.qrgen.api.BinarySvgWriter;
import com.analogideas.qrgen.api.QrCodeGenerator;
import com.analogideas.qrgen.api.QrGenFactory;
import com.analogideas.qrgen.impl.png.PngImpl;
import com.analogideas.qrgen.impl.svg.SvgImpl;

public class QrGenFactoryImpl implements QrGenFactory {

    @Override
    public QrCodeGenerator qrCodeGenerator() {
        return new QrCodeGeneratorImpl();
    }

    @Override
    public BinaryPngWriter pngWriter() {
        return new PngImpl();
    }

    @Override
    public BinarySvgWriter svgWriter() {
        return new SvgImpl();
    }
}
