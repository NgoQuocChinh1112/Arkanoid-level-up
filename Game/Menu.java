package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Menu extends JPanel {

    private final GamePanel parent;

    private BufferedImage background;
    private BufferedImage title;
    private BufferedImage startTop, startBot;
    private BufferedImage exitTop, exitBot;
    private BufferedImage chooseTop, chooseBot;

    private boolean hoverStart = false;
    private boolean hoverExit = false;
    private boolean hoverChoose = false;

    private Rectangle startRect;
    private Rectangle exitRect;
    private Rectangle chooseRect;

    public Menu(GamePanel parent) {
        this.parent = parent;
        setLayout(null);

        // Nạp ảnh
        try {
            background = ImageIO.read(getClass().getResource("/assets/back_ground.png"));
            title = ImageIO.read(getClass().getResource("/assets/title.png"));
            startTop = ImageIO.read(getClass().getResource("/assets/start_top.png"));
            startBot = ImageIO.read(getClass().getResource("/assets/start_bot.png"));
            exitTop = ImageIO.read(getClass().getResource("/assets/exit_top.png"));
            exitBot = ImageIO.read(getClass().getResource("/assets/exit_bot.png"));
            chooseTop = ImageIO.read(getClass().getResource("/assets/choose_levels_top.png"));
            chooseBot = ImageIO.read(getClass().getResource("/assets/choose_levels_bot.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        // Vùng bấm
        startRect = new Rectangle();
        exitRect = new Rectangle();
        chooseRect = new Rectangle();

        // Mouse
        addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int w = getWidth();
                int h = getHeight();
                int btnW = 220, btnH = 80;
                int centerX = w/2 - btnW/2;
                int startY = h/2 - 40;
                int chooseY = startY + 100;
                int exitY = chooseY + 100;

                Rectangle startRectLocal = new Rectangle(centerX, startY, btnW, btnH);
                Rectangle chooseRectLocal = new Rectangle(centerX, chooseY, btnW, btnH);
                Rectangle exitRectLocal = new Rectangle(centerX, exitY, btnW, btnH);

                if (startRectLocal.contains(p)) {
                    parent.startGame();
                } else if (exitRectLocal.contains(p)) {
                    System.exit(0);
                } else if (chooseRect.contains(p)) {
                    // Hiển thị danh sách level để chọn
                    String[] levels = {"Level 1", "Level 2", "Level 3"};
                    String selected = (String) JOptionPane.showInputDialog(
                        Menu.this,
                        "Chọn màn chơi:",
                        "Chọn Level",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        levels,
                        levels[0]
                    );

                    if (selected != null) {
                        int level = 1;
                        if (selected.equals("Level 2")) level = 2;
                        else if (selected.equals("Level 3")) level = 3;
                        parent.startGame(level);
                    }
                }

            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverStart = hoverExit = hoverChoose = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int w = getWidth();
                int h = getHeight();
                int btnW = 220, btnH = 80;
                int centerX = w/2 - btnW/2;
                int startY = h/2 - 40;
                int chooseY = startY + 100;
                int exitY = chooseY + 100;

                Rectangle startRectLocal = new Rectangle(centerX, startY, btnW, btnH);
                Rectangle chooseRectLocal = new Rectangle(centerX, chooseY, btnW, btnH);
                Rectangle exitRectLocal = new Rectangle(centerX, exitY, btnW, btnH);

                boolean oldStart = hoverStart, oldChoose = hoverChoose, oldExit = hoverExit;
                hoverStart = startRectLocal.contains(p);
                hoverChoose = chooseRectLocal.contains(p);
                hoverExit = exitRectLocal.contains(p);

                if (hoverStart != oldStart || hoverChoose != oldChoose || hoverExit != oldExit) repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        // Vẽ nền
        if (background != null)
            g2.drawImage(background, 0, 0, w, h, null);
        else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, w, h);
        }

        // Vẽ tiêu đề
        if (title != null) {
            int titleW = (int)(title.getWidth() * 1.2);
            int titleH = (int)(title.getHeight() * 1.2);
            int titleX = w / 2 - titleW / 2;
            int titleY = h / 6;
            g2.drawImage(title, titleX, titleY, titleW, titleH, null);
        }

        // Kích thước nút
        int btnW = 220;
        int btnH = 80;
        int centerX = w / 2 - btnW / 2;
        int startY = h / 2 - 40;
        int chooseY = startY + 100;
        int exitY = chooseY + 100;

        // Cập nhật vùng click
        startRect.setBounds(centerX, startY, btnW, btnH);
        chooseRect.setBounds(centerX, chooseY, btnW, btnH);
        exitRect.setBounds(centerX, exitY, btnW, btnH);

        // Vẽ nút start
        if (hoverStart && startTop != null)
            g2.drawImage(startTop, centerX, startY, btnW, btnH, null);
        else if (startBot != null)
            g2.drawImage(startBot, centerX, startY, btnW, btnH, null);

        // Vẽ nút choose
        if (hoverChoose && chooseTop != null)
            g2.drawImage(chooseTop, centerX, chooseY, btnW, btnH, null);
        else if (chooseBot != null)
            g2.drawImage(chooseBot, centerX, chooseY, btnW, btnH, null);

        // Vẽ nút exit
        if (hoverExit && exitTop != null)
            g2.drawImage(exitTop, centerX, exitY, btnW, btnH, null);
        else if (exitBot != null)
            g2.drawImage(exitBot, centerX, exitY, btnW, btnH, null);
    }
}
