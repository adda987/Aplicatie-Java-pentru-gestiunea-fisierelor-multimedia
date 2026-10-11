package clase;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Reprezintă un fișier imagine (extensii suportate: {@code .jpg}, {@code .png}).
 *
 * <p>Extinde {@link MediaFile} și adaugă informații specifice imaginilor:
 * lățime, înălțime și matricea de pixeli. Matricea de pixeli ({@code int[][]})
 * reprezintă valorile ARGB ale fiecărui pixel și este utilizată pentru
 * aplicarea de efecte grafice (grayscale, sepia, negativ).</p>
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 * @see MediaFile
 */
public class ImageFile extends MediaFile {
    private int width;
    private int height;
    private int[][] pixelMatrix;


    public ImageFile(File file) {
        super(file);
        this.width = 0;
        this.height = 0;
        this.pixelMatrix = null;
    }

    public ImageFile(String name, String extension, long sizeInBytes,
                     String path, int width, int height) {
        super(name, extension, sizeInBytes, path);
        this.width = width;
        this.height = height;
        this.pixelMatrix = null;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public String getResolution() {
        if (width == 0 || height == 0) return "necunoscuta";
        return width + "x" + height;
    }

    public int[][] getPixelMatrix() {
        return pixelMatrix;
    }

    public void loadPixelMatrix() throws IOException {
        File imageFile = new File(path);
        BufferedImage bufferedImage = ImageIO.read(imageFile);

        if (bufferedImage == null) {
            throw new IOException("Fisierul nu poate fi citit ca imagine: " + path);
        }

        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.pixelMatrix = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pixelMatrix[row][col] = bufferedImage.getRGB(col, row);
            }
        }
    }

    public boolean isPixelMatrixLoaded() {
        return pixelMatrix != null;
    }

    public static int extractAlpha(int argb) {
        return (argb >> 24) & 0xFF;
    }
    public static int extractRed(int argb) {
        return (argb >> 16) & 0xFF;
    }
    public static int extractGreen(int argb) {
        return (argb >> 8) & 0xFF;
    }
    public static int extractBlue(int argb) {
        return argb & 0xFF;
    }


    public static int buildARGB(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    @Override
    public String getType() {
        return "IMAGE";
    }

    @Override
    public String getInfo() {
        return String.format("[%s | %s] %s%s | Rezolutie: %s | %.2f MB | %s",
                getType(),
                extension.replace(".", "").toUpperCase(),
                name, extension,
                getResolution(),
                getSizeInMB(),
                path);
    }

    @Override
    public String toString() {
        return getInfo();
    }
}
