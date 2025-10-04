package Model;

public class Pos {
    private int x;
    private int y;

    public Pos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Pos p){
        return this.x == p.x && this.y == p.y;
    }
    public int x(){
        return this.x;
    }
    public int y(){
        return this.y;
    }
}
