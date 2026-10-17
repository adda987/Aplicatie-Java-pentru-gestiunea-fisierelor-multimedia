package clase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Clasa care descrie un fisier imagine (.jpg, .png).
 * Pe langa dimensiuni, poate incarca in memorie matricea de pixeli,
 * folosita ulterior pentru aplicarea de efecte (grayscale, sepia, negativ).
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class FisierImagine extends FisierMultimedia {

    private int latime;
    private int lungime;
    private int[][] matricePixeli;

    public FisierImagine(String nume, String extensie, long dimensiune, String cale, int latime, int lungime) {
        super(nume, extensie, dimensiune, cale, TipFisier.IMAGINE);
        this.latime = latime;
        this.lungime = lungime;
    }

    public FisierImagine(File fisier) {
        super(fisier, TipFisier.IMAGINE);
    }

    public int getLatime() {
        return latime;
    }

    public int getLungime() {
        return lungime;
    }

    public int[][] getMatricePixeli() {
        return matricePixeli;
    }

    public void setMatricePixeli(int[][] matricePixeli) {
        this.matricePixeli = matricePixeli;
    }

    /**
     * Citeste imaginea de pe disc si incarca fiecare pixel in matricePixeli.
     * Fiecare element retine culoarea pixelului (alpha, rosu, verde, albastru).
     *
     * @throws IOException daca fisierul nu poate fi citit sau nu este o imagine valida
     */
    public void incarcaPixeli() throws IOException {
        BufferedImage imagine = ImageIO.read(new File(getCale()));
        if (imagine == null)
            throw new IOException("Fisierul nu este o imagine valida: " + getCale());

        latime = imagine.getWidth();
        lungime = imagine.getHeight();
        matricePixeli = new int[lungime][latime];

        for (int i = 0; i < lungime; i++)
            for (int j = 0; j < latime; j++)
                matricePixeli[i][j] = imagine.getRGB(j, i);
    }

    @Override
    public String genereazaDescriere() {
        String rezolutie = "necunoscuta";
        if (latime > 0 && lungime > 0)
            rezolutie = latime + "x" + lungime;
        return "Fisier imagine " + getNume() + " (" + getExtensie() + "), rezolutie " + rezolutie +
                ", " + String.format("%.2f", getDimensiuneKB()) + " KB, in " + getCale();
    }

    @Override
    public String toString() {
        return "FisierImagine{" +
                "latime=" + latime +
                ", lungime=" + lungime +
                "} " + super.toString();
    }
}
