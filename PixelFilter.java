package filters;


import java.awt.image.BufferedImage;

/**
 * The abstract class for pixel filters.
 * This class provides a common framework for implementing pixel-based image filters.
 * Subclasses must implement the abstract method `calculate` to define the filter's specific logic.
 */
public abstract class PixelFilter {

    /**
     * Applies the filter to the given images.
     *
     * @param images the input images to be filtered
     * @return the filtered image
     */
    public BufferedImage process(BufferedImage... images) {
        BufferedImage sourceImage = images[0];
        BufferedImage maskImage = images.length > 1 ? images[1] : null;
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int sourcePixel = sourceImage.getRGB(x, y);
                int maskPixel = (maskImage != null) ? maskImage.getRGB(x, y) : 0;
                int processedPixel = calculate(sourcePixel, maskPixel);
                resultImage.setRGB(x, y, processedPixel);
            }
        }

        return resultImage;
    }

    /**
     * Calculates the new pixel color based on the source pixel color and mask pixel color (if available).
     * Subclasses must implement this method to define the specific filter logic.
     *
     * @param pixelColor the color of the source pixel
     * @param maskColor  the color of the mask pixel, or 0 if no mask is used
     * @return the new pixel color
     */
    protected abstract int calculate(int pixelColor, int maskColor);
}