/*
 * Copyright 2021,2026 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen.impl;

import com.analogideas.qrgen.api.BitMatrix;
import com.analogideas.qrgen.api.ECL;
import com.analogideas.qrgen.api.QrCode;
import com.analogideas.qrgen.api.QrCodeGenerator;
import java.nio.charset.StandardCharsets;

/**
 * Simple library for generating URL QR Codes.
 *
 */
public class QrCodeGeneratorImpl implements QrCodeGenerator {

    /**
     * The minimum dimension of a QR code, in modules.
     */
    public static final int MIN_DIM = 21;
    /**
     * The maximum dimension of a QR code, in modules.
     */
    public static final int MAX_DIM = 177;

    /**
     * The mode of the QR code.
     */
    public static enum Mode {
        /** Numeric mode, ASCII 0-9 */
        NUMERIC,
        /** Alphanumeric mode, ASCII 0-9, A-Z, $, %, *, +, -, ., /, : */
        ALPHANUMERIC,
        /** Byte mode, 8-bit bytes */
        BYTE,
        /** Kanji mode, Shift JIS characters */
        KANJI,
    }

    static record CodewordCapacity(
        int modulesPerSide,
        int funcPatternModules,
        int formatVersionModules,
        int dataModules,
        int dataCapacity, // codewords
        int remainderBits
    ) {}

    // 7.4.10
    /**
     * The first padding codeword, used to pad the data section.
     */
    public static final byte PAD_CW1 = (byte) 0b11101100;
    /**
     * The second padding codeword, used to pad the data section.
     */
    public static final byte PAD_CW2 = (byte) 0b00010001;

    // Table 1 from spec 7.1
    // row 0 = QR Version 1
    static final CodewordCapacity[] capacityTable = {
        new CodewordCapacity(21, 202, 31, 208, 26, 0),
        new CodewordCapacity(25, 235, 31, 359, 44, 7),
        new CodewordCapacity(29, 243, 31, 567, 70, 7),
        new CodewordCapacity(33, 251, 31, 807, 100, 7),
        new CodewordCapacity(37, 259, 31, 1079, 134, 7),
        new CodewordCapacity(41, 267, 31, 1383, 172, 7),
        new CodewordCapacity(45, 390, 67, 1568, 196, 0),
        new CodewordCapacity(49, 398, 67, 1936, 242, 0),
        new CodewordCapacity(53, 406, 67, 2336, 292, 0),
        new CodewordCapacity(57, 414, 67, 2768, 346, 0),
        new CodewordCapacity(61, 422, 67, 3232, 404, 0),
        new CodewordCapacity(65, 430, 67, 3728, 466, 0),
        new CodewordCapacity(69, 438, 67, 4256, 532, 0),
        new CodewordCapacity(73, 611, 67, 4651, 581, 3),
        new CodewordCapacity(77, 619, 67, 5243, 655, 3),
        new CodewordCapacity(81, 627, 67, 5867, 733, 3),
        new CodewordCapacity(85, 635, 67, 6523, 815, 3),
        new CodewordCapacity(89, 643, 67, 7211, 901, 3),
        new CodewordCapacity(93, 651, 67, 7931, 991, 3),
        new CodewordCapacity(97, 659, 67, 8683, 1085, 3),
        new CodewordCapacity(101, 882, 67, 9252, 1156, 4),
        new CodewordCapacity(105, 890, 67, 10068, 1258, 4),
        new CodewordCapacity(109, 898, 67, 10916, 1364, 4),
        new CodewordCapacity(113, 906, 67, 11796, 1474, 4),
        new CodewordCapacity(117, 914, 67, 12708, 1588, 4),
        new CodewordCapacity(121, 922, 67, 13652, 1706, 4),
        new CodewordCapacity(125, 930, 67, 14628, 1828, 4),
        new CodewordCapacity(129, 1203, 67, 15371, 1921, 3),
        new CodewordCapacity(133, 1211, 67, 16411, 2051, 3),
        new CodewordCapacity(137, 1219, 67, 17483, 2185, 3),
        new CodewordCapacity(141, 1227, 67, 18587, 2323, 3),
        new CodewordCapacity(145, 1235, 67, 19723, 2465, 3),
        new CodewordCapacity(149, 1243, 67, 20891, 2611, 3),
        new CodewordCapacity(153, 1251, 67, 22091, 2761, 3),
        new CodewordCapacity(157, 1574, 67, 23008, 2876, 0),
        new CodewordCapacity(161, 1582, 67, 24272, 3034, 0),
        new CodewordCapacity(165, 1590, 67, 25568, 3196, 0),
        new CodewordCapacity(169, 1598, 67, 26896, 3362, 0),
        new CodewordCapacity(173, 1606, 67, 28256, 3532, 0),
        new CodewordCapacity(177, 1614, 67, 29648, 3706, 0),
    };

