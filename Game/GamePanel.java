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
    private int prevWid;
    private int prevHei;
    public int sca;
    public static float resize = 1;

    public float scaleX = 1f;
    public float scaleY = 1f;
    public static float scale = 1f;

    public int offsetX = 0;
    public int offsetY = 0;



    /**
     * Khởi tạo GamePanel với kích thước xác định và các màn hình con.
     */

    public GameManager getGameManager() {
        return game;
    }

    public GamePanel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.sca = HEIGHT;
        this.prevHei = this.HEIGHT;
        this.prevWid = this.WIDTH;

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
                scaleX = (float) WIDTH / 800;
                scaleY = (float) HEIGHT / 600;
                scale = Math.min(scaleX, scaleY);
                System.out.println(sca + " " + HEIGHT);
                if (sca == HEIGHT) {
                    resize = 1;
                } else if (sca < WIDTH) {
                    resize = 793f / 600f;
                    sca = WIDTH;
                } else if (sca > WIDTH) {
                    resize = 600f / 793f;
                    sca = WIDTH;
                }

                offsetX = (int)((WIDTH - 800 * scale) / 2);
                offsetY = (int)((HEIGHT - 600 * scale) / 2);

                if (game != null) {
                    game.setGameSize(scale);
                }
            }
        });

        add(menu, "Menu");
        add(game, "Game");
        add(levelPanel, "LevelPanel");
        showMenu();
    }

    public int getWIDTH() {
        return this.WIDTH;
    }

    public int getHEIGHT() {
        return this.HEIGHT;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
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