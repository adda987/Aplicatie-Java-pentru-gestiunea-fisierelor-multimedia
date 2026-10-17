package decorator;

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
    protected int aplicaEfect(int pixel) {
        int r = rosu(pixel);
        int g = verde(pixel);
        int b = albastru(pixel);

        int rosuNou = (int) (0.393 * r + 0.769 * g + 0.189 * b);
        int verdeNou = (int) (0.349 * r + 0.686 * g + 0.168 * b);
        int albastruNou = (int) (0.272 * r + 0.534 * g + 0.131 * b);

        return construiestePixel(alpha(pixel), rosuNou, verdeNou, albastruNou);
    }

    @Override
    public String genereazaDescriere() {
        return super.genereazaDescriere() + " + efect sepia";
    }
}
