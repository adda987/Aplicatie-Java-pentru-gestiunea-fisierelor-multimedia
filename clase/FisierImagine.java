package clase;

import decorator.InterfataImagine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clasa care descrie un fisier imagine (.jpg, .jpeg, .png).
 * Pe langa rezolutie, poate citi matricea de pixeli, folosita pentru aplicarea de efecte prin decoratori (grayscale, sepia, negativ).
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class FisierImagine extends FisierMultimedia implements InterfataImagine {

    private int latime;
    private int lungime;

    public FisierImagine(File fisier) {
        super(fisier, TipFisier.IMAGINE);
        try {
            BufferedImage imagine = ImageIO.read(fisier);
            if (imagine != null) {
                this.latime = imagine.getWidth();
                this.lungime = imagine.getHeight();
            }
        } catch (IOException e) {
            System.out.println("Imaginea " + nume + " nu a putut fi citita.");
        }
    }

    /**
     * Citeste imaginea si returneaza culoarea fiecarui pixel.
     *
     * @return matricea de pixeli [lungime][latime]
     * @throws IOException daca imaginea nu poate fi citita
     */
    @Override
    public int[][] getPixeli() throws IOException {
        BufferedImage imagine = ImageIO.read(new File(cale));
        if (imagine == null)
            throw new IOException("Fisierul nu este o imagine valida: " + cale);

        int[][] pixeli = new int[imagine.getHeight()][imagine.getWidth()];
        for (int i = 0; i < pixeli.length; i++)
            for (int j = 0; j < pixeli[i].length; j++)
                pixeli[i][j] = imagine.getRGB(j, i);
        return pixeli;
    }

    @Override
    public String genereazaDescriere() {
        return "Fisier imagine " + nume + " (" + extensie + "), rezolutie " + latime + "x" + lungime +
                ", " + String.format("%.2f", getDimensiuneKB()) + " KB, in " + cale;
    }
}
