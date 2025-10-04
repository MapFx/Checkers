package View;
import javax.swing.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        setTitle("Checkers");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Add your custom board panel
        add(new GameBoard());

        pack();                      // sizes frame to fit GameBoard
        setLocationRelativeTo(null); // center on screen
        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}