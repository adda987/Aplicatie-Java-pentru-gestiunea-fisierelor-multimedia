package decorator;

import java.awt.Color;

/**
 * Decorator care aplica imaginii un ton sepia (aspect de fotografie veche).
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class DecoratorSepia extends DecoratorImagine {

    public DecoratorSepia(InterfataImagine imagine) {
        super(imagine);
    }

    @Override
    public String getNumeEfect() {
        return "sepia";
    }

    @Override
    protected Color aplicaEfect(Color c) {
        int r = (int) (0.393 * c.getRed() + 0.769 * c.getGreen() + 0.189 * c.getBlue());
        int g = (int) (0.349 * c.getRed() + 0.686 * c.getGreen() + 0.168 * c.getBlue());
        int b = (int) (0.272 * c.getRed() + 0.534 * c.getGreen() + 0.131 * c.getBlue());

        return new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
    }
}
