package com.analogideas.qrgen.api;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;

public interface BinarySvgWriter {
    void write(ReadOnlyBitMatrix data, File file) throws IOException;
    void write(ReadOnlyBitMatrix data, WritableByteChannel file) throws IOException;
    void write(ReadOnlyBitMatrix data, OutputStream os) throws IOException;
}
