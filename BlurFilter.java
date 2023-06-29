package filters;
import java.awt.image.BufferedImage;

/**
 * Ein Filter, der einen Unschärfeeffekt auf ein Bild anwendet.
 * Dieser Filter berechnet die Durchschnittsfarbe jedes Pixels und seiner benachbarten Pixel, um einen unscharfen Effekt zu erzeugen.
 * Die Unschärfe kann auf das gesamte Bild oder selektiv unter Verwendung einer Maske angewendet werden.
 */
public class BlurFilter extends AreaFilter {

    /**
     * Konstruiert einen neuen `BlurFilter` mit der angegebenen Blockgröße und der Verwendung der Maske.
     *
     * @param blockSize die Größe der Blöcke, die zur Berechnung der Durchschnittsfarbe verwendet werden
     * @param useMask   true, wenn der Filter eine Maske verwenden soll, andernfalls false
     */
    public BlurFilter(int blockSize, boolean useMask) {
        super(blockSize, useMask);
    }

    /**
     * Berechnet die neue Pixel-Farbe basierend auf der Durchschnittsfarbe der Pixel und der Maske.
     * Diese Methode wird für jeden Block von Pixeln im Bild aufgerufen.
     *
     * @param pixels     das Array der Pixel im Block
     * @param maskPixels das Array der Maskenpixel im Block
     * @param count      die Anzahl der gültigen Pixel im Block
     * @param blockSize  die Größe des Blocks
     * @return die neue Pixel-Farbe
     */
    @Override
    protected int calculatePixel(int[] pixels, int[] maskPixels, int count, int blockSize) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int pixelCount = 0;

        for (int i = 0; i < count; i++) {
            int currPixel = pixels[i];
            int maskPixel = maskPixels[i];

            if ((maskPixel & 0x00FFFFFF) != 0x00000000) {
                int red = (currPixel >> 16) & 0xFF;
                int green = (currPixel >> 8) & 0xFF;
                int blue = currPixel & 0xFF;

                redSum += red;
                greenSum += green;
                blueSum += blue;
                pixelCount++;
            }
        }

        if (pixelCount == 0) {
            return 0;  // Schwarze Farbe, wenn keine gültigen Pixel gefunden wurden
        }

        int averagedRed = redSum / pixelCount;
        int averagedGreen = greenSum / pixelCount;
        int averagedBlue = blueSum / pixelCount;

        return (averagedRed << 16) | (averagedGreen << 8) | averagedBlue;
    }

    /**
     * Berechnet die neue Pixel-Farbe basierend auf der Durchschnittsfarbe der Pixel im Block.
     * Diese Methode wird für jeden Block von Pixeln im Bild aufgerufen, wenn keine Maske verwendet wird.
     *
     * @param pixels     das Array der Pixel im Block
     * @param count      die Anzahl der gültigen Pixel im Block
     * @param blockSize  die Größe des Blocks
     * @return die neue Pixel-Farbe
     */
    @Override
    protected int calculatePixel(int[] pixels, int count, int blockSize) {
        int pixelCount = 0;
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;

        for (int i = 0; i < count; i++) {
            int currPixel = pixels[i];
            int red = (currPixel >> 16) & 0xFF;
            int green = (currPixel >> 8) & 0xFF;
            int blue = currPixel & 0xFF;

            redSum += red;
            greenSum += green;
            blueSum += blue;
            pixelCount++;
        }

        int averagedRed = redSum / pixelCount;
        int averagedGreen = greenSum / pixelCount;
        int averagedBlue = blueSum / pixelCount;

        return (averagedRed << 16) | (averagedGreen << 8) | averagedBlue;
    }
}