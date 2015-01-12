public class Cube {
    private Couleur couleur;

    public Cube(Couleur couleur) {
        this.couleur = couleur;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    @Override
    public String toString() {
        return "Cube(" + couleur + ')';
    }
}
