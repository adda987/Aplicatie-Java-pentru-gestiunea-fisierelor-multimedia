package director;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru gestionarea fisierului text care contine calea catre directoarele monitorizate.
 * Nu sterge fisiere de pe hard disk; sterge doar referinta din fisierul de configurare.
 */
public class ManagerDirectoare {

    private final String caleFisier;
    private final List<String> directoare;

    public ManagerDirectoare(String caleFisier) {
        this.caleFisier = caleFisier;
        this.directoare = new ArrayList<>();
        citesteDinFisier();
    }

    public List<String> getDirectoare() {
        return directoare;
    }

    /**
     * Adauga un director in lista de directoare monitorizate.
     * Calea trebuie sa indice un director existent si accesibil.
     *
     * @param cale calea directorului introdusa de utilizator
     * @throws DirectorInvalidException daca calea nu indica un director valid si accesibil
     * @throws IOException daca lista directoarelor nu poate fi salvata
     */
    public void adaugaDirector(String cale) throws IOException {
        if (cale == null || cale.trim().isEmpty())
            throw new DirectorInvalidException("Calea nu poate fi citita.");

        File director = new File(cale.trim());
        if (!director.exists() || !director.isDirectory() || !director.canRead() || !director.canExecute())
            throw new DirectorInvalidException("Calea nu indica un director existent si accesibil: " + cale);

        String caleAbsoluta = director.getAbsolutePath();
        if (!directoare.contains(caleAbsoluta)) {
            directoare.add(caleAbsoluta);
            try {
                salveazaInFisier();
            } catch (IOException e) {
                directoare.remove(caleAbsoluta);
                throw e;
            }
        }
    }

    /**
     * Sterge un director din lista de directoare monitorizate.
     *
     * @param cale calea directorului ce trebuie eliminat
     * @return true daca directorul a fost gasit si eliminat, altfel false
     * @throws IOException daca lista directoarelor nu poate fi salvata
     */
    public boolean stergeDirector(String cale) throws IOException {
        String caleAbsoluta = new File(cale.trim()).getAbsolutePath();
        int index = directoare.indexOf(caleAbsoluta);
        if (index >= 0) {
            directoare.remove(index);
            try {
                salveazaInFisier();
            } catch (IOException e) {
                directoare.add(index, caleAbsoluta);
                throw e;
            }
            return true;
        }
        return false;
    }

    /**
     * Afiseaza toate directoarele monitorizate, numerotate.
     */
    public void afiseazaDirectoare() {
        if (directoare.isEmpty()) {
            System.out.println("Nu exista directoare monitorizate.");
            return;
        }
        System.out.println("Directoare monitorizate:");
        for (int i = 0; i < directoare.size(); i++)
            System.out.println((i + 1) + ". " + directoare.get(i));
    }

    /**
     * Incarca directoarele salvate in fisier la pornirea aplicatiei.
     */
    private void citesteDinFisier() {
        File fisier = new File(caleFisier);
        if (!fisier.exists()) {
            System.out.println("Fisierul cu directoare nu exista; lista va porni goala: " + caleFisier);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fisier))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (!linie.trim().isEmpty())
                    directoare.add(linie.trim());
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului " + caleFisier + ": " + e.getMessage());
        }
    }

    /**
     * Scrie lista de directoare in fisier, cate unul pe linie.
     */
    private void salveazaInFisier() throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caleFisier))) {
            for (String director : directoare)
                bw.write(director + System.lineSeparator());
        }
    }
}
