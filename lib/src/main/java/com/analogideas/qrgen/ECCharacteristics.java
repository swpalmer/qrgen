/*
 * Copyright 2021,2026 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen;

/**
 * Represents the error correction characteristics for a given QR code version and error correction level.
 */
public record ECCharacteristics(
    int nCW, // total number of codewords
    ECL ecl, // Error correction level
    int nECCW, // number of EC codewords per block
    int p, // (unused remainder field)
    int c1, // data codewords per block in group 1
    int b1, // number of blocks in group 1
    int c2, // data codewords per block in group 2 (0 if none)
    int b2 // number of blocks in group 2 (0 if none)
) {
    /**
     * Table 9: EC characteristics for each version and error correction level.
     */
    public static final ECCharacteristics[] table9 = {
        // version 1
        new ECCharacteristics(26, ECL.L, 7, 3, 19, 1, 0, 0),
        new ECCharacteristics(26, ECL.M, 10, 2, 16, 1, 0, 0),
        new ECCharacteristics(26, ECL.Q, 13, 1, 13, 1, 0, 0),
        new ECCharacteristics(26, ECL.H, 17, 1, 9, 1, 0, 0),
        // version 2
        new ECCharacteristics(44, ECL.L, 10, 2, 34, 1, 0, 0),
        new ECCharacteristics(44, ECL.M, 16, 0, 28, 1, 0, 0),
        new ECCharacteristics(44, ECL.Q, 22, 0, 22, 1, 0, 0),
        new ECCharacteristics(44, ECL.H, 28, 0, 16, 1, 0, 0),
        // version 3
        new ECCharacteristics(70, ECL.L, 15, 1, 55, 1, 0, 0),
        new ECCharacteristics(70, ECL.M, 26, 0, 44, 1, 0, 0),
        new ECCharacteristics(70, ECL.Q, 36, 0, 17, 2, 0, 0),
        new ECCharacteristics(70, ECL.H, 44, 0, 13, 2, 0, 0),
        // version 4
        new ECCharacteristics(100, ECL.L, 20, 0, 80, 1, 0, 0),
        new ECCharacteristics(100, ECL.M, 36, 0, 32, 2, 0, 0),
        new ECCharacteristics(100, ECL.Q, 52, 0, 24, 2, 0, 0),
        new ECCharacteristics(100, ECL.H, 64, 0, 9, 4, 0, 0),
        // version 5
        new ECCharacteristics(134, ECL.L, 26, 0, 108, 1, 0, 0),
        new ECCharacteristics(134, ECL.M, 48, 0, 43, 2, 0, 0),
        new ECCharacteristics(134, ECL.Q, 72, 0, 15, 2, 16, 2),
        new ECCharacteristics(134, ECL.H, 88, 0, 11, 2, 12, 2),
        // version 6
        new ECCharacteristics(172, ECL.L, 36, 0, 68, 2, 0, 0),
        new ECCharacteristics(172, ECL.M, 64, 0, 27, 4, 0, 0),
        new ECCharacteristics(172, ECL.Q, 96, 0, 19, 4, 0, 0),
        new ECCharacteristics(172, ECL.H, 112, 0, 15, 4, 0, 0),
        // version 7
        new ECCharacteristics(196, ECL.L, 40, 0, 78, 2, 0, 0),
        new ECCharacteristics(196, ECL.M, 72, 0, 31, 4, 0, 0),
        new ECCharacteristics(196, ECL.Q, 108, 0, 14, 2, 15, 4),
        new ECCharacteristics(196, ECL.H, 130, 0, 13, 4, 14, 1),
        // version 8
        new ECCharacteristics(242, ECL.L, 48, 0, 97, 2, 0, 0),
        new ECCharacteristics(242, ECL.M, 88, 0, 38, 2, 39, 2),
        new ECCharacteristics(242, ECL.Q, 132, 0, 18, 4, 19, 2),
        new ECCharacteristics(242, ECL.H, 156, 0, 14, 4, 15, 2),
        // version 9
        new ECCharacteristics(292, ECL.L, 60, 0, 116, 2, 0, 0),
        new ECCharacteristics(292, ECL.M, 110, 0, 36, 3, 37, 2),
        new ECCharacteristics(292, ECL.Q, 160, 0, 16, 4, 17, 4),
        new ECCharacteristics(292, ECL.H, 192, 0, 12, 4, 13, 4),
        // version 10
        new ECCharacteristics(346, ECL.L, 72, 0, 68, 2, 69, 2),
        new ECCharacteristics(346, ECL.M, 130, 0, 43, 4, 44, 1),
        new ECCharacteristics(346, ECL.Q, 192, 0, 19, 6, 20, 2),
        new ECCharacteristics(346, ECL.H, 224, 0, 15, 6, 16, 2),
        // version 11
        new ECCharacteristics(404, ECL.L, 80, 0, 81, 4, 0, 0),
        new ECCharacteristics(404, ECL.M, 150, 0, 50, 1, 51, 4),
        new ECCharacteristics(404, ECL.Q, 224, 0, 22, 4, 23, 4),
        new ECCharacteristics(404, ECL.H, 264, 0, 12, 3, 13, 8),
        // version 12
        new ECCharacteristics(466, ECL.L, 96, 0, 92, 2, 93, 2),
        new ECCharacteristics(466, ECL.M, 176, 0, 36, 6, 37, 2),
        new ECCharacteristics(466, ECL.Q, 260, 0, 20, 4, 21, 6),
        new ECCharacteristics(466, ECL.H, 308, 0, 14, 7, 15, 4),
        // version 13
        new ECCharacteristics(532, ECL.L, 104, 0, 107, 4, 0, 0),
        new ECCharacteristics(532, ECL.M, 198, 0, 37, 8, 38, 1),
        new ECCharacteristics(532, ECL.Q, 288, 0, 20, 8, 21, 4),
        new ECCharacteristics(532, ECL.H, 352, 0, 11, 12, 12, 4),
        // version 14
        new ECCharacteristics(581, ECL.L, 120, 0, 115, 3, 116, 1),
        new ECCharacteristics(581, ECL.M, 216, 0, 40, 4, 41, 5),
        new ECCharacteristics(581, ECL.Q, 320, 0, 16, 11, 17, 5),
        new ECCharacteristics(581, ECL.H, 384, 0, 12, 11, 13, 5),
        // version 15
        new ECCharacteristics(655, ECL.L, 132, 0, 87, 5, 88, 1),
        new ECCharacteristics(655, ECL.M, 240, 0, 41, 5, 42, 5),
        new ECCharacteristics(655, ECL.Q, 360, 0, 24, 5, 25, 7),
        new ECCharacteristics(655, ECL.H, 432, 0, 12, 11, 13, 7),
        // version 16
        new ECCharacteristics(733, ECL.L, 144, 0, 98, 5, 99, 1),
        new ECCharacteristics(733, ECL.M, 280, 0, 45, 7, 46, 3),
        new ECCharacteristics(733, ECL.Q, 408, 0, 19, 15, 20, 2),
        new ECCharacteristics(733, ECL.H, 480, 0, 15, 3, 16, 13),
        // version 17
        new ECCharacteristics(815, ECL.L, 168, 0, 107, 1, 108, 5),
        new ECCharacteristics(815, ECL.M, 308, 0, 46, 10, 47, 1),
        new ECCharacteristics(815, ECL.Q, 448, 0, 22, 1, 23, 15),
        new ECCharacteristics(815, ECL.H, 532, 0, 14, 2, 15, 17),
        // version 18
        new ECCharacteristics(901, ECL.L, 180, 0, 120, 5, 121, 1),
        new ECCharacteristics(901, ECL.M, 338, 0, 43, 9, 44, 4),
        new ECCharacteristics(901, ECL.Q, 504, 0, 22, 17, 23, 1),
        new ECCharacteristics(901, ECL.H, 588, 0, 14, 2, 15, 19),
        // version 19
        new ECCharacteristics(991, ECL.L, 196, 0, 113, 3, 114, 4),
        new ECCharacteristics(991, ECL.M, 364, 0, 44, 3, 45, 11),
        new ECCharacteristics(991, ECL.Q, 546, 0, 21, 17, 22, 4),
        new ECCharacteristics(991, ECL.H, 650, 0, 13, 9, 14, 16),
        // version 20
        new ECCharacteristics(1085, ECL.L, 224, 0, 107, 3, 108, 5),
        new ECCharacteristics(1085, ECL.M, 416, 0, 41, 3, 42, 13),
        new ECCharacteristics(1085, ECL.Q, 600, 0, 24, 15, 25, 5),
        new ECCharacteristics(1085, ECL.H, 700, 0, 15, 15, 16, 10),
        // version 21
        new ECCharacteristics(1156, ECL.L, 224, 0, 116, 4, 117, 4),
        new ECCharacteristics(1156, ECL.M, 442, 0, 42, 17, 0, 0),
        new ECCharacteristics(1156, ECL.Q, 644, 0, 22, 17, 23, 6),
        new ECCharacteristics(1156, ECL.H, 750, 0, 16, 19, 17, 6),
        // version 22
        new ECCharacteristics(1258, ECL.L, 252, 0, 111, 2, 112, 7),
        new ECCharacteristics(1258, ECL.M, 476, 0, 46, 17, 0, 0),
        new ECCharacteristics(1258, ECL.Q, 690, 0, 24, 7, 25, 16),
        new ECCharacteristics(1258, ECL.H, 816, 0, 13, 34, 0, 0),
        // version 23
        new ECCharacteristics(1364, ECL.L, 270, 0, 121, 4, 122, 5),
        new ECCharacteristics(1364, ECL.M, 504, 0, 47, 4, 48, 14),
        new ECCharacteristics(1364, ECL.Q, 750, 0, 24, 11, 25, 14),
        new ECCharacteristics(1364, ECL.H, 900, 0, 15, 16, 16, 14),
        // version 24
        new ECCharacteristics(1474, ECL.L, 300, 0, 117, 6, 118, 4),
        new ECCharacteristics(1474, ECL.M, 560, 0, 45, 6, 46, 14),
        new ECCharacteristics(1474, ECL.Q, 810, 0, 24, 11, 25, 16),
        new ECCharacteristics(1474, ECL.H, 960, 0, 16, 30, 17, 2),
        // version 25
        new ECCharacteristics(1588, ECL.L, 312, 0, 106, 8, 107, 4),
        new ECCharacteristics(1588, ECL.M, 588, 0, 45, 8, 46, 13),
        new ECCharacteristics(1588, ECL.Q, 870, 0, 24, 7, 25, 22),
        new ECCharacteristics(1588, ECL.H, 1050, 0, 15, 22, 16, 13),
        // version 26
        new ECCharacteristics(1706, ECL.L, 336, 0, 114, 10, 115, 2),
        new ECCharacteristics(1706, ECL.M, 644, 0, 46, 19, 47, 4),
        new ECCharacteristics(1706, ECL.Q, 952, 0, 22, 28, 23, 6),
        new ECCharacteristics(1706, ECL.H, 1110, 0, 16, 33, 17, 4),
        // version 27
        new ECCharacteristics(1828, ECL.L, 360, 0, 122, 8, 123, 4),
        new ECCharacteristics(1828, ECL.M, 700, 0, 45, 22, 46, 3),
        new ECCharacteristics(1828, ECL.Q, 1020, 0, 23, 8, 24, 26),
        new ECCharacteristics(1828, ECL.H, 1200, 0, 15, 12, 16, 28),
        // version 28
        new ECCharacteristics(1921, ECL.L, 390, 0, 117, 3, 118, 10),
        new ECCharacteristics(1921, ECL.M, 728, 0, 45, 3, 46, 23),
        new ECCharacteristics(1921, ECL.Q, 1050, 0, 24, 4, 25, 31),
        new ECCharacteristics(1921, ECL.H, 1260, 0, 15, 11, 16, 31),
        // version 29
        new ECCharacteristics(2051, ECL.L, 420, 0, 116, 7, 117, 7),
        new ECCharacteristics(2051, ECL.M, 812, 0, 45, 21, 46, 7),
        new ECCharacteristics(2051, ECL.Q, 1200, 0, 23, 1, 24, 37),
        new ECCharacteristics(2051, ECL.H, 1440, 0, 15, 19, 16, 26),
        // version 30
        new ECCharacteristics(2185, ECL.L, 450, 0, 115, 5, 116, 10),
        new ECCharacteristics(2185, ECL.M, 812, 0, 45, 19, 46, 10),
        new ECCharacteristics(2185, ECL.Q, 1200, 0, 24, 15, 25, 25),
        new ECCharacteristics(2185, ECL.H, 1440, 0, 15, 23, 16, 25),
        // version 31
        new ECCharacteristics(2323, ECL.L, 480, 0, 115, 13, 116, 3),
        new ECCharacteristics(2323, ECL.M, 868, 0, 45, 2, 46, 29),
        new ECCharacteristics(2323, ECL.Q, 1290, 0, 24, 42, 25, 1),
        new ECCharacteristics(2323, ECL.H, 1530, 0, 15, 23, 16, 28),
        // version 32
        new ECCharacteristics(2465, ECL.L, 510, 0, 115, 17, 0, 0),
        new ECCharacteristics(2465, ECL.M, 924, 0, 45, 10, 46, 23),
        new ECCharacteristics(2465, ECL.Q, 1350, 0, 24, 10, 25, 35),
        new ECCharacteristics(2465, ECL.H, 1620, 0, 15, 19, 16, 35),
        // version 33
        new ECCharacteristics(2611, ECL.L, 540, 0, 115, 17, 116, 1),
        new ECCharacteristics(2611, ECL.M, 980, 0, 45, 14, 46, 21),
        new ECCharacteristics(2611, ECL.Q, 1440, 0, 24, 29, 25, 19),
        new ECCharacteristics(2611, ECL.H, 1710, 0, 15, 11, 16, 46),
        // version 34
        new ECCharacteristics(2761, ECL.L, 570, 0, 115, 13, 116, 6),
        new ECCharacteristics(2761, ECL.M, 1036, 0, 46, 14, 47, 23),
        new ECCharacteristics(2761, ECL.Q, 1530, 0, 24, 44, 25, 7),
        new ECCharacteristics(2761, ECL.H, 1800, 0, 16, 59, 17, 1),
        // version 35
        new ECCharacteristics(2876, ECL.L, 570, 0, 121, 12, 122, 7),
        new ECCharacteristics(2876, ECL.M, 1064, 0, 47, 12, 48, 26),
        new ECCharacteristics(2876, ECL.Q, 1590, 0, 24, 39, 25, 14),
        new ECCharacteristics(2876, ECL.H, 1890, 0, 15, 22, 16, 41),
        // version 36
        new ECCharacteristics(3034, ECL.L, 600, 0, 121, 6, 122, 14),
        new ECCharacteristics(3034, ECL.M, 1120, 0, 47, 6, 48, 34),
        new ECCharacteristics(3034, ECL.Q, 1680, 0, 24, 46, 25, 10),
        new ECCharacteristics(3034, ECL.H, 1980, 0, 15, 2, 16, 64),
        // version 37
        new ECCharacteristics(3196, ECL.L, 630, 0, 122, 17, 123, 4),
        new ECCharacteristics(3196, ECL.M, 1204, 0, 46, 29, 47, 14),
        new ECCharacteristics(3196, ECL.Q, 1770, 0, 24, 49, 25, 10),
        new ECCharacteristics(3196, ECL.H, 2100, 0, 15, 24, 16, 46),
        // version 38
        new ECCharacteristics(3362, ECL.L, 660, 0, 122, 4, 123, 18),
        new ECCharacteristics(3362, ECL.M, 1260, 0, 46, 13, 47, 32),
        new ECCharacteristics(3362, ECL.Q, 1860, 0, 24, 48, 25, 14),
        new ECCharacteristics(3362, ECL.H, 2220, 0, 15, 42, 16, 32),
        // version 39
        new ECCharacteristics(3532, ECL.L, 720, 0, 117, 20, 118, 4),
        new ECCharacteristics(3532, ECL.M, 1316, 0, 47, 40, 48, 7),
        new ECCharacteristics(3532, ECL.Q, 1950, 0, 24, 43, 25, 22),
        new ECCharacteristics(3532, ECL.H, 2310, 0, 15, 10, 16, 67),
        // version 40
        new ECCharacteristics(3706, ECL.L, 750, 0, 118, 19, 119, 6),
        new ECCharacteristics(3706, ECL.M, 1372, 0, 47, 18, 48, 31),
        new ECCharacteristics(3706, ECL.Q, 2040, 0, 24, 34, 25, 34),
        new ECCharacteristics(3706, ECL.H, 2430, 0, 15, 20, 16, 61), // End of Table 9
    };

    /**
     * Total number of blocks across both groups.
     * @return the total number of blocks across both groups
     */
    public int totalBlocks() {
        return b1 + b2;
    }

    /**
     * EC codewords per block (nECCW stores total; divide by block count).
     * @return the number of EC codewords per block
     */
    public int ecPerBlock() {
        return nECCW / totalBlocks();
    }

    /**
     * Lookup by version (1-40) and ECL.
     * @param version the version of the QR code
     * @param ecl the error correction level to use
     * @return the EC characteristics for the given version and error correction level`
     */
    public static ECCharacteristics forVersion(int version, ECL ecl) {
        return table9[(version - 1) * 4 + ecl.ordinal()];
    }
}
