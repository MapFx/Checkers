package Model;

public class Board {
    Piece[][] board;
    int size;
    public Board(int size) {
        this.board = new Piece[size][size];
        this.size = size;
        initialize(size);
    }

    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    public Piece getPiece(Pos p){
        return getPiece(p.x(), p.y());
    }

    public int getSize() {
        return board.length;
    }

    public Piece[][] getBoard() {
        return board;
    }

    public void movePiece(Pos p1, Pos p2){
        movePiece(p1.x(), p1.y(), p2.x(), p2.y());
    }

    public void movePiece(int x1, int y1, int x2, int y2){
        this.board[x2][y2] = this.board[x1][y1];
        this.board[x1][y1] = null;
        this.board[x2][y2].setPos(x2,y2);
        // king handling
        int k = this.board[x2][y2].getTeam() ? 0 : this.getSize()-1;
        if(y2 == k){
            this.board[x2][y2].promotion();
        }

        // capture handling
        if(x2-x1 == -2 || x2-x1 == 2){
            board[(x1+x2)/2][(y1+y2)/2] = null;
        }
    }

    public void initialize(int size){
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if((x + y) % 2 == 0){
                    if(y < 3){
                        this.board[x][y] = new Piece(false,x,y);
                    } else if (y > 4){
                        this.board[x][y] = new Piece(true,x,y);
                    }
                }
            }
        }
    }
}
