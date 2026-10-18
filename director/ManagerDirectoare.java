package director;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Clasa pentru gestionarea fisierului text care contine calea catre directoarele monitorizate.
 * Nu sterge fisiere de pe hard disk; sterge doar referinta din fisierul de configurare.
 */
public class ManagerDirectoare {

    private String caleFisier;
    private List<String> directoare;

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
     * Daca directorul nu exista pe disc, este creat (gol).
     *
     * @param cale calea directorului introdusa de utilizator
     * @throws IllegalArgumentException daca exista deja un fisier cu acea cale sau daca directorul nu poate fi creat
     */
    public void adaugaDirector(String cale) {
        File director = new File(cale.trim());
        if (director.isFile())
            throw new IllegalArgumentException("Calea indica un fisier, nu un director: " + cale);
        if (!director.exists() && !director.mkdirs())
            throw new IllegalArgumentException("Directorul nu a putut fi creat: " + cale);

        String caleAbsoluta = director.getAbsolutePath();
        if (!directoare.contains(caleAbsoluta)) {
            directoare.add(caleAbsoluta);
            salveazaInFisier();
        }
    }

    /**
     * Sterge un director din lista de directoare monitorizate.
     *
     * @param cale calea directorului ce trebuie eliminat
     * @return true daca directorul a fost gasit si eliminat, altfel false
     */
    public boolean stergeDirector(String cale) {
        String caleAbsoluta = new File(cale.trim()).getAbsolutePath();
        if (directoare.remove(caleAbsoluta)) {
            salveazaInFisier();
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
        if (!fisier.exists())
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(fisier))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                if (!linie.trim().isEmpty())
                    directoare.add(linie.trim());
            }
        } catch (IOException e) {
            System.out.println("Eroare la citirea fisierului " + caleFisier);
        }
    }

    /**
     * Scrie lista de directoare in fisier, cate unul pe linie.
     */
    private void salveazaInFisier() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(caleFisier))) {
            for (String director : directoare)
                pw.println(director);
        } catch (IOException e) {
            System.out.println("Eroare la salvarea fisierului " + caleFisier);
        }
    }
}
