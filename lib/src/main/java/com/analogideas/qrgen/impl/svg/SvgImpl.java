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
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * An implementation of {@link BinarySvgWriter} that writes SVG data to a file or stream.
 */
public class SvgImpl implements BinarySvgWriter {

    private static final int QUIET = 4; // quiet zone in modules

    @Override
    public void write(ReadOnlyBitMatrix data, File outputFile) throws IOException {
        Files.writeString(outputFile.toPath(), toSvgString(data), StandardCharsets.UTF_8);
    }

    @Override
    public void write(ReadOnlyBitMatrix data, WritableByteChannel channel) throws IOException {
        channel.write(ByteBuffer.wrap(toSvgString(data).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void write(ReadOnlyBitMatrix data, OutputStream os) throws IOException {
        os.write(toSvgString(data).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String toSvgString(ReadOnlyBitMatrix data) {
        int dim = data.dim();
        int total = dim + QUIET * 2;
        var sb = new StringBuilder(512 + dim * dim * 20);
        sb
            .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 ")
            .append(total)
            .append(' ')
            .append(total)
            .append("\">")
            .append("<rect width=\"")
            .append(total)
            .append("\" height=\"")
            .append(total)
            .append("\" fill=\"white\"/>");
        for (int y = 0; y < dim; y++) {
            for (int x = 0; x < dim; x++) {
                if (data.get(x, y)) {
                    sb
                        .append("<rect x=\"")
                        .append(x + QUIET)
                        .append("\" y=\"")
                        .append(y + QUIET)
                        .append("\" width=\"1\" height=\"1\"/>");
                }
            }
        }
        sb.append("</svg>");
        return sb.toString();
    }
}
