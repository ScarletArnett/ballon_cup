import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Joueur {
    private int id;
    private Main main;
    private List<Cube> cubeGagnes = new LinkedList<>();
    private List<Trophee> tropheeGagnes = new LinkedList<>();

    public Joueur(int id, Main main) {
        this.id = id;
        this.main = main;
    }

    public Joueur(int id, Table table) {
        this(id, new Main(table, id));
    }

    public int getId() {
        return id;
    }

    public Main getMain() {
        return main;
    }

    public List<Cube> getCubeGagnes() {
        return cubeGagnes;
    }

    public void gagneCubes(List<Cube> cubes) {
        this.cubeGagnes.addAll(cubes);
    }

    public List<Cube> getCubeGagnesParCouleur(Couleur couleur, int nbCubes) {
        List<Cube> res = new LinkedList<>();

        for (Cube c : cubeGagnes) {
            if (c.getCouleur() == couleur && res.size() < nbCubes) {
                res.add(c);
            }
        }

        if (res.size() < nbCubes) {
            return Collections.emptyList();
        }

        return res;
    }

    public List<Cube> getCubeGagnesParCouleur(Couleur couleur) {
        List<Cube> res = new LinkedList<>();

        for (Cube c : cubeGagnes) {
            if (c.getCouleur() == couleur) {
                res.add(c);
            }
        }

        return res;
    }

    public void enleveCubes(List<Cube> cubes) {
        this.cubeGagnes.removeAll(cubes);
    }

    public List<Trophee> getTropheeGagnes() {
        return tropheeGagnes;
    }

    public int getNbTropheeGagnes() {
        return tropheeGagnes.size();
    }

    public void gagneTrophee(Trophee trophee) {
        this.tropheeGagnes.add(trophee);
    }

    @Override
    public String toString() {
        return "Joueur(id=" + id + ")";
    }
}
