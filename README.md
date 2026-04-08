# qrgen
Java library for QR code generation

Usage:
```java
import com.analogideas.qrgen.api.ECL;
import com.analogideas.qrgen.api.QrCode;
import com.analogideas.qrgen.api.QrCodeGenerator;

QrCodeGenerator qrCodeGen = QrGenFactory.instance.qrCodeGenerator();
QrCode qrCode = qrCodeGen.generate("Some payload", ECL.M);
```

The QR Code module placements can be read from `QrCode` objects
via the `ReadOnlyBitMatrix` class.
```java
import com.analogideas.qrgen.api.ReadOnlyBitMatrix;

static final String OCCUPIED = "\u2588"; // Unicode "Full Block"
static final String BLANK = " ";
ReadOnlyBitMatrix matrix = qrCode.getMatrix();
int dim = matix.dim();
for (int y = 0 ; y < dim; y++) {
    for (int x = 0; x < dim; x++) {
        IO.print(matrix.get(x,y) ? OCCUPIED : BLANK)
    }
    IO.println();
}
```

Support for writing to a PNG image file is included:
```java
import com.analogideas.qrgen.api.BinaryPngWriter;
var pngWriter = QrGenFactory.instance.pngWriter();
int moduleSize = 4; // use 4x4 pixels per module
pngWriter.write(qrCode.getMatrix(), moduleSize, new File("QRCode.png"));
```

Support for writing as a SVG file is planned.
