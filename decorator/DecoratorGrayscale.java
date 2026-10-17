package decorator;

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
    protected int aplicaEfect(int pixel) {
        int gri = (int) (0.299 * rosu(pixel) + 0.587 * verde(pixel) + 0.114 * albastru(pixel));
        return construiestePixel(alpha(pixel), gri, gri, gri);
    }

    @Override
    public String genereazaDescriere() {
        return super.genereazaDescriere() + " + efect grayscale";
    }
}
