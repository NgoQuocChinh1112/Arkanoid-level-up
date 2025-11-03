package Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.*;

import static Game.GamePanel.scale;
import static Game.GamePanel.scaleX;


public class LevelPanel extends JPanel {
    private final GamePanel parent;
    private BufferedImage levelsBackGround;
    private final BufferedImage[] image_level;
    private BufferedImage title_choose_level;
    private BufferedImage back_bot;
    private BufferedImage back_top;

    private Rectangle[] levelsRect;
    private Rectangle back;

    private boolean hoverBack = false;
    private boolean[] hoverlevel = {false, false, false, false, false, false, false, false, false, false};

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
                int btnW = (int)(100 * scale);
                int btnH = (int)(100 * scale);
                int centerX = (int)(50 * scaleX);
                int startY = (int)(200 * scale);

                Rectangle backLocal = new Rectangle((int)(700 * scaleX), (int)(30 *  scale),
                        (int)(60 * scale), (int)(60 * scale));

                Rectangle[] levelsRectLocal = new Rectangle[10];

                for (int i = 0; i < 5; i++) {
                    levelsRectLocal[i] = new Rectangle(centerX + i * (int)(150 * scaleX), startY, btnW, btnH);
                    if (levelsRectLocal[i].contains(p)) {
                        SoundEffect.play("click");
                        if (Menu.isCompetitive) {
                            parent.showCompetitiveMode(i + 1);
                        }
                        else {
                            parent.startGame(i + 1);
                        }
                    }
                }

                for (int i = 5; i < 10; ++i) {
                    levelsRectLocal[i] = new Rectangle(centerX + (i - 5) * (int)(150 * scaleX), startY + btnH * 2, btnW, btnH);
                    if (levelsRectLocal[i].contains(p)) {
                        SoundEffect.play("click");
                        if (Menu.isCompetitive) {
                            parent.showCompetitiveMode(i + 1);
                        }
                        else {
                            parent.startGame(i + 1);
                        }
                    }
                }

                if (backLocal.contains(p)) {
                    SoundEffect.play("click");
                    parent.showMenu();
                    Menu.isCompetitive = false;
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
                int btnW = (int)(100 * scale);
                int btnH = (int)(100 * scale);
                int centerX = (int)(50 * scale);
                int startY = (int)(200 * scale);

                Rectangle backLocal = new Rectangle((int)(700 * scaleX), (int)(30 *  scale),
                        (int)(60 * scale), (int)(60 * scale));

                Rectangle[] levelsRectLocal = new Rectangle[10];

                for (int i = 0; i < 5; i++) {
                    levelsRectLocal[i] = new Rectangle(centerX + i * (int)(150 * scaleX), startY, btnW, btnH);
                }

                for (int i = 5; i < 10; ++i) {
                    levelsRectLocal[i] = new Rectangle(centerX + (i - 5) * (int)(150 * scaleX), startY + btnH * 2, btnW, btnH);
                }

                boolean oldHoverLevel[] = new boolean[10];
                for (int i = 0; i < 10; ++i) {
                    oldHoverLevel[i] = hoverlevel[i];
                }
                boolean oldHoverBack = hoverBack;

                hoverBack = backLocal.contains(p);
                for (int i = 0; i < 10; ++i) {
                    hoverlevel[i] = levelsRectLocal[i].contains(p);
                }
                if (oldHoverBack != hoverBack) repaint();
                for (int i = 0; i < 10; ++i) {
                    if (oldHoverLevel[i] != hoverlevel[i]) repaint();
                }
            }
        });
    }

    /**
     * Phương thức vẽ vẽ giao diện chọn level.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int btnW = (int)(100 * scale);
        int btnH = (int)(100 * scale);
        int centerX = (int)(50 * scaleX);
        int startY = (int)(200 * scale);

        if (levelsBackGround != null) {
            g2.drawImage(levelsBackGround, 0, 0, getWidth(), getHeight(), null);
        }

        if (title_choose_level != null) {
            g2.drawImage(title_choose_level, (parent.getWIDTH() - (int)(400 * scale)) / 2, (int)(8 *  scale),
                    (int)(400 *  scale), (int)(100 *  scale), null);
        }

        back.setBounds((int)(700 * scaleX), (int)(30 *  scale),
                (int)(60 * scale), (int)(60 * scale));

        for (int i = 0; i < 10; ++i) {
            levelsRect[i] = new Rectangle();
        }

        for (int i = 0; i < 5; ++i) {
            levelsRect[i].setBounds(centerX + i * (int)(150 * scaleX), startY, btnW, btnH);
        }

        for (int i = 5; i < 10; ++i) {
            levelsRect[i].setBounds(centerX + (i - 5) * (int)(150 * scaleX), startY + btnH * 2, btnW, btnH);
        }

        if (hoverBack && back_top != null) {
            g2.drawImage(back_top, (int)(700 * scaleX), (int)(30 *  scale),
                    (int)(60 * scale), (int)(60 * scale), null);
        } else if (back_bot != null) {
            g2.drawImage(back_bot, (int)(700 * scaleX), (int)(30 *  scale),
                    (int)(60 * scale), (int)(60 * scale), null);
        }

        if (image_level != null) {
            for (int i = 0; i < 10; ++i) {
                int x, y;
                if (i < 5) {
                    x = centerX + i * (int)(150 * scaleX);
                    y = startY;
                } else {
                    x = centerX + (i - 5) * (int)(150 * scaleX);
                    y = startY + btnH * 2;
                }

                // Nếu đang hover → phóng to 10%
                int drawW = btnW;
                int drawH = btnH;
                if (hoverlevel[i]) {
                    drawW = (int) (btnW * 1.1);
                    drawH = (int) (btnH * 1.1);
                    x -= (drawW - btnW) / 2;  // căn giữa ảnh phóng to
                    y -= (drawH - btnH) / 2;
                }

                g2.drawImage(image_level[i], x, y, drawW, drawH, null);
            }
        }
    }
}