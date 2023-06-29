package filters;

public class PseudoNegativeImage extends PixelFilter implements Filter {
	
	@Override
	protected int calculate(int pixelColor, int maskColor) {
        int red = 255 - ((pixelColor >> 16) & 0xFF);
        int green = 255 - ((pixelColor >> 8) & 0xFF);
        int blue = 255 - (pixelColor & 0xFF);

        int invertedRGB = (red << 16) | (green << 8) | blue;
        return invertedRGB;
    }

}