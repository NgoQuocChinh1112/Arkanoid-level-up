package Game;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Arkanoid - Simple");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            GameManager gm = new GameManager(800, 600);
            frame.setContentPane(gm);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            gm.startGameThread();
        });
    }
}
