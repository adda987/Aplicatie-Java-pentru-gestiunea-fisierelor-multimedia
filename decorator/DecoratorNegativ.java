package decorator;

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
    protected int aplicaEfect(int pixel) {
        return construiestePixel(alpha(pixel), 255 - rosu(pixel), 255 - verde(pixel), 255 - albastru(pixel));
    }

    @Override
    public String genereazaDescriere() {
        return super.genereazaDescriere() + " + efect negativ";
    }
}
