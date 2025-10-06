package Game;

import Objects.*;
import PowerUps.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameManager extends JPanel implements Runnable, KeyListener {
    private final int WIDTH;
    private final int HEIGHT;

    private Thread gameThread;
    private boolean running = false;
    private final int FPS = 60;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

    private int score = 0;
    private int lives = 3;
    private String gameState = "MENU"; // MENU, RUNNING, GAMEOVER, WIN

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private Random rand = new Random();

    public GameManager(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        initGame();
    }

    private void initGame() {
        paddle = new Paddle(WIDTH / 2f - 60, HEIGHT - 60, 120, 16);
        ball = new Ball(WIDTH / 2f - 8, HEIGHT - 80, 16, 16);
        bricks = new ArrayList<>();
        powerUps = new ArrayList<>();
        buildLevel();
    }

    private void buildLevel() {
        bricks.clear();
        int rows = 5;
        int cols = 10;
        int brickW = 64;
        int brickH = 24;
        int padding = 8;
        int offsetX = (WIDTH - (cols * (brickW + padding) - padding)) / 2;
        int offsetY = 60;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int x = offsetX + c * (brickW + padding);
                int y = offsetY + r * (brickH + padding);
                // randomly strong bricks
                if ((r == 0 && c % 4 == 0) || rand.nextDouble() < 0.12) {
                    bricks.add(new StrongBrick(x, y, brickW, brickH));
                } else {
                    bricks.add(new NormalBrick(x, y, brickW, brickH));
                }
            }
        }
    }

    public void startGameThread() {
        if (gameThread == null) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    @Override
    public void run() {
        long drawInterval = 1000000000 / FPS;
        long lastTime = System.nanoTime();
        long delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += now - lastTime;
            lastTime = now;

            if (delta >= drawInterval) {
                updateGame();
                repaint();
                delta = 0;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {}
        }
    }

    private void updateGame() {
        if (gameState.equals("RUNNING")) {
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

            // update powerups
            for (PowerUp p : powerUps) p.update();

            checkCollisions();

            // remove expired powerups from active lists (powerup objects falling)
            powerUps.removeIf(PowerUp::isCollectedOrOffscreen);

            // check win or lose
            if (bricks.isEmpty()) gameState = "WIN";
            if (lives <= 0) gameState = "GAMEOVER";
        }
    }

    private void handleInput() {
        float sp = paddle.getSpeed();
        if (leftPressed && !rightPressed) paddle.setDx(-sp);
        else if (rightPressed && !leftPressed) paddle.setDx(sp);
        else paddle.setDx(0);
    }

    private void checkCollisions() {
        // Ball vs Walls
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(-ball.getDx());
        } else if (ball.getX() + ball.getWidth() >= WIDTH) {
            ball.setX(WIDTH - ball.getWidth());
            ball.setDx(-ball.getDx());
        }
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(-ball.getDy());
        }
        if (ball.getY() > HEIGHT) {
            // lose life
            lives--;
            ball.resetToPaddle(paddle);
        }

        // Ball vs Paddle
        if (ball.intersects(paddle)) {
            // reflect depending on where it hits the paddle
            float paddleCenter = paddle.getX() + paddle.getWidth() / 2f;
            float ballCenter = ball.getX() + ball.getWidth() / 2f;
            float diff = (ballCenter - paddleCenter) / (paddle.getWidth() / 2f); // -1 .. 1
            float maxSpeed = 6f;
            float angle = diff * (float)Math.toRadians(60); // angle deviation
            float speed = (float) Math.hypot(ball.getDx(), ball.getDy());
            speed = Math.max(speed, 4f);
            ball.setDx((float)(Math.sin(angle) * speed));
            ball.setDy((float)(-Math.abs(Math.cos(angle) * speed)));
            // ensure ball is above paddle
            ball.setY(paddle.getY() - ball.getHeight() - 1);
        }

        // Ball vs Bricks
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick b = it.next();
            if (b.isDestroyed()) continue;
            if (ball.intersects(b)) {
                // simple collision response: reverse dy if vertical hit, else dx
                Rectangle overlap = ball.getBounds().intersection(b.getBounds());
                if (overlap.width < overlap.height) {
                    // horizontal overlap -> reflect dx
                    if (ball.getX() < b.getX()) ball.setX(ball.getX() - overlap.width);
                    else ball.setX(ball.getX() + overlap.width);
                    ball.setDx(-ball.getDx());
                } else {
                    // vertical -> reflect dy
                    if (ball.getY() < b.getY()) ball.setY(ball.getY() - overlap.height);
                    else ball.setY(ball.getY() + overlap.height);
                    ball.setDy(-ball.getDy());
                }
                b.takeHit();
                if (b.isDestroyed()) {
                    it.remove();
                    score += 100;
                    // random chance to drop powerup
                    if (rand.nextDouble() < 0.18) {
                        PowerUp pu = rand.nextBoolean()
                                ? new ExpandPaddlePowerUp(b.getX() + b.getWidth()/2f - 12, b.getY() + b.getHeight()/2f, 24, 24, 8_000)
                                : new FastBallPowerUp(b.getX() + b.getWidth()/2f - 12, b.getY() + b.getHeight()/2f, 24, 24, 6_000);
                        powerUps.add(pu);
                    }
                }
                break; // only one brick per update
            }
        }

        // Paddle vs PowerUps (collect)
        Iterator<PowerUp> pit = powerUps.iterator();
        while (pit.hasNext()) {
            PowerUp pu = pit.next();
            if (pu.getY() > HEIGHT) {
                pu.markCollectedOrOffscreen();
                pit.remove();
                continue;
            }
            if (pu.intersects(paddle)) {
                pu.applyEffect(paddle, ball, this);
                pu.markCollectedOrOffscreen();
                pit.remove();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0,0,WIDTH,HEIGHT);

        // draw HUD
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.drawString("Score: " + score, 12, 22);
        g2.drawString("Lives: " + lives, WIDTH - 110, 22);

        // draw paddles, ball, bricks, powerups
        paddle.render(g2);
        ball.render(g2);

        for (Brick b : bricks) b.render(g2);
        for (PowerUp p : powerUps) p.render(g2);

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
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = false;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public void increaseScore(int v) { score += v; }
    public void restart() {
        score = 0;
        lives = 3;
        initGame();
        gameState = "MENU";
    }
}