    //    private static final byte [] bitsPerCharCount = {
    //        10,10,10,10,10,10,10,10,10, // Versions 1-9
    //        12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12,12, // Versions 10-26
    //        14,14,14,14,14,14,14,14,14,14,14,14,14,14 // Versions 27-40
    //    };

    // QR Code version
    private int version = 1;

    // from spec Table 3
    /**
     * Returns the number of bits per character for numeric mode.
     *
     * @param version the QR code version
     * @return the number of bits per character for numeric mode
     */
    public static int numericCharCountBits(int version) {
        if (version <= 9) return 10;
        if (version <= 26) return 12;
        return 14;
    }

    // from spec Table 3
    /**
     * Returns the number of bits per character for alphanumeric mode.
     *
     * @param version the QR code version
     * @return the number of bits per character for alphanumeric mode
     */
    public static int alphanumericCharCountBits(int version) {
        if (version <= 9) return 9;
        if (version <= 26) return 11;
        return 13;
    }

    // from spec Table 3
    /**
     * Returns the number of bits per character for byte mode.
     *
     * @param version the QR code version
     * @return the number of bits per character for byte mode
     */
    public static int bytecharCountBits(int version) {
        if (version <= 9) return 8;
        return 16;
    }

    // from spec Table 3
    /**
     * Returns the number of bits per character for kanji mode.
     *
     * @param version the QR code version
     * @return the number of bits per character for kanji mode
     */
    public static int kanjiCharCountBits(int version) {
        if (version <= 9) return 8;
        if (version <= 26) return 10;
        return 12;
    }

    /**
     * Returns the number of bits per character for the given mode and version.
     *
     * @param mode the QR code mode
     * @param version the QR code version
     * @return the number of bits per character for the given mode and version
     */
    public static int charCountBits(Mode mode, int version) {
        return switch (mode) {
            case NUMERIC -> numericCharCountBits(version);
            case ALPHANUMERIC -> alphanumericCharCountBits(version);
            case BYTE -> bytecharCountBits(version);
            case KANJI -> kanjiCharCountBits(version);
        };
    }

    /**
     * Returns <code>true</code> if the given characters are all ASCII digits.
     *
     * @param characters the characters to check
     * @return <code>true</code> if the given characters are all ASCII digits
     */
    public static boolean isNumeric(String characters) {
        for (char c : characters.toCharArray()) {
            if (!isNumeric(c)) return false;
        }
        return true;
    }

    /**
     * Returns <code>true</code> if the given character is an ASCII digit.
     *
     * @param c the character to check
     * @return <code>true</code> if the given character is an ASCII digit
     */
    public static boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Returns <code>true</code> if the given characters are all alphanumeric characters.
     *
     * @param characters the characters to check
     * @return <code>true</code> if the given characters are all alphanumeric characters
     */
    public static boolean isAlphanumeric(String characters) {
        for (char c : characters.toCharArray()) {
            if (alphaNumericValue(c) < 0) return false;
        }
        return true;
    }

    /**
     * Returns <code>true</code> if the given character is an alphanumeric character.
     *
     * @param c the character to check
     * @return <code>true</code> if the given character is an alphanumeric character
     */
    public static boolean isAlphanumeric(char c) {
        return alphaNumericValue(c) != -1;
    }

    /**
     * Picks the best mode to encode the given characters.
     * (This method will currently never choose KANJI mode.)
     *
     * @param characters the characters to encode
     * @return The best mode to encode the given characters
     */
    public static Mode selectMode(String characters) {
        if (isNumeric(characters)) return Mode.NUMERIC;
        if (isAlphanumeric(characters)) return Mode.ALPHANUMERIC;
        return Mode.BYTE;
    }

