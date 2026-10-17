package decorator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Design pattern Decorator - clasa abstracta de baza.
 * Primeste o imagine si ii adauga un efect
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public abstract class DecoratorImagine implements InterfataImagine {

    protected InterfataImagine imagine;

    public DecoratorImagine(InterfataImagine imagine) {
        this.imagine = imagine;
    }

    @Override
    public String getNume() {
        return imagine.getNume();
    }

    @Override
    public String genereazaDescriere() {
        return imagine.genereazaDescriere();
    }

    /**
     * Returneaza o matrice noua de pixeli, cu efectul aplicat pe fiecare pixel.
     * Matricea imaginii decorate ramane nemodificata.
     */
    @Override
    public int[][] getPixeli() throws IOException {
        int[][] original = imagine.getPixeli();
        int[][] rezultat = new int[original.length][];

        for (int i = 0; i < original.length; i++) {
            rezultat[i] = new int[original[i].length];
            for (int j = 0; j < original[i].length; j++)
                rezultat[i][j] = aplicaEfect(original[i][j]);
        }
        return rezultat;
    }


    protected abstract int aplicaEfect(int pixel);

    /**
     * Salveaza imaginea cu efectele aplicate intr-un fisier nou (.png sau .jpg).
     *
     * @param destinatie fisierul in care se salveaza
     * @throws IOException daca imaginea nu poate fi scrisa
     */
    public void salveaza(File destinatie) throws IOException {
        int[][] pixeli = getPixeli();
        int lungime = pixeli.length;
        int latime = pixeli[0].length;

        String format = destinatie.getName().toLowerCase().endsWith(".png") ? "png" : "jpg";
        int tipImagine = format.equals("png") ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage rezultat = new BufferedImage(latime, lungime, tipImagine);

        for (int i = 0; i < lungime; i++)
            for (int j = 0; j < latime; j++)
                rezultat.setRGB(j, i, pixeli[i][j]);

        if (!ImageIO.write(rezultat, format, destinatie))
            throw new IOException("Imaginea nu a putut fi salvata in formatul " + format);
    }

    // metode ajutatoare pentru componentele unui pixel

    protected static int alpha(int pixel) {
        return (pixel >> 24) & 0xFF;
    }

    protected static int rosu(int pixel) {
        return (pixel >> 16) & 0xFF;
    }

    protected static int verde(int pixel) {
        return (pixel >> 8) & 0xFF;
    }

    protected static int albastru(int pixel) {
        return pixel & 0xFF;
    }

    protected static int construiestePixel(int alpha, int rosu, int verde, int albastru) {
        return (alpha << 24) | (limiteaza(rosu) << 16) | (limiteaza(verde) << 8) | limiteaza(albastru);
    }

    private static int limiteaza(int valoare) {
        return Math.max(0, Math.min(255, valoare));
    }
}
