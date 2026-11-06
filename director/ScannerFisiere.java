package director;

import clase.FisierMultimedia;
import clase.TipFisier;
import factory.FactoryFisierMultimedia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Clasa care parcurge directoarele monitorizate si cauta fisiere multimedia.
 */
public class ScannerFisiere {

    private ManagerDirectoare managerDirectoare;
    private final int[] statistici = new int[5];
    private final HashMap<String, List<FisierMultimedia>> fisiereDupaExtensie = new HashMap<>();

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
        fisiereDupaExtensie.clear();
        for (int i = 0; i < statistici.length; i++)
            statistici[i] = 0;

        for (String cale : managerDirectoare.getDirectoare())
            parcurgeDirector(new File(cale), rezultate);
        Collections.sort(rezultate);

        for (FisierMultimedia fisier : rezultate) {
            String extensie = fisier.getExtensie();
            fisiereDupaExtensie
                    .computeIfAbsent(extensie, k -> new ArrayList<>())
                    .add(fisier);
            int index = indexExtensie(extensie);
            if (index >= 0)
                statistici[index]++;
        }

        return rezultate;
    }

    private int indexExtensie(String extensie) {
        switch (extensie) {
            case ".mp3":
                return 0;
            case ".wav":
                return 1;
            case ".jpg":
                return 2;
            case ".jpeg":
                return 3;
            case ".png":
                return 4;
            default:
                return -1;
        }
    }

    public void salveazaStatistici(String cale) throws IOException {
        try (FileWriter scriitor = new FileWriter(cale)) {
            scriitor.write(genereazaStatistici());
        }
    }

    private String genereazaStatistici() {
        cautaToateFisiereleMedia();
        int total = 0;
        String[] extensii = {".mp3", ".wav", ".jpg", ".jpeg", ".png"};
        StringBuilder raport = new StringBuilder("Statistici fisiere multimedia:\n");
        for (int i = 0; i < statistici.length; i++) {
            raport.append(extensii[i]).append(": ").append(statistici[i]).append('\n');
            total += statistici[i];
        }
        return raport.append("Total: ").append(total).append('\n').toString();
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
        if (continut == null) {
            System.out.println("Directorul nu exista sau nu poate fi accesat: " + director.getAbsolutePath());
            return;
        }

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

    public List<FisierMultimedia> cautaToateFisiereleSortateDupaExtensie() {
        List<FisierMultimedia> rezultate = cautaToateFisiereleMedia();
        rezultate.sort(Comparator.comparing(FisierMultimedia::getExtensie)
                .thenComparing(FisierMultimedia::getNume, String.CASE_INSENSITIVE_ORDER));
        return rezultate;
    }

    public List<FisierMultimedia> cautaFisiereDupaExtensie(String extensie) {
        cautaToateFisiereleMedia();
        return new ArrayList<>(fisiereDupaExtensie.getOrDefault(
                extensie.trim().toLowerCase(Locale.ROOT), Collections.emptyList()));
    }
}
