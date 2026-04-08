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

import com.analogideas.qrgen.api.ECL;

/**
 * Determines the data capacity of a QR code for a given mode and error correction level.
 */
public record DataCapacity(
    ECL ecl,
    int dataCodewords,
    int numericCapacity,
    int alphanumericCapcity,
    int byteCapacity,
    int kanjiCapacity
) {
    // Table 7 from spec 7.4.10
    private static final DataCapacity[] dataCapacityTable = {
        new DataCapacity(ECL.L, 19, 41, 25, 17, 10), // Version 1
        new DataCapacity(ECL.M, 16, 34, 20, 14, 8),
        new DataCapacity(ECL.Q, 13, 27, 16, 11, 7),
        new DataCapacity(ECL.H, 9, 17, 10, 7, 4),
        new DataCapacity(ECL.L, 34, 77, 47, 32, 20), // Version 2
        new DataCapacity(ECL.M, 28, 63, 38, 26, 16),
        new DataCapacity(ECL.Q, 22, 48, 29, 20, 12),
        new DataCapacity(ECL.H, 16, 34, 20, 14, 8),
        new DataCapacity(ECL.L, 55, 127, 77, 53, 32), // Version 3
        new DataCapacity(ECL.M, 44, 101, 61, 42, 26),
        new DataCapacity(ECL.Q, 34, 77, 47, 32, 20),
        new DataCapacity(ECL.H, 26, 58, 35, 24, 15),
        new DataCapacity(ECL.L, 80, 187, 114, 78, 48), // Version 4
        new DataCapacity(ECL.M, 64, 149, 90, 62, 38),
        new DataCapacity(ECL.Q, 48, 111, 67, 46, 28),
        new DataCapacity(ECL.H, 36, 82, 50, 34, 21),
        new DataCapacity(ECL.L, 108, 255, 154, 106, 65), // Version 5
        new DataCapacity(ECL.M, 86, 202, 122, 84, 52),
        new DataCapacity(ECL.Q, 62, 144, 87, 60, 37),
        new DataCapacity(ECL.H, 46, 106, 64, 44, 27),
        new DataCapacity(ECL.L, 136, 322, 195, 134, 82), // Version 6
        new DataCapacity(ECL.M, 108, 255, 154, 106, 65),
        new DataCapacity(ECL.Q, 76, 178, 108, 74, 45),
        new DataCapacity(ECL.H, 60, 139, 84, 56, 36),
        new DataCapacity(ECL.L, 156, 370, 224, 154, 95), // Version 7
        new DataCapacity(ECL.M, 124, 293, 178, 122, 75),
        new DataCapacity(ECL.Q, 88, 207, 125, 86, 53),
        new DataCapacity(ECL.H, 66, 154, 93, 24, 39),
        new DataCapacity(ECL.L, 194, 461, 279, 192, 118), // Version 8
        new DataCapacity(ECL.M, 154, 365, 221, 152, 93),
        new DataCapacity(ECL.Q, 110, 259, 157, 108, 66),
        new DataCapacity(ECL.H, 86, 202, 122, 84, 52),
        new DataCapacity(ECL.L, 232, 552, 335, 230, 141), // Version 9
        new DataCapacity(ECL.M, 182, 432, 262, 180, 111),
        new DataCapacity(ECL.Q, 132, 312, 189, 130, 80),
        new DataCapacity(ECL.H, 100, 235, 143, 98, 60),
        new DataCapacity(ECL.L, 274, 652, 395, 271, 167), // Version 10
        new DataCapacity(ECL.M, 216, 513, 311, 213, 131),
        new DataCapacity(ECL.Q, 154, 364, 221, 151, 93),
        new DataCapacity(ECL.H, 122, 288, 174, 119, 74),
        new DataCapacity(ECL.L, 324, 772, 468, 321, 198), // Version 11
        new DataCapacity(ECL.M, 254, 604, 366, 251, 155),
        new DataCapacity(ECL.Q, 180, 427, 259, 177, 109),
        new DataCapacity(ECL.H, 140, 331, 200, 137, 85),
        new DataCapacity(ECL.L, 370, 883, 535, 367, 226), // Version 12
        new DataCapacity(ECL.M, 290, 691, 419, 287, 177),
        new DataCapacity(ECL.Q, 206, 489, 296, 203, 125),
        new DataCapacity(ECL.H, 158, 374, 227, 155, 96),
        new DataCapacity(ECL.L, 428, 1022, 619, 425, 262), // Version 13
        new DataCapacity(ECL.M, 334, 796, 483, 331, 204),
        new DataCapacity(ECL.Q, 244, 580, 352, 241, 149),
        new DataCapacity(ECL.H, 180, 427, 259, 177, 109),
        new DataCapacity(ECL.L, 461, 1101, 667, 458, 282), // Version 14
        new DataCapacity(ECL.M, 365, 871, 528, 362, 223),
        new DataCapacity(ECL.Q, 261, 621, 376, 258, 159),
        new DataCapacity(ECL.H, 197, 468, 283, 194, 120),
        new DataCapacity(ECL.L, 523, 1250, 758, 520, 320), // Version 15
        new DataCapacity(ECL.M, 415, 991, 600, 412, 254),
        new DataCapacity(ECL.Q, 295, 703, 426, 292, 180),
        new DataCapacity(ECL.H, 223, 530, 321, 220, 136),
        new DataCapacity(ECL.L, 589, 1408, 854, 586, 361), // Version 16
        new DataCapacity(ECL.M, 453, 1082, 656, 450, 277),
        new DataCapacity(ECL.Q, 325, 775, 470, 322, 198),
        new DataCapacity(ECL.H, 253, 602, 365, 250, 154),
        new DataCapacity(ECL.L, 647, 1548, 938, 644, 397), // Version 17
        new DataCapacity(ECL.M, 507, 1212, 734, 504, 310),
        new DataCapacity(ECL.Q, 367, 876, 531, 364, 224),
        new DataCapacity(ECL.H, 283, 674, 408, 280, 173),
        new DataCapacity(ECL.L, 721, 1725, 1046, 718, 442), // Version 18
        new DataCapacity(ECL.M, 563, 1346, 816, 560, 345),
        new DataCapacity(ECL.Q, 397, 948, 574, 394, 243),
        new DataCapacity(ECL.H, 313, 746, 452, 310, 191),
        new DataCapacity(ECL.L, 795, 1903, 1153, 792, 488), // Version 19
        new DataCapacity(ECL.M, 627, 1500, 909, 624, 384),
        new DataCapacity(ECL.Q, 445, 1063, 644, 442, 272),
        new DataCapacity(ECL.H, 341, 813, 493, 338, 208),
        new DataCapacity(ECL.L, 861, 2061, 1249, 858, 528), // Version 20
        new DataCapacity(ECL.M, 669, 1600, 970, 666, 410),
        new DataCapacity(ECL.Q, 485, 1159, 702, 482, 297),
        new DataCapacity(ECL.H, 385, 919, 557, 382, 235),
        new DataCapacity(ECL.L, 932, 2232, 1352, 929, 572), // Version 21
        new DataCapacity(ECL.M, 714, 1708, 1035, 711, 438),
        new DataCapacity(ECL.Q, 512, 1224, 742, 509, 314),
        new DataCapacity(ECL.H, 406, 969, 587, 403, 248),
        new DataCapacity(ECL.L, 1006, 2409, 1460, 1003, 618), // Version 22
        new DataCapacity(ECL.M, 782, 1872, 1134, 779, 480),
        new DataCapacity(ECL.Q, 568, 1358, 823, 565, 348),
        new DataCapacity(ECL.H, 442, 1956, 640, 439, 270),
        new DataCapacity(ECL.L, 1094, 2620, 1588, 1091, 672), // Version 23
        new DataCapacity(ECL.M, 860, 2059, 1248, 857, 528),
        new DataCapacity(ECL.Q, 614, 1468, 890, 611, 376),
        new DataCapacity(ECL.H, 464, 1108, 672, 461, 284),
        new DataCapacity(ECL.L, 1174, 2812, 1704, 1171, 721), // Version 24
        new DataCapacity(ECL.M, 914, 2188, 1326, 911, 561),
        new DataCapacity(ECL.Q, 664, 1588, 963, 661, 407),
        new DataCapacity(ECL.H, 514, 1228, 744, 511, 315),
        new DataCapacity(ECL.L, 1276, 3057, 1853, 1273, 784), // Version 25
        new DataCapacity(ECL.M, 1000, 2395, 1451, 997, 614),
        new DataCapacity(ECL.Q, 718, 1718, 1041, 715, 440),
        new DataCapacity(ECL.H, 538, 1286, 779, 535, 330),
        new DataCapacity(ECL.L, 1370, 3283, 1990, 1367, 842), // Version 26
        new DataCapacity(ECL.M, 1062, 2544, 1542, 1059, 652),
        new DataCapacity(ECL.Q, 754, 1804, 1094, 751, 462),
        new DataCapacity(ECL.H, 596, 1425, 864, 593, 365),
        new DataCapacity(ECL.L, 1468, 3517, 2132, 1465, 902), // Version 27
        new DataCapacity(ECL.M, 1128, 2701, 1637, 1125, 692),
        new DataCapacity(ECL.Q, 808, 1933, 1172, 805, 496),
        new DataCapacity(ECL.H, 628, 1591, 910, 625, 385),
        new DataCapacity(ECL.L, 1531, 3669, 2223, 1528, 940), // Version 28
        new DataCapacity(ECL.M, 1193, 2857, 1732, 1190, 732),
        new DataCapacity(ECL.Q, 871, 2085, 1263, 868, 534),
        new DataCapacity(ECL.H, 661, 1581, 958, 659, 405),
        new DataCapacity(ECL.L, 1631, 3909, 2369, 1628, 1002), // Version 29
        new DataCapacity(ECL.M, 1267, 3035, 1839, 1264, 778),
        new DataCapacity(ECL.Q, 911, 2181, 1322, 908, 559),
        new DataCapacity(ECL.H, 701, 1677, 1016, 698, 430),
        new DataCapacity(ECL.L, 1735, 4158, 2520, 1732, 1066), // Version 30
        new DataCapacity(ECL.M, 1373, 3289, 1994, 1370, 843),
        new DataCapacity(ECL.Q, 985, 2358, 1429, 982, 604),
        new DataCapacity(ECL.H, 745, 1782, 1080, 742, 457),
        new DataCapacity(ECL.L, 1843, 4417, 2677, 1840, 1132), // Version 31
        new DataCapacity(ECL.M, 1455, 3486, 2113, 1452, 894),
        new DataCapacity(ECL.Q, 1033, 2473, 1499, 1030, 634),
        new DataCapacity(ECL.H, 793, 1897, 1150, 790, 486),
        new DataCapacity(ECL.L, 1955, 4686, 2840, 1952, 1201), // Version 32
        new DataCapacity(ECL.M, 1541, 3693, 2238, 1538, 947),
        new DataCapacity(ECL.Q, 1115, 2670, 1618, 1112, 684),
        new DataCapacity(ECL.H, 845, 2022, 1226, 842, 518),
        new DataCapacity(ECL.L, 2071, 4965, 3009, 2068, 1273), // Version 33
        new DataCapacity(ECL.M, 1631, 3909, 2369, 1628, 1002),
        new DataCapacity(ECL.Q, 1171, 2805, 1700, 1168, 719),
        new DataCapacity(ECL.H, 901, 2157, 1307, 898, 553),
        new DataCapacity(ECL.L, 2191, 5253, 3183, 2188, 1347), // Version 34
        new DataCapacity(ECL.M, 1725, 4134, 2506, 1722, 1060),
        new DataCapacity(ECL.Q, 1231, 2949, 1787, 1228, 756),
        new DataCapacity(ECL.H, 961, 2301, 1394, 958, 590),
        new DataCapacity(ECL.L, 2306, 5529, 3351, 2303, 1417), // Version 35
        new DataCapacity(ECL.M, 1812, 4343, 2632, 1809, 1113),
        new DataCapacity(ECL.Q, 1286, 3081, 1867, 1283, 790),
        new DataCapacity(ECL.H, 986, 2361, 1431, 983, 605),
        new DataCapacity(ECL.L, 2434, 5836, 3537, 2431, 1496), // Version 36
        new DataCapacity(ECL.M, 1914, 4588, 2780, 1911, 1176),
        new DataCapacity(ECL.Q, 1354, 3244, 1966, 1351, 832),
        new DataCapacity(ECL.H, 1054, 2524, 1530, 1051, 647),
        new DataCapacity(ECL.L, 2566, 6153, 3729, 2563, 1577), // Version 37
        new DataCapacity(ECL.M, 1992, 4775, 2894, 1989, 1224),
        new DataCapacity(ECL.Q, 1426, 3417, 2071, 1423, 876),
        new DataCapacity(ECL.H, 1096, 2625, 1591, 1093, 673),
        new DataCapacity(ECL.L, 2702, 6479, 3927, 2699, 1661), // Version 38
        new DataCapacity(ECL.M, 2102, 5039, 3054, 2099, 1292),
        new DataCapacity(ECL.Q, 1502, 3599, 2181, 1499, 923),
        new DataCapacity(ECL.H, 1142, 2735, 1658, 1139, 701),
        new DataCapacity(ECL.L, 2812, 6743, 4087, 2809, 1729), // Version 39
        new DataCapacity(ECL.M, 2216, 5313, 3220, 2213, 1362),
        new DataCapacity(ECL.Q, 1582, 3791, 2298, 1579, 972),
        new DataCapacity(ECL.H, 1222, 2927, 1774, 1219, 750),
        new DataCapacity(ECL.L, 2956, 7089, 4296, 2953, 1817), // Version 40
        new DataCapacity(ECL.M, 2334, 5596, 3391, 2331, 1435),
        new DataCapacity(ECL.Q, 1666, 3993, 2420, 1663, 1024),
        new DataCapacity(ECL.H, 1276, 3057, 1852, 1273, 784),
    };

    /**
     * Fits the given payload length to the appropriate QR code version for the
     * given mode and error correction level.
     *
     * @param mode the mode to use for encoding
     * @param ecl the error correction level to use
     * @param length the length of the payload to encode
     * @return the version number of the QR code that can fit the payload, or -1 if no suitable version is found
     */
    public static int fitToVersion(QrCodeGeneratorImpl.Mode mode, ECL ecl, int length) {
        final int eclOffset = ecl.ordinal();
        final int maxVersion = 40;
        for (int i = 0; i < maxVersion; i++) {
            DataCapacity dc = dataCapacityTable[i * 4 + eclOffset];
            final int maxCap = switch (mode) {
                case NUMERIC -> dc.numericCapacity;
                case ALPHANUMERIC -> dc.alphanumericCapcity;
                case BYTE -> dc.byteCapacity;
                case KANJI -> dc.kanjiCapacity;
            };
            if (maxCap >= length) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Returns the data capacity for the given version and error correction level.
     *
     * @param version the version of the QR code
     * @param ecl the error correction level to use
     * @return the data capacity for the given version and error correction level
     */
    public static DataCapacity forVersion(int version, ECL ecl) {
        if (version < 1 || version > 40) {
            throw new IllegalArgumentException("Version must be from 1-40");
        }
        if (ecl == null) {
            throw new IllegalArgumentException("ECL must not be null");
        }
        return dataCapacityTable[(version - 1) * 4 + ecl.ordinal()];
    }

    /**
     * Returns the number of data bits for this data capacity.
     *
     * @return the number of data bits for this data capacity
     */
    public int dataBits() {
        return dataCodewords * 8;
    }
}
