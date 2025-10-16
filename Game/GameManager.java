package Game;

import Objects.*;
import PowerUps.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameManager extends JPanel implements KeyListener, ActionListener {
    public static int WIDTH;
    public static int HEIGHT;

    private float scaleX;
    private float scaleY;

    private Timer gameTimer;
    private final int FPS = 60;
    private int currentLevel = 1;

    private Paddle paddle;
    public Ball ball;
    private List<Brick> bricks;
    public List<PowerUp> powerUps;

    private int score = 0;
    private int lives = 3;
    private String gameState = "MENU"; // MENU, RUNNING, GAMEOVER, WIN

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private BufferedImage backgroundImage;


    public void setGameSize(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.scaleX = (float) WIDTH / 800f;
        this.scaleY = (float) HEIGHT / 600f;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        if (backgroundImage != null) {
            backgroundImage = resizeImage(backgroundImage, width, height);
        }
        revalidate(); // cập nhật layout nếu cần
    }

    public GameManager(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        this.scaleX = (float) WIDTH / 800f;
        this.scaleY = (float) HEIGHT / 600f;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);


        backgroundImage = Renderer.loadBgroundTexture();
        if (backgroundImage != null) {
            backgroundImage = resizeImage(backgroundImage, width, height);
        }
        initGame();

        int delay = 1000 / FPS;
        gameTimer = new Timer(delay, this);
        gameTimer.start();
    }

    private void initGame() {
        paddle = new Paddle((WIDTH / 2f - (60 * scaleX)), (HEIGHT - (60 * scaleY)), (int) (120 * scaleX), (int) (16 * scaleY));
        ball = new Ball(WIDTH / 2f - (8 * scaleX), HEIGHT - 80 * scaleY, (int) (16 * scaleY), (int) (16 * scaleY));
        bricks = new  ArrayList<>();
        bricks = Level.buildLevel(currentLevel, WIDTH, HEIGHT, scaleX, scaleY);
        powerUps = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }


    private void updateGame() {
        if (!gameState.equals("RUNNING")) return;

        // xử lý input & di chuyển paddle
        handleInput();
        paddle.update();

        // clamp paddle inside screen
        if (paddle.getX() < 0) paddle.setX(0);
        if (paddle.getX() + paddle.getWidth() > WIDTH) paddle.setX(WIDTH - paddle.getWidth());

        // Ball sticks to paddle until launched
        if (!ball.isLaunched()) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2f - ball.getWidth() / 2f);
            ball.setY(paddle.getY() - ball.getHeight() - 1);
        } else {
            ball.update();
        }

        // update powerups (falling)
        for (PowerUp p : powerUps) p.update();

        // collisions (ball vs walls / paddle / bricks / powerups)
        ball.checkCollisions(paddle, bricks);

        // remove expired/collected powerups from list
        powerUps.removeIf(PowerUp::isCollectedOrOffscreen);

        // check win/lose
        if (bricks.isEmpty()) {
            currentLevel++;
            if (currentLevel > 3) {
                gameState = "WIN";
            } else {
                ball.resetToPaddle(paddle);
                ball.setDx(0);
                ball.setDy(0);
            }
        }
        if (lives <= 0) {
            gameState = "GAMEOVER";
        }
        ExplosiveBallPowerUp.updateExplosions();
    }


    private void handleInput() {
        float sp = paddle.getSpeed();
        if (leftPressed && !rightPressed) paddle.setDx(-sp);
        else if (rightPressed && !leftPressed) paddle.setDx(sp);
        else paddle.setDx(0);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // background
        Graphics2D g2 = (Graphics2D) g.create();
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // draw HUD
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, (int) (18 * scaleY)));
        g2.drawString("Score: " + score, (int)(12 * scaleX), (int) (22 * scaleY));
        g2.drawString("Lives: " + lives, WIDTH - (int) (90 * scaleY), (int) (22 *  scaleY));

        // draw paddles, ball, bricks, powerups
        paddle.render(g2);
        ball.render(g2);

        for (Brick b : bricks) b.render(g2);
        for (PowerUp p : powerUps) p.render(g2);
        ExplosiveBallPowerUp.drawExplosions(g2);


        // overlays
        if (gameState.equals("MENU")) {
            drawCenteredString(g2, "PRESS SPACE TO START", WIDTH, HEIGHT);
        } else if (gameState.equals("GAMEOVER")) {
            drawCenteredString(g2, "GAME OVER - PRESS R TO RESTART", WIDTH, HEIGHT);
        } else if (gameState.equals("WIN")) {
            drawCenteredString(g2, "YOU WIN! PRESS R TO RESTART", WIDTH, HEIGHT);
        }

        g2.dispose();
    }

    private void drawCenteredString(Graphics2D g2, String text, int w, int h) {
        g2.setColor(new Color(0,0,0,160));
        g2.fillRect(0,h/2 - 40, w, 80);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 26));
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        g2.drawString(text, (w - tw)/2, h/2 + fm.getAscent()/2 - 6);
    }

    // KeyListener
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = true;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = true;
        if (kc == KeyEvent.VK_SPACE) {
            if (gameState.equals("MENU")) {
                gameState = "RUNNING";
                ball.resetToPaddle(paddle);
                ball.launch(4f, -4f);
            } else if (gameState.equals("RUNNING")) {
                if (!ball.isLaunched()) ball.launch(4f, -4f);
            }
        }
        if (kc == KeyEvent.VK_R) {
            if (gameState.equals("GAMEOVER") || gameState.equals("WIN")) {
                restart();
            }
        }
        if (kc == KeyEvent.VK_F11) {
            Main.onFullscreen();
            initGame();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = false;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public void restart() {
        score = 0;
        lives = 3;
        initGame();
        gameState = "MENU";
    }

    public void setLevel(int level) {
        this.currentLevel = level;
        restart(); // khởi động lại game với level mới
    }


    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image tmp = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

}