import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Table {
    public static final File CARTES_FILE = new File("./Cartes.txt");
    public static final int NB_TUILES = 4;

    private List<Carte> pioche   = new LinkedList<>();
    private List<Carte> defausse = new LinkedList<>();
    private List<Cube>  sac      = new LinkedList<>();
    private Tuile[]     tuiles   = new Tuile[NB_TUILES];

    public Table() {
        chargerPioche();
        chargerSac();
        chargerTuiles();
    }

    private void chargerPioche() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARTES_FILE))) {
            String content = reader.readLine();

            for (int i = 0; i < content.length(); i += 3) {
                String part = content.substring(i, i + 3);

                Couleur couleur = Couleur.getCouleur(part.charAt(0));
                int numero = Integer.parseInt(part.substring(1, 3), 10);

                pioche.add(new Carte(couleur, numero));
            }

            Collections.shuffle(pioche); // E'RRYDAY IM SHUFFLIN
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chargerSac() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CARTES_FILE))) {
            String content = reader.readLine();

            for (int i = 0; i < content.length(); i += 3) {
                Couleur couleur = Couleur.getCouleur(content.charAt(i));

                sac.add(new Cube(couleur));
            }

            Collections.shuffle(sac);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void chargerTuiles() {
        for (int i = 0; i < NB_TUILES; i++) {
            tuiles[i] = new Tuile(i % 2 == 0, i + 1);
        }
    }

    public int getNbPioche() {
        return pioche.size();
    }

    public Tuile getTuile(int numero) {
        return tuiles[numero - 1];
    }

    public int getNbTuiles() {
        return NB_TUILES;
    }

    public Tuile getDerniereTuile() {
        return getTuile(getNbTuiles());
    }

    public Carte piocher() {
        return pioche.remove(0);
    }

    public List<Carte> piocher(int nbCartes) {
        List<Carte> res = new LinkedList<>();
        for (int i = 0; i < nbCartes; i++) {
            res.add(piocher());
        }
        return res;
    }

    public void remettrePioche() {
        Collections.shuffle(defausse);
        pioche.addAll(defausse);
        defausse.clear();
    }

    public boolean remettrePiocheSiVide() {
        if (pioche.isEmpty()) {
            remettrePioche();
            return true;
        }
        return false;
    }

    public void viderTuile(Tuile tuile) {
        defausse.addAll(tuile.vider());
    }

    public void viderTuile(int numero) {
        viderTuile(getTuile(numero));
    }

    public void vider() {
        for (Tuile tuile : tuiles) {
            defausse.addAll(tuile.vider());
        }

        remettrePiocheSiVide();
    }

    public void placerCubes(Tuile tuile) {
        for (int i = 0; i < tuile.getNumero(); i++) {
            tuile.addCube(sac.remove(0));
        }
    }

    public void placerCubes() {
        for (Tuile tuile : tuiles) {
            placerCubes(tuile);
        }
    }

    public void replacerDansSac(List<Cube> cubes) {
        sac.addAll(cubes);
    }

    public void jeteCarte(Carte carte) {
        defausse.add(carte);
    }

    public List<Cube> prendreCubes(Couleur couleur, int nbCubes) {
        List<Cube> cubes = new LinkedList<>();

        for (Cube cube : sac) {
            if (cube.getCouleur() == couleur && cubes.size() < nbCubes) {
                cubes.add(cube);
            }
        }

        if (cubes.size() < nbCubes) {
            return null;
        }

        sac.removeAll(cubes);
        return cubes;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Pioche (").append(pioche.size()).append(") : ").append(pioche).append('\n');
        sb.append("Défausse (").append(defausse.size()).append(") : ").append(defausse).append('\n');
        sb.append("Tuiles :\n");
        for (Tuile tuile : tuiles) {
            sb.append('\t').append(tuile).append('\n');
        }

        return sb.toString();
    }
}
