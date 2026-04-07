package com.analogideas.qrgen.png;

import static org.junit.jupiter.api.Assertions.*;

import com.analogideas.qrgen.png.PngWriter.Chunk;
import org.junit.jupiter.api.Test;

class PngWriterTest {

    @Test
    void testType() {
        Chunk chunk = new Chunk("IHDR", new byte[0]);
        assertEquals(0x49484452, chunk.type());
    }
}
