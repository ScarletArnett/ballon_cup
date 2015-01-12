public enum Camp {
    A,
    B,
    ;

    public Camp oppose() {
        if (this == A) {
            return B;
        } else if (this == B) {
            return A;
        } else {
            throw new Error();
        }
    }
}
