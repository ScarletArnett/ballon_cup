import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BallonCupApp {
    public static void main(String[] args) {
        genCarteTxt();
//        Table table = new Table();
//        BallonCup jeu = new BallonCup(table);
//
//        jeu.distribuerCartes();
//
//        jeu.getOlive().gagneCubes(table.prendreCubes(Couleur.GRIS, 4));
//
//        while (!jeu.estTermine()) {
//            afficherTable(table);
//
//            Joueur joueur = jeu.getJoueurEnCours();
//
//            System.out.println("A ton tour joueur " + joueur.getId());
//            System.out.println("Tu possèdes les cubes " + joueur.getCubeGagnes());
//            System.out.println("Tu possèdes les trophées " + joueur.getTropheeGagnes());
//
//            for (Trophee trophee : joueur.getTropheeGagnes()) {
//                List<Cube> cubes = joueur.getCubeGagnesParCouleur(trophee.getCouleur());
//
//                if (cubes.isEmpty()) {
//                    continue;
//                }
//
//                System.out.printf("Tu as %d cubes %s mais tu as déjà un trophée %2$s, ",
//                        cubes.size(), trophee.getCouleur());
//
//                if (!demanderBoolean("souhaites-tu les convertir ?")) {
//                    continue;
//                }
//
//                List<Cube> cubesPris;
//
//                while (true) {
//                    Couleur couleur = demanderCouleur("Prendre des cubes de quelle couleur ? ");
//                    cubesPris = table.prendreCubes(couleur, cubes.size());
//
//                    if (cubesPris != null) break;
//
//                    System.out.printf("Tu ne peux pas prendre %d cubes de couleur %s\n", cubes.size(), trophee.getCouleur());
//                }
//
//                joueur.enleveCubes(cubes);
//                joueur.gagneCubes(cubesPris);
//            }
//
//            while (true) {
//                afficherMain(joueur.getMain());
//
//
//                if (!joueur.getMain().peutPlacerCarte()) {
//                    System.out.print("Tu ne peux pas poser de carte, ");
//                    int jete = demanderEntier("combien souhaites-tu en jeter ? ");
//
//                    joueur.getMain().jeteCartes(jete);
//                    joueur.getMain().prendreCartes(jete);
//
//                    if (!joueur.getMain().peutPlacerCarte()) {
//                        System.out.println("Tu ne peux toujours pas jouer, tu passes ton tour...");
//                        break;
//                    }
//                }
//
//                int carte = demanderEntier("Quelle carte souhaitez vous poser ? ");
//                int tuileId = demanderEntier("Sur quelle tuile ? ");
//                Camp campCible = demanderCamp("Dans quel camp ? ");
//
//                if (joueur.getMain().placerCarte(carte, tuileId, campCible)) {
//                    joueur.getMain().prendreCarte();
//                    break;
//                } else {
//                    System.out.println("Vous ne pouvez pas poser cette carte ici");
//                    System.out.println();
//                }
//            }
//
//            jeu.distribuerCubes();
//            jeu.distribuerTrophees();
//            jeu.passerTour();
//        }
//
//        afficherTable(table);
//
//        System.out.printf("bien joué, joueur %d, tu as gagné !\n", jeu.getJoueurGagnant().getId());
    }

    public static Camp nextCamp(Camp camp) {
        switch (camp) {
            case A: return Camp.B;
            case B: return Camp.A;
            default: throw new Error();
        }
    }

    public static int demanderEntier(String msg) {
        Scanner stdin = new Scanner(System.in);
        System.out.print(msg);
        while (true) {
            try {
                return stdin.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("veuillez entrer un entier");
                stdin = new Scanner(System.in);
            }
        }
    }

    public static Camp demanderCamp(String msg) {
        Scanner stdin = new Scanner(System.in);
        System.out.print(msg);
        while (true) {
            try {
                return Camp.valueOf(stdin.nextLine());
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("veuillez entrer un camp valide : " + Arrays.toString(Camp.values()));
            }
        }
    }

    public static boolean demanderBoolean(String msg) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(msg);
        System.out.print("(y/n) ");

        while (true) {
            try {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("y")) {
                    return true;
                }
                if (input.equalsIgnoreCase("n")) {
                    return false;
                }

                System.out.print("veuillez entrer `y' ou `n' ");
            } catch (InputMismatchException e) {
                System.out.print("veuillez entrer `y' ou `n' ");
            }
        }
    }

    public static Couleur demanderCouleur(String msg) {
        Scanner stdin = new Scanner(System.in);
        System.out.print(msg);
        while (true) {
            try {
                return Couleur.valueOf(stdin.nextLine());
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("veuillez entrer une couleur valide : " + Arrays.toString(Couleur.values()));
            }
        }
    }

    public static void afficherTable(Table table) {
        for (int i = 1; i <= table.getNbTuiles(); i++) {
            Tuile tuile = table.getTuile(i);

            afficherTuile(tuile);

            System.out.print('\n');
        }
    }

    public static void afficherTuile(Tuile tuile) {
        System.out.printf(
                "%20s | %8s %d %-55s | %-20s",
                afficherCartePlacee(tuile.getCartePlacees(Camp.A)),
                getTypeDeTuile(tuile),
                tuile.getNumero(),
                tuile.getCubes(),
                afficherCartePlacee(tuile.getCartePlacees(Camp.B))
        );
    }

    public static String getTypeDeTuile(Tuile tuile) {
        if (tuile.estMontagne()) return "Montagne";
        else return "Plaine";
    }

    public static String afficherCartePlacee(List<CartePlacee> listCarte) {
        StringBuilder sb = new StringBuilder();

        for (CartePlacee carte : listCarte) {
            sb.append(carte.getCarte().getCouleur()).append(" ").append(carte.getCarte().getNumero());
        }

        return sb.toString();
    }

    public static void afficherMain(Main main) {
        for (int i = 0; i < main.getCartes().size(); i++) {
            if (main.getCartes().get(i).getNumero() < 10)
                System.out.print("[" + i + "- " + "0" + main.getCartes().get(i).getNumero() + "(" + main.getCartes().get(i).getCouleur() + ")] \t");
            else
                System.out.print("[" + i + "- " + main.getCartes().get(i).getNumero() + "(" + main.getCartes().get(i).getCouleur() + ")] \t");
        }
        System.out.println();
    }

    public static void genCarteTxt() {
        Pattern pattern = Pattern.compile("^carte_(\\d+)([A-Z]+)\\.jpg$");
        StringBuilder sb = new StringBuilder();

        for (File file : new File("./img/").listFiles()) {
            Matcher matcher = pattern.matcher(file.getName());
            if (!matcher.matches()) {
                continue;
            }

            int numero = Integer.parseInt(matcher.group(1));
            Couleur couleur = Couleur.valueOf(matcher.group(2));

            sb.append(couleur.toChar()).append(String.format("%02d", numero));
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Cartes2.txt"))) {
            writer.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
