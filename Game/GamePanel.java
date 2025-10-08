package Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private CardLayout cardLayout;
    private Menu menu;
    private GameManager game;
    private int WIDTH;
    private int HEIGHT;

    public GamePanel(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // Thiết đặt kích thước panel
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        menu = new Menu(this);
        game = new GameManager(WIDTH, HEIGHT);

        add(menu, "Menu");
        add(game, "Game");

        showMenu();
    }

    public void setPanelSize(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        if (game != null) {
            game.setGameSize(width, height);
        }
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        revalidate(); // cập nhật layout nếu cần
    }

    public void showMenu() {
        cardLayout.show(this, "Menu");
    }


    public void startGame() {
        cardLayout.show(this, "Game");
        game.setLevel(1);
        game.startGameThread();
        SwingUtilities.invokeLater(() -> {
            game.setFocusable(true);
            game.requestFocusInWindow();
            game.grabFocus();
        });
    }

    public void startGame(int level) {
        cardLayout.show(this, "Game");
        game.setLevel(level); // khi chọn level cụ thể
        game.startGameThread();
        SwingUtilities.invokeLater(() -> {
            game.setFocusable(true);
            game.requestFocusInWindow();
        });
    }
}

