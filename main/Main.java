package main;

import clase.FisierImagine;
import clase.FisierMultimedia;
import clase.TipFisier;
import decorator.DecoratorGrayscale;
import decorator.DecoratorImagine;
import decorator.DecoratorNegativ;
import decorator.DecoratorSepia;
import director.ManagerDirectoare;
import director.ScannerFisiere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ManagerDirectoare manager = new ManagerDirectoare("directoare.txt");
        ScannerFisiere scan = new ScannerFisiere(manager);
        Scanner in = new Scanner(System.in);
        String meniu = citesteMeniu("meniu.txt");
        Map<DecoratorImagine, String> imaginiDeSalvat = new LinkedHashMap<>();

        String optiune = "";
        while (!optiune.equals("0")) {
            System.out.println(meniu);
            System.out.print("Alege optiunea: ");
            optiune = in.hasNextLine() ? in.nextLine().trim() : "0";

            switch (optiune) {
                case "1":
                    manager.afiseazaDirectoare();
                    break;
                case "2":
                    System.out.print("Introdu calea directorului: ");
                    try {
                        manager.adaugaDirector(in.nextLine());
                        System.out.println("Directorul a fost adaugat in memorie; se salveaza la iesire.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Eroare: " + e.getMessage());
                    }
                    break;
                case "3":
                    System.out.print("Introdu calea directorului pe care vrei sa il stergi: ");
                    if (manager.stergeDirector(in.nextLine()))
                        System.out.println("Directorul a fost sters din lista.");
                    else
                        System.out.println("Directorul nu exista in lista.");
                    break;
                case "4":
                    afiseazaFisiere(scan.cautaToateFisiereleMedia());
                    break;
                case "5":
                    filtreazaFisiere(in, scan);
                    break;
                case "6":
                    aplicaEfect(in, scan, imaginiDeSalvat);
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
        try {
            manager.salveazaInFisier();
        } catch (IOException e) {
            System.out.println("Eroare la salvarea directoarelor: " + e.getMessage());
        }
        for (Map.Entry<DecoratorImagine, String> imagine : imaginiDeSalvat.entrySet()) {
            try {
                File salvat = imagine.getKey().salveaza(imagine.getValue());
                System.out.println("Imagine salvata: " + salvat.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Eroare la salvarea imaginii: " + e.getMessage());
            }
        }
        try {
            scan.salveazaStatistici("statistici.txt");
        } catch (IOException e) {
            System.out.println("Eroare la salvarea statisticilor: " + e.getMessage());
        }
        System.out.println("La revedere!");
    }

    private static String citesteMeniu(String caleFisier) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caleFisier))) {
            String linie;
            while ((linie = br.readLine()) != null)
                sb.append(linie).append("\n");
        } catch (IOException e) {
            System.out.println("Eroare la citirea meniului din fisier: " + e.getMessage());
        }
        return sb.toString();
    }

    private static void filtreazaFisiere(Scanner in, ScannerFisiere scan) {
        System.out.println("Filtreaza dupa:");
        System.out.println("1. Audio");
        System.out.println("2. Imagine");
        System.out.println("3. Extensie");
        System.out.print("Alege optiunea: ");

        switch (in.nextLine().trim()) {
            case "1":
                afiseazaFisiere(scan.cautaFisiereDupaTip(TipFisier.AUDIO));
                break;
            case "2":
                afiseazaFisiere(scan.cautaFisiereDupaTip(TipFisier.IMAGINE));
                break;
            case "3":
                afiseazaFisiere(scan.cautaToateFisiereleSortateDupaExtensie());
                break;
            default:
                System.out.println("Optiune invalida!");
        }
    }

    /**
     * Utilizatorul alege o imagine si un efect. Imaginea este "decorata" cu efectul ales
     * si pastrata in memorie pana la iesire, cand se salveaza langa original.
     */
    private static void aplicaEfect(Scanner in, ScannerFisiere scan,
                                  Map<DecoratorImagine, String> imaginiDeSalvat) {
        List<FisierMultimedia> imagini = scan.cautaFisiereDupaTip(TipFisier.IMAGINE);
        if (imagini.isEmpty()) {
            System.out.println("Nu au fost gasite imagini.");
            return;
        }

        System.out.println("Alege imaginea:");
        for (int i = 0; i < imagini.size(); i++)
            System.out.println((i + 1) + ". " + imagini.get(i).getNume());
        System.out.print("Numarul imaginii: ");
        int nr;
        try {
            nr = Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            nr = 0;
        }
        if (nr < 1 || nr > imagini.size()) {
            System.out.println("Numar invalid!");
            return;
        }
        FisierImagine imagine = (FisierImagine) imagini.get(nr - 1);

        System.out.println("Alege efectul:");
        System.out.println("1. Grayscale");
        System.out.println("2. Sepia");
        System.out.println("3. Negativ");
        System.out.print("Alege optiunea: ");
        DecoratorImagine decorator;
        switch (in.nextLine().trim()) {
            case "1":
                decorator = new DecoratorGrayscale(imagine);
                break;
            case "2":
                decorator = new DecoratorSepia(imagine);
                break;
            case "3":
                decorator = new DecoratorNegativ(imagine);
                break;
            default:
                System.out.println("Optiune invalida!");
                return;
        }

        try {
            decorator.getPixeli();
            imaginiDeSalvat.put(decorator, new File(imagine.getCale()).getParent());
            System.out.println(decorator.genereazaDescriere());
            System.out.println("Imaginea procesata este in memorie; se salveaza la iesire.");
        } catch (IOException e) {
            System.out.println("Eroare la procesarea imaginii: " + e.getMessage());
        }
    }

    private static void afiseazaFisiere(List<FisierMultimedia> fisiere) {
        if (fisiere.isEmpty()) {
            System.out.println("Nu au fost gasite fisiere multimedia.");
            return;
        }
        System.out.println("Lista fisiere multimedia:");
        for (FisierMultimedia fisier : fisiere)
            System.out.println(fisier);
    }
}
