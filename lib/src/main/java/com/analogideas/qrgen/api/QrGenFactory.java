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
