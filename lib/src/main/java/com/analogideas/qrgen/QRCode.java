/*
 * Copyright 2021,2026 Scott W. Palmer
 * All Rights Reserved.
 */
package com.analogideas.qrgen;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.BitSet;

/**
 * The two-dimensional representation of the QR code bit patterns, including
 * finder patterns, alignment and timing patterns and the codeword data.
 *
 * @author scott
 */
public class QRCode {

    private static final Logger LOGGER = System.getLogger(QRCode.class.getName());

    BitMatrix modules;
    BitMatrix mask; // tracks all occupied modules (function patterns + data)
    BitMatrix funcMask; // tracks only function pattern modules (for data masking)
    int dim;
    int version;
    ECL ecl;

    // for filling in data bytes
    boolean vertical = true;
    boolean highX = true;
    int px;
    int py;
    int dy = -1; // starting direction is up

    /**
     * Constructs the QR Code matrix from a symbol version, error correction
     * level, and the codewords.
     *
     * @param version The QRCode version (1-40)
     * @param ecl The error correction level to use
     * @param codewords The codewords to encode
     */
    public QRCode(int version, ECL ecl, BitSet codewords) {
        this.version = version;
        this.ecl = ecl;
        dim = 21 + (version - 1) * 4;
        LOGGER.log(Level.INFO, "Version: {0} ({1} x {1}), ECL: {2}", version, dim, ecl);
        modules = new BitMatrix(dim);
        mask = new BitMatrix(dim);
        px = py = dim - 1; // start in lower right

        addFinderPatterns();
        reserveFormatAndVersionAreas();
        addTimingPatterns();
        addAlignmentPatterns();

        // Snapshot the function-pattern mask before placing codewords
        funcMask = mask.copy();

        addCodewords(codewords);

        // Step 6: apply best data mask
        int maskPattern = selectBestMask();
        applyMask(maskPattern);

        // Step 7: place format and version information
        placeFormatInfo(maskPattern);
        if (version >= 7) {
            placeVersionInfo();
        }
    }

    private void addFinderPatterns() {
        placeFinder(-1, -1); // Adds with a separator on all four sides, so positioned one off the edge in each direction
        placeFinder(dim - 8, -1);
        placeFinder(-1, dim - 8);
    }

    //  .........
    //  .XXXXXXX.
    //  .X.....X.
    //  .X.XXX.X.
    //  .X.XXX.X.
    //  .X.XXX.X.
    //  .X.....X.
    //  .XXXXXXX.
    //  .........
    private static final boolean[] FINDER_PATTERN1 = { false, true, true, true, true, true, true, true, false };
    private static final boolean[] FINDER_PATTERN2 = { false, true, false, false, false, false, false, true, false };
    private static final boolean[] FINDER_PATTERN3 = { false, true, false, true, true, true, false, true, false };

    void placeFinder(int x, int y) {
        finderRow(x, y++);
        finderRow(x, y++, FINDER_PATTERN1);
        finderRow(x, y++, FINDER_PATTERN2);
        finderRow(x, y++, FINDER_PATTERN3);
        finderRow(x, y++, FINDER_PATTERN3);
        finderRow(x, y++, FINDER_PATTERN3);
        finderRow(x, y++, FINDER_PATTERN2);
        finderRow(x, y++, FINDER_PATTERN1);
        finderRow(x, y);
    }

    void finderRow(int x, int y) {
        for (int i = 0; i < FINDER_PATTERN1.length; i++) {
            placeModule(x + i, y, false);
        }
    }

    void finderRow(int x, int y, boolean[] finderPattern) {
        for (int i = 0; i < finderPattern.length; i++) {
            placeModule(x + i, y, finderPattern[i]);
        }
    }

    private void addTimingPatterns() {
        for (int i = 8; i < dim - 8; i++) {
            boolean bit = ((i & 1) == 0);
            placeModule(i, 6, bit);
            placeModule(6, i, bit);
        }
    }