    /**
     * Selects the best version for the given mode, error correction level, and characters.
     *
     * @param mode the mode to encode the characters
     * @param correctLevel the error correction level
     * @param characters the characters to encode
     * @return The best version for the given mode, error correction level, and characters,
     * or -1 if no suitable version is found
     */
    public static int selectVersion(Mode mode, ECL correctLevel, String characters) {
        int len = characters.length();

        // calculate required codewords
        // - will be different depending on mode

        // Quick sanity check that message length does not exceed maximum QR Code size
        int maxChars = switch (mode) {
            case NUMERIC -> 7089;
            case ALPHANUMERIC -> 4296;
            case BYTE -> 2953;
            case KANJI -> 1817;
        };
        if (len > maxChars) {
            return -1;
        }

        // TODO: something clever that can handle switching modes to optimize encoding.

        if (correctLevel != null) {
            return DataCapacity.fitToVersion(mode, correctLevel, len);
        }

        final int versionL = DataCapacity.fitToVersion(mode, ECL.L, len);
        final int versionM = DataCapacity.fitToVersion(mode, ECL.M, len);
        final int versionQ = DataCapacity.fitToVersion(mode, ECL.Q, len);
        final int versionH = DataCapacity.fitToVersion(mode, ECL.H, len);

        // This method picks the smallest version that will fit, ignoring ECL

        int version = versionL != -1 ? versionL : (versionM != -1) ? versionM : (versionQ != -1) ? versionQ : versionH;
        return version;
    }

    // Use this (multiple times) when determining which version to use for mixed modes
    int dataBitsNeeded(Mode mode, int length) {
        int dataBitsNeeded =
            4 + // mode indicator
            charCountBits(mode, version) + // Character numbre indicator
            switch (mode) {
                // Data
                // 10 bits per set of three, add 4 bits for 1 moe or 7 bits for 2 more
                case NUMERIC -> ((length / 3) * 10) + ((length % 3) == 0 ? 0 : ((length % 3) * 3 + 1));
                // 11 bits per pair, 6 bits for remainder
                case ALPHANUMERIC -> ((length / 2) * 11) + (length % 2) * 6;
                case BYTE -> length * 8;
                case KANJI -> length * 13; // assumes chars are 2-bytes
            };
        return dataBitsNeeded;
    }

    boolean[][] forURL(String url) {
        // Step 1: Data analysis
        //         check variety of chracters to encode
        //         pick modes to get optimal encoding
        //         pick error correction level
        //         select smallest version that will accomodate data

        // TODO determine mode needed.. possibly series of different modes with mode switches
        // default is ISO/IEC 8859-1 character set (ECI 000003)
        // Numberic mode 0-9 only  (10-bits per 3 input characters)
        // Alphanumeric mode 0-9, A-Z, SP,$,%,*,+,-,.,/,:  (11-bits per two input characters)
        // Byte mode (8-bits per character)
        // Kanji mode (Shift JIS based on JIS X 0208)
        // Strucutred Append mode
        // FNC1 mode

        // Step 2: Data encoding
        //         convert chracters into bitstream for the mode in force
        //         insert mode indicators as needed to change modes at beginning of each mode segment
        //         add Terminator at end of data sequence
        //         split bitstream into 8-bit codewords
        //         add Pad Characters as necessary to fill required number of data codewords for version

        // - Mode indicator
        // - Character count indicator
        // - Data bit stream
        // If first ECI is not default, then start with ECI header
        // - ECI mode indicator (4 bits)
        // - ECI designator (8,16, or 24 bits)
        // (bit stream is always MSbit to LSBit)
        //
        // Mode indicator is 4 bits
        // ECI          0111
        // Numeric      0001
        // Alphanumeric 0010
        // Byte         0100
        // Kanji        1000
        // FNC1         0101 (1st position)
        //              1001 (2nd position)
        // Terminator   0000 (not really a mode indicator)

        // Step 3: Error correction coding
        //         divide codeword sequence into required number of blocks to enable error correction
        //         generate error correction codewords for each block,
        //         appending error correction codewords to end of the data codeword sequence

        // Step 4: Structure final message
        //         interleave data and error correction codewords from each block as per 7.6 (step 3)
        //         add remainder bits as necessary

        // Step 5: Module placement in matrix
        //         place codeword modules into matric with finder pattern, separators, timing pattern,
        //         and alignment patterns

        // Step 6: Data masking
        //         apply data masking patterns in turn to the encodeing region.
        //         select the pattern which optimises the dark/light module balance
        //         and minimizes the occurance of undesireable patterns

        // Step 7: Format and version information
        //         generate the format information and version information to complete the QR code

        int dim = MIN_DIM;
        boolean[][] data = new boolean[dim][dim];
        return data;
    }

    BitStream encodeNumeric(String digits) {
        return encodeNumeric(digits, version);
    }

