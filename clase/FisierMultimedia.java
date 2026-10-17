package clase;

import java.io.File;
import java.util.Objects;

/**
 * Clasa abstracta care descrie un fisier multimedia gasit
 * Retine numele, extensia, dimensiunea, calea si tipul fisierului.
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public abstract class FisierMultimedia implements Comparable<FisierMultimedia> {

    public static final String[] EXTENSII_AUDIO = {".mp3", ".wav"};
    public static final String[] EXTENSII_IMAGINE = {".jpg", ".png"};

    private String nume;
    private String extensie;
    private long dimensiune;
    private String cale;
    private TipFisier tip;

    public FisierMultimedia(String nume, String extensie, long dimensiune, String cale, TipFisier tip) {
        this.nume = nume;
        this.extensie = extensie;
        this.dimensiune = dimensiune;
        this.cale = cale;
        this.tip = tip;
    }

    public FisierMultimedia(File fisier, TipFisier tip) {
        this(fisier.getName(), extrageExtensie(fisier.getName()),
                fisier.length(), fisier.getAbsolutePath(), tip);
    }

    public String getNume() {
        return nume;
    }

    public String getExtensie() {
        return extensie;
    }

    public long getDimensiune() {
        return dimensiune;
    }

    public double getDimensiuneKB() {
        return dimensiune / 1024.0;
    }

    public String getCale() {
        return cale;
    }

    public TipFisier getTip() {
        return tip;
    }


    public abstract String genereazaDescriere();


    public static String extrageExtensie(String numeFisier) {
        int poz = numeFisier.lastIndexOf('.');
        if (poz <= 0)
            return "";
        return numeFisier.substring(poz).toLowerCase();
    }

    /**
     * Verifica daca extensia primita este una audio suportata.
     *
     * @param extensie extensia de verificat
     * @return true daca se afla in vectorul EXTENSII_AUDIO
     */
    public static boolean esteAudio(String extensie) {
        for (int i = 0; i < EXTENSII_AUDIO.length; i++)
            if (EXTENSII_AUDIO[i].equalsIgnoreCase(extensie))
                return true;
        return false;
    }

    /**
     * Verifica daca extensia primita este una de imagine suportata.
     *
     * @param extensie extensia de verificat
     * @return true daca se afla in vectorul EXTENSII_IMAGINE
     */
    public static boolean esteImagine(String extensie) {
        for (int i = 0; i < EXTENSII_IMAGINE.length; i++)
            if (EXTENSII_IMAGINE[i].equalsIgnoreCase(extensie))
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "FisierMultimedia{" +
                "nume='" + nume + '\'' +
                ", extensie='" + extensie + '\'' +
                ", dimensiune=" + String.format("%.2f", getDimensiuneKB()) + " KB" +
                ", tip=" + tip +
                ", cale='" + cale + '\'' +
                '}';
    }

    @Override
    public int compareTo(FisierMultimedia f) {
        return this.nume.compareToIgnoreCase(f.nume);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FisierMultimedia that = (FisierMultimedia) o;
        return cale.equals(that.cale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cale);
    }
}