    private void addAlignmentPatterns() {
        if (version == 1) {
            return;
        }
        final int beg = 4;
        final int end = dim - 9;
        if (version < 7) {
            placeAlignment(end, end);
            return;
        }
        final int apdim;
        if (version < 14) {
            apdim = 3; // version 7-13
        } else if (version < 21) {
            apdim = 4; // version 14-20
        } else if (version < 28) {
            apdim = 5; // version 21-27
        } else if (version < 35) {
            apdim = 6; // version 28-34
        } else {
            apdim = 7; // version 35-40
        }
        // Spec says "spaced as evenly as possible with any uneven spacing being accomodated
        // between the timing pattern and the first alignment pattern in the symbol interior.
        // HOWEVER: This does not match the table in Annex E.
        //        Specifically version 16 which could have the alignment patterns
        //        spaced 22 modules apart, but instead spaces 24 apart, unnecessarily forcing
        //        an uneven gap between the timing pattern and the first alignment pattern
        //        (similar issues with version 19,30,36,39)
        int gap = switch (version) {
            case 16 -> 24;
            case 19 -> 28;
            case 30 -> 26;
            case 36 -> 26;
            case 39 -> 28;
            default -> ((end - beg) / (apdim - 1) + 1) & ~1;
        };
        // DEBUG
        //        System.out.println("No.aligment: "+(apdim*apdim - 3));
        //        System.out.print("beg: "+(beg+2));
        //        System.out.print("   end: "+(end+2));
        //        System.out.println("   gap: "+gap);

        /*
        if (version < 7) {
            placeAlignment(end,end);
        } else if (version < 14) {
            // version 7 has 6 alignment patterns
            int mid = dim/2-2;
            placeAlignment(end,end);
            placeAlignment(end,mid);
            placeAlignment(mid,end);
            placeAlignment(mid,mid);
            placeAlignment(mid,beg);
            placeAlignment(beg,mid);
        } else if (version < 21) {
            // version 14 has 13 alignment patterns
            int gap = (end-beg)/3;
            int mid1 = beg + gap;
            int mid2 = mid1 + gap;
            placeAlignment(end,end);
            placeAlignment(end,mid2);
            placeAlignment(end,mid1);
            placeAlignment(mid2,end);
            placeAlignment(mid2,mid2);
            placeAlignment(mid2,mid1);
            placeAlignment(mid2,beg);
            placeAlignment(mid1,end);
            placeAlignment(mid1,mid2);
            placeAlignment(mid1,mid1);
            placeAlignment(mid1,beg);
            placeAlignment(beg,mid2);
            placeAlignment(beg,mid1);

        } else {
        }
        */
        LOGGER.log(
            Level.INFO,
            """
            No.alignment: {0}
            beg: {1}   end: {2}   gap: {3}
            """,
            (apdim * apdim - 3),
            (beg + 2),
            (end + 2),
            gap
        );

        // anchor from the end, and forcing beginning to align, casuing uneven
        // part to happen between timing pattern and first internal alignment pattern
        int apLim = apdim - 1;
        for (int i = 0; i < apdim; i++) {
            int y = (i == apLim) ? beg : (end - i * gap);
            for (int j = 0; j < apdim; j++) {
                int x = (j == apLim) ? beg : (end - j * gap);
                if ((x == beg && (y == beg || y == end)) || (x == end && y == beg)) {
                    continue; // skip 3 corners
                }
                placeAlignment(x, y);
            }
        }
    }

    void placeAlignment(int x, int y) {
        for (int i = 0; i < 5; i++) {
            boolean bb = (i == 0 || i == 4);
            placeModule(x + i, y, true);
            placeModule(x + i, y + 1, bb);
            placeModule(x + i, y + 2, (i & 1) == 0);
            placeModule(x + i, y + 3, bb);
            placeModule(x + i, y + 4, true);
        }
    }

