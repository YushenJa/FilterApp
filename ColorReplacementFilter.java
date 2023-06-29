package filters;


import java.awt.Color;
import java.util.Random;

/**
 * Der ColorReplacementFilter ist ein Filter, der die Farben von Pixeln in einem Bild durch andere Farben ersetzt.
 * Die Ersatzfarben können entweder anhand von Graustufenwerten oder als benutzerdefinierte Farben angegeben werden.
 */
public class ColorReplacementFilter extends PixelFilter implements Filter {
    private Color[] replacementColors;

    /**
     * Konstruktor, der die Ersatzfarben basierend auf den angegebenen Graustufenwerten generiert.
     *
     * @param grayLevels Die Graustufenwerte, für die Ersatzfarben generiert werden sollen.
     */
    public ColorReplacementFilter(int... grayLevels) {
        replacementColors = new Color[256];
        for (int level : grayLevels) {
            replacementColors[level] = generateRandomColor();
        }
    }

    /**
     * Konstruktor, der benutzerdefinierte Ersatzfarben verwendet.
     *
     * @param replacementColors Die benutzerdefinierten Ersatzfarben.
     */
    public ColorReplacementFilter(Color[] replacementColors) {
        this.replacementColors = replacementColors;
    }

    /**
     * Berechnet den Ersatzwert für den gegebenen Pixel- und Maskenfarbwert.
     * Der Ersatzwert wird anhand des Graustufenwerts des Pixels ermittelt und durch die entsprechende Ersatzfarbe ersetzt.
     *
     * @param pixelColor Der Farbwert des Pixels.
     * @param maskColor  Der Farbwert der Maske.
     * @return Der berechnete Ersatzwert für den Pixel.
     */
    protected int calculate(int pixelColor, int maskColor) {
        int grayLevel = calculateGrayLevel(pixelColor);
        Color replacementColor = replacementColors[grayLevel];
        if (replacementColor != null) {
            return replacementColor.getRGB();
        } else {
            return pixelColor;
        }
    }

    /**
     * Berechnet den Graustufenwert eines Farbwerts.
     *
     * @param pixelColor Der Farbwert des Pixels.
     * @return Der Graustufenwert des Farbwerts.
     */
    private int calculateGrayLevel(int pixelColor) {
        int red = (pixelColor >> 16) & 0xFF;
        int green = (pixelColor >> 8) & 0xFF;
        int blue = pixelColor & 0xFF;
        int grayLevel = (red + green + blue) / 3;
        return grayLevel;
    }

    /**
     * Generiert eine zufällige Farbe.
     *
     * @return Die generierte zufällige Farbe.
     */
    private Color generateRandomColor() {
        Random random = new Random();
        int red = random.nextInt(256);
        int green = random.nextInt(256);
        int blue = random.nextInt(256);
        return new Color(red, green, blue);
    }
}