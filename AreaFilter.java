package filters;

import java.awt.image.BufferedImage;

/**
 * Die abstrakte Klasse AreaFilter implementiert das Filter-Interface und bietet eine gemeinsame Basis für Filter, die auf
 * Bereichen (Blöcken) von Pixeln angewendet werden.
 */
public abstract class AreaFilter implements Filter {
    protected int blockSize;
    protected boolean useMask;

    /**
     * Konstruktor für den AreaFilter.
     *
     * @param blockSize Die Größe des zu verarbeitenden Bereichs (Blockgröße).
     * @param useMask   Gibt an, ob eine Maske verwendet werden soll.
     */
    public AreaFilter(int blockSize, boolean useMask) {
        this.blockSize = blockSize;
        this.useMask = useMask;
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
    protected abstract int calculatePixel(int[] pixels, int[] maskPixels, int count, int blockSize);

    /**
     * Berechnet den Pixelwert für einen bestimmten Bereich (Block) von Pixeln ohne Berücksichtigung einer Maske.
     *
     * @param pixels    Ein Array von Pixelwerten im Bereich (Block).
     * @param count     Die Anzahl der Pixel im Bereich (Block).
     * @param blockSize Die Größe des Bereichs (Blockgröße).
     * @return Der berechnete Pixelwert.
     */
    protected abstract int calculatePixel(int[] pixels, int count, int blockSize);

    /**
     * Verarbeitet die gegebenen Bilder, indem der Filter auf Bereiche (Blöcke) von Pixeln angewendet wird.
     *
     * @param images Die Bilder, die verarbeitet werden sollen.
     * @return Das verarbeitete Bild.
     */
    public BufferedImage process(BufferedImage... images) {
        BufferedImage sourceImage = images[0];
        BufferedImage mask = images[1];
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        int blockCountX = width / blockSize;
        int blockCountY = height / blockSize;
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Verarbeite das Bild, ohne die Maske zu berücksichtigen
        for (int blockY = 0; blockY < blockCountY; blockY++) {
            for (int blockX = 0; blockX < blockCountX; blockX++) {
                int startX = blockX * blockSize;
                int startY = blockY * blockSize;
                int endX = startX + blockSize;
                int endY = startY + blockSize;

                int[] pixels = new int[blockSize * blockSize];
                int[] maskPixels = new int[blockSize * blockSize];
                int index = 0;

                if (!useMask) {
                    // Sammle Pixel ohne Maske
                    for (int y = startY; y < endY; y++) {
                        for (int x = startX; x < endX; x++) {
                            pixels[index] = sourceImage.getRGB(x, y);
                            index++;
                        }
                    }

                    if (index > 0) {
                        int blockColor = calculatePixel(pixels, index, blockSize);

                        for (int y = startY; y < endY; y++) {
                            for (int x = startX; x < endX; x++) {
                                resultImage.setRGB(x, y, blockColor);
                            }
                        }
                    }
                } else if (useMask) {
                    for (int y = startY; y < endY; y++) {
                        for (int x = startX; x < endX; x++) {
                            int maskRGB = mask.getRGB(x, y);
                            if ((maskRGB & 0x00FFFFFF) != 0x00000000) {
                                pixels[index] = sourceImage.getRGB(x, y);
                                maskPixels[index] = maskRGB;
                                index++;
                            }
                        }
                    }

                    if (index > 0) {
                        int blockColor = calculatePixel(pixels, maskPixels, index, blockSize);

                        for (int y = startY; y < endY; y++) {
                            for (int x = startX; x < endX; x++) {
                                int maskRGB = mask.getRGB(x, y);
                                int original = sourceImage.getRGB(x, y);
                                if ((maskRGB & 0x00FFFFFF) == 0x00000000) {
                                    resultImage.setRGB(x, y, original);
                                } else {
                                    resultImage.setRGB(x, y, blockColor);
                                }
                            }
                        }
                    } else {
                        for (int y = startY; y < endY; y++) {
                            for (int x = startX; x < endX; x++) {
                                int original = sourceImage.getRGB(x, y);
                                resultImage.setRGB(x, y, original);
                            }
                        }
                    }
                }
            }
        }
        return resultImage;
    }
}