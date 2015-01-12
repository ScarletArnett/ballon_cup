public class CartePlacee {
    private Carte carte;
    private int joueurId;

    public CartePlacee(Carte carte, int joueurId) {
        this.carte = carte;
        this.joueurId = joueurId;
    }

    public Carte getCarte() {
        return carte;
    }

    public int getJoueurId() {
        return joueurId;
    }

    @Override
    public String toString() {
        return carte.toString() + " du joueur " + joueurId;
    }
}
