import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BallonCupImages {

    private static Map<Couleur, Map<Integer, Image>> carteImages = new HashMap<>();
    private static Map<Couleur, Image> tropheeImages = new HashMap<>();
    private static java.util.List<Image[]> tuileImages = new ArrayList<>();
    private static Map<Couleur, Image> cubeImages = new HashMap<>();

    public static Image getCarteImage(Couleur couleur, int numero) {
        return carteImages.get(couleur).get(numero);
    }

    public static Image getTropheeImage(Couleur couleur) {
        return tropheeImages.get(couleur);
    }

    public static Image getTuileImage(int tuileNumero, boolean montagne) {
        return tuileImages.get(tuileNumero - 1)[montagne ? 1 : 0];
    }

    public static Image getCubeImage(Couleur couleur) {
        return cubeImages.get(couleur);
    }

    public static void loadAll() {
        loadCarteImages();
        loadTropheeImages();
        loadTuileImages();
        loadCubeImages();
    }

    public static void loadCarteImages() {
        try (BufferedReader reader = new BufferedReader(new FileReader(Table.CARTES_FILE))) {
            String content = reader.readLine();

            for (int i = 0; i < content.length(); i += 3) {
                String part = content.substring(i, i + 3);

                Couleur couleur = Couleur.getCouleur(part.charAt(0));
                int numero = Integer.parseInt(part.substring(1, 3), 10);

                String file = String.format("./img/carte_%d%s.jpg", numero, couleur.name());
                BufferedImage img = ImageIO.read(new File(file));

                Map<Integer, Image> images = carteImages.get(couleur);
                if (images == null) {
                    images = new HashMap<>();
                    carteImages.put(couleur, images);
                }
                images.put(numero, img);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTropheeImages(){
        for (Couleur couleur : Couleur.values()) {
            Image image = null;
            try {
                image = ImageIO.read(new File(String.format("./img/carte_Trophe%s.jpg", couleur)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            tropheeImages.put(couleur, image);
        }
    }

    public static void loadTuileImages(){
        try {
            for (int i = 1; i <= 4; i++) {
                Image[] img = new Image[2];
                img[0] = ImageIO.read(new File(String.format("./img/tuile%dPlaine.jpg", i)));
                img[1] = ImageIO.read(new File(String.format("./img/tuile%dMontagne.jpg", i)));
                tuileImages.add(img);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadCubeImages(){
        for (Couleur coul : Couleur.values()) {
            Image image = null;
            try {
                image = ImageIO.read(new File(String.format("./img/cube_%s.jpg", coul.name())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            cubeImages.put(coul, image);
        }
    }
}
