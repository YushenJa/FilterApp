package filters;
import java.awt.image.BufferedImage;

/**
 * Die Klasse PixelGraphicFilter erweitert die abstrakte Klasse AreaFilter und implementiert das Filter-Interface.
 * Sie wendet den Filter auf Blöcke von Pixeln an und berechnet den durchschnittlichen Farbwert für jeden Block,
 * basierend auf den Pixelwerten des Blocks und einer optionalen Maske.
 */
public class PixelGraphicFilter extends AreaFilter {

    /**
     * Konstruktor für den PixelGraphicFilter.
     *
     * @param blockSize Die Größe des zu verarbeitenden Bereichs (Blockgröße).
     * @param useMask   Gibt an, ob eine Maske verwendet werden soll.
     */
    public PixelGraphicFilter(int blockSize, boolean useMask) {
        super(blockSize, useMask);
    }

    /**
     * Berechnet den Pixelwert für einen bestimmten Bereich (Block) von Pixeln, unter Berücksichtigung der Maske.
     *
     * @param pixels     Ein Array von Pixelwerten im Bereich (Block).
     * @param maskPixels Ein Array von Maskenpixelwerten im Bereich (Block).
     * @param count      Die Anzahl der Pixel im Bereich (Block).
     * @param blockSize  Die Größe des Bereichs (Blockgröße).
     * @return Der berechnete Pixelwert.
     */
    @Override
    protected int calculatePixel(int[] pixels, int[] maskPixels, int count, int blockSize) {
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;

        for (int i = 0; i < count; i++) {
            int currPixel = pixels[i];
            int maskPixel = maskPixels[i];

            if ((maskPixel & 0x00FFFFFF) > 0x00808080) {
                int red = (currPixel >> 16) & 0xFF;
                int green = (currPixel >> 8) & 0xFF;
                int blue = currPixel & 0xFF;

                sumRed += red;
                sumGreen += green;
                sumBlue += blue;
            }
        }

        int avgRed = sumRed / count;
        int avgGreen = sumGreen / count;
        int avgBlue = sumBlue / count;

        return (avgRed << 16) | (avgGreen << 8) | avgBlue;
    }
    
    /**
     * Berechnet den Pixelwert für einen bestimmten Bereich (Block) von Pixeln ohne Berücksichtigung einer Maske.
     *
     * @param pixels    Ein Array von Pixelwerten im Bereich (Block).
     * @param count     Die Anzahl der Pixel im Bereich (Block).
     * @param blockSize Die Größe des Bereichs (Blockgröße).
     * @return Der berechnete Pixelwert.
     */
    protected int calculatePixel(int[] pixels, int count, int blockSize) {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int pixelCount = 0;

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