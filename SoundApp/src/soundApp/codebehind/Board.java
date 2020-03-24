package soundApp.codebehind;

import soundApp.codebehind.Shape.Tetrominoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Board extends JPanel {

    private final int BOARD_WIDTH = 10;
    private final int BOARD_HEIGHT = 22;

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int numLinesRemoved = 0;
    private int curX = 0;
    private int curY = 0;
    private JLabel statusbar;
    private Shape curPiece;
    private Tetrominoe[] board;
    private Timer timer1;
    private int [] toneArray;


    Board(Tetris parent) {

        initBoard(parent);
    }

    private void initBoard(Tetris parent) {

        setFocusable(true);
        statusbar = parent.getStatusBar();
        timer1 = new Timer(600, e -> tone());
        timer1.start();
    }

    /*
    ANDREAS LYTTER
    Jag tog bort er implementering av KeyListener och jobbade vidare med den här
    metoden som verkar göra samma sak.
    Utökade spannet för vad som är giltiga toner (om jag fattat rätt)
    Tog bort paus (den ställer till det när man testar)
    Tog bort loopen som gjorde attprogrammer inte kommer framåt, istället
    förlitar vi oss på Timern som körs varje sekund via initBoard.
    Ett problem är att vi bara kollar tonen en gång per sekund, oklart vilka konsekvenser det får.

    Jag har försökt göra så lite som möjligt i er kod, så det finns mer arbete att göra :)
     */
    private void tone() {
        if (curPiece.getShape() == Tetrominoe.NoShape) {

            return;
        }
        int ton = Tetris.plotCanvas.testaSkit();
        System.out.println(ton);
        // Java 12 switch expressions
        switch (ton) {
            case 23:
            case 24:
            case 25:
                pause();
                break;
            case 26:
            case 27:
            case 28:
            case 29:
                tryMove(curPiece, curX - 1, curY);
                break;
            case 30:
                tryMove(curPiece, curX + 1, curY);
                break;
            case 31:
            case 32:
            case 33:
            case 34:
                tryMove(curPiece.rotateRight(), curX, curY);
                break;
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
                tryMove(curPiece.rotateLeft(), curX, curY);
                break;
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                dropDown();
                break;
            case 45:
            case 46:
                oneLineDown();
                break;
        }
    }

    private int squareWidth() {

        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    private int squareHeight() {

        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private Tetrominoe shapeAt(int x, int y) {

        return board[(y * BOARD_WIDTH) + x];
    }

    void start() {

        curPiece = new Shape();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];

        clearBoard();
        newPiece();


        int PERIOD_INTERVAL = 600;
        timer = new Timer(PERIOD_INTERVAL, new GameCycle());
        timer.start();

    }

    private void pause() {

        isPaused = !isPaused;

        if (isPaused) {

            statusbar.setText("paused");
        } else {

            statusbar.setText(String.valueOf(numLinesRemoved));
        }

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        var size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

        for (int i = 0; i < BOARD_HEIGHT; i++) {

            for (int j = 0; j < BOARD_WIDTH; j++) {

                Tetrominoe shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                if (shape != Tetrominoe.NoShape) {

                    drawSquare(g, j * squareWidth(),
                            boardTop + i * squareHeight(), shape);
                }
            }
        }

        if (curPiece.getShape() != Tetrominoe.NoShape) {

            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);

                drawSquare(g, x * squareWidth(),
                        boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(),
                        curPiece.getShape());
            }
        }
    }

    private void dropDown() {

        int newY = curY;

        while (newY > 0) {

            if (!tryMove(curPiece, curX, newY - 1)) {

                break;
            }

            newY--;
        }

        pieceDropped();
    }

    private void oneLineDown() {

        if (!tryMove(curPiece, curX, curY - 1)) {

            pieceDropped();
        }
    }

    private void clearBoard() {

        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {

            board[i] = Tetrominoe.NoShape;
        }
    }

    private void pieceDropped() {

        if (!isPaused) {
            for (int i = 0; i < 4; i++) {

                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                board[(y * BOARD_WIDTH) + x] = curPiece.getShape();
            }

            removeFullLines();

            if (!isFallingFinished) {

                newPiece();
            }
        }
    }

    private void newPiece() {

        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();

        if (!tryMove(curPiece, curX, curY)) {

            curPiece.setShape(Tetrominoe.NoShape);
            timer.stop();

            var msg = String.format("Game over. Score: %d", numLinesRemoved);
            statusbar.setText(msg);
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {

        if (!isPaused) {
            for (int i = 0; i < 4; i++) {

                int x = newX + newPiece.x(i);
                int y = newY - newPiece.y(i);

                if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {

                    return false;
                }

                if (shapeAt(x, y) != Tetrominoe.NoShape) {

                    return false;
                }
            }

            curPiece = newPiece;
            curX = newX;
            curY = newY;

            repaint();

        }
        return true;
    }

    private void removeFullLines() {

        int numFullLines = 0;

        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {

            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; j++) {

                if (shapeAt(j, i) == Tetrominoe.NoShape) {

                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {

                numFullLines++;

                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = shapeAt(j, k + 1);
                    }
                }
            }
        }

        if (numFullLines > 0) {

            numLinesRemoved += numFullLines;

            statusbar.setText(String.valueOf(numLinesRemoved));
            isFallingFinished = true;
            curPiece.setShape(Tetrominoe.NoShape);
        }
    }

    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {

        Color[] colors = {new Color(0, 0, 0), new Color(204, 102, 102),
                new Color(102, 204, 102), new Color(102, 102, 204),
                new Color(204, 204, 102), new Color(204, 102, 204),
                new Color(102, 204, 204), new Color(218, 170, 0)
        };

        var color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                x + squareWidth() - 1, y + 1);
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private void doGameCycle() {

        update();
        repaint();
    }

    private void update() {

        if (isPaused) {

            return;
        }

        if (isFallingFinished) {

            isFallingFinished = false;
            newPiece();
        } else {

            oneLineDown();
        }
    }
}
