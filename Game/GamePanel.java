package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Quản lý giao diện chính của trò chơi, điều hướng giữa menu, level và game.
 */
public class GamePanel extends JPanel {
    private final CardLayout cardLayout;
    private final GameManager game;
    private final Competitive competitive;

    private int WIDTH;
    private int HEIGHT;
    public int sca;
    public static float resize = 1;

    public static float scaleX = 1f;
    public static float scaleY = 1f;
    public static float scale = 1f;
    public static boolean loopMenu = false, loopStartGame = false;

    public int offsetX = 0;
    public int offsetY = 0;


    public static boolean switchVol = true;

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

        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Thiết đặt kích thước panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        Menu menu = new Menu(this);
        game = new GameManager(this, WIDTH, HEIGHT);
        LevelPanel levelPanel = new LevelPanel(this);
        competitive = new Competitive(this, WIDTH, HEIGHT);

        SoundEffect.loadAllSounds();
        SoundEffect.setVolume(6);
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
                game.setGameSize(scale);
                competitive.setGameSize(scale);
            }
        });

        add(menu, "Menu");
        add(game, "Game");
        add(levelPanel, "LevelPanel");
        add(competitive, "Competitive");
        SoundEffect.loop("sound_Menu");
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
        if(switchVol) SoundEffect.loop("soundMenu");
        cardLayout.show(this, "Menu");
    }

    /**
     * Hiển thị màn hình chọn level.
     */
    public void showLevelPanel() {
        SoundEffect.stop("bgm");
        SoundEffect.stop("soundMenu");
        cardLayout.show(this, "LevelPanel");
    }

    /**
     * Bắt đầu trò chơi từ level 1 (mặc định).
     */
    public void startGame() {
        SoundEffect.stop("soundMenu");
        if(switchVol) SoundEffect.loop("bgm");
        cardLayout.show(this, "Game");
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
        SoundEffect.stop("soundMenu");
        if(switchVol) SoundEffect.loop("bgm");
        cardLayout.show(this, "Game");
        game.setLevel(level); // khi chọn level cụ thể
        SwingUtilities.invokeLater(() -> {
            game.setFocusable(true);
            game.requestFocusInWindow();
        });
    }

    /**
     * Bắt đầu chế độ đối kháng 2 người chơi.
     */
    public void showCompetitiveMode(int level) {
        SoundEffect.stop("soundMenu");
        if(switchVol) SoundEffect.loop("bgm");
        cardLayout.show(this, "Competitive");
        competitive.setLevel(level); // khi chọn level cụ thể
        SwingUtilities.invokeLater(() -> {
            competitive.setFocusable(true);
            competitive.requestFocusInWindow();
            competitive.grabFocus();
        });
    }
}