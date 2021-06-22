package tictactoe;

import javax.swing.*;
import java.util.Arrays;


public class Board extends JPanel {
    public static final int n = 3;
    private final char[][] arr = new char[n][n];
    private Status status = Status.NOT_STARTED;
    private char turn = 'X';

    public Board() {
        super();
        clearBoard();
    }

    public void clearBoard() {
        for (char[] c : arr) {
            Arrays.fill(c ,' ');
        }
        status = Status.NOT_STARTED;
        turn = 'X';
    }

    public Status getStatus() {
        status = detSt(arr);
        return status;
    }

    public Status determineStatus(char[][] field) {
        return detSt(field);
    }

    private Status detSt(char[][] field) {
        boolean b_ = false;
        boolean b3X;
        boolean b3O;
        boolean X_O = false;


        // checks for empty cells
        for (int k = 0; k < n; k++) {
            for (int l = 0; l < n; l++) {
                if (field[k][l] == ' ') {
                    b_ = true;
                    break;
                }
            }
            if (b_) {
                break;
            }
        }

        // Checks for 3 X
        b3X = threeInARow(field, 'X');

        // Checks for 3 O
        b3O = threeInARow(field, 'O');
        int j;

        //counts Xs and Os
        int Os = 0;
        int Xs = 0;
        for (int i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (field[i][j] == 'O') {
                    Os ++;
                } else if (field[i][j] == 'X') {
                    Xs++;
                }
            }
        }

        //checks for an impossible number of Xs and Os
        if (Xs > Os + 1 || Os > Xs + 1) {
            X_O = true;
        }

        if (X_O) {
            return Status.IMPOSSIBLE;
        } else if (b3X) {
            if (b3O) {
                return Status.IMPOSSIBLE;
            } else {
                return Status.X_WINS;
            }
        } else if (b3O) {
            return Status.O_WINS;
        } else if (b_) {
            return Status.UNFINISHED;
        } else {
            return Status.DRAW;
        }
    }

    public static boolean threeInARow(char[][] arr, char o) {
        int j = 0;
        boolean b3O = false;
        for ( int i = 0; i < n; i++ ) {
            if ((arr[i][j] == o && arr[i][j + 1] == o && arr[i][j + 2] == o) ||
                    ((arr[j][i] == o && arr[j + 1][i] == o && arr[j + 2][i] == o))) {
                b3O = true;
                break;
            }
        }
        if ((arr[0][0] == o && arr[1][1] == o && arr[2][2] == o) ||
                ((arr[0][2] == o && arr[1][1] == o && arr[2][0] == o))) {
            b3O = true;
        }
        return b3O;
    }

    public void setCell(int i, int j, char symbol) {
        arr[i][j] = symbol;
    }

    public char getCell(int i, int j) {
        return arr[i][j];
    }

    public void updateTurn() {
        if (turn == 'O') {
            turn = 'X';
        } else {
            turn = 'O';
        }
    }

    public char getTurn() {
        return turn;
    }

    public char[][] getArr() {
        return arr;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        O_WINS,X_WINS,DRAW,IMPOSSIBLE,UNFINISHED,NOT_STARTED
    }

}


