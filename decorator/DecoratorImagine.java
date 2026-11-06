package decorator;

import java.awt.Color;
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
        return imagine.genereazaDescriere() + " + efect " + getNumeEfect();
    }

    /**
     * Ia pixelii imaginii decorate si aplica efectul pe fiecare pixel.
     */
    @Override
    public int[][] getPixeli() throws IOException {
        int[][] pixeli = imagine.getPixeli();
        for (int i = 0; i < pixeli.length; i++)
            for (int j = 0; j < pixeli[i].length; j++) {
                Color culoare = new Color(pixeli[i][j]);
                pixeli[i][j] = aplicaEfect(culoare).getRGB();
            }
        return pixeli;
    }

    /**
     * @return numele efectului, folosit si la denumirea fisierului salvat (ex: "sepia")
     */
    public abstract String getNumeEfect();

    /**
     * Fiecare decorator concret spune cum se transforma culoarea unui pixel.
     *
     * @param culoare culoarea initiala a pixelului
     * @return culoarea noua a pixelului
     */
    protected abstract Color aplicaEfect(Color culoare);

    /**
     * Salveaza imaginea cu efectul aplicat in directorul primit, ca fisier .png.
     *
     * @param director directorul in care se salveaza imaginea
     * @return fisierul creat
     * @throws IOException daca imaginea nu poate fi citita sau scrisa
     */
    public File salveaza(String director) throws IOException {

        String nume = getNume();
        int poz = nume.lastIndexOf('.');
        if (poz > 0)
            nume = nume.substring(0, poz);
        String numeNou = nume + "_" + getNumeEfect();

        File destinatie = new File(director, numeNou + ".png");
        int nr = 1;
        while (destinatie.exists()) {
            destinatie = new File(director, numeNou + nr + ".png");
            nr++;
        }

        int[][] pixeli = getPixeli();
        BufferedImage rezultat = new BufferedImage(pixeli[0].length, pixeli.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < pixeli.length; i++)
            for (int j = 0; j < pixeli[i].length; j++)
                rezultat.setRGB(j, i, pixeli[i][j]);
        ImageIO.write(rezultat, "png", destinatie);
        return destinatie;
    }
}
