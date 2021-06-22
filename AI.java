package tictactoe;

import java.util.Random;


public class AI extends Player{
    private final Difficulty difficulty;
    static private Board board;

    public AI(Difficulty difficulty, char player, Board board) {
        super(player);
        this.difficulty = difficulty;
        this.player = player;
        AI.board = board;
    }

    private int[] aiTurnEasy() {
        char[][] arr = board.getArr();
        Random random = new Random();
        int[] coordinates = new int[2];
        coordinates[0] = random.nextInt(3);
        coordinates[1] = random.nextInt(3);

        while (true) {
            if (arr[coordinates[0]][coordinates[1]] != ' ') {
                coordinates[0] = random.nextInt(3);
                coordinates[1] = random.nextInt(3) ;
            } else {
                return coordinates;
            }
        }
    }

    private int[] aiTurnMedium() {
        char[][] arr = board.getArr();
        int[] cord = new int[2];
        // checks for two in a row, vertical
        for ( int i = 0; i < Board.n; i++ ) {
            if (arr[i][1] == arr[i][0] && arr[i][1] != ' ' && arr[i][2] == ' ') { // first and second same
                cord[0] = i;
                cord[1] = 2;
                return cord;
            }
            if (arr[i][1] == arr[i][2] && arr[i][1] != ' ' && arr[i][0] == ' ') { // second and third same
                cord[0] = i;
                // cord[1] = 0
                return cord;
            }
            if (arr[i][0] == arr[i][2] && arr[i][2] != ' ' && arr[i][1] == ' ') { //  first and third same
                cord[0] = i;
                cord[1] = 1;
                return cord;
            }
        }

        // checks for two in a row, horizontal
        for ( int i = 0; i < Board.n; i++ ) {
            if (arr[1][i] == arr[0][i] && arr[1][i] != ' ' && arr[2][i] == ' ') { // first and second same
                cord[0] = 2;
                cord[1] = i;
                return cord;
            }
            if (arr[1][i] == arr[2][i] && arr[1][i] != ' ' && arr[0][i] == ' ') { // second and third same
                // cord[0] = 0;
                cord[1] = i;
                return cord;
            }
            if (arr[0][i] == arr[2][i] && arr[2][i] != ' ' && arr[1][i] == ' ') { //  first and third same
                cord[0] = 1;
                cord[1] = i;
                return cord;
            }
        }

        return diagonals(arr, cord);
    }

    private int[] diagonals(char[][] arr, int[] cord) {
        // upper-left to lower-right
        if (arr[1][1] == arr[0][0] && arr[0][0] != ' ' && arr[2][2] == ' ') { // first and second same
                cord[0] = 2;
                cord[1] = 2;
                return cord;
        }
        if (arr[1][1] == arr[2][2] && arr[2][2] != ' ' && arr[0][0] == ' ') { // second and third same
                cord[0] = 0;
                cord[1] = 0;
                return cord;
        }
        if (arr[0][0] == arr[2][2] && arr[2][2] != ' ' && arr[1][1] == ' ') { //  first and third same
            cord[0] = 1;
            cord[1] = 1;
            return cord;
        }

        //upper-right to lower-left
        if (arr[2][0] == arr[1][1] && arr[1][1] != ' ' && arr[0][2] == ' ') { // first and second same
                cord[0] = 0;
                cord[1] = 2;
                return cord;
        }
        if (arr[1][1] == arr[0][2] && arr[0][2] != ' ' && arr[2][0] == ' ') { // second and third same
                cord[0] = 2;
                cord[1] = 0;
                return cord;
        }
        if (arr[2][0] == arr[0][2] && arr[0][2] != ' ' && arr[1][1] == ' ') { //  first and third same
                cord[0] = 1;
                cord[1] = 1;
                return cord;
        }

        return aiTurnEasy();
    }

    private int[] aiTurnHard() {
        int[] tempRes = new int[2];
        char[][] tempArr = board.getArr().clone();
        int bestMove = -2;

        char min;
        char max;
        if (player == 'X') {
            max = 'X';
            min = 'O';
        } else {
            max = 'O';
            min = 'X';
        }

        // finding number of empty fields
        int emptySpaces = 0;
        for ( int i = 0; i < Board.n; i++ ) {
            for ( int j = 0; j < Board.n; j++ ) {
                if ( tempArr[i][j] == ' ') {
                    emptySpaces++;
                }
            }
        }
        if ( emptySpaces == 9) {
            return new int[] {2, 2}; // best first spot is in the middle
        }


        for ( int i = 0; i < Board.n; i++ ){
            for ( int j = 0; j < Board.n; j++ ) {
                if (tempArr[i][j] == ' ') {
                    tempArr[i][j] = max;
                    int tResult = alphaBeta(tempArr, min, emptySpaces - 1, -2, 2);
                    tempArr[i][j] = ' ';
                    if (tResult > bestMove) {
                        bestMove = tResult;
                        tempRes[0]= i;
                        tempRes[1]= j;
                    }
                }
            }
        }
        return tempRes;
    }

    private int alphaBeta(char[][] tempArr, char actPlayer, int emptySpaces, int alpha, int beta){
        char enemy;
        if (actPlayer == 'X') {
            enemy = 'O';
        } else {
            enemy = 'X';
        }
        boolean tempWinner;
        tempWinner = Board.threeInARow(tempArr, 'X') || Board.threeInARow(tempArr, 'O');

        // stop when somebody wins, draw
        if (emptySpaces == 0 || tempWinner) {
            switch (board.determineStatus(tempArr)) {
                case X_WINS:
                    if (player == 'O') {
                        return -1;
                    } else {
                        return 1;
                    }
                case O_WINS:
                    if (player == 'O') {
                        return 1;
                    } else {
                        return -1;
                    }
                case DRAW:
                    return 0;
                default:
                    return -2;
            }
        }


        for ( int i = 0; i < Board.n; i++ ) {
            for ( int j = 0; j < Board.n; j++ ) {
                if ( tempArr[i][j] == ' ' ) {
                    tempArr[i][j] = actPlayer;
                    int tResult = alphaBeta(tempArr, enemy,emptySpaces-1,alpha,beta);
                    tempArr[i][j] = ' ';
                    if (player == actPlayer) {
                        if (tResult > alpha) {
                            alpha = tResult;
                        }
                        if (alpha >= beta) {
                            return beta;
                        }
                    } else {
                        if (tResult < beta) {
                            beta = tResult;
                        }
                        if (beta <= alpha) {
                            return alpha;
                        }
                    }
                }
            }
        }

        if (player == actPlayer) {
            return alpha;
        } else {
            return beta;
        }
    }

    public int[] aiTurn() {
        switch (difficulty) {
            case EASY:
                return aiTurnEasy();
            case HARD:
                return aiTurnHard();
            default:
                return aiTurnMedium();
        }
    }

    @Override
    public void makeMove() {
        TicTacToe.aiButton(aiTurn());
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

}
