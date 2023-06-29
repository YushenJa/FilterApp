package filters;

import java.awt.image.BufferedImage;

/**
 * Ein Filter, der einen einzelnen Farbkanal aus einem Bild extrahiert.
 */
public class ColorBandFilter extends PixelFilter implements Filter {

    /**
     * Die verfügbaren Farbkanäle.
     */
    public enum ColorBand {
        ROT, GRÜN, BLAU
    }

    private ColorBand colorBand;

    /**
     * Erstellt einen neuen ColorBandFilter mit dem angegebenen Farbkanal.
     *
     * @param colorBand der Farbkanal
     */
    public ColorBandFilter(ColorBand colorBand) {
        this.colorBand = colorBand;
    }

    /**
     * Wendet den Farbbandfilter auf das angegebene Bild an.
     *
     * @param images die Eingangsbilder, die gefiltert werden sollen
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
     * Berechnet den neuen Farbwert basierend auf dem ausgewählten Farbkanal.
     *
     * @param pixelColor der Farbwert des Pixels
     * @param maskColor  der Farbwert der Maske (nicht verwendet)
     * @return der neue Farbwert
     */
    @Override
    protected int calculate(int pixelColor, int maskColor) {
        // Extrahieren des ausgewählten Farbkanals
        int r = (pixelColor >> 16) & 0xFF;
        int g = (pixelColor >> 8) & 0xFF;
        int b = pixelColor & 0xFF;

        int processedColor = 0;

        switch (colorBand) {
            case ROT:
                processedColor = (r << 16) | (0 << 8) | 0;
                break;
            case GRÜN:
                processedColor = (0 << 16) | (g << 8) | 0;
                break;
            case BLAU:
                processedColor = (0 << 16) | (0 << 8) | b;
                break;
        }

        return processedColor;
    }
}