package com.analogideas.qrgen.cmd;

import com.analogideas.qrgen.api.BinaryPngWriter;
import com.analogideas.qrgen.api.ECL;
import com.analogideas.qrgen.api.QrCode;
import com.analogideas.qrgen.api.QrCodeGenerator;
import com.analogideas.qrgen.api.QrGenFactory;
import com.analogideas.qrgen.api.ReadOnlyBitMatrix;
import java.io.File;
import java.io.IOException;

/**
 * Generates a QR code from the given payload and displays it in the console.
 */
public class GenerateQRCode {

    /**
     * Entry point for the QR code generation application.
     *
     * @param args the command line arguments (optional: payload string)
     * @throws IOException if an I/O error occurs while writing the QR code image
     */
    public static void main(String[] args) throws IOException {
        boolean inverted = false;
        int pixelSize = 4;
        ECL ecl = ECL.M;
        String payload = "HTTP://GOOGLE.COM/";
        String outputFile = null;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-p":
                case "--payload":
                    if (i + 1 < args.length) {
                        payload = args[i + 1];
                        i++;
                    }
                    break;
                case "-o":
                case "--output":
                    if (i + 1 < args.length) {
                        outputFile = args[i + 1];
                        i++;
                    }
                    break;
                case "-s":
                case "--size":
                    if (i + 1 < args.length) {
                        pixelSize = Math.max(1, Integer.parseInt(args[i + 1]));
                        i++;
                    }
                    break;
                case "-e":
                case "--ecl":
                    if (i + 1 < args.length) {
                        try {
                            ecl = ECL.valueOf(args[i + 1].toUpperCase());
                            i++;
                        } catch (IllegalArgumentException e) {
                            System.err.printf(
                                """
                                Invalid error correction level: %s
                                Must be one of L, M, Q, or H\n""",
                                args[i + 1]
                            );
                            System.exit(1);
                        }
                    }
                    break;
                case "-i":
                case "--invert":
                    inverted = true;
                    break;
            }
        }
        var factory = QrGenFactory.instance;
        var qrCodeGen = factory.qrCodeGenerator();
        QrCode qr = qrCodeGen.generate(payload, ecl);
        if (outputFile != null) {
            var extension = outputFile.substring(outputFile.lastIndexOf('.') + 1).toLowerCase();
            if (extension.equals("png")) {
                var pngWriter = factory.pngWriter();
                pngWriter.write(qr.getMatrix(), pixelSize, new File(outputFile));
            } else if (extension.equals("svg")) {
                var svgWriter = factory.svgWriter();
                svgWriter.write(qr.getMatrix(), new File(outputFile));
            } else {
                System.err.println("Unsupported output format: " + extension);
                System.exit(1);
            }
        } else {
            print(qr.getMatrix(), inverted);
        }
    }

    static void print(ReadOnlyBitMatrix m, boolean invert) {
        // assuming a dark background, so zero is '█' and one is ' '
        String zero = invert ? " " : "█";
        String one = invert ? "█" : " ";
        int d = m.dim();
        String border = zero.repeat(d + 4);
        System.out.println(border);
        System.out.println(border);
        for (int y = 0; y < d; y++) {
            StringBuilder sb = new StringBuilder().append(zero).append(zero);
            for (int x = 0; x < d; x++) sb.append(m.get(x, y) ? one : zero);
            sb.append(zero).append(zero);
            System.out.println(sb);
        }
        System.out.println(border);
        System.out.println(border);
    }
}
