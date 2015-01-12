import java.util.LinkedList;
import java.util.List;

public class Main {
    public static final int MAX_CARTES_MAIN = 8;

    private Table table;
    private int joueurId;
    private List<Carte> cartes = new LinkedList<>();

    public Main(Table table, int joueurId) {
        this.table = table;
        this.joueurId = joueurId;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getJoueurId() {
        return joueurId;
    }

    public List<Carte> getCartes() {
        return cartes;
    }

    public void prendreCartes(int nbCartes) {
        if (cartes.size() + nbCartes > MAX_CARTES_MAIN) {
            throw new IllegalStateException("vous ne pouvez pas prendre plus de " + MAX_CARTES_MAIN + " cartes");
        }

        cartes.addAll(table.piocher(nbCartes));
    }

    public void prendreCartes() {
        prendreCartes(MAX_CARTES_MAIN);
    }

    public void prendreCarte() {
        prendreCartes(1);
    }

    public boolean peutPlacerCarte() {
        for (int i = 1; i <= table.getNbTuiles(); i++) {
            Tuile tuile = table.getTuile(i);

            for (Carte carte : cartes) {
                if (tuile.peutPoser(carte, Camp.A)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean placerCarte(int index, int tuileNumero, Camp camp) {
        Carte carte = cartes.get(index);
        Tuile tuile = table.getTuile(tuileNumero);

        if (tuile.peutPoser(carte, camp)) {
            cartes.remove(index);
            tuile.setCarte(carte, joueurId, camp);
            return true;
        }

        return false;
    }

    public Carte jeteCarte(int index) {
        Carte carte = cartes.remove(index);
        table.jeteCarte(carte);
        return carte;
    }

    public void jeteCartes(int nbCartes) {
        if (nbCartes <= 0 || nbCartes > MAX_CARTES_MAIN) {
            throw new IllegalArgumentException("tu ne peux pas jeter " + nbCartes + " cartes");
        }
        for (int i = 0; i < nbCartes; i++) {
            table.jeteCarte(cartes.remove(0));
        }
    }

    @Override
    public String toString() {
        return "Main (" + cartes.size() + ") : " + cartes;
    }
}
