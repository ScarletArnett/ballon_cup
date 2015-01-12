import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Controleur extends Frame implements MouseListener {
    private Image dos, troll;
    private Table table;
    private BallonCup jeu;
    private String message = "Choisissez une carte";

    private int currentCarteIndex = -1;
    private Image carteDefaussee;

    public Controleur()throws Exception{
        setSize(1920, 1040);
        setTitle("Ballon Cup");
        setVisible(true);

        try {
            dos = ImageIO.read(new File("./img/carte_DOS.jpg"));
            troll = ImageIO.read(new File("troll-face.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        table = new Table();
        jeu = new BallonCup(table);
        jeu.distribuerCartes();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addMouseListener(this);

        //Musique
        String gongFile = "zic.wav";
        InputStream in = new FileInputStream(gongFile);
        AudioStream audioStream = new AudioStream(in);
        AudioPlayer.player.start(audioStream);

        addMouseListener(this);
    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        // background
//        for (int i = 0; i < 1920; i += 640) {
//            for (int j = 0; j < 1080; j += 640) {
//                g.drawImage(troll, i, j, null);
//            }
//        }

        // pioche
        g.drawImage(dos, 10, 30, null);
        g.drawString("" + table.getNbPioche(), 10 + 119 + 5, 30 + 168);

        //défausse
        g.drawImage(dos, 1920 - 10 - 119, 30, null);
        if (carteDefaussee != null) {
            g.drawImage(carteDefaussee, 1920 - 10 - 119, 30, null);
        }

        // tuiles
        for (int i = 0; i < table.getNbTuiles(); i++) {
            int x = (1920 - 190) / 2;
            int y = 30 + i * 186;

            Tuile tuile = table.getTuile(i + 1);

            //tuile
            g.drawImage(BallonCupImages.getTuileImage(tuile.getNumero(), tuile.estMontagne()), x, y, null);

            //campB
            java.util.List<CartePlacee> campB = tuile.getCartePlacees(Camp.B);
            for (int j = 0; j < tuile.getNumero(); j++) {
                int x1 = x + 190 + 119 * j;
                if (j < campB.size()) {
                    CartePlacee carte = campB.get(j);
                    g.drawImage(BallonCupImages.getCarteImage(carte.getCarte().getCouleur(), carte.getCarte().getNumero()), x1, y, null);
                } else {
                    g.drawRect(x1, y, 119, 186);
                    //g.drawString("slot", x1 + 5, y + 20);
                }
            }

            //campA
            java.util.List<CartePlacee> campA = tuile.getCartePlacees(Camp.A);
            for (int j = 0; j < tuile.getNumero(); j++) {
                int x1 = x - 119 - 119 * j;
                if (j < campA.size()) {
                    CartePlacee carte = campA.get(j);
                    g.drawImage(BallonCupImages.getCarteImage(carte.getCarte().getCouleur(), carte.getCarte().getNumero()), x1, y, null);
                } else {
                    g.drawRect(x1, y, 119, 186);
                    //g.drawString("slot", x1 + 5, y + 20);
                }
            }

            //cubes
            for (int j = 0; j < tuile.getCubes().size(); j++) {
                g.drawImage(BallonCupImages.getCubeImage(tuile.getCubes().get(j).getCouleur()), x + (190 * 2 / 3) - 20 - 20 * j, y + (186 / 2) - 19, null);
            }
        }

        // main
        List<Carte> cartes = jeu.getJoueurEnCours().getMain().getCartes();
        for (int i = 0; i < cartes.size(); i++) {
            Carte carte = cartes.get(i);
            int x = 1920 / 2 - (4 - i) * 119;
            int y = 1040 - 10 - 186;
            g.drawImage(BallonCupImages.getCarteImage(carte.getCouleur(), carte.getNumero()), x, y, null);
            /*g.drawString("" + i, x, y);*/
        }

        // trophés gagnés
        List<Trophee> trophee = jeu.getJoueurEnCours().getTropheeGagnes();
        for (int i = 0; i < trophee.size(); i++) {
            g.drawImage(BallonCupImages.getTropheeImage(trophee.get(i).getCouleur()), 10, 30 + 236 + 253 * i, null);
        }

        // cubes gagnés
        List<Cube> cubes = jeu.getJoueurEnCours().getCubeGagnes();
        for (int i = 0; i < cubes.size(); i++) {
            g.drawImage(BallonCupImages.getCubeImage(cubes.get(i).getCouleur()), 10 + 190, 30 + 236 + 19 * i, null);
        }

        // cubes de l'adversaire
        List<Cube> cubeAdversaires = jeu.getJoueurAdversaireEnCours().getCubeGagnes();
        for (int i = 0; i < cubeAdversaires.size(); i++) {
            g.drawImage(BallonCupImages.getCubeImage(cubeAdversaires.get(i).getCouleur()), 1920 - 10 - 20, 30 + 236 + 19 * i, null);
        }

        // trophés de l'adversaire
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Avancement de l'adversaire", 1530, 300);
        g.drawString(String.format("- %2d trophés", jeu.getJoueurAdversaireEnCours().getTropheeGagnes().size()), 1710, 330);
        g.drawString(String.format("- %2d cubes", jeu.getJoueurAdversaireEnCours().getCubeGagnes().size()), 1710, 360);

        // message
        g.drawString("À ton tour, joueur " + jeu.getJoueurEnCours().getId(), 200, 70);
        g.drawString(message, 200, 100);
    }

    private boolean estDansZoneMain(Point pt) {
        return pt.x >= (1920 / 2 - 4 * 119) &&
               pt.x <= (1920 / 2 - (4 - 8) * 119) &&
               pt.y <= (1040 - 10) &&
               pt.y >= (1040 - 10 - 186);
    }

    private int getIndexMain(Point pt) {
        return 7 - (int) ((pt.x - 1920 / 2) / -119.0 + 4);
    }

    private boolean estDansZoneSlots(Point pt) {
        for (int i = 0; i < table.getNbTuiles(); i++) {
            int x = (1920 - 190) / 2;
            int y = 30 + i * 186;
            boolean res = pt.x >= x - 119 * (i + 1) &&
                   pt.x <= x + 190 + 119 * (i + 1) &&
                   pt.y >= y &&
                   pt.y <= y + 186;

            if (res) return true;
        }
        return false;
    }

    private int getTuileNumero(Point pt) {
        return (pt.y - 30) / 186 + 1;
    }

    private Camp getCamp(Point pt) {
        if (pt.x < (1920 - 190) / 2) {
            return Camp.A;
        } else {
            return Camp.B;
        }
    }

    private boolean estDansZoneDefausse(Point pt) {
        return pt.y >= 30 &&
               pt.y <= 30 + 236 &&
               pt.x >= 1920 - 10 - 160 &&
               pt.x <= 1920 - 10;
    }

    private void erreur(String title, String message) {
        JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
    }

    private void finTour() {
        jeu.distribuerCubes();
        jeu.distribuerTrophees();
        jeu.passerTour();

        repiocheSiBesoin();
    }

    private void repiocheSiBesoin() {
        final Joueur joueur = jeu.getJoueurEnCours();

        if (!joueur.getMain().peutPlacerCarte()) {
            RepiocheFrame frame = new RepiocheFrame();

            frame.addPropertyChangeListener("value", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    int jete = (Integer) evt.getNewValue();

                    joueur.getMain().jeteCartes(jete);
                    joueur.getMain().prendreCartes(jete);

                    if (!joueur.getMain().peutPlacerCarte()) {
                        erreur("Erreur", "Tu ne peux toujours pas jouer, tu passes ton tour...");
                    }
                }
            });
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (estDansZoneMain(e.getPoint())) {
            currentCarteIndex = getIndexMain(e.getPoint());
            Carte carte = jeu.getJoueurEnCours().getMain().getCartes().get(currentCarteIndex);
            message = String.format("Vous avez sélectionné la carte %d %s", carte.getNumero(), carte.getCouleur().name().toLowerCase());
        } else if (estDansZoneSlots(e.getPoint())) {
            if (currentCarteIndex == 1) {
                message = "Vous devez choisir une carte";
            } else {
                int tuileNumero = getTuileNumero(e.getPoint());
                Camp camp = getCamp(e.getPoint());

                if (jeu.getJoueurEnCours().getMain().placerCarte(currentCarteIndex, tuileNumero, camp)) {
                    jeu.getJoueurEnCours().getMain().prendreCarte();
                    finTour();
                    message = "";
                } else {
                    message = "Vous ne pouvez pas poser cette carte ici";
                }

                currentCarteIndex = -1;
            }
        } else if (estDansZoneDefausse(e.getPoint())) {
            if (currentCarteIndex == -1) {
                message = "Vous devez choisir une carte";
            } else {
                Carte defaussee = jeu.getJoueurEnCours().getMain().jeteCarte(currentCarteIndex);
                carteDefaussee = BallonCupImages.getCarteImage(defaussee.getCouleur(), defaussee.getNumero());

                jeu.getJoueurEnCours().getMain().prendreCarte();
                if (table.remettrePiocheSiVide()) {
                    carteDefaussee = null;
                }

                currentCarteIndex = -1;
            }
        }

        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public static void main(String[] args ) throws Exception {
        setSystemLookAndFeel();
        BallonCupImages.loadAll();
        new Controleur();
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            throw new Error(e);
        }
    }

}
