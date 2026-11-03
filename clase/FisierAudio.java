package clase;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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

    private static final int[] MPEG1_LAYER3_BITRATES =
            {0, 32, 40, 48, 56, 64, 80, 96, 112, 128, 160, 192, 224, 256, 320, 0};
    private static final int[] MPEG2_LAYER3_BITRATES =
            {0, 8, 16, 24, 32, 40, 48, 56, 64, 80, 96, 112, 128, 144, 160, 0};

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
            if (".mp3".equals(extensie))
                return citesteDurataMp3(fisier);

            try (AudioInputStream ais = AudioSystem.getAudioInputStream(fisier)) {
                AudioFormat format = ais.getFormat();
                if (ais.getFrameLength() < 0 || format.getFrameRate() <= 0)
                    throw new IOException("Durata audio nu poate fi determinata.");
                return (int) (ais.getFrameLength() / format.getFrameRate());
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            System.out.println("Eroare la citirea duratei fisierului audio.");
            return 0;
        }
    }

    private int citesteDurataMp3(File fisier) throws IOException {
        double durata = 0;
        int cadreGasite = 0;

        try (RandomAccessFile audio = new RandomAccessFile(fisier, "r")) {
            long pozitie = inceputDateMp3(audio);
            long lungimeFisier = audio.length();

            while (pozitie + 4 <= lungimeFisier) {
                audio.seek(pozitie);
                int antet = audio.readInt();
                int versiune = (antet >>> 19) & 0x3;
                int strat = (antet >>> 17) & 0x3;
                int indexBitrate = (antet >>> 12) & 0xf;
                int indexFrecventa = (antet >>> 10) & 0x3;

                if ((antet & 0xffe00000) != 0xffe00000
                        || (versiune != 3 && versiune != 2 && versiune != 0)
                        || strat != 1
                        || indexBitrate == 0 || indexBitrate == 15
                        || indexFrecventa == 3) {
                    pozitie++;
                    continue;
                }

                int[] rateBit = versiune == 3
                        ? MPEG1_LAYER3_BITRATES : MPEG2_LAYER3_BITRATES;
                int[] rateEsantionare = {44100, 48000, 32000};
                int frecventa = rateEsantionare[indexFrecventa];
                if (versiune == 2)
                    frecventa /= 2;
                else if (versiune == 0)
                    frecventa /= 4;

                int bitrate = rateBit[indexBitrate] * 1000;
                int lungimeCadru = (versiune == 3 ? 144 : 72) * bitrate / frecventa
                        + ((antet >>> 9) & 1);
                if (pozitie + lungimeCadru > lungimeFisier) {
                    pozitie++;
                    continue;
                }

                durata += (versiune == 3 ? 1152.0 : 576.0) / frecventa;
                cadreGasite++;
                pozitie += lungimeCadru;
            }
        }

        if (cadreGasite == 0)
            throw new IOException("Nu au fost gasite cadre MP3 valide.");
        return (int) durata;
    }

    private long inceputDateMp3(RandomAccessFile audio) throws IOException {
        if (audio.length() < 10)
            return 0;

        byte[] antet = new byte[10];
        audio.readFully(antet);
        if (antet[0] != 'I' || antet[1] != 'D' || antet[2] != '3')
            return 0;

        int marime = ((antet[6] & 0x7f) << 21)
                | ((antet[7] & 0x7f) << 14)
                | ((antet[8] & 0x7f) << 7)
                | (antet[9] & 0x7f);
        return Math.min(audio.length(), 10L + marime);
    }

    @Override
    public String genereazaDescriere() {
        String durata = String.format("%02d:%02d", durataSecunde / 60, durataSecunde % 60);
        return "Fisier audio " + nume + " (" + extensie + "), durata " + durata +
                ", " + String.format("%.2f", getDimensiuneKB()) + " KB, in " + cale;
    }
}
