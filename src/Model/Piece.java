package Model;
import java.awt.Color;

public class Piece {
    private boolean team;
    private boolean isKing;
    private Color color;
    private Pos position;

    public Piece(boolean team, int x, int y){
        this.team = team;
        this.isKing = false;
        this.position = new Pos(x, y);
        color = team ? Color.RED : Color.BLACK;
    }

    public void promotion(){
        this.isKing = true;
    }

    public Pos getPos() {
        return position;
    }

    public void setPos(Pos p) {
        this.position = p;
    }

    public void setPos(int x, int y) {
        this.position = new Pos(x, y);
    }

    public boolean getTeam(){
        return this.team;
    }

    public Color getColor(){
        return this.color;
    }

    public boolean isKing(){
        return this.isKing;
    }
}
