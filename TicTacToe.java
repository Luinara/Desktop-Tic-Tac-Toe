package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TicTacToe extends JFrame {
    static Board jpGameBoard;
    static JLabel jlbStatus;
    static Cell A1;
    static Cell A2;
    static Cell A3;
    static Cell B1;
    static Cell B2;
    static Cell B3;
    static Cell C1;
    static Cell C2;
    static Cell C3;
    static JButton btnP1;
    static JButton btnP2;
    static JButton btnReset;
    static Player p1;
    static Player p2;

    Timer AvATimer;
    int gameCounter = 0;
    static GameMode gameMode = GameMode.PvP;
    static AI.Difficulty ai1Difficulty = AI.Difficulty.MEDIUM;
    static AI.Difficulty ai2Difficulty = AI.Difficulty.MEDIUM;

    public TicTacToe() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe");
        setSize(450, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        initLayout();
        setVisible(true);
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        {
            jpGameBoard = new Board();
            jpGameBoard.setBounds(0, 0, 450, 450);
            jpGameBoard.setLayout(new GridLayout(3, 3));
            {
                A1 = new Cell("ButtonA1", " ");
                A2 = new Cell("ButtonA2", " ");
                A3 = new Cell("ButtonA3", " ");
                B1 = new Cell("ButtonB1", " ");
                B2 = new Cell("ButtonB2", " ");
                B3 = new Cell("ButtonB3", " ");
                C1 = new Cell("ButtonC1", " ");
                C2 = new Cell("ButtonC2", " ");
                C3 = new Cell("ButtonC3", " ");

                A1.setEnabled(false);
                A2.setEnabled(false);
                A3.setEnabled(false);
                B1.setEnabled(false);
                B2.setEnabled(false);
                B3.setEnabled(false);
                C1.setEnabled(false);
                C2.setEnabled(false);
                C3.setEnabled(false);

                jpGameBoard.add(A3);
                jpGameBoard.add(B3);
                jpGameBoard.add(C3);
                jpGameBoard.add(A2);
                jpGameBoard.add(B2);
                jpGameBoard.add(C2);
                jpGameBoard.add(A1);
                jpGameBoard.add(B1);
                jpGameBoard.add(C1);
            }
            add(jpGameBoard, BorderLayout.CENTER);

            JPanel jpToolBar = new JPanel();
            jpToolBar.setLayout(new BorderLayout());
            jpToolBar.setBounds(0, 450, 450, 50);
            jpToolBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            {
                jlbStatus = new JLabel();
                jlbStatus.setText("Game is not started");
                jlbStatus.setName("LabelStatus");
                jpToolBar.add(jlbStatus, BorderLayout.WEST);
                {
                    JPanel jpButtonMenu = new JPanel();
                    jpButtonMenu.setLayout(new FlowLayout());
                    jpButtonMenu.setBounds(0, 300, 300, 50);

                    btnReset = new JButton("Start");
                    btnReset.setName("ButtonStartReset");

                    btnP1 = new JButton("Human");
                    btnP1.setName("ButtonPlayer1");

                    btnP2 = new JButton("Human");
                    btnP2.setName("ButtonPlayer2");
                    jpButtonMenu.add(btnP1);
                    jpButtonMenu.add(btnP2);
                    jpButtonMenu.add(btnReset);

                    jpToolBar.add(jpButtonMenu, BorderLayout.EAST);
                }
            }
            add(jpToolBar, BorderLayout.SOUTH);

            AvATimer = new Timer(500, null);
            AvATimer.addActionListener(actionEvent1 -> {
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (jpGameBoard.getTurn() == 'X') {
                        p1.makeMove();
                    } else {
                        p2.makeMove();
                    }
                } else {
                    AvATimer.stop();
                }
            });
            AvATimer.setInitialDelay(0);

            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);

            JMenu gameMenu = new JMenu("Game");
            gameMenu.setName("MenuGame");
            gameMenu.setMnemonic(KeyEvent.VK_F);
            menuBar.add(gameMenu);
            {
                JMenuItem PvP = new JMenuItem("Human vs Human");
                JMenuItem PvA = new JMenuItem("Human vs Robot");
                JMenuItem AvP = new JMenuItem("Robot vs Human");
                JMenuItem AvA = new JMenuItem("Robot vs Robot");
                JMenuItem Exit = new JMenuItem("Exit");

                PvP.setName("MenuHumanHuman");
                PvA.setName("MenuHumanRobot");
                AvA.setName("MenuRobotRobot");
                AvP.setName("MenuRobotHuman");
                Exit.setName("MenuExit");

                gameMenu.add(PvP);
                gameMenu.add(PvA);
                gameMenu.add(AvP);
                gameMenu.add(AvA);
                gameMenu.addSeparator();
                gameMenu.add(Exit);

                Exit.addActionListener(actionEvent -> System.exit(0));

                PvP.addActionListener(actionEvent -> {
                    reset();
                    jpGameBoard.setStatus(Board.Status.UNFINISHED);
                    gameMode = GameMode.PvP;

                    p1 = new User('X');
                    p2 = new User('O');

                    btnP1.setText("Human");
                    btnP2.setText("Human");

                    startButtons();
                });

                PvA.addActionListener(actionEvent -> {
                    reset();
                    jpGameBoard.setStatus(Board.Status.UNFINISHED);
                    gameMode = GameMode.PvA;

                    p1 = new User('X');
                    p2 = new AI(ai2Difficulty, 'O',jpGameBoard);

                    btnP1.setText("Human");
                    btnP2.setText("Robot");

                    startButtons();

                    p1.makeMove();
                });

                AvP.addActionListener(actionEvent -> {
                    reset();
                    jpGameBoard.setStatus(Board.Status.UNFINISHED);
                    gameMode = GameMode.AvP;

                    p1 = new AI(ai1Difficulty, 'X',jpGameBoard);
                    p2 = new User('O');

                    btnP1.setText("Robot");
                    btnP2.setText("Human");

                    startButtons();

                    p1.makeMove();
                });

                AvA.addActionListener(actionEvent -> {
                    reset();
                    jpGameBoard.setStatus(Board.Status.UNFINISHED);
                    gameMode = GameMode.AvA;

                    p1 = new AI(ai1Difficulty, 'X',jpGameBoard);
                    p2 = new AI(ai2Difficulty, 'O',jpGameBoard);

                    btnP1.setText("Robot");
                    btnP2.setText("Robot");

                    startButtons();

                    AvATimer.start();
                });
            }

            JMenu difMenu = new JMenu("AI");
            gameMenu.setMnemonic(KeyEvent.VK_F);
            menuBar.add(difMenu);
            {
                JMenuItem easy1 = new JMenuItem("AI 1 Difficulty: Easy");
                JMenuItem medium1 = new JMenuItem("AI 1 Difficulty: Medium");
                JMenuItem Hard1 = new JMenuItem("AI 1 Difficulty: Hard");
                JMenuItem easy2 = new JMenuItem("AI 2 Difficulty: Easy");
                JMenuItem medium2 = new JMenuItem("AI 2 Difficulty: Medium");
                JMenuItem Hard2 = new JMenuItem("AI 2 Difficulty: Hard");

                difMenu.add(easy1);
                difMenu.add(medium1);
                difMenu.add(Hard1);
                difMenu.addSeparator();
                difMenu.add(easy2);
                difMenu.add(medium2);
                difMenu.add(Hard2);

                easy1.addActionListener(actionEvent -> ai1Difficulty = AI.Difficulty.EASY);
                easy2.addActionListener(actionEvent -> ai2Difficulty = AI.Difficulty.EASY);
                medium1.addActionListener(actionEvent -> ai1Difficulty = AI.Difficulty.MEDIUM);
                medium2.addActionListener(actionEvent -> ai2Difficulty = AI.Difficulty.MEDIUM);
                Hard1.addActionListener(actionEvent -> ai1Difficulty = AI.Difficulty.HARD);
                Hard2.addActionListener(actionEvent -> ai2Difficulty = AI.Difficulty.HARD);
            }
        }

        A1.addActionListener(actionEvent -> {
            if (A1.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(2, 0, A1);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if ( gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });
        A2.addActionListener(actionEvent -> {
            if (A2.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(1, 0, A2);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if ( gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });
        A3.addActionListener(actionEvent -> {
            if (A3.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(0, 0, A3);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if ( gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });

        B1.addActionListener(actionEvent -> {
            if (B1.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(2, 1, B1);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if ( gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });
        B2.addActionListener(actionEvent -> {
            if (B2.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(1, 1, B2);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if ( gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });
        B3.addActionListener(actionEvent -> {
            if (B3.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(0, 1, B3);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if (gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });

        C1.addActionListener(actionEvent -> {
            if (C1.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(2, 2, C1);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if (gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });
        C2.addActionListener(actionEvent -> {
            if (C2.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(1, 2, C2);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if (gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });
        C3.addActionListener(actionEvent -> {
            if (C3.getText().equals(" ")) {
                if (gameMode != GameMode.AvA) btn_click(0, 2, C3);
                if (jpGameBoard.getStatus() == Board.Status.UNFINISHED) {
                    if (gameMode == GameMode.PvA) p2.makeMove();
                    if (gameMode == GameMode.AvP) p1.makeMove();
                }
            }
        });

        btnP1.addActionListener(actionEvent -> {
            if (btnP1.getText().equals("Human")) {
                btnP1.setText("Robot");
            } else {
                btnP1.setText("Human");
            }
        });

        btnP2.addActionListener(actionEvent -> {
            if (btnP2.getText().equals("Human")) {
                btnP2.setText("Robot");
            } else {
                btnP2.setText("Human");
            }
        });

        btnReset.addActionListener(actionEvent -> {
            if (btnReset.getText().equals("Start")){
                jpGameBoard.setStatus(Board.Status.UNFINISHED);

                if (btnP1.getText().equals("Robot")) {
                    if (btnP2.getText().equals("Robot")) {
                        gameMode = GameMode.AvA;
                    } else {
                        gameMode = GameMode.AvP;
                    }
                } else {
                    if (btnP2.getText().equals("Robot")) {
                        gameMode = GameMode.PvA;
                    } else {
                        gameMode = GameMode.PvP;
                    }
                } // init gameMode

                switch (gameMode) {
                    case AvA:
                        p1 = new AI(ai1Difficulty, 'X',jpGameBoard);
                        p2 = new AI(ai2Difficulty, 'O',jpGameBoard);
                        break;
                    case AvP:
                        p1 = new AI(ai1Difficulty, 'X',jpGameBoard);
                        p2 = new User('O');
                        break;
                    case PvA:
                        p1 = new User('X');
                        p2 = new AI(ai2Difficulty, 'O',jpGameBoard);
                        break;
                    case PvP:
                        p1 = new User('X');
                        p2 = new User('O');
                } // init players

                startButtons();

                switch (gameMode) {
                    case PvP:
                        break;
                    case AvA:
                        AvATimer.start();
                        break;
                    case PvA:
                    case AvP:
                        p1.makeMove();
                        break;
                }

            } else {
                reset();
            }
        });
    }

    private void startButtons() {
        btnP1.setEnabled(false);
        btnP2.setEnabled(false);

        A1.setEnabled(true);
        A2.setEnabled(true);
        A3.setEnabled(true);
        B1.setEnabled(true);
        B2.setEnabled(true);
        B3.setEnabled(true);
        C1.setEnabled(true);
        C2.setEnabled(true);
        C3.setEnabled(true);

        btnReset.setText("Reset");

        String plType;
        char turn = jpGameBoard.getTurn();
        plType = getPlayerType(turn);

        jlbStatus.setText("The turn of " + plType + " Player (" + turn + ")");

        gameCounter++;

        System.out.println("Game #" + gameCounter + " Started");
        System.out.println("Players are: p1: " + btnP1.getText() + "; p2: " + btnP2.getText());
        if (gameMode == GameMode.AvA) {
            System.out.println("Difficulty X : " + ai1Difficulty + "; Difficulty O : " + ai2Difficulty);
        }
    }

    private static String getPlayerType(char turn) {
        String plType;
        switch (gameMode) {
            case AvP:
                plType = turn == 'X'  ? "Robot" : "Human";
                break;
            case PvP:
                plType = "Human";
                break;
            case AvA:
                plType = "Robot";
                break;
            case PvA:
                plType = turn == 'O'  ? "Robot" : "Human";
                break;
            default:
                plType = "Unknown";
        }
        return plType;
    }

    private void reset() {
        jpGameBoard.clearBoard();
        AvATimer.stop();

        A1.setText(" ");
        A2.setText(" ");
        A3.setText(" ");

        B1.setText(" ");
        B2.setText(" ");
        B3.setText(" ");

        C1.setText(" ");
        C2.setText(" ");
        C3.setText(" ");

        btnP1.setEnabled(true);
        btnP2.setEnabled(true);

        A1.setEnabled(false);
        A2.setEnabled(false);
        A3.setEnabled(false);
        B1.setEnabled(false);
        B2.setEnabled(false);
        B3.setEnabled(false);
        C1.setEnabled(false);
        C2.setEnabled(false);
        C3.setEnabled(false);

        jlbStatus.setText("Game is not started");
        btnReset.setText("Start");

        System.out.println("Game Reset");
        System.out.println();
    }

    public static void aiButton(int[] cell) {
        Cell c = A1;
        switch (cell[0]) {
            case 0:
                switch (cell[1]) {
                    case 0:
                        c = A3;
                        break;
                    case 1:
                        c = B3;
                        break;
                    case 2:
                        c = C3;
                        break;
                }
                break;
            case 1:
                switch (cell[1]) {
                    case 0:
                        c = A2;
                        break;
                    case 1:
                        c = B2;
                        break;
                    case 2:
                        c = C2;
                        break;
                }
                break;
            case 2:
                switch (cell[1]) {
                    case 0:
                        c = A1;
                        break;
                    case 1:
                        c = B1;
                        break;
                    case 2:
                        c = C1;
                        break;
                }
                break;
        }
        btn_click(cell[0],cell[1],c);
    }

    private static synchronized void btn_click(int i, int j, Cell cell) {
        Board.Status st = jpGameBoard.getStatus();
        if (st == Board.Status.UNFINISHED) {
            if (jpGameBoard.getCell(i, j) == ' ') {

                char c = jpGameBoard.getTurn();

                jpGameBoard.setCell(i, j, c);
                cell.setText(String.valueOf(c));

                System.out.println("Cell " + cell.getName() + " (" + i + "," + j + ")" + " filled with " + c);
            } else {
                System.out.println("Cell " + cell.getName() + " (" + i + "," + j + ")" + " couldn't be filled");
            }

            st = jpGameBoard.getStatus();
            char turn;
            String plType;
            switch (st) {
                case DRAW:
                    jlbStatus.setText("Draw");
                    System.out.println("Draw");
                    break;
                case O_WINS:
                    turn = 'O';
                    plType = getPlayerType(turn);
                    jlbStatus.setText("The " + plType + " Player (" + turn + ") wins");
                    System.out.println("X wins");
                    System.out.println("O wins");
                    break;
                case X_WINS:
                    turn = 'X';
                    plType = getPlayerType(turn);
                    jlbStatus.setText("The " + plType + " Player (" + turn + ") wins");
                    System.out.println("X wins");
                    break;
                case UNFINISHED:
                    jpGameBoard.updateTurn();
                    turn = jpGameBoard.getTurn();
                    plType = getPlayerType(turn);
                    jlbStatus.setText("The turn of " + plType + " Player (" + turn + ")");
                    break;
                case NOT_STARTED:
                    jlbStatus.setText("Game is not started");
                    break;
                case IMPOSSIBLE:
                    jlbStatus.setText("If you're here, the game is broken");
            }
        }
    }

    enum GameMode {
        PvP, PvA, AvP, AvA
    }
}