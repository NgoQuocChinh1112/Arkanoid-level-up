package Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.PublicKey;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.*;


public class LevelPanel extends JPanel {
    private final GamePanel parent;
    private BufferedImage levelsBackGround;
    private final BufferedImage[] image_level;
    private BufferedImage title_choose_level;
    private BufferedImage back_bot;
    private BufferedImage back_top;
    private static int WIDTH_BUTTON_LEVEL = (int)(100 * GamePanel.scaleY);
    private static int HEIGHT_BUTTON_LEVEL = (int)(100 * GamePanel.scaleY);
    private static int WIDTH_BUTTON_BACK = (int)(60 * GamePanel.scaleY);
    private static int HEIGHT_BUTTON_BACK = (int)(60 * GamePanel.scaleY);
    private static int WIDTH_TITLE = (int)(400 *  GamePanel.scaleY);
    private static int HEIGHT_TITLE = (int)(100 *  GamePanel.scaleY);

    private static int X_BUTTON_LEVEL_1 = (int)(50 * GamePanel.scaleX);
    private static int Y_BUTTON_LEVEL_1 = (int)(200 * GamePanel.scaleY);
    private static int X_BUTTON_BACK = (int)(700 * GamePanel.scaleX);
    private static int Y_BUTTON_BACK = (int)(30 *  GamePanel.scaleY);
    private static int X_TITLE = (int)(200 *  GamePanel.scaleX);
    private static int Y_TITLE = (int)(8 *  GamePanel.scaleY);

    private Rectangle[] levelsRect;
    private Rectangle back;

    private boolean hoverBack = false;

    /**
     *Tạo panel chọn level, nạp ảnh giao diện và gắn các sự kiện chuột.
     * @param parent panel cha dùng để chuyển giữa menu và game.
     */
    public LevelPanel(GamePanel parent) {
        this.parent = parent;
        setLayout(null);
        image_level = new BufferedImage[10];
        //nap anh
        try {
            levelsBackGround = ImageIO.read(getClass().getResource("/assets/BGLevel.png"));
            image_level[0] = ImageIO.read(getClass().getResource("/assets/lv1.png"));
            image_level[1] = ImageIO.read(getClass().getResource("/assets/lv2.png"));
            image_level[2] = ImageIO.read(getClass().getResource("/assets/lv3.png"));
            image_level[3] = ImageIO.read(getClass().getResource("/assets/lv4.png"));
            image_level[4] = ImageIO.read(getClass().getResource("/assets/lv5.png"));
            image_level[5] = ImageIO.read(getClass().getResource("/assets/lv6.png"));
            image_level[6] = ImageIO.read(getClass().getResource("/assets/lv7.png"));
            image_level[7] = ImageIO.read(getClass().getResource("/assets/lv8.png"));
            image_level[8] = ImageIO.read(getClass().getResource("/assets/lv9.png"));
            image_level[9] = ImageIO.read(getClass().getResource("/assets/lv10.png"));
            title_choose_level = ImageIO.read(getClass().getResource("/assets/title_choose_level.png"));
            back_bot = ImageIO.read(getClass().getResource("/assets/back_bot.png"));
            back_top = ImageIO.read(getClass().getResource("/assets/back_top.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } 

        levelsRect = new Rectangle[10];
        back = new Rectangle();

        /**
         * Kiem tra click chuot
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int btnW = (int)(100 * GamePanel.scaleY);
                int btnH = (int)(100 * GamePanel.scaleY);
                int centerX = (int)(50 * GamePanel.scaleX);
                int startY = (int)(200 * GamePanel.scaleY);

                Rectangle[] levelsRectLocal = new Rectangle[10];
                Rectangle backLocal = new Rectangle((int)(700 * GamePanel.scaleX), (int)(30 *  GamePanel.scaleY),
                        (int)(60 * GamePanel.scaleY), (int)(60 * GamePanel.scaleY));

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

        /**
         * Kiem tra di chuot
         */
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                Rectangle backLocal = new Rectangle((int)(700 * GamePanel.scaleX), (int)(30 *  GamePanel.scaleY),
                        (int)(60 * GamePanel.scaleY), (int)(60 * GamePanel.scaleY));

                boolean oldHoverBack = hoverBack;
                hoverBack = backLocal.contains(p);
                if (oldHoverBack != hoverBack) repaint();
            }
        });
    }

    // ve man hinh hien thi level
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int btnW = (int)(100 * GamePanel.scaleY);
        int btnH = (int)(100 * GamePanel.scaleY);
        int centerX = (int)(50 * GamePanel.scaleX);
        int startY = (int)(200 * GamePanel.scaleY);

        if (levelsBackGround != null) {
            g2.drawImage(levelsBackGround, 0, 0, getWidth(), getHeight(), null);
        }

        if (title_choose_level != null) {
            g2.drawImage(title_choose_level, (int)(200 *  GamePanel.scaleY), (int)(8 *  GamePanel.scaleY),
                    (int)(400 *  GamePanel.scaleY), (int)(100 *  GamePanel.scaleY), null);
        }

        back.setBounds((int)(700 * GamePanel.scaleX), (int)(30 *  GamePanel.scaleY),
                (int)(60 * GamePanel.scaleY), (int)(60 * GamePanel.scaleY));

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
            g2.drawImage(back_top, (int)(700 * GamePanel.scaleX), (int)(30 *  GamePanel.scaleY),
                    (int)(60 * GamePanel.scaleY), (int)(60 * GamePanel.scaleY), null);
        } else if (back_bot != null) {
            g2.drawImage(back_bot, (int)(700 * GamePanel.scaleX), (int)(30 *  GamePanel.scaleY),
                    (int)(60 * GamePanel.scaleY), (int)(60 * GamePanel.scaleY), null);
        }

        if (image_level != null) {
            for (int i = 0; i < 5; ++i) {
                g2.drawImage(image_level[i], centerX + i * btnW * 3/2, startY, btnW, btnH, null);
            }

            for (int i = 5; i < 10; ++i) {
                g2.drawImage(image_level[i], centerX + (i - 5) * btnW * 3/2, startY + btnH * 2, btnW, btnH, null);
            }
        }
    } 
}