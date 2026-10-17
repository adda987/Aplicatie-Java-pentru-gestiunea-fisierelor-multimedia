package factory;

import clase.FisierAudio;
import clase.FisierImagine;
import clase.FisierMultimedia;
import clase.TipFisier;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.UnsupportedAudioFileException;

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
     * @param extensie extensia fisierului, ex: ".mp3"
     * @return tipul fisierului
     * @throws UnsupportedOperationException daca extensia nu este suportata
     */
    public static TipFisier getTip(String extensie) {
        if (FisierMultimedia.esteAudio(extensie))
            return TipFisier.AUDIO;
        if (FisierMultimedia.esteImagine(extensie))
            return TipFisier.IMAGINE;
        throw new UnsupportedOperationException("Extensie nesuportata: " + extensie);
    }

    /**
     * Creeaza fisierul multimedia corespunzator unui fisier de pe disc.
     *
     * @param fisier fisierul de pe disc
     * @return un obiect FisierAudio sau FisierImagine
     * @throws UnsupportedOperationException daca extensia nu este suportata
     */
    public static FisierMultimedia getFisier(File fisier) {
        TipFisier tip = getTip(FisierMultimedia.extrageExtensie(fisier.getName()));

        switch (tip) {
            case AUDIO:
                FisierAudio audio = new FisierAudio(fisier);
                try {
                    audio.citesteDurata();
                } catch (UnsupportedAudioFileException | IOException e) {
                    throw new RuntimeException("Eroare la citirea duratei audio: " + e.getMessage(), e);
                }
                return audio;
            case IMAGINE:
                return new FisierImagine(fisier);
            default:
                throw new UnsupportedOperationException("Tip de fișier nesuportat: " + tip);
        }
    }
}
