package Game;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static boolean fullscreen = false;
    private static JFrame frame;
    private static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = 800;
    private static int height = 600;
    private static GamePanel gamePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("Arkanoid - Simple");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);

            // Tạo GamePanel chứa cả menu và game
            gamePanel = new GamePanel(width, height);
            frame.setContentPane(gamePanel);
            frame.pack();

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public static void onFullscreen() {
        fullscreen = !fullscreen;
        frame.dispose();

        if (fullscreen) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            gamePanel.setPanelSize(screenSize.width, screenSize.height);
        } else {
            gamePanel.setPanelSize(width, height);
            gd.setFullScreenWindow(null);
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
