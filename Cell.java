package tictactoe;

import javax.swing.*;
import java.awt.*;

public class Cell extends JButton {


    public Cell(String name, String text) {
        super(text);
        setName(name);
        setFocusPainted(false);
        Font font = new Font("Serif",Font.BOLD,50);
        setFont(font);
    }
}
