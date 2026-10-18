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
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ManagerDirectoare manager = new ManagerDirectoare("directoare.txt");
        ScannerFisiere scan = new ScannerFisiere(manager);
        Scanner in = new Scanner(System.in);
        String meniu = citesteMeniu("meniu.txt");

        String optiune = "";
        while (!optiune.equals("0")) {
            System.out.println(meniu);
            System.out.print("Alege optiunea: ");
            optiune = in.nextLine().trim();

            switch (optiune) {
                case "1":
                    manager.afiseazaDirectoare();
                    break;
                case "2":
                    System.out.print("Introdu calea directorului: ");
                    try {
                        manager.adaugaDirector(in.nextLine());
                        System.out.println("Directorul a fost adaugat.");
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
                    aplicaEfect(in, scan);
                    break;
                case "0":
                    System.out.println("La revedere!");
                    break;
                default:
                    System.out.println("Optiune invalida!");
            }
        }
    }

    /**
     * Citeste textul meniului din fisier.
     */
    private static String citesteMeniu(String caleFisier) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(caleFisier))) {
            String linie;
            while ((linie = br.readLine()) != null)
                sb.append(linie).append("\n");
        } catch (IOException e) {
            System.out.println("Eroare la citirea meniului din fisier.");
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
                System.out.print("Introdu extensia (.mp3, .wav, .jpg, .jpeg, .png): ");
                afiseazaFisiere(scan.cautaFisiereDupaExtensie(in.nextLine()));
                break;
            default:
                System.out.println("Optiune invalida!");
        }
    }

    /**
     * Utilizatorul alege o imagine si un efect. Imaginea este "decorata" cu efectul ales
     * si salvata in acelasi director cu imaginea originala.
     */
    private static void aplicaEfect(Scanner in, ScannerFisiere scan) {
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
            String director = new File(imagine.getCale()).getParent();

            //String director = "test";
            // System.out.print("Introdu directorul unde vrei sa salvezi: "); --> de la tastatura
            // String director = in.nextLine().trim();

            File salvat = decorator.salveaza(director);
            System.out.println(decorator.genereazaDescriere());
            System.out.println("Imaginea a fost salvata in: " + salvat.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Eroare la salvarea imaginii: " + e.getMessage());
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