    // divide into groups of 3 digits, each encoded as a number using 10 bits,
    // if there are 1 or 2 remaining digits they are are encoded with 4 or 7 bits
    /**
     * Encodes a string of numeric digits as a numeric QR code.
     *
     * @param digits the digits to encode
     * @param version the version of the QR code
     * @return a {@link BitStream} containing the encoded data
     */
    public static BitStream encodeNumeric(String digits, int version) {
        BitStream bits = new BitStream();
        bits.addBits(0b0001, 4); // Mode numeric
        int len = digits.length();
        bits.addBits(len, numericCharCountBits(version));
        int p = 0;
        while (len - p >= 3) {
            String numStr = digits.substring(p, p + 3);
            int n = Integer.parseInt(numStr);
            bits.addBits(n, 10);
            p += 3;
        }
        if (p < len) {
            int n = Integer.parseInt(digits.substring(p));
            bits.addBits(n, (len - p > 1) ? 7 : 4);
        }
        return bits;
    }

    // spec Table 5 shows character to value mapping
    // basically 0-9 are digits,
    // 10-35 are uppercase letters
    // followed by SP,$,%,*,+,-,.,/,:  for 36-44
    /**
     * Returns the numeric value of the given character for alpha numeric mode,
     * or -1 if the character is not valid.
     *
     * @param c the character to get the value of
     * @return the numeric value of the given character
     */
    static int alphaNumericValue(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        switch (c) {
            case ' ':
                return 36;
            case '$':
                return 37;
            case '%':
                return 38;
            case '*':
                return 39;
            case '+':
                return 40;
            case '-':
                return 41;
            case '.':
                return 42;
            case '/':
                return 43;
            case ':':
                return 44;
        }
        return -1;
        //throw new RuntimeException("Bad character data for alphanumeric mode: "+c);
    }

    BitStream encodeAlphanumeric(String characters) {
        return encodeAlphanumeric(characters, version);
    }

    /**
     * Encodes the given characters as alpha numeric mode.
     *
     * @param characters the characters to encode
     * @param version the QR code version
     * @return the encoded bits
     */
    public static BitStream encodeAlphanumeric(String characters, int version) {
        BitStream bits = new BitStream();
        int len = characters.length();
        int p = 0;
        bits.addBits(0b0010, 4); // Mode
        bits.addBits(len, alphanumericCharCountBits(version)); // character count
        while (len - p >= 2) {
            int v1 = alphaNumericValue(characters.charAt(p));
            int v2 = alphaNumericValue(characters.charAt(p + 1));
            int v = v1 * 45 + v2; // 11-bit equivalent
            //System.err.println("("+v1+','+v2+") v = "+v+" = "+Integer.toBinaryString(v));
            bits.addBits(v, 11);
            p += 2;
        }
        if (
            p < len // remaining single char encoded in 6 bits
        ) bits.addBits(alphaNumericValue(characters.charAt(p)), 6);
        return bits;
    }

    BitStream encodeBytes(String characters) {
        return encodeBytes(characters, version);
    }

    // byte mode
    // default character set ISO 8859-1
    /**
     * Encodes the given characters as byte mode.
     *
     * @param characters the characters to encode
     * @param version the QR code version
     * @return the encoded bits
     */
    public static BitStream encodeBytes(String characters, int version) {
        BitStream bits = new BitStream();
        byte[] bytes = characters.getBytes(StandardCharsets.ISO_8859_1);
        bits.addBits(0b0100, 4); // mode
        bits.addBits(bytes.length, bytecharCountBits(version)); // character count
        for (byte b : bytes) {
            bits.addByte(b);
        }
        return bits;
    }

