import java.util.LinkedList;
import java.util.List;

public class Tuile {
    private boolean estMontagne;
    private int numero;
    private List<Cube> cubes = new LinkedList<>();

    private List<CartePlacee> campA = new LinkedList<>();
    private List<CartePlacee> campB = new LinkedList<>();
    private CartePlacee derniereCartePlacee;

    public Tuile(boolean estMontagne, int numero) {
        this.estMontagne = estMontagne;
        this.numero = numero;
    }

    public boolean estMontagne() {
        return estMontagne;
    }

    public void inverserMontagne() {
        estMontagne = !estMontagne;
    }

    public int getNumero() {
        return numero;
    }

    public CartePlacee getDerniereCartePlacee() {
        return derniereCartePlacee;
    }

    public List<Cube> getCubes() {
        return cubes;
    }

    public void addCube(Cube cube) {
        this.cubes.add(cube);
    }

    public List<Cube> viderCubes() {
        List<Cube> cpy = new LinkedList<>(cubes);
        cubes.clear();
        derniereCartePlacee = null;
        return cpy;
    }

    public void setCarte(Carte carte, int joueurId, Camp camp) {
        List<CartePlacee> campCartes;
        switch (camp) {
            case A:
                campCartes = campA;
                break;
            default:
                campCartes = campB;
                break;
        }

        CartePlacee cartePlacee = new CartePlacee(carte, joueurId);
        campCartes.add(cartePlacee);
        this.derniereCartePlacee = cartePlacee;
    }

    public List<CartePlacee> getCartePlacees(Camp camp) {
        switch (camp) {
            case A: return campA;
            case B: return campB;
            default: throw new Error();
        }
    }

    public List<CartePlacee> getCartePlacees() {
        List<CartePlacee> res = new LinkedList<>();
        res.addAll(campA);
        res.addAll(campB);
        return res;
    }

    public List<Carte> vider() {
        LinkedList<Carte> res = new LinkedList<>();

        // vide le premier camp
        while (!campA.isEmpty()) {
            res.add(campA.remove(0).getCarte());
        }

        // vide le second camp
        while (!campB.isEmpty()) {
            res.add(campB.remove(0).getCarte());
        }

        return res;
    }

    public boolean estComplete() {
        return campA.size() == numero &&
               campB.size() == numero ;
    }

    public int getNbCubeDeCouleur(Couleur couleur) {
        int res = 0;
        for (Cube cube : cubes) {
            if (cube.getCouleur() == couleur) {
                res++;
            }
        }
        return res;
    }

    public int getNbCartePoseeDeCouleur(Couleur couleur, Camp camp) {
        int res = 0;
        for (CartePlacee c : getCartePlacees(camp)) {
            if (c.getCarte().getCouleur() == couleur) {
                res++;
            }
        }
        return res;
    }

    public boolean peutPoser(Carte carte, Camp camp) {
        return getNbCartePoseeDeCouleur(carte.getCouleur(), camp) < getNbCubeDeCouleur(carte.getCouleur());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Tuile ").append(numero).append(' ');

        for (CartePlacee c : campA) {
            sb.append(c).append(' ');
        }
        sb.append('|');

        for (CartePlacee c : campB) {
            sb.append(' ').append(c);
        }

        return sb.toString();
    }
}
