package filters;
import java.awt.image.BufferedImage;

/**
 * Das Filter-Interface definiert die Methode zum Verarbeiten von Bildern.
 */
public interface Filter {
    /**
     * Verarbeitet die gegebenen Bilder und gibt das verarbeitete Bild zurück.
     *
     * @param images Die Bilder, die verarbeitet werden sollen.
     * @return Das verarbeitete Bild.
     */
    BufferedImage process(BufferedImage... images);
}