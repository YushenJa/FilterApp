package filters;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ChainFilter implements Filter {
	private List<PixelFilter> filters;

    /**
     * Konstruiert einen ChainFilter mit der angegebenen maximalen Anzahl von Filtern.
     *
     * @param maxFilters die maximale Anzahl von Filtern, die zur Kette hinzugefügt werden können
     */
    public ChainFilter(int maxFilters) {
    	filters = new ArrayList<>();
    }

    /**
     * Fügt einen PixelFilter zur Kette hinzu.
     *
     * @param filter der hinzuzufügende Filter
     */
    public void add(PixelFilter filter) {
    	filters.add(filter);
    }

    /**
     * Wendet die Kette von Filtern auf die Eingangsbilder an.
     *
     * @param images die zu verarbeitenden Eingangsbilder
     * @return das resultierende verarbeitete Bild
     */
    public BufferedImage process(BufferedImage... images) {
    	if (images.length == 0) {
            throw new IllegalArgumentException("No input images provided.");
        }

        BufferedImage result = images[0];
        for (PixelFilter filter : filters) {
            result = filter.process(result);
        }
        return result;
    }
}