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

    public FisierAudio(String nume, String extensie, long dimensiune, String cale, int durataSecunde) {
        super(nume, extensie, dimensiune, cale, TipFisier.AUDIO);
        this.durataSecunde = durataSecunde;
    }

    public FisierAudio(File fisier) {
        super(fisier, TipFisier.AUDIO);
        this.durataSecunde = 0;
    }

    public int getDurataSecunde() {
        return durataSecunde;
    }

    public void setDurataSecunde(int durataSecunde) {
        if (durataSecunde < 0)
            throw new IllegalArgumentException("Durata nu poate fi negativa!");
        this.durataSecunde = durataSecunde;
    }

    /**
     * Citeste durata fisierului de pe disc.
     *
     * @throws UnsupportedAudioFileException daca formatul nu poate fi citit (ex: .mp3)
     * @throws IOException                   daca fisierul nu poate fi accesat
     */
    public void citesteDurata() throws UnsupportedAudioFileException, IOException {
        AudioInputStream ais = AudioSystem.getAudioInputStream(new File(getCale()));
        AudioFormat format = ais.getFormat();
        this.durataSecunde = (int) (ais.getFrameLength() / format.getFrameRate());
        ais.close();
    }

    @Override
    public String genereazaDescriere() {
        String durata = "necunoscuta";
        if (durataSecunde > 0)
            durata = String.format("%02d:%02d", durataSecunde / 60, durataSecunde % 60);
        return "Fisier audio " + getNume() + " (" + getExtensie() + "), durata " + durata +
                ", " + String.format("%.2f", getDimensiuneKB()) + " KB, in " + getCale();
    }

    @Override
    public String toString() {
        return "FisierAudio{" +
                "durataSecunde=" + durataSecunde +
                "} " + super.toString();
    }
}
