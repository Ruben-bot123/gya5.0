package soundApp.codebehind;

import javax.swing.*;
import java.awt.*;

/*
Java soundApp.codebehind.Shape.Tetris game clone

Author: Jan Bodnar
Website: http://zetcode.com
 */
public class Tetris extends JFrame {

    private JLabel statusbar;

    public Tetris() {

        initUI();
    }

    private void initUI() {

        statusbar = new JLabel(" 0");
        add(statusbar, BorderLayout.SOUTH);

        var board = new Board(this);
        add(board);
        board.start();

        setTitle("Tetris");
        setSize(200, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    JLabel getStatusBar() {

        return statusbar;
    }

    /*
    ANDREAS LYTTER
    Ni skapade nya instanser av PlotCanvas.
    Istället ska ni använda bara en instans.
    Plockade ut den första insstansen som skapades och gjorde den
    public static så att ni kan komma åt den överallt via
    Tetris.plotCanvas
     */
    public static PlotCanvas plotCanvas;
    public static void main(String[] args) {
        plotCanvas = new PlotCanvas();
        InputSource inputSource = new InputSource(plotCanvas);
        inputSource.start();



        EventQueue.invokeLater(() -> {

            var game = new Tetris();
            game.setVisible(true);
        });
    }
}
