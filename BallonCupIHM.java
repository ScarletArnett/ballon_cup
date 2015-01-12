import java.awt.*;

public class BallonCupIHM extends Frame {
    private final Table table;
    private BallonCup bc;

    public BallonCupIHM(){
        table = new Table();
        bc = new BallonCup(table);
    }



}