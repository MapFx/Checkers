package Controller;

import Model.*;
import java.util.ArrayList;
g
public class GameController {
    ArrayList<Pos> selectablePieces;
    ArrayList<Pos> pieceMoves;
    Pos selectedPiece;
    boolean captured;

    GameState gameState;
    Board board;

    public GameController(GameState gameState, Board board) {
        this.gameState = gameState;
        this.board = board;
        this.captured = false;
        this.pieceMoves = new ArrayList<>();
        this.selectablePieces = new ArrayList<>();
        this.selectedPiece = null;
    }

    // entry point to this class - basically every click it directs all the fun things.
    public void handleClick(int x, int y) {
        if(!gameState.gameOver()) {
            if (!captured) {
                getSelectablePieces(); //creates a list of which pieces you are allowed to select
                if (canSelect(x, y)) {
                    selectedPiece = new Pos(x, y);
                }
                if (selectedPiece != null) {
                    getMoves(selectedPiece.x(), selectedPiece.y());
                    if (canMoveTo(x, y)) {
                        board.movePiece(selectedPiece.x(), selectedPiece.y(), x, y);
                        if (Math.abs(selectedPiece.x() - x) == 2) {
                            captured = true;
                            getMoves(x, y);
                            if (!pieceMoves.isEmpty()) {
                                selectedPiece = new Pos(x, y);
                            } else {
                                endTurn();
                            }
                        } else {
                            endTurn();
                        }
                    }
                }
            } else {
                getMoves(selectedPiece.x(), selectedPiece.y());
                if (canMoveTo(x, y)) {
                    board.movePiece(selectedPiece.x(), selectedPiece.y(), x, y);
                    selectedPiece = new Pos(x, y);
                    getMoves(selectedPiece.x(), selectedPiece.y());
                }
                if (pieceMoves.isEmpty()) {
                    endTurn();
                }
            }
        }
        checkWinCondition();
    }

    // clears then populates the selectablePieces list for the turn
    public void getSelectablePieces() {
        selectablePieces.clear();
        for (int y = 0; y < board.getSize(); y++) {
            for (int x = 0; x < board.getSize(); x++) {
                if (teamCheck(x, y)) {
                    getMoves(x, y);
                    if (!pieceMoves.isEmpty()) {
                        selectablePieces.add(new Pos(x, y));
                    }
                }
            }
        }
    }

    // for the given piece, populates pieceMoves
    private void getMoves(int x, int y) {

        pieceMoves.clear();
        // only checks basic moves if you haven't already captured one this turn.
        if(!captured) {
            // -1 moves up, +1 moves down.
            int direction = gameState.getTurn() ? -1 : 1;

            // checks for the basic moves a piece can do (includes logic for king)
            if (inBounds(x + 1, y + direction) && isNull(x + 1, y + direction)) {
                pieceMoves.add(new Pos(x + 1, y + direction));
            }
            if (inBounds(x - 1, y + direction) && isNull(x - 1, y + direction)) {
                pieceMoves.add(new Pos(x - 1, y + direction));
            }
            if  (board.getPiece(x, y) != null && board.getPiece(x, y).isKing()) {
                if (inBounds(x + 1, y - direction) && isNull(x + 1, y - direction)) {
                    pieceMoves.add(new Pos(x + 1, y - direction));
                }
                if (inBounds(x - 1, y - direction) && isNull(x - 1, y - direction)) {
                    pieceMoves.add(new Pos(x - 1, y - direction));
                }
            }
        }
        // fill the remainder of the arraylist with potential capture moves.
        // will only fill this if captured == true (when you capture one checker)
        getCaptureMoves(x,y);

    }

    public ArrayList<Pos> getMoves(){
        if(selectedPiece != null) {
            getMoves(selectedPiece.x(), selectedPiece.y());
            return pieceMoves;
        } else {
            return new ArrayList<>();
        }
    }

