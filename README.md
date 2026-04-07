# qrgen
Java library for QR code generation

Usage:
```java
import com.analogideas.qrgen.ECL;
import com.analogideas.qrgen.QRCode;
import com.analogideas.qrgen.QRCodeGen;

QRCode qrCode = QRCodeGen.generate("Some payload", ECL.M);
```

The QR Code module placements can be read from qrCode
via the BitMatrix class.
```java
import com.analogideas.qrgen.BitMatrix;

static final String OCCUPIED = "\u2588"; // Unicode "Full Block"
static final String BLANK = " ";
BitMatrix matrix = qrCode.getMatrix();
int dim = matix.dim();
for (int y = 0 ; y < dim; y++) {
    for (int x = 0; x < dim; x++) {
        IO.print(matrix.get(x,y) ? OCCUPIED : BLANK)
    }
    IO.println();
}
```

Basic support for writing to a PNG image file is included
```java
import com.analogideas.qrgen.png.PngWriter;
int moduleSize = 4; // use 4x4 pixels per module
PngWriter.write(qrCode, moduleSize, new File("QRCode.png"));
```