    private void reserveFormatAndVersionAreas() {
        if (version < 7) {
            // mask off the format area so it is not used when filling codewords
            for (int i = 0; i < 9; i++) {
                placeModule(i, 8, false);
                placeModule(8, i, false);

                if (i < 8) {
                    placeModule(dim - 1 - i, 8, false);
                    placeModule(8, dim - 1 - i, false);
                }
            }
        } else {
            for (int i = 0; i < 9; i++) {
                placeModule(i, 8, false);
                placeModule(8, i, false);
                if (i < 8) {
                    placeModule(dim - 1 - i, 8, false);
                    placeModule(8, dim - 1 - i, false);

                    if (i < 7) {
                        placeModule(dim - 9, i, false);
                        placeModule(dim - 10, i, false);
                        placeModule(dim - 11, i, false);
                        placeModule(i, dim - 9, false);
                        placeModule(i, dim - 10, false);
                        placeModule(i, dim - 11, false);
                    }
                }
            }
        }
    }

    /**
     * Converts from a bitstream into byte-size codewords and adds them to the
     * matrix in the available positions.
     * @param codewords
     */
    private void addCodewords(BitSet codewords) {
        int totalBits =
            ECCharacteristics.forVersion(version, ecl).nCW() * 8 + QRCodeGen.capacityTable[version - 1].remainderBits();
        LOGGER.log(Level.INFO, "codewords num bits = {0}", totalBits);
        for (int i = 0; i < totalBits; i += 8) {
            byte val = 0;
            for (int j = 0; j < 8; j++) {
                if (codewords.get(i + j)) {
                    val |= (byte) (0x80 >>> j);
                }
            }
            addDataVert(val);
        }
    }

    // vertical placement of data
    void addDataVert(byte b) {
        // bits are filled from MSB to LSB in pairs
        int bitMask = 0x80;
        while (bitMask != 0) {
            boolean bit = (b & bitMask) != 0;
            placeModule(bit);
            advancePosition();
            bitMask >>>= 1;
        }
    }

    void placeModule(boolean bit) {
        placeModule(px, py, bit);
    }

    // ignores out of bounds positions
    void placeModule(int x, int y, boolean bit) {
        if (x >= 0 && x < dim && y >= 0 && y < dim) {
            modules.set(x, y, bit);
            mask.set(x, y, true);
        }
    }

    void advancePosition() {
        while (px >= 0 && mask.get(px, py)) {
            // if space px,py is used, advance
            if (vertical) {
                if (highX) {
                    px--;
                    highX = false;
                } else {
                    px++;
                    py += dy;
                    if (py < 0 || py >= dim) {
                        // reverse direction, move to next column
                        px -= 2;
                        if (px == 6) px--; // skip timing pattern column
                        dy = -dy;
                        py += dy;
                    }
                    highX = true;
                }
            } else {
                // horizontal
                LOGGER.log(Level.WARNING, "horizontal placement not implemented");
                break;
            }
        }
    }

    /**
     * Returns the matrix of modules for this QR code.
     * @return the matrix of modules for this QR code
     */
    public BitMatrix getMatrix() {
        return modules; // TODO return a read-only copy
    }

    /**
     * Returns the ECL used for this code.
     * @return the ECL used for this code
     */
    public ECL getECL() {
        return ecl;
    }

    // -------------------------------------------------------------------------
    // Step 6: Data Masking  (ISO/IEC 18004 section 7.8)
    // -------------------------------------------------------------------------

    /** Returns true if mask pattern p makes module (x,y) dark. */
    private static boolean maskCondition(int p, int x, int y) {
        return switch (p) {
            case 0 -> (y + x) % 2 == 0;
            case 1 -> y % 2 == 0;
            case 2 -> x % 3 == 0;
            case 3 -> (y + x) % 3 == 0;
            case 4 -> (y / 2 + x / 3) % 2 == 0;
            case 5 -> ((y * x) % 2) + ((y * x) % 3) == 0;
            case 6 -> (((y * x) % 2) + ((y * x) % 3)) % 2 == 0;
            case 7 -> (((y + x) % 2) + ((y * x) % 3)) % 2 == 0;
            default -> false;
        };
    }

