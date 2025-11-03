package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import static Game.GamePanel.scale;

public class Menu extends JPanel {

    private final GamePanel parent;

    private final BufferedImage[] button = Renderer.loadButtonMenu();
    private final BufferedImage background;
    private final BufferedImage title;
    private final BufferedImage startTop;
    private final BufferedImage startBot;
    private final BufferedImage chooseTop;
    private final BufferedImage chooseBot;
    private final BufferedImage onePlayerTop;
    private final BufferedImage onePlayerBot;
    private final BufferedImage twoPlayerTop;
    private final BufferedImage twoPlayerBot;
    private final BufferedImage competitiveTop;
    private final BufferedImage competitiveBot;
    private final BufferedImage exitTop;
    private final BufferedImage exitBot;

    private boolean hoverStart = false;
    private boolean hoverChoose = false;
    private boolean hoverRegime = false;
    private boolean hoverCompetitive =false;
    private boolean hoverExit = false;

    private final Rectangle startRect;
    private final Rectangle chooseRect;
    private final Rectangle regimeRect;
    private final Rectangle competitive;
    private final Rectangle exitRect;

    public static boolean isCompetitive = false;
    public Menu(GamePanel parent) {
        this.parent = parent;
        setLayout(null);

        background = button[0];
        title = button[1];
        startTop = button[2];
        startBot = button[3];
        exitTop = button[4];
        exitBot = button[5];
        chooseTop = button[6];
            chooseBot = button[7];
        onePlayerTop = button[8];
        onePlayerBot = button[9];
        twoPlayerTop = button[10];
        twoPlayerBot = button[11];
        competitiveTop = button[12];
        competitiveBot = button[13];

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
                    SoundEffect.play("click");
                    parent.startGame();
                } else if (chooseRectLocal.contains(p)) {
                    SoundEffect.play("click");
                    parent.showLevelPanel();
                } else if (regimeRectLocal.contains(p)) {
                    SoundEffect.play("click");
                    parent.getGameManager().setTwoPlayerMode();
                    repaint();
                } else if (exitRectLocal.contains(p)) {
                    SoundEffect.play("click");
                    System.exit(0);
                } else if (competitiveLocal.contains(p)) {
                    SoundEffect.play("click");
                    isCompetitive = true;
                    parent.showLevelPanel();
                }

                int butW = (int)(50 * scale) , butH = (int)(50 * scale);
                int volX = parent.getWIDTH() - (int)(70 * scale);
                int butY = parent.getHEIGHT() - (int)(70 * scale);
                Rectangle volRect = new Rectangle(volX, butY, butW, butH);

                if (volRect.contains(p)) {
                    SoundEffect.play("click");
                    GamePanel.switchVol = !GamePanel.switchVol;
                    if (GamePanel.switchVol) {
                        SoundEffect.loop("soundMenu");
                        SoundEffect.setVolume(6);
                    } else {
                        SoundEffect.stop("bgm");
                        SoundEffect.stop("soundMenu");
                    }
                    repaint();
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
        if (hoverExit && exitTop != null) {
            g2.drawImage(exitTop, exitX, exitY, exitW, exitH, null);
        }
        else if (exitBot != null) {
            g2.drawImage(exitBot, exitX, exitY, exitW, exitH, null);
        }

        int butW = (int)(50 * scale), butH = (int)(50 * scale);
        int volX = parent.getWIDTH() - (int)(70 * scale);
        int butY = parent.getHEIGHT() - (int)(70 * scale);

        if (button[14] != null && GamePanel.switchVol) {
            g2.drawImage(button[14], volX, butY, butW, butH, null);
        } else if (button[15] != null) {
            g2.drawImage(button[15], volX, butY, butW, butH, null);
        }
    }
}
