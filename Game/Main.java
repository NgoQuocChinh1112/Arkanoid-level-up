package Game;

import javax.swing.*;

public class Main {
    private static JFrame frame;
    private final static int width = 800;
    private final static int height = 600;
    private static GamePanel gamePanel;

    /**
     * Điểm khởi đầu của trò chơi Arkanoid.
     * Khởi tạo cửa sổ chính của ứng dụng bằng Swing,
     * tạo GamePanel để chứa cả menu, game và màn chơi, '
     * sau đó hiển thị cửa sổ ra màn hình.
     * @param args tham số dòng lệnh (không sử dụng)
     */
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