    // for the given piece, populate pieceMoves with possible captures only.
    private void getCaptureMoves(int x, int y) {
        // -1 moves up, +1 moves down.
        int direction = gameState.getTurn() ? -2 : 2;

        // this gets called after getMoves so we need to clear those if we captured one.
        if(captured){
            pieceMoves.clear();
        }

        // check for captures and add them to the pieceMoves list.
        if(inBounds(x + 2,y + direction) && isNull(x + 2, y + direction)){
            if(checkMidForOpponent(new Pos(x,y), new Pos(x + 2, y + direction))){
                pieceMoves.add(new Pos(x + 2, y + direction));
            }
        }
        if(inBounds(x - 2,y + direction) && isNull(x - 2, y + direction)){
            if(checkMidForOpponent(new Pos(x,y), new Pos(x - 2, y + direction))){
                pieceMoves.add(new Pos(x - 2, y + direction));
            }
        }
        if(board.getPiece(x,y) != null && board.getPiece(x,y).isKing()){
            if(inBounds(x + 2,y - direction) && isNull(x + 2, y - direction)){
                if(checkMidForOpponent(new Pos(x,y), new Pos(x + 2, y - direction))){
                    pieceMoves.add(new Pos(x + 2, y - direction));
                }
            }
            if(inBounds(x - 2,y - direction) && isNull(x - 2, y - direction)){
                if(checkMidForOpponent(new Pos(x,y), new Pos(x - 2, y - direction))){
                    pieceMoves.add(new Pos(x - 2, y - direction));
                }
            }
        }
    }

    // check the middle point between coordinate 1 and 2.
    private Pos getMid(Pos p1, Pos p2){
        return new Pos((p1.x() + p2.x()) / 2, (p1.y() + p2.y()) / 2);
    }

    // simply checks if the piece between 2 other pieces is an opponent.
    private boolean checkMidForOpponent(Pos p1, Pos p2){
        return (board.getPiece(getMid(p1,p2)) != null &&
                board.getPiece(getMid(p1,p2)).getTeam() != gameState.getTurn());
    }

    // simply checks if a spot is empty.
    private boolean isNull (int x, int y){
        Piece p = board.getPiece(x,y);
        return p == null;
    }

    // checks if a move is valid
    private boolean canMoveTo(int x, int y){
        if(pieceMoves.isEmpty()){
            return false;
        }
        for(Pos p : pieceMoves){
            if(p.x() == x && p.y() == y){
                return true;
            }
        }
        return false;
    }

    // simply checks if the piece at x, y is the current players piece.
    private boolean teamCheck(int x, int y){
        if(!inBounds(x,y)){
            return false;
        }
        Piece p = board.getPiece(x,y);
        return p != null && (p.getTeam() == gameState.getTurn());
    }

    // simply checks if the x and y coordinates are in bounds.
    private boolean inBounds(int x, int y){
        return x >= 0 && x < board.getSize() && y >= 0 && y < board.getSize();
    }

    // simply check if a coordinate is present in selectablePieces list.
    private boolean canSelect(int x, int y) {
        if(selectablePieces.isEmpty()){
            return false;
        }
        for (Pos p : selectablePieces) {
            if (x == p.x() && y == p.y()) {
                return true;
            }
        }
        return false;
    }

    // I found myself using this all over so I just combined it.
    private void endTurn(){
        gameState.endTurn();
        selectedPiece = null;
        captured = false;
    }

    //counts the number of black and red pieces you have and picks the winner.
    private void checkWinCondition(){
        int redPieces = 0;
        int blackPieces = 0;

        for(int y = 0; y < board.getSize(); y++){
            for(int x = 0; x < board.getSize(); x++){
                if(board.getPiece(x,y) != null){
                    if(board.getPiece(x,y).getTeam()){
                        redPieces++;
                    } else {
                        blackPieces++;
                    }
                }
            }
        }
        if(redPieces == 0){
            gameState.setWinner(false);
        } else if (blackPieces == 0){
            gameState.setWinner(true);
        }
    }
}