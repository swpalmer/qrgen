/*
   Copyright 2021, 2026 Scott W. Palmer

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

import static org.junit.jupiter.api.Assertions.*;

import com.analogideas.qrgen.api.QrCodeGenerator;
import com.analogideas.qrgen.api.QrGenFactory;
import org.junit.jupiter.api.Test;

public class QRCodeGenTest {

    public QRCodeGenTest() {}

    /**
     * Test of forURL method, of class QRCodeGen.
     */
    //@Test
    public void testForURL() {
        System.out.println("forURL");
        String url = "";
        QrCodeGeneratorImpl instance = (QrCodeGeneratorImpl) QrGenFactory.instance.qrCodeGenerator();
        boolean[][] expResult = null;
        boolean[][] result = instance.forURL(url);
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of encodeNumeric method, of class QRCodeGen.
     */
    @Test
    public void testEncodeNumeric() {
        System.out.println("encodeNumeric");
        String digits = "01234567";
        QrCodeGeneratorImpl instance = (QrCodeGeneratorImpl) QrGenFactory.instance.qrCodeGenerator();
        //                  mode   char count         '012'        '345'      '67'
        String expResult = "0001" + "0000001000" + "0000001100" + "0101011001" + "1000011";
        BitStream result = instance.encodeNumeric(digits);
        assertEquals(expResult, result.toString());

        digits = "012345678";
        //                                                                678
        expResult = "0001" + "0000001001" + "0000001100" + "0101011001" + "1010100110";
        result = instance.encodeNumeric(digits);
        assertEquals(expResult, result.toString());

        digits = "0123456";
        //                                                            6
        expResult = "0001" + "0000000111" + "0000001100" + "0101011001" + "0110";
        result = instance.encodeNumeric(digits);
        assertEquals(expResult, result.toString());
    }

    @Test
    public void testEncodeAlphanumeric() {
        System.out.println("encodeAlphanumeric");
        String characters = "AC-42";
        QrCodeGeneratorImpl instance = (QrCodeGeneratorImpl) QrGenFactory.instance.qrCodeGenerator();
        // AC-42  --> (10,12,41,4,2)
        // 10*45+12 = 461 = 00111001110
        // 41*45+4 = 1849 = 11100111001
        // 2       =    2 =      000010
        //                  mode  char count          '462'        '1849'     '2'
        String expResult = "0010" + "000000101" + "00111001110" + "11100111001" + "000010";
        BitStream result = instance.encodeAlphanumeric(characters);
        assertEquals(expResult, result.toString());

        characters = "SCOTT";
        // SCOTT --> (28,12,24,29,29)
        // 28*45+12 = 1272 = 10011111000
        // 24*45+29 = 1109 = 10001010101
        // 29       =   29 =      011101
        //                                                              29
        expResult = "0010" + "000000101" + "10011111000" + "10001010101" + "011101";
        result = instance.encodeAlphanumeric(characters);
        assertEquals(expResult, result.toString());

        System.err.println("---");
        characters = "HTTP://WWW.ANALOGIDEAS.COM/";
        // --> (17,29,29,25,44,43,43,32,32,32,42,10,23,10,21,24,16,18,13,14,10,28,42,12,24,22,43)
        // 17*45+29 =  794 = 01100011010
        // 29*45+25 = 1330 = 10100110010
        // 44*45+43 = 2023 = 11111100111
        // 43*45+32 = 1967 = 11110101111
        // 32*45+32 = 1472 = 10111000000
        // 42*45+10 = 1900 = 11101101100
        // 23*45+10 = 1045 = 10000010101
        // 21*45+24 =  969 = 01111001001
        // 16*45+18 =  738 = 01011100010
        // 13*45+14 =  599 = 01001010111
        // 10*45+28 =  478 = 00111011110
        // 42*45+12 = 1902 = 11101101110
        // 24*45+22 = 1102 = 10001001110
        // 43
        //                         27
        expResult =
            "0010" +
            "000011011" +
            "01100011010" +
            "10100110010" +
            "11111100111" +
            "11110101111" +
            "10111000000" +
            "11101101100" +
            "10000010101" +
            "01111001001" +
            "01011100010" +
            "01001010111" +
            "00111011110" +
            "11101101110" +
            "10001001110" +
            "101011";
        result = instance.encodeAlphanumeric(characters);
        assertEquals(expResult, result.toString());
        System.err.println("---");

        characters = "FOUR";
        // FOUR --> (15,24,30,27)
        // 15*45+24 =  699 = 01010111011
        // 30*45+27 = 1377 = 10101100001
        //
        expResult = "0010" + "000000100" + "01010111011" + "10101100001";
        result = instance.encodeAlphanumeric(characters);
        assertEquals(expResult, result.toString());
    }
}
