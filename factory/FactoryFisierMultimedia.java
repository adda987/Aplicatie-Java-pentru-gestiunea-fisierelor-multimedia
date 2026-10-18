package factory;

import clase.FisierAudio;
import clase.FisierImagine;
import clase.FisierMultimedia;
import clase.TipFisier;

import java.io.File;

/**
 * Design pattern Simple Factory.
 * Creeaza obiectul potrivit (FisierAudio sau FisierImagine)
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class FactoryFisierMultimedia {

    /**
     * Stabileste tipul fisierului pe baza extensiei.
     *
     * @param fisier fisierul de pe disc
     * @return tipul fisierului sau null daca extensia nu este suportata
     */
    public static TipFisier getTip(File fisier) {
        String extensie = FisierMultimedia.extrageExtensie(fisier.getName());
        switch (extensie) {
            case ".mp3":
            case ".wav":
                return TipFisier.AUDIO;
            case ".jpg":
            case ".jpeg":
            case ".png":
                return TipFisier.IMAGINE;
            default:
                return null;
        }
    }

    /**
     * Creeaza fisierul multimedia corespunzator tipului primit.
     *
     * @param fisier fisierul de pe disc
     * @param tip    tipul fisierului
     * @return un obiect FisierAudio sau FisierImagine
     */
    public static FisierMultimedia getFisier(File fisier, TipFisier tip) {
        switch (tip) {
            case AUDIO:
                return new FisierAudio(fisier);
            case IMAGINE:
                return new FisierImagine(fisier);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
