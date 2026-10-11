package clase;

import java.io.File;

/**
 * Clasa abstractă care reprezintă un fișier multimedia generic.
 * Stochează informații comune tuturor tipurilor de fișiere multimedia:
 * nume, extensie, dimensiune și cale pe disk.
 *
 * <p>Subclasele concrete ({@link AudioFile}, {@link ImageFile}) trebuie
 * să implementeze metoda {@link #getType()} pentru a returna tipul specific.</p>
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public abstract class MediaFile {
    protected String name;
    protected String extension;
    protected long sizeInBytes;
    protected String path;


    public static final String[] SUPPORTED_AUDIO = {".mp3", ".wav"};
    public static final String[] SUPPORTED_IMAGE = {".jpg", ".png"};

    public MediaFile(String name, String extension, long sizeInBytes, String path) {
        this.name = name;
        this.extension = extension;
        this.sizeInBytes = sizeInBytes;
        this.path = path;
    }

    public MediaFile(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            this.name = fileName.substring(0, dotIndex);
            this.extension = fileName.substring(dotIndex).toLowerCase();
        } else {
            this.name = fileName;
            this.extension = "";
        }
        this.sizeInBytes = file.length();
        this.path = file.getAbsolutePath();
    }

    public String getName() {
        return name;
    }
    public String getExtension() {
        return extension;
    }
    public long getSizeInBytes() {
        return sizeInBytes;
    }
    public double getSizeInMB() {
        return Math.round((sizeInBytes / (1024.0 * 1024.0)) * 100.0) / 100.0;
    }
    public String getPath() {
        return path;
    }
    public abstract String getType();


    public static boolean isSupportedAudio(String ext) {
        for (String supported : SUPPORTED_AUDIO) {
            if (supported.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSupportedImage(String ext) {
        for (String supported : SUPPORTED_IMAGE) {
            if (supported.equalsIgnoreCase(ext)) {
                return true;
            }
        }
        return false;
    }


    public String getInfo() {
        return String.format("[%s] %s%s | %.2f MB | %s",
                getType(), name, extension, getSizeInMB(), path);
    }


    @Override
    public String toString() {
        return getInfo();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MediaFile)) return false;
        MediaFile other = (MediaFile) obj;
        return this.path.equals(other.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}
