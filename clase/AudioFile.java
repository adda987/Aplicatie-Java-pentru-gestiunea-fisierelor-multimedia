package clase;

import java.io.File;

/**
 * Reprezintă un fișier audio (extensii suportate: {@code .mp3}, {@code .wav}).
 *
 * <p>Extinde {@link MediaFile} și adaugă informații specifice fișierelor audio,
 * precum durata estimată și formatul audio.</p>
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 * @see MediaFile
 */
public class AudioFile extends MediaFile {

    private String audioFormat;
    private int durationInSeconds;

    public AudioFile(File file) {
        super(file);
        this.audioFormat = extension.replace(".", "").toUpperCase();
        this.durationInSeconds = 0;
    }


    public AudioFile(String name, String extension, long sizeInBytes,
                     String path, int durationInSeconds) {
        super(name, extension, sizeInBytes, path);
        this.audioFormat = extension.replace(".", "").toUpperCase();
        this.durationInSeconds = durationInSeconds;
    }


    public String getAudioFormat() {
        return audioFormat;
    }
    public int getDurationInSeconds() {
        return durationInSeconds;
    }
    public void setDurationInSeconds(int durationInSeconds) {
        if (durationInSeconds >= 0) {
            this.durationInSeconds = durationInSeconds;
        }
    }

    public String getFormattedDuration() {
        if (durationInSeconds == 0) return "N/A";
        int minutes = durationInSeconds / 60;
        int seconds = durationInSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    public String getType() {
        return "AUDIO";
    }

    @Override
    public String getInfo() {
        return String.format("[%s | %s] %s%s | %.2f MB | Durata: %s | %s",
                getType(), audioFormat, name, extension,
                getSizeInMB(), getFormattedDuration(), path);
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
