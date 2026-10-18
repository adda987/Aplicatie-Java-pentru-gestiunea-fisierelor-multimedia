package clase;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clasa care descrie un fisier audio (.mp3, .wav).
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class FisierAudio extends FisierMultimedia {

    private int durataSecunde;

    public FisierAudio(File fisier) {
        super(fisier, TipFisier.AUDIO);
        this.durataSecunde = citesteDurata(fisier);
    }

    /**
     * Calculeaza durata fisierului audio in secunde.
     *
     * @param fisier fisierul audio de pe disc
     * @return durata in secunde
     */
    private int citesteDurata(File fisier) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(fisier);
            AudioFormat format = ais.getFormat();
            int durata = (int) (ais.getFrameLength() / format.getFrameRate());
            ais.close();
            return durata;
        } catch (UnsupportedAudioFileException | IOException e) {
            System.out.println("Eroare la citirea duratei fisierului audio.");
            return 0;
        }
    }

    @Override
    public String genereazaDescriere() {
        String durata = String.format("%02d:%02d", durataSecunde / 60, durataSecunde % 60);
        return "Fisier audio " + nume + " (" + extensie + "), durata " + durata +
                ", " + String.format("%.2f", getDimensiuneKB()) + " KB, in " + cale;
    }
}
