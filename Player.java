package tictactoe;

abstract class Player {
    char player;
    public Player(char player) {
        this.player = player;
    }
    abstract public void makeMove();
}