    /**
     * Encodes the given payload into a QR code using the specified error correction level.
     * Automatically selects the best mode and smallest version that fits the data.
     *
     * @param payload the data to encode
     * @param ecl     the error correction level
     * @return the generated {@link QrCode}
     */
    public QrCode generate(String payload, ECL ecl) {
        Mode mode = selectMode(payload);
        int version = selectVersion(mode, ecl, payload);
        DataCapacity dc = DataCapacity.forVersion(version, ecl);

        // Step 2: Data encoding
        BitStream bs = switch (mode) {
            case NUMERIC -> encodeNumeric(payload, version);
            case ALPHANUMERIC -> encodeAlphanumeric(payload, version);
            case BYTE -> encodeBytes(payload, version);
            default -> throw new IllegalArgumentException("Unsupported mode: " + mode);
        };

        // Terminator + bit-padding + codeword padding (spec 7.4.10)
        int bitsRemaining = dc.dataBits() - bs.length();
        for (int t = 4; t-- > 0 && bitsRemaining > 0; bitsRemaining--) bs.add(false);
        while ((bitsRemaining & 7) > 0) {
            bs.add(false);
            bitsRemaining--;
        }
        boolean pad = false;
        for (int cw = dc.dataCodewords() - bs.length() / 8; cw > 0; cw--, pad = !pad) bs.addByte(
            (byte) (pad ? 0b00010001 : 0b11101100)
        );

        // Step 3+4: Error correction and interleaving (spec 7.5, 7.6)
        ECCharacteristics ec = ECCharacteristics.forVersion(version, ecl);
        int totalDataCW = ec.c1() * ec.b1() + ec.c2() * ec.b2();
        java.util.BitSet rawBits = bs.toBitSet();
        byte[] data = new byte[totalDataCW];
        for (int i = 0; i < totalDataCW; i++) {
            int b = 0;
            for (int j = 0; j < 8; j++) if (rawBits.get(i * 8 + j)) b |= (0x80 >>> j);
            data[i] = (byte) b;
        }

        int total = ec.totalBlocks();
        int[][] dataBlocks = new int[total][];
        int[][] ecBlocks = new int[total][];
        int offset = 0;
        for (int i = 0; i < total; i++) {
            int len = (i < ec.b1()) ? ec.c1() : ec.c2();
            dataBlocks[i] = new int[len];
            for (int j = 0; j < len; j++) dataBlocks[i][j] = data[offset++] & 0xFF;
            ecBlocks[i] = ReedSolomon.computeEC(dataBlocks[i], ec.ecPerBlock());
        }

        BitStream out = new BitStream();
        int maxData = (ec.b2() > 0) ? ec.c2() : ec.c1();
        for (int col = 0; col < maxData; col++) for (int blk = 0; blk < total; blk++) if (
            col < dataBlocks[blk].length
        ) out.addBits(dataBlocks[blk][col], 8);
        for (int col = 0; col < ec.ecPerBlock(); col++) for (int blk = 0; blk < total; blk++) out.addBits(
            ecBlocks[blk][col],
            8
        );

        // Step 5-7: Matrix placement, masking, format/version info
        return new QrCodeImpl(version, ecl, out.toBitSet());
    }
}

/*

Finder patterns
marking corners are: dark light dark  light dark
         with ratio:   1    1     3     1    1


     11 3 11
     ||/ \||
 1   XXXXXXX
 1   X     X
     X XXX X
 3   X XXX X
     X XXX X
 1   X     X
 1   XXXXXXX


They are separated from the other bits with white

    012345678901234567890

 0  XXXXXXX ..... XXXXXXX
 1  X     X ..... X     X
 2  X XXX X ..... X XXX X
 3  X XXX X ..... X XXX X
 4  X XXX X ..... X XXX X
 5  X     X ..... X     X
 6  XXXXXXX ..... XXXXXXX
 7          .....
 8  .....................
 9  .....................
 0  .....................
 1  .....................
 2  .....................
 3          .............
 4  XXXXXXX .............
 5  X     X .............
 6  X XXX X .............
 7  X XXX X .............
 8  X XXX X .............
 9  X     X .............
 0  XXXXXXX .............

There must be a "quiet zone" border around it
on all four sides


There can be alignment patterns

   XXXXX
   X   X
   X X X   <-- centre of alignment pattern is lined up with inner corner of finder pattern
   X   x
   XXXXX


M3 Micro QR code

Finder pattern
      ||     ____separator
      \/    /
 0  XXXXXXX X X X X   <--- timing pattern (row 0 of Micro QR)
 1  X     X .      |
 2  X XXX X .      |
 3  X XXX X .      |
 4  X XXX X .      |   <--- quiet zone
 5  X     X .      |
 6  XXXXXXX .      |
 7          .      |
 8  X........<--- Format information
 9                 |
 0  X              |
 1                 |
 2  X          <-------- Encoding region
 3                 |
 4  X______________|
    ^
    |
   Timing pattern


QR code quiet zone is 4x
Micro QR Code quiet zone is 2x

Version 1 is 21 x 21
Version 2 is 25 x 25
.
.  increase by 4...
.
Version 40 is 177 x 177


Timing patterns run across row 6 and down column 6
between the finder patterns (and any alignment patterns)

Alignment patterns are only present in Version 2 and higher.



*/
