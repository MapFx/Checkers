package View;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import Controller.GameController;
import Model.*;

public class GameBoard extends JPanel {

    private final int PSIZE = 80;   // Number of pixels per cell
    private final int BSIZE = 8;    // Number of rows & columns
    private final int BORDER = 10;  // Border width around checkers (in pixels)
    // Used for drawing the board.
    private final Color lightSquare = new Color(204, 192, 167);
    private final Color darkSquare = new Color(163, 150, 121);
    private final Color selector = new Color(58, 200, 188, 113);

    private Board board;
    private GameState gameState;
    private GameController gc;

    public GameBoard() {
        this.gameState = new GameState();
        this.board = new Board(BSIZE);
        gc = new GameController(this.gameState, this.board);

        setPreferredSize(new Dimension(BSIZE*PSIZE, BSIZE*PSIZE));
        setBackground(Color.WHITE);

        // Setup input handling
        installMouseListener();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        drawBlankBoard(g2);
        drawCheckers(g2);
        drawSelectors(g2);
        if(gameState.gameOver()){
            String winner = gameState.playerOneWins() ? "Red" : "Black";
            drawGameOver(g2, winner);
        }
    }
    private void installMouseListener() {
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                // Convert pixel coordinates to board coordinates
                repaint();
                int x = e.getX() / PSIZE;
                int y = e.getY() / PSIZE;
                gc.handleClick(x,y);
                repaint();
            }
        });
    }
    public void drawCheckers(Graphics2D g2) {
        for(int y = 0; y < BSIZE; y++) {
            for(int x = 0; x < BSIZE; x++) {
                drawChecker(g2,x,y);
            }
        }
    }
    public void drawSelectors(Graphics2D g2) {
        if(gameState.gameOver()){
            return;
        }
        ArrayList<Pos> b = gc.getMoves();
        if(b.isEmpty()) {
            return;
        }
        for(Pos p : b) {
            int x = p.x();
            int y = p.y();
            drawSelector(g2,x,y);
        }
    }
    private void drawSelector(Graphics2D g2, int x, int y) {
        g2.setColor(selector);
        g2.fillRect(x*PSIZE,y*PSIZE, PSIZE, PSIZE);
    }
    private void drawChecker(Graphics2D g2, int x, int y) {
        Piece piece = board.getPiece(x, y);
        if(piece != null) {
            g2.setColor(piece.getColor());
            g2.fillOval(x * PSIZE + BORDER, y * PSIZE + BORDER,
                    PSIZE - BORDER * 2, PSIZE - BORDER * 2);
        }
        if (piece != null && piece.isKing()) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, PSIZE / 2));

            // Use FontMetrics to center the text
            FontMetrics fm = g2.getFontMetrics();
            String k = "K";
            int textWidth = fm.stringWidth(k);
            int textAscent = fm.getAscent();

            int centerX = x * PSIZE + PSIZE / 2;
            int centerY = y * PSIZE + PSIZE / 2;

            int textX = centerX - textWidth / 2;
            int textY = centerY + textAscent / 2-4;

            g2.drawString(k, textX, textY);
        }
    }
    private void drawBlankBoard(Graphics2D g2) {
        // Draw background square (light squares)
        g2.setColor(lightSquare);
        g2.fillRect(0, 0, BSIZE*PSIZE, BSIZE*PSIZE);

        // Draw foreground squares (dark squares)
        g2.setColor(darkSquare);
        for (int y = 0; y < BSIZE; y++) {
            for (int x = 0; x < BSIZE; x++) {
                if((x + y) % 2 == 0) {
                    g2.fillRect(x * PSIZE, y * PSIZE, PSIZE, PSIZE);
                }
            }
        }
    }
    private void drawGameOver(Graphics2D g2, String winner) {
        // Turn on text antialiasing for smoother text
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Big bold font
        g2.setFont(new Font("SansSerif", Font.BOLD, 48));
        g2.setColor(Color.WHITE);

        String line1 = "GAME OVER";
        String line2 = winner + " wins!";

        // Get font metrics for positioning
        FontMetrics fm = g2.getFontMetrics();

        int panelWidth = getWidth();   // assuming this is inside a JPanel subclass
        int panelHeight = getHeight();

        // Compute text positions (center horizontally)
        int line1X = (panelWidth - fm.stringWidth(line1)) / 2;
        int line2X = (panelWidth - fm.stringWidth(line2)) / 2;

        // Vertical positioning: line1 a bit above center, line2 below
        int centerY = panelHeight / 2;
        int lineHeight = fm.getHeight();

        int line1Y = centerY - lineHeight / 2;
        int line2Y = centerY + lineHeight;

        // Draw both lines
        g2.drawString(line1, line1X, line1Y);
        g2.drawString(line2, line2X, line2Y);
    }

}