package Model;

public class GameState {
    boolean turn = true;
    boolean gameOver = false;
    boolean winner = false;

    public GameState(){
    }

    public boolean gameOver(){
        return gameOver;
    }

    public void endGame(){
        this.gameOver = true;
    }

    public boolean playerOneWins(){
        return this.winner;
    }

    public void setWinner(boolean winner){
        this.winner = winner;
        endGame();
    }

    public boolean getTurn(){
        return turn;
    }

    public void endTurn(){
        turn = !turn;
    }
}