    /** Apply mask pattern p to all non-function modules. */
    private void applyMask(int p) {
        for (int y = 0; y < dim; y++) {
            for (int x = 0; x < dim; x++) {
                if (!funcMask.get(x, y) && maskCondition(p, x, y)) {
                    modules.set(x, y, !modules.get(x, y));
                }
            }
        }
    }

    /** Evaluate penalty score for the current module matrix (spec Table 10).
     * @return the penalty score for the current module matrix
     */
    private int penaltyScore() {
        int score = 0;

        // N1: 5+ consecutive same-colour modules in a row or column
        for (int y = 0; y < dim; y++) {
            score += runPenalty(y, true);
            score += runPenalty(y, false);
        }

        // N2: 2x2 blocks of same colour
        for (int y = 0; y < dim - 1; y++) {
            for (int x = 0; x < dim - 1; x++) {
                boolean b = modules.get(x, y);
                if (modules.get(x + 1, y) == b && modules.get(x, y + 1) == b && modules.get(x + 1, y + 1) == b) {
                    score += 3;
                }
            }
        }

        // N3: specific patterns 1:1:3:1:1 (finder-like)
        int[] pat1 = { 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0 };
        int[] pat2 = { 0, 0, 0, 0, 1, 0, 1, 1, 1, 0, 1 };
        for (int y = 0; y < dim; y++) {
            for (int x = 0; x <= dim - 11; x++) {
                if (matchPattern(x, y, pat1, true) || matchPattern(x, y, pat2, true)) score += 40;
                if (matchPattern(y, x, pat1, false) || matchPattern(y, x, pat2, false)) score += 40;
            }
        }

        // N4: proportion of dark modules
        int dark = 0;
        for (int y = 0; y < dim; y++) for (int x = 0; x < dim; x++) if (modules.get(x, y)) dark++;
        int total = dim * dim;
        int pct = (dark * 100) / total;
        int prev5 = (pct / 5) * 5;
        int next5 = prev5 + 5;
        score += (Math.min(Math.abs(prev5 - 50), Math.abs(next5 - 50)) / 5) * 10;

        return score;
    }

    private int runPenalty(int line, boolean horizontal) {
        int score = 0;
        int run = 1;
        boolean prev = horizontal ? modules.get(0, line) : modules.get(line, 0);
        for (int i = 1; i < dim; i++) {
            boolean cur = horizontal ? modules.get(i, line) : modules.get(line, i);
            if (cur == prev) {
                run++;
                if (run == 5) score += 3;
                else if (run > 5) score += 1;
            } else {
                run = 1;
                prev = cur;
            }
        }
        return score;
    }

    private boolean matchPattern(int x, int y, int[] pat, boolean horizontal) {
        for (int i = 0; i < pat.length; i++) {
            boolean b = horizontal ? modules.get(x + i, y) : modules.get(x, y + i);
            if ((pat[i] == 1) != b) return false;
        }
        return true;
    }

    /** Try all 8 masks, return the one with the lowest penalty score.
     *
     * @return the mask with the lowest penalty score
     */
    private int selectBestMask() {
        // Save a snapshot of the codeword modules before masking
        BitMatrix saved = new BitMatrix(dim);
        for (int y = 0; y < dim; y++) for (int x = 0; x < dim; x++) saved.set(x, y, modules.get(x, y));

        int bestMask = 0;
        int bestScore = Integer.MAX_VALUE;
        for (int p = 0; p < 8; p++) {
            applyMask(p);
            int score = penaltyScore();
            if (score < bestScore) {
                bestScore = score;
                bestMask = p;
            }
            // undo mask (XOR is its own inverse)
            applyMask(p);
        }
        return bestMask;
    }

