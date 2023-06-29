package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;

import filters.ChainFilter;
import filters.ColorBandFilter;
import filters.ColorReplacementFilter;
import filters.Filter;
import filters.MonochromeFilter;
import filters.PixelGraphicFilter;
import filters.PseudoNegativeImage;
import filters.ThresholdFilter;
import filters.BlurFilter;

/**
 * Die Main-Klasse ist die Hauptklasse des Programms, die die Filter auf Bilder anwendet und die Ergebnisse speichert.
 */
public class Main {
    private static Map<String, Filter> filters = new HashMap<>();

    /**
     * Die Hauptmethode des Programms. Hier wird die Benutzerinteraktion durchgeführt, Filter angewendet und die Ergebnisse gespeichert.
     *
     * @param args Die Kommandozeilenargumente.
     */
    public static void main(String[] args) {
        try {
            BufferedImage image = ImageIO.read(new File("images/test_image.bmp"));
            BufferedImage mask = ImageIO.read(new File("images/mask.bmp"));

            // Hinzufügen der Filter zum HashMap
            filters.put("monochrome", new MonochromeFilter());
            filters.put("colorBand", new ColorBandFilter(ColorBandFilter.ColorBand.ROT));
            filters.put("threshold", new ThresholdFilter(128));
            filters.put("multiThreshold", new ThresholdFilter(64, 128, 192));
            filters.put("colorReplacement", createColorReplacementChainFilter());
            filters.put("multiColorReplacement", createMultiColorReplacementChainFilter());
            filters.put("blurWithoutMask", new BlurFilter(5, false));
            filters.put("blurWithMask", new BlurFilter(10, true));
            filters.put("pixelGraphicWithoutMask", new PixelGraphicFilter(10, false));
            filters.put("pixelGraphicWithMask", new PixelGraphicFilter(15, true));
            filters.put("negativFilter", new PseudoNegativeImage());

            Scanner scanner = new Scanner(System.in);
            System.out.println("Verfügbare Filter:");
            for (String filterName : filters.keySet()) {
                System.out.println(filterName);
            }
            
            boolean validFilter = false;
            while (!validFilter) {
                System.out.println("Geben Sie den Namen des Filters ein, den Sie anwenden möchten ('test' wendet alle Filter an und speichert alle Ergebnisse):");
                String selectedFilter = scanner.nextLine();

                if (selectedFilter.equals("test")) {
                    testFilters(image, mask);
                    validFilter = true;
                } else if (filters.containsKey(selectedFilter)) {
                    Filter filter = filters.get(selectedFilter);
                    BufferedImage result;
                    if (selectedFilter.equals("colorReplacement") || selectedFilter.equals("multiColorReplacement")) {
                        result = applyChainFilter(image, mask, selectedFilter);
                    } else {
                        result = applyFilter(image, mask, filter);
                    }
                    File output = new File(selectedFilter + "_output.bmp");
                    ImageIO.write(result, "bmp", output);
                    System.out.println("Filter erfolgreich angewendet. Ergebnis gespeichert.");
                    validFilter = true;
                } else {
                    System.out.println("Ausgewählter Filter nicht gefunden.");
                }
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Verarbeiten des Bildes: " + e.getMessage());
        }
    }

    /**
     * Wendet alle verfügbaren Filter auf ein Bild an und speichert die Ergebnisse.
     *
     * @param image Das Eingangsbild.
     * @param mask  Die Maske, falls vorhanden.
     */
    public static void testFilters(BufferedImage image, BufferedImage mask) {
        for (Map.Entry<String, Filter> entry : filters.entrySet()) {
            String filterName = entry.getKey();
            Filter filter = entry.getValue();

            BufferedImage result = applyFilter(image, mask, filter);
            File output = new File(filterName + "_output.bmp");
            try {
                ImageIO.write(result, "bmp", output);
                System.out.println("Filter " + filterName + " erfolgreich angewendet. Ergebnis gespeichert.");
            } catch (IOException e) {
                System.out.println("Fehler beim Speichern des Ergebnisses für den Filter " + filterName + ": " + e.getMessage());
            }
        }
    }
    
    /**
     * Wendet einen einzelnen Filter auf ein Bild an.
     *
     * @param image  Das Eingangsbild.
     * @param mask   Die Maske, falls vorhanden.
     * @param filter Der anzuwendende Filter.
     * @return Das bearbeitete Bild.
     */
    public static BufferedImage applyFilter(BufferedImage image, BufferedImage mask, Filter filter) {
        if (filter != null) {
        	 if (mask != null) {
                 return filter.process(image, mask);
             } else {
                 return filter.process(image);
             }
        } else {
            System.out.println("Filter mit dem Namen " + " nicht gefunden.");
            return image;
        }
    }

    /**
     * Wendet eine Filterkette auf ein Bild an.
     *
     * @param image       Das Eingangsbild.
     * @param mask        Die Maske, falls vorhanden.
     * @param filterName  Der Name der Filterkette.
     * @return Das bearbeitete Bild.
     */
    public static BufferedImage applyChainFilter(BufferedImage image, BufferedImage mask, String filterName) {
        // Abrufen der Filterkette anhand des Namens aus dem HashMap
        ChainFilter chainFilter = (ChainFilter) filters.get(filterName);
        if (chainFilter != null) {
            return chainFilter.process(image);
        } else {
            System.out.println("Filterkette mit dem Namen " + filterName + " nicht gefunden.");
            return image;
        }
    }

    /**
     * Erstellt eine Filterkette für die Farbersetzung.
     *
     * @return Die erstellte Filterkette.
     */
    public static ChainFilter createColorReplacementChainFilter() {
        // Erstellen der Filterkette und Hinzufügen der Filter
        ChainFilter chainFilter = new ChainFilter(2);
        chainFilter.add(new ThresholdFilter(128));
        chainFilter.add(new ColorReplacementFilter(0));
        return chainFilter;
    }

    /**
     * Erstellt eine Filterkette für die mehrfache Farbersetzung.
     *
     * @return Die erstellte Filterkette.
     */
    public static ChainFilter createMultiColorReplacementChainFilter() {
        ThresholdFilter multiThresholdFilter = new ThresholdFilter(64, 128, 192);
        ColorReplacementFilter multiColorReplacementFilter = new ColorReplacementFilter(0, 96, 160, 255);

        ChainFilter chainFilter = new ChainFilter(2);
        chainFilter.add(multiThresholdFilter);
        chainFilter.add(multiColorReplacementFilter);

        return chainFilter;
    }
}