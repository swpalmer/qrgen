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
package com.analogideas.qrgen.impl;

import com.analogideas.qrgen.api.BinaryPngWriter;
import com.analogideas.qrgen.api.BinarySvgWriter;
import com.analogideas.qrgen.api.QrCodeGenerator;
import com.analogideas.qrgen.api.QrGenFactory;
import com.analogideas.qrgen.impl.png.PngImpl;
import com.analogideas.qrgen.impl.svg.SvgImpl;

/**
 * The default implementation of {@link QrGenFactory}.
 */
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
