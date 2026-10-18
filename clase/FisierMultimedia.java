package clase;

import java.io.File;

/**
 * Clasa abstracta care descrie un fisier multimedia gasit
 * Retine numele, extensia, dimensiunea, calea si tipul fisierului.
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public abstract class FisierMultimedia implements Comparable<FisierMultimedia> {

    protected String nume;
    protected String extensie;
    protected long dimensiune;
    protected String cale;
    protected TipFisier tip;

    public FisierMultimedia(File fisier, TipFisier tip) {
        this.nume = fisier.getName();
        this.extensie = extrageExtensie(fisier.getName());
        this.dimensiune = fisier.length();
        this.cale = fisier.getAbsolutePath();
        this.tip = tip;
    }

    public String getNume() {
        return nume;
    }

    public String getExtensie() {
        return extensie;
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

    /**
     * Extrage extensia dintr-un nume de fisier
     *
     * @param numeFisier numele fisierului
     * @return extensia sau "" daca fisierul nu are extensie
     */
    public static String extrageExtensie(String numeFisier) {
        int poz = numeFisier.lastIndexOf('.');
        if (poz <= 0)
            return "";
        return numeFisier.substring(poz).toLowerCase();
    }

    @Override
    public String toString() {
        return genereazaDescriere();
    }

    @Override
    public int compareTo(FisierMultimedia f) {
        return this.nume.compareToIgnoreCase(f.nume);
    }

}
