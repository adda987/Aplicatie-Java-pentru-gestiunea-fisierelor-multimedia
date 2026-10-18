package decorator;

import java.awt.Color;

/**
 * Decorator care inverseaza culorile imaginii (negativ).
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public class DecoratorNegativ extends DecoratorImagine {

    public DecoratorNegativ(InterfataImagine imagine) {
        super(imagine);
    }

    @Override
    public String getNumeEfect() {
        return "negativ";
    }

    @Override
    protected Color aplicaEfect(Color c) {
        return new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue());
    }
}
