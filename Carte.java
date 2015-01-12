public class Carte {
    private Couleur couleur;
    private int numero;

    public Carte(Couleur couleur, int numero) {
        this.couleur = couleur;
        this.numero = numero;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public int getNumero() {
        return numero;
    }

    @Override
    public String toString() {
        return "Carte(" + numero + ", " + couleur + ")";
    }
}
