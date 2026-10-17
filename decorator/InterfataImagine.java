package decorator;

import java.io.IOException;

/**
 * Interfata comuna pentru imagini si pentru decoratorii care aplica efecte.
 *
 * @author Sfetcu Andreea-Daniela
 * @version 1.0
 */
public interface InterfataImagine {

    String getNume();

    String genereazaDescriere();

    int[][] getPixeli() throws IOException;
}
