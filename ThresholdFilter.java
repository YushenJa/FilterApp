package filters;

/**
 * Der ThresholdFilter ist ein Filter, der die Graustufenwerte von Pixeln in ein Bild basierend auf Schwellenwerten ändert.
 */
public class ThresholdFilter extends PixelFilter implements Filter {
    private int[] thresholds;

    /**
     * Konstruktor, der die Schwellenwerte für den Filter festlegt.
     *
     * @param thresholds Die Schwellenwerte, die zur Berechnung der Graustufen verwendet werden.
     */
    public ThresholdFilter(int... thresholds) {
        this.thresholds = thresholds;
    }

    /**
     * Berechnet den neuen Farbwert für den gegebenen Pixel- und Maskenfarbwert basierend auf den Schwellenwerten.
     *
     * @param pixelColor Der Farbwert des Pixels.
     * @param maskColor  Der Farbwert der Maske.
     * @return Der berechnete Farbwert für den Pixel.
     */
    @Override
    protected int calculate(int pixelColor, int maskColor) {
        int grayLevel = (pixelColor >> 16) & 0xFF;

        // Berechnung der Graustufen basierend auf den übergebenen Schwellenwerten
        int[] grayLevels = new int[thresholds.length + 1];
        grayLevels[0] = 0;
        grayLevels[grayLevels.length - 1] = 255;

        for (int i = 1; i < grayLevels.length - 1; i++) {
            grayLevels[i] = (thresholds[i - 1] + thresholds[i]) / 2;
        }

        // Suche des nächstgelegenen Graustufenwerts basierend auf den berechneten Werten
        int closestGrayLevel = grayLevels[0];

        for (int level : grayLevels) {
            if (Math.abs(grayLevel - level) < Math.abs(grayLevel - closestGrayLevel)) {
                closestGrayLevel = level;
            }
        }

        return (0xFF << 24) | (closestGrayLevel << 16) | (closestGrayLevel << 8) | closestGrayLevel;
    }
}