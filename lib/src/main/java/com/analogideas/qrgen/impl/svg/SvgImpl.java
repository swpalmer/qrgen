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
package com.analogideas.qrgen.impl.svg;

import com.analogideas.qrgen.api.BinarySvgWriter;
import com.analogideas.qrgen.api.ReadOnlyBitMatrix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

/**
 * An implementation of {@link BinarySvgWriter} that writes SVG data to a file or stream.
 */
public class SvgImpl implements BinarySvgWriter {

    @Override
    public void write(ReadOnlyBitMatrix data, File outputFile) throws IOException {
        int width = data.dim();
        int height = data.dim();
        try (FileOutputStream fos = new FileOutputStream(outputFile); var channel = fos.getChannel()) {
            write(data, channel);
        }
    }

    @Override
    public void write(ReadOnlyBitMatrix data, WritableByteChannel file) throws IOException {
        int width = data.dim();
        int height = data.dim();
    }

    public void write(ReadOnlyBitMatrix data, OutputStream os) throws IOException {
        int width = data.dim();
        int height = data.dim();
    }
}
