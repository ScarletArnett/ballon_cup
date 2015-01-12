public enum Couleur {
    ROUGE,
    VERT,
    BLEU,
    JAUNE,
    GRIS,
    ;

    public char toChar() {
        switch (this) {
            case ROUGE:
                return 'R';
            case VERT:
                return 'V';
            case BLEU:
                return 'B';
            case JAUNE:
                return 'J';
            case GRIS:
                return 'G';
            default: throw new Error("ne doit pas arriver");
        }
    }

    public static Couleur getCouleur(char c) {
        switch (c) {
            case 'R':
                return ROUGE;
            case 'V':
                return VERT;
            case 'B':
                return BLEU;
            case 'J':
                return JAUNE;
            case 'G':
                return GRIS;
            default:
                return null;
        }
    }
}
