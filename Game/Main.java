package Game;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static JFrame frame;
    private final static int width = 800;
    private final static int height = 600;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Arkanoid - Simple");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(true);

            // Tạo GamePanel chứa cả menu và game
            gamePanel = new GamePanel(width, height);
            frame.setContentPane(gamePanel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
