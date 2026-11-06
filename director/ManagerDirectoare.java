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
    private final List<File> directoareNoi = new ArrayList<>();

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
     * Directorul este creat la iesire daca nu exista, cu conditia ca parintele sa existe.
     *
     * @param cale calea directorului introdusa de utilizator
     * @throws DirectorInvalidException daca calea nu indica un director valid si accesibil
     */
    public void adaugaDirector(String cale) {
        if (cale == null || cale.trim().isEmpty())
            throw new DirectorInvalidException("Calea nu poate fi citita.");

        File director = new File(cale.trim());
        File parinte = director.getAbsoluteFile().getParentFile();
        if (!director.exists() && (parinte == null || !parinte.isDirectory()
                || !parinte.canWrite() || !parinte.canExecute()))
            throw new DirectorInvalidException("Directorul parinte nu exista sau nu este accesibil: " + cale);
        if (director.exists() && (!director.isDirectory() || !director.canRead() || !director.canExecute()))
            throw new DirectorInvalidException("Calea nu indica un director existent si accesibil: " + cale);

        String caleAbsoluta = director.getAbsolutePath();
        if (!directoare.contains(caleAbsoluta)) {
            directoare.add(caleAbsoluta);
            if (!director.exists())
                directoareNoi.add(director.getAbsoluteFile());
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
        directoareNoi.remove(new File(caleAbsoluta));
        return directoare.remove(caleAbsoluta);
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
     * Creeaza directoarele noi si salveaza lista la inchiderea aplicatiei.
     */
    public void salveazaInFisier() throws IOException {
        for (File director : directoareNoi)
            if (!director.isDirectory() && !director.mkdir())
                throw new IOException("Nu s-a putut crea directorul: " + director);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caleFisier))) {
            for (String director : directoare)
                bw.write(director + System.lineSeparator());
        }
    }
}
