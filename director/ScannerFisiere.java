package director;

import clase.FisierMultimedia;
import clase.TipFisier;
import factory.FactoryFisierMultimedia;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clasa care parcurge directoarele monitorizate si cauta fisiere multimedia.
 */
public class ScannerFisiere {

    private ManagerDirectoare managerDirectoare;

    public ScannerFisiere(ManagerDirectoare managerDirectoare) {
        this.managerDirectoare = managerDirectoare;
    }

    /**
     * Parcurge toate directoarele monitorizate si colecteaza fisierele multimedia.
     *
     * @return lista fisierelor multimedia gasite, sortata dupa nume
     */
    public List<FisierMultimedia> cautaToateFisiereleMedia() {
        List<FisierMultimedia> rezultate = new ArrayList<>();
        for (String cale : managerDirectoare.getDirectoare())
            parcurgeDirector(new File(cale), rezultate);
        Collections.sort(rezultate);
        return rezultate;
    }

    /**
     * Parcurge recursiv un director
     * si adauga in lista fisierele multimedia gasite.
     *
     * @param director  directorul care trebuie parcurs
     * @param rezultate lista in care se adauga fisierele gasite
     */
    private void parcurgeDirector(File director, List<FisierMultimedia> rezultate) {
        File[] continut = director.listFiles();
        if (continut == null) // directorul nu exista sau nu poate fi citit
            return;

        for (File fisier : continut) {
            if (fisier.isDirectory()) {
                parcurgeDirector(fisier, rezultate);
            } else {
                TipFisier tip = FactoryFisierMultimedia.getTip(fisier);
                if (tip != null)
                    rezultate.add(FactoryFisierMultimedia.getFisier(fisier, tip));
            }
        }
    }

    public List<FisierMultimedia> cautaFisiereDupaTip(TipFisier tip) {
        List<FisierMultimedia> rezultate = new ArrayList<>();
        for (FisierMultimedia fisier : cautaToateFisiereleMedia())
            if (fisier.getTip() == tip)
                rezultate.add(fisier);
        return rezultate;
    }

    public List<FisierMultimedia> cautaFisiereDupaExtensie(String extensie) {
        List<FisierMultimedia> rezultate = new ArrayList<>();
        for (FisierMultimedia fisier : cautaToateFisiereleMedia())
            if (fisier.getExtensie().equalsIgnoreCase(extensie.trim()))
                rezultate.add(fisier);
        return rezultate;
    }
}
