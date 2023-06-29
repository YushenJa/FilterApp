package filters;
import java.awt.image.BufferedImage;

public class MonochromeFilter extends PixelFilter implements Filter {

    /**
     * Wendet den Monochromfilter auf das angegebene Bild an.
     * Der Monochromfilter wandelt jedes Pixel in Schwarz-Weiß um.
     *
     * @param images das Eingangsbild, das gefiltert werden soll
     * @return das gefilterte Bild
     */
    @Override
    public BufferedImage process(BufferedImage... images) {
        BufferedImage image = images[0]; // Das erste Bild in der Liste abrufen

        // Bildbearbeitung
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sourcePixel = image.getRGB(x, y);

                int processedPixel = calculate(sourcePixel, 0); // Die Maske wird in diesem Filter nicht verwendet
                resultImage.setRGB(x, y, processedPixel);
            }
        }

        return resultImage;
    }

    /**
     * Berechnet die Schwarz-Weiß-Farbe eines Pixels basierend auf seinen RGB-Werten.
     *
     * @param pixelColor die Farbe des Pixels
     * @param maskColor  die Farbe der Maske (wird in diesem Filter nicht verwendet)
     * @return die Schwarz-Weiß-Farbe des Pixels
     */
    @Override
    protected int calculate(int pixelColor, int maskColor) {
        // Berechnung der Schwarz-Weiß-Farbe eines Pixels
        int r = (pixelColor >> 16) & 0xFF;
        int g = (pixelColor >> 8) & 0xFF;
        int b = pixelColor & 0xFF;

        int gray = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        return (gray << 16) | (gray << 8) | gray;
    }
}