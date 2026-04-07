package com.analogideas.qrgen.impl.png;

import static org.junit.jupiter.api.Assertions.*;

import com.analogideas.qrgen.impl.png.PngImpl.Chunk;
import org.junit.jupiter.api.Test;

class PngWriterTest {

    @Test
    void testType() {
        Chunk chunk = new Chunk("IHDR", new byte[0]);
        assertEquals(0x49484452, chunk.type());
    }
}