    // -------------------------------------------------------------------------
    // Step 7a: Format Information  (ISO/IEC 18004 section 7.9)
    // -------------------------------------------------------------------------

    // Format info mask pattern (XOR with format bits before placement)
    private static final int FORMAT_MASK = 0b101010000010010;

    // ECL indicator bits: L=01, M=00, Q=11, H=10
    private static final int[] ECL_BITS = { 0b01, 0b00, 0b11, 0b10 };

    /**
     * Compute 15-bit format information word for the given ECL and mask pattern.
     * Bits 14-13: ECL, bits 12-10: mask, bits 9-0: BCH error correction.
     */
    static int formatInfoBits(ECL ecl, int maskPattern) {
        int data = (ECL_BITS[ecl.ordinal()] << 3) | maskPattern;
        // BCH(15,5) with generator polynomial x^10+x^8+x^5+x^4+x^2+x+1 = 0x537
        int rem = data << 10;
        for (int i = 14; i >= 10; i--) {
            if ((rem & (1 << i)) != 0) rem ^= (0x537 << (i - 10));
        }
        return ((data << 10) | rem) ^ FORMAT_MASK;
    }

    /** Place the 15-bit format information in both copies around the finder patterns.
     * @param maskPattern the mask pattern to use
     */
    private void placeFormatInfo(int maskPattern) {
        int bits = formatInfoBits(ecl, maskPattern);

        // Copy 1: around top-left finder pattern (x=col, y=row)
        int[] xPos1 = { 0, 1, 2, 3, 4, 5, 7, 8, 8, 8, 8, 8, 8, 8, 8 };
        int[] yPos1 = { 8, 8, 8, 8, 8, 8, 8, 8, 7, 5, 4, 3, 2, 1, 0 };
        // Copy 2: right side and bottom
        int[] xPos2 = { dim - 1, dim - 2, dim - 3, dim - 4, dim - 5, dim - 6, dim - 7, dim - 8, 8, 8, 8, 8, 8, 8, 8 };
        int[] yPos2 = { 8, 8, 8, 8, 8, 8, 8, 8, dim - 7, dim - 6, dim - 5, dim - 4, dim - 3, dim - 2, dim - 1 };

        for (int i = 0; i < 15; i++) {
            boolean bit = ((bits >> (14 - i)) & 1) == 1;
            modules.set(xPos1[i], yPos1[i], bit);
            modules.set(xPos2[i], yPos2[i], bit);
        }
        // Dark module (always dark) at (8, dim-8)
        modules.set(8, dim - 8, true);
    }

    // -------------------------------------------------------------------------
    // Step 7b: Version Information  (ISO/IEC 18004 section 7.10, versions 7+)
    // -------------------------------------------------------------------------

    /**
     * Compute 18-bit version information word.
     * Bits 17-12: version number, bits 11-0: BCH error correction.
     */
    static int versionInfoBits(int version) {
        int data = version;
        // BCH(18,6) with generator polynomial x^12+x^11+x^10+x^9+x^8+x^5+x^2+1 = 0x1F25
        int rem = data << 12;
        for (int i = 17; i >= 12; i--) {
            if ((rem & (1 << i)) != 0) rem ^= (0x1F25 << (i - 12));
        }
        return (data << 12) | rem;
    }

    /** Place the 18-bit version information in the two 6x3 blocks (versions 7+). */
    private void placeVersionInfo() {
        int bits = versionInfoBits(version);
        // Two 6x3 blocks: top-right (cols dim-11..dim-9, rows 0..5)
        // and bottom-left (rows dim-11..dim-9, cols 0..5)
        for (int i = 0; i < 18; i++) {
            boolean bit = ((bits >> i) & 1) == 1;
            int r = i / 3;
            int c = i % 3;
            // top-right block
            modules.set(dim - 11 + c, r, bit);
            // bottom-left block
            modules.set(r, dim - 11 + c, bit);
        }
    }
}
