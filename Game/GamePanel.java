package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Quản lý giao diện chính của trò chơi, điều hướng giữa menu, level và game.
 */
public class GamePanel extends JPanel {
    private CardLayout cardLayout;
    private Menu menu;
    private GameManager game;
    private LevelPanel levelPanel;

    private int WIDTH;
    private int HEIGHT;

    public static float scaleX = 1f;
    public static float scaleY = 1f;



    /**
     * Khởi tạo GamePanel với kích thước xác định và các màn hình con.
     * @param width chiều rộng khung game.
     * @param height chiều cao khung game.
     */

    public GamePanel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Thiết đặt kích thước panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        menu = new Menu(this);
        game = new GameManager(this, WIDTH, HEIGHT);
        levelPanel = new LevelPanel(this);

        SoundEffect.loadAllSounds();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                WIDTH = getWidth();
                HEIGHT = getHeight();
                setPanelSize(WIDTH, HEIGHT);
                scaleX = (float) WIDTH / 800f;
                scaleY = (float) HEIGHT / 600f;
            }
        });

        add(menu, "Menu");
        add(game, "Game");
        add(levelPanel, "LevelPanel");
        showMenu();
    }

    /**
     * Cập nhật lại kích thước panel và các thành phần bên trong.
     * @param width chiều rộng mới.
     * @param height chiều cao mới.
     */
    public void setPanelSize(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        if (game != null) {
            game.setGameSize(width, height);
        }
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        revalidate(); // cập nhật layout nếu cần
    }

    /**
     * Hiển thị màn hình menu chính.
     */
    public void showMenu() {
        SoundEffect.stop("bgm");
        cardLayout.show(this, "Menu");
    }

    /**
     * Hiển thị màn hình chọn level.
     */
    public void showLevelPanel() {
        SoundEffect.stop("bgm");
        cardLayout.show(this, "LevelPanel");
    }

    /**
     * Bắt đầu trò chơi từ level 1 (mặc định).
     */
    public void startGame() {
        SoundEffect.loop("bgm");
        cardLayout.show(this, "Game");
        game.setCurrentLevel(1);
        game.setLevel(1);
        SwingUtilities.invokeLater(() -> {
            game.setFocusable(true);
            game.requestFocusInWindow();
            game.grabFocus();
        });
    }

    /**
     * Bắt đầu trò chơi tại level được chọn.
     * @param level cấp độ người chơi chọn để bắt đầu.
     */
    public void startGame(int level) {
        cardLayout.show(this, "Game");
        game.setCurrentLevel(level);
        game.setLevel(level); // khi chọn level cụ thể
        SwingUtilities.invokeLater(() -> {
            game.setFocusable(true);
            game.requestFocusInWindow();
        });
    }
}