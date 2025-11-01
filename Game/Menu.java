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
    private BufferedImage chooseTop, chooseBot;
    private BufferedImage onePlayerTop, onePlayerBot;
    private BufferedImage twoPlayerTop, twoPlayerBot;
    private BufferedImage competitiveTop, competitiveBot;
    private BufferedImage exitTop, exitBot;

    private boolean hoverStart = false;
    private boolean hoverChoose = false;
    private boolean hoverRegime = false;
    private boolean hoverCompetitive =false;
    private boolean hoverExit = false;

    private Rectangle startRect;
    private Rectangle chooseRect;
    private Rectangle regimeRect;
    private Rectangle competitive;
     private Rectangle exitRect;

    public static boolean isCompetitive = false;

    public Menu(GamePanel parent) {
        this.parent = parent;
        setLayout(null);

        // Nạp ảnh
        try {
            background = ImageIO.read(getClass().getResource("/assets/BG_Level_1.png"));
            title = ImageIO.read(getClass().getResource("/assets/title.png"));
            startTop = ImageIO.read(getClass().getResource("/assets/start_top.png"));
            startBot = ImageIO.read(getClass().getResource("/assets/start_bot.png"));
            exitTop = ImageIO.read(getClass().getResource("/assets/exit_top.png"));
            exitBot = ImageIO.read(getClass().getResource("/assets/exit_bot.png"));
            chooseTop = ImageIO.read(getClass().getResource("/assets/choose_levels_top.png"));
            chooseBot = ImageIO.read(getClass().getResource("/assets/choose_levels_bot.png"));
            onePlayerTop = ImageIO.read(getClass().getResource("/assets/one_player_top.png"));
            onePlayerBot = ImageIO.read(getClass().getResource("/assets/one_player_bot.png"));
            twoPlayerTop = ImageIO.read(getClass().getResource("/assets/two_player_top.png"));
            twoPlayerBot = ImageIO.read(getClass().getResource("/assets/two_player_bot.png"));
            competitiveTop = ImageIO.read(getClass().getResource("/assets/isCompetitive_top.png"));
            competitiveBot = ImageIO.read(getClass().getResource("/assets/isCompetitive_bot.png"));
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        // Vùng bấm
        startRect = new Rectangle();
        exitRect = new Rectangle();
        chooseRect = new Rectangle();
        regimeRect = new Rectangle();
        competitive = new Rectangle();

        // Mouse
        addMouseListener(new MouseAdapter() {
            @Override
             public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int w = getWidth();
                int h = getHeight();
                int startW = 90, startH = 90;
                int startX = w/2 - startW/2, startY = h/2;
                int chooseX = startX - 105, chooseY = startY - 20, chooseW = startW - 10, chooseH = startW - 10;
                int regimeX = startX + 105, regimeY = startY - 20, regimeW = startW - 10, regimeH = startW - 10;
                int competitiveX = regimeX + 105, competitiveY = regimeY - 20, competitiveW = regimeW - 10, competitiveH = regimeH - 10;
                int exitX = chooseX - 105, exitY = chooseY - 20, exitW = chooseW - 10, exitH = chooseH - 10;

                Rectangle startRectLocal = new Rectangle(startX, startY, startW, startH);
                Rectangle chooseRectLocal = new Rectangle(chooseX, chooseY, chooseW, chooseH);
                Rectangle regimeRectLocal = new Rectangle(regimeX, regimeY, regimeW, regimeH);
                Rectangle competitiveLocal = new Rectangle(competitiveX, competitiveY, competitiveW, competitiveH);
                Rectangle exitRectLocal = new Rectangle(exitX, exitY, exitW, exitH);
                

                if (startRectLocal.contains(p)) {
                    parent.startGame();
                } else if (chooseRectLocal.contains(p)) {
                    parent.showLevelPanel();
                } else if (regimeRectLocal.contains(p)) {
                    parent.getGameManager().setTwoPlayerMode();
                    repaint();
                } else if (exitRectLocal.contains(p)) {
                    System.exit(0);
                } else if (competitiveLocal.contains(p)) {
                    isCompetitive = true;
                    parent.showLevelPanel();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hoverStart = hoverChoose = hoverRegime = hoverExit = hoverCompetitive = false;
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int w = getWidth();
                int h = getHeight();
                int startW = 90, startH = 90;
                int startX = w/2 - startW/2, startY = h/2;
                int chooseX = startX - 105, chooseY = startY - 20, chooseW = startW - 10, chooseH = startW - 10;
                int regimeX = startX + 105, regimeY = startY - 20, regimeW = startW - 10, regimeH = startW - 10;
                int competitiveX = regimeX + 105, competitiveY = regimeY - 20, competitiveW = regimeW - 10, competitiveH = regimeH - 10;
                int exitX = chooseX - 105, exitY = chooseY - 20, exitW = chooseW - 10, exitH = chooseH - 10;

                Rectangle startRectLocal = new Rectangle(startX, startY, startW, startH);
                Rectangle chooseRectLocal = new Rectangle(chooseX, chooseY, chooseW, chooseH);
                Rectangle regimeRectLocal = new Rectangle(regimeX, regimeY, regimeW, regimeH);
                Rectangle competitiveLocal = new Rectangle(competitiveX, competitiveY, competitiveW, competitiveH);
                Rectangle exitRectLocal = new Rectangle(exitX, exitY, exitW, exitH);
                

                boolean oldStart = hoverStart, oldChoose = hoverChoose, oldRegime = hoverRegime, 
                        oldCompetitive = hoverCompetitive, oldExit = hoverExit;
                hoverStart = startRectLocal.contains(p);
                hoverChoose = chooseRectLocal.contains(p);
                hoverRegime = regimeRectLocal.contains(p);
                hoverCompetitive = competitiveLocal.contains(p);
                hoverExit = exitRectLocal.contains(p);

                if (hoverStart != oldStart || hoverChoose != oldChoose || hoverRegime != oldRegime 
                                           || hoverCompetitive != oldCompetitive || hoverExit != oldExit) repaint();
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
            int titleY = h / 20;
            g2.drawImage(title, titleX, titleY, titleW, titleH, null);
        }

        // Kích thước nút
        int startW = 90, startH = 90;
        int startX = w/2 - startW/2, startY = h/2;
        int chooseX = startX - 105, chooseY = startY - 20, chooseW = startW - 10, chooseH = startW - 10;
        int regimeX = startX + 105, regimeY = startY - 20, regimeW = startW - 10, regimeH = startW - 10;
        int competitiveX = regimeX + 105, competitiveY = regimeY - 20, competitiveW = regimeW - 10, competitiveH = regimeH - 10;
        int exitX = chooseX - 105, exitY = chooseY - 20, exitW = chooseW - 10, exitH = chooseH - 10;

        // Cập nhật vùng click
        startRect.setBounds(startX, startY, startW, startH);
        chooseRect.setBounds(chooseX, chooseY, chooseW, chooseH);
        regimeRect.setBounds(regimeX, regimeY, regimeW, regimeH);
        competitive.setBounds(competitiveX, competitiveY, competitiveW, competitiveH);
        exitRect.setBounds(exitX, exitY, exitW, exitH);
        
        // Vẽ nút start
        if (hoverStart && startTop != null)
            g2.drawImage(startTop, startX, startY, startW, startH, null);
        else if (startBot != null)
            g2.drawImage(startBot, startX, startY, startW, startH, null);

        // Vẽ nút choose
        if (hoverChoose && chooseTop != null)
            g2.drawImage(chooseTop, chooseX, chooseY, chooseW, chooseH, null);
        else if (chooseBot != null)
            g2.drawImage(chooseBot, chooseX, chooseY, chooseW, chooseH, null);

        // Vẽ nút regime
        if (!parent.getGameManager().getTwoPlayerMode()) {
            if (hoverRegime && onePlayerTop != null) {
                g2.drawImage(onePlayerTop, regimeX, regimeY, regimeW, regimeH, null);
            } else if (onePlayerBot != null) {
                g2.drawImage(onePlayerBot, regimeX, regimeY, regimeW, regimeH, null);
            }
        } else {
            if (hoverRegime && twoPlayerTop != null) {
                g2.drawImage(twoPlayerTop, regimeX, regimeY, regimeW, regimeH, null);
            } else if (twoPlayerBot != null) {
                g2.drawImage(twoPlayerBot, regimeX, regimeY, regimeW, regimeH, null);
            }
        }

        // Vẽ nút competitive
        if (hoverCompetitive && competitiveTop != null) {
            g2.drawImage(competitiveTop, competitiveX, competitiveY, competitiveW, competitiveH, null);
        } else if (competitiveBot != null) {
            g2.drawImage(competitiveBot, competitiveX, competitiveY, competitiveW, competitiveH, null);
        }

        // Vẽ nút exit
        if (hoverExit && exitTop != null)
            g2.drawImage(exitTop, exitX, exitY, exitW, exitH, null);
        else if (exitBot != null)
            g2.drawImage(exitBot, exitX, exitY, exitW, exitH, null);
    }
}
