package Game;

import javax.swing.*;
import java.awt.*;

public class Main {
    private static boolean fullscreen = false;
    private static JFrame frame;
    private static GraphicsDevice gd  = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static int width = 800;
    private static int height = 600;
    private static GameManager game = new GameManager(width, height);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


            frame = new JFrame("Arkanoid Game");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setContentPane(game);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            game.startGameThread();
        });
    }

    public static void onFullscreen() {
        // Đảo giá trị của fullsreen
        fullscreen = !fullscreen;
        frame.dispose(); // phải dispose trước khi đổi thuộc tính

        if (fullscreen) {
            frame.setUndecorated(true);
            gd.setFullScreenWindow(frame);

            //cập nhật kích thước
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            game.setGameSize(screenSize.width, screenSize.height);
        } else {
            game.setGameSize(width, height);
            gd.setFullScreenWindow(null);
            frame.setUndecorated(false);
            frame.setExtendedState(JFrame.NORMAL);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
