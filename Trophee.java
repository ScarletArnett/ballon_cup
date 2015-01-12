public class Trophee {
    private Couleur couleur;
    private int numero;

    public Trophee(Couleur couleur, int numero) {
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
        return "Trophee(" + couleur + ", " + numero + ')';
    }
}
