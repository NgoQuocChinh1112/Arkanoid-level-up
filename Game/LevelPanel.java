package Game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.PublicKey;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.Point;


public class LevelPanel extends JPanel {
    private final GamePanel parent;
    private BufferedImage levelsBackGround;
    private BufferedImage image_level;
    private BufferedImage back_bot;
    private BufferedImage back_top;

    private static int WIDTH_BUTTON_LEVEL = 100;
    private static int HEIGHT_BUTTON_LEVEL = 100;
    private static int WIDTH_BUTTON_BACK = 60;
    private static int HEIGHT_BUTTON_BACK = 60;

    private static int X_BUTTON_LEVEL_1 = 50;
    private static int Y_BUTTON_LEVEL_1 = 200;
    private static int X_BUTTON_BACK = 700;
    private static int Y_BUTTON_BACK = 30;

    private Rectangle[] levelsRect;
    private Rectangle back;

    private boolean hoverBack = false;

    public LevelPanel(GamePanel parent) {
        this.parent = parent;
        setLayout(null);

        try {
            levelsBackGround = ImageIO.read(getClass().getResource("/assets/BGLevel.png"));
            image_level = ImageIO.read(getClass().getResource("/assets/levelImage.png"));
            back_bot = ImageIO.read(getClass().getResource("/assets/back_bot.png"));
            back_top = ImageIO.read(getClass().getResource("/assets/back_top.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } 

        levelsRect = new Rectangle[10];
        back = new Rectangle();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int btnW = WIDTH_BUTTON_LEVEL;
                int btnH = HEIGHT_BUTTON_LEVEL;
                int centerX = X_BUTTON_LEVEL_1;
                int startY = Y_BUTTON_LEVEL_1;

                Rectangle[] levelsRectLocal = new Rectangle[10];
                Rectangle backLocal = new Rectangle(X_BUTTON_BACK, Y_BUTTON_BACK, WIDTH_BUTTON_BACK, HEIGHT_BUTTON_BACK);

                for (int i = 0; i < 5; i++) {
                    levelsRectLocal[i] = new Rectangle(centerX + i * btnW * 3/2, startY, btnW, btnH);
                    if (levelsRectLocal[i].contains(p)) {
                        parent.startGame(i + 1);
                    }
                }

                for (int i = 5; i < 10; ++i) {
                    levelsRectLocal[i] = new Rectangle(centerX + (i - 5) * btnW * 3/2, startY + btnH * 2, btnW, btnH);
                    if (levelsRectLocal[i].contains(p)) {
                        parent.startGame(i + 1);
                    }
                }

                if (backLocal.contains(p)) {
                    parent.showMenu();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverBack = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                Rectangle backLocal = new Rectangle(X_BUTTON_BACK, Y_BUTTON_BACK, WIDTH_BUTTON_BACK, HEIGHT_BUTTON_BACK);

                boolean oldHoverBack = hoverBack;
                hoverBack = backLocal.contains(p);
                if (oldHoverBack != hoverBack) repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int btnW = WIDTH_BUTTON_LEVEL;
        int btnH = HEIGHT_BUTTON_LEVEL;
        int centerX = X_BUTTON_LEVEL_1;
        int startY = Y_BUTTON_LEVEL_1;

        if (levelsBackGround != null) {
            g2.drawImage(levelsBackGround, 0, 0, getWidth(), getHeight(), null);
        }

        back.setBounds(X_BUTTON_BACK, Y_BUTTON_BACK, WIDTH_BUTTON_BACK, HEIGHT_BUTTON_BACK);

        for (int i = 0; i < 10; ++i) {
            levelsRect[i] = new Rectangle();
        }

        for (int i = 0; i < 5; ++i) {
            levelsRect[i].setBounds(centerX + i * btnW * 3/2, startY, btnW, btnH);
        }

        for (int i = 5; i < 10; ++i) {
            levelsRect[i].setBounds(centerX + (i - 5) * btnW * 3/2, startY + btnH * 2, btnW, btnH);
        }

        if (hoverBack && back_top != null) {
            g2.drawImage(back_top, X_BUTTON_BACK, Y_BUTTON_BACK, WIDTH_BUTTON_BACK, HEIGHT_BUTTON_BACK, null);
        } else if (back_bot != null) {
            g2.drawImage(back_bot, X_BUTTON_BACK, Y_BUTTON_BACK, WIDTH_BUTTON_BACK, HEIGHT_BUTTON_BACK, null);
        }

        if (image_level != null) {
            for (int i = 0; i < 5; ++i) {
                g2.drawImage(image_level, centerX + i * btnW * 3/2, startY, btnW, btnH, null);
            }

            for (int i = 5; i < 10; ++i) {
                g2.drawImage(image_level, centerX + (i - 5) * btnW * 3/2, startY + btnH * 2, btnW, btnH, null);
            }
        }
    } 
}