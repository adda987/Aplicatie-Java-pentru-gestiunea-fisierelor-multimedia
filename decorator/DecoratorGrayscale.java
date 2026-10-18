package decorator;

import java.awt.Color;

/**
 * Decorator care transforma imaginea in nuante de gri.
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class DecoratorGrayscale extends DecoratorImagine {

    public DecoratorGrayscale(InterfataImagine imagine) {
        super(imagine);
    }

    @Override
    public String getNumeEfect() {
        return "grayscale";
    }

    @Override
    protected Color aplicaEfect(Color c) {
        int gri = (int) (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue());
        return new Color(gri, gri, gri);
    }
}
