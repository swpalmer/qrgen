package com.analogideas.qrgen.impl.svg;

import com.analogideas.qrgen.api.BinarySvgWriter;
import com.analogideas.qrgen.api.ReadOnlyBitMatrix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

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
