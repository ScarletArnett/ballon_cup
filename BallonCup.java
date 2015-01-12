import java.util.*;

public class BallonCup {
    private Table table;
    private Joueur olive, tom;
    private Map<Couleur, Trophee> trophees = new HashMap<>();

    private Camp enCours, prochainCamp;

    public BallonCup(Table table) {
        this.table = table;
        this.table.placerCubes();

        this.olive = new Joueur(1, table);
        this.tom   = new Joueur(2, table);

        this.enCours = Camp.A;

        int numero = 3;
        for (Couleur couleur : Arrays.asList(Couleur.GRIS, Couleur.BLEU, Couleur.VERT, Couleur.JAUNE, Couleur.ROUGE)) {
            trophees.put(couleur, new Trophee(couleur, numero++));
        }
    }

    public Table getTable() {
        return table;
    }

    public Joueur getOlive() {
        return olive;
    }

    public Joueur getTom() {
        return tom;
    }

    public Joueur getJoueur(Camp camp) {
        switch (camp) {
            case A:  return getOlive();
            case B:  return getTom();
            default: throw new Error();
        }
    }

    public Joueur getJoueur(int id) {
        if (olive.getId() == id) {
            return olive;
        } else if (tom.getId() == id) {
            return tom;
        } else {
            throw new IllegalStateException(String.format("joueur id %d invalide", id));
        }
    }

    public Camp getCampDe(Joueur joueur) {
        if (joueur == olive) {
            return Camp.A;
        } else if (joueur == tom) {
            return Camp.B;
        } else {
            throw new IllegalStateException(joueur + " est inconnu");
        }
    }

    public Joueur getJoueurEnCours() {
        return getJoueur(enCours);
    }

    public Joueur getJoueurAdversaireEnCours() {
        return getJoueur(enCours.oppose());
    }

    public void passerTour() {
        if (prochainCamp != null) {
            enCours = prochainCamp;
            prochainCamp = null;
        } else {
            enCours = enCours.oppose();
        }
    }

    public void distribuerCubes() {
        for (int i = 1; i <= table.getNbTuiles(); i++) {
            Tuile tuile = table.getTuile(i);

            if (!tuile.estComplete()) continue;

            // calculer les scores
            int olivePoints = 0, tomPoints = 0;

            for (CartePlacee c : tuile.getCartePlacees()) {
                if (c.getJoueurId() == olive.getId()) {
                    olivePoints += c.getCarte().getNumero();
                } else if (c.getJoueurId() == tom.getId()) {
                    tomPoints += c.getCarte().getNumero();
                } else {
                    throw new IllegalStateException(String.format("joueur id %d invalide", c.getJoueurId()));
                }
            }

            // calculer le gagnant
            Camp campGagnant;

            if (olivePoints > tomPoints) {
                if (tuile.estMontagne()) {
                    campGagnant = Camp.A;
                } else {
                    campGagnant = Camp.B;
                }
            } else if (tomPoints > olivePoints) {
                if (tuile.estMontagne()) {
                    campGagnant = Camp.B;
                } else {
                    campGagnant = Camp.A;
                }
            } else {
                campGagnant = getCampDe(getJoueur(tuile.getDerniereCartePlacee().getJoueurId()));
            }

            // faire gagner
            Joueur gagnant = getJoueur(campGagnant);
            gagnant.gagneCubes(tuile.viderCubes());

            tuile.inverserMontagne();
            table.viderTuile(tuile);
            table.placerCubes(tuile);

            prochainCamp = campGagnant.oppose();
        }
    }

    public void distribuerTrophees() {
        if (trophees.isEmpty()) return;

        for (Camp camp : Camp.values()) {
            Joueur joueur = getJoueur(camp);

            for (Couleur couleur : Couleur.values()) {
                Trophee trophee = trophees.get(couleur);

                if (trophee == null) {
                    continue; // le trophée a déjà été gagné
                }

                List<Cube> cubes = joueur.getCubeGagnesParCouleur(couleur, trophee.getNumero());

                if (cubes.size() >= trophee.getNumero()) {
                    joueur.gagneTrophee(trophee);
                    joueur.enleveCubes(cubes);
                    table.replacerDansSac(cubes);

                    trophees.remove(couleur);
                }
            }
        }
    }

    public void distribuerCartes() {
        // les joueurs piochent à tour de role
        for (int i = 0; i < Main.MAX_CARTES_MAIN; i++) {
            olive.getMain().prendreCarte();
            tom.getMain().prendreCarte();
        }
    }

    public Joueur getJoueurGagnant() {
        if (olive.getNbTropheeGagnes() >= 3) {
            return olive;
        } else if (tom.getNbTropheeGagnes() >= 3) {
            return tom;
        }
        return null;
    }

    public boolean estTermine() {
        return getJoueurGagnant() != null;
    }
}
