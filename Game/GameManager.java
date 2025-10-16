package Game;

import Objects.*;
import PowerUps.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class GameManager extends JPanel implements KeyListener, ActionListener {
    private int WIDTH;
    private int HEIGHT;

    private float scaleX;
    private float scaleY;

    private Timer gameTimer;
    private final int FPS = 60;
    private int currentLevel = 1;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

    private int score = 0;
    private int lives = 3;
    private String gameState = "MENU"; // MENU, RUNNING, GAMEOVER, WIN

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private BufferedImage backgroundImage;

    private Random rand = new Random();

    // Constants để tránh magic numbers
    public static final float MAX_BOUNCE_ANGLE = 60f;
    public static final float MIN_ANGLE = 15f;
    public static final float MAX_ANGLE = 165f;
    public static final float VERTICAL_ANGLE = 90f;
    public static final float EPSILON = 0.001f; // Để so sánh float

    // Cache để tránh tính toán lại

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
        requestFocus();
        addKeyListener(this);

        setFocusable(true);
        requestFocusInWindow();

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

        checkCollisions(paddle, bricks);

        // remove expired/collected powerups from list
        powerUps.removeIf(PowerUp::isCollectedOrOffscreen);

        // check win/lose
        if (bricks.isEmpty()) {
            currentLevel++;
            if (currentLevel > 3) {
                gameState = "WIN";
            } else {
                ball.resetToPaddle(paddle); // reset sets launched=false already
                // optional: ensure ball not moving
                ball.setDx(0); ball.setDy(0);
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

    public void checkCollisions(Paddle paddle, List<Brick> bricks) {
        if (!ball.launched) return;

        // Kiểm tra va chạm với tường
        checkWallCollisions();

        // Kiểm tra va chạm với paddle
        checkPaddleCollision(paddle);

        // Kiểm tra va chạm với bricks
        checkBrickCollisions(bricks);
    }

    private void checkWallCollisions() {
        boolean collided = false;

        // Tường trái
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(Math.abs(ball.getDx()));
            collided = true;
        }
        // Tường phải
        else if (ball.getX() + ball.getWidth() >= WIDTH) {
            ball.setX(WIDTH - ball.getWidth());
            ball.setDx(-Math.abs(ball.getDx()));
            collided = true;
        }

        // Tường trên
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(Math.abs(ball.getDy()));
            collided = true;
        }
        // Tường dưới
        else if (ball.getY() + ball.getHeight() >= HEIGHT) {
            ball.setY(HEIGHT - ball.getHeight());
            ball.setDy(-Math.abs(ball.getDy()));
            Ball.launched = false;
            ball.setDx(0);
            ball.setDy(0);
            lives--;
            return;

        }

        if (collided) {
            normalizeVelocity();
        }
    }

    private void checkPaddleCollision(Paddle paddle) {
        Rectangle paddleRect = paddle.getBounds();

        float ballCenterX = ball.getCenterX();
        float ballCenterY = ball.getCenterY();
        float ballBottom = ball.getY() + ball.getHeight();

        float paddleTop = paddleRect.y;
        float paddleBottom = paddleRect.y + paddleRect.height;
        float paddleLeft = paddleRect.x;
        float paddleRight = paddleRect.x + paddleRect.width;

        // Kiểm tra overlap
        boolean overlapX = ballCenterX >= paddleLeft && ballCenterX <= paddleRight;
        boolean overlapY = ballBottom >= paddleTop && ball.getY() <= paddleBottom;

        if (!overlapX || !overlapY) return;

        float prevY = ball.getY() - ball.getDy();
        float prevBottom = prevY + ball.getHeight();

        // Va chạm từ trên xuống
        if (ball.getDy() > 0 && prevBottom <= paddleTop) {
            handlePaddleTopCollision(paddle, paddleRect, ballCenterX);
        }
        // Va chạm từ bên
        else {
            handlePaddleSideCollision(paddleRect, ballCenterX, ballCenterY);
        }
    }

    private void handlePaddleTopCollision(Paddle paddle, Rectangle paddleRect, float ballCenterX) {
        // Đặt bóng lên trên paddle
        ball.setY(paddleRect.y - ball.getHeight() - 0.5f);

        // Tính góc phản xạ dựa trên vị trí va chạm
        float paddleCenter = paddleRect.x + paddleRect.width / 2f;
        float hitPosition = (ballCenterX - paddleCenter) / (paddleRect.width / 2f);

        // Clamp hitPosition trong khoảng [-1, 1]
        hitPosition = Math.max(-1f, Math.min(1f, hitPosition));

        // Tính góc output (90° = thẳng lên, giảm dần về 2 bên)
        float angleInDegrees = VERTICAL_ANGLE - hitPosition * MAX_BOUNCE_ANGLE;

        // Clamp angle để tránh góc quá ngang
        angleInDegrees = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, angleInDegrees));

        // Convert sang radians và set velocity mới
        double angleInRadians = Math.toRadians(angleInDegrees);
        float speedMagnitude = ball.getSpeed();

        ball.setDx((float) (speedMagnitude * Math.cos(angleInRadians)));
        ball.setDy(-(float) (speedMagnitude * Math.sin(angleInRadians)));// Âm vì đi lên

        // Đảm bảo dy luôn âm (đi lên)
        if (ball.getDy() > 0) {
            ball.setDy(-ball.getDy());
        }
    }

    private void handlePaddleSideCollision(Rectangle paddleRect, float ballCenterX, float ballCenterY) {
        float paddleLeft = paddleRect.x;
        float paddleRight = paddleRect.x + paddleRect.width;

        float prevX = ball.getX() - ball.getDx();
        float prevCenterX = prevX + ball.getRadius();

        // Xác định va chạm bên trái hay phải
        boolean hitFromLeft = prevCenterX < paddleLeft && ballCenterX >= paddleLeft;
        boolean hitFromRight = prevCenterX > paddleRight && ballCenterX <= paddleRight;

        if (hitFromLeft) {
            ball.setX(paddleLeft - ball.getWidth() - 0.5f);
            ball.setDx(-Math.abs(ball.getDx()));
            normalizeVelocity();
        } else if (hitFromRight) {
            ball.setX(paddleRight + 0.5f);
            ball.setDx(Math.abs(ball.getDx()));
            normalizeVelocity();
        }
    }

    public float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public boolean circleCheckCollision(Rectangle rect) {
        float closestX = clamp(ball.getCenterX(), rect.x,rect.x + rect.width );
        float closestY = clamp(ball.getCenterY(), rect.y,rect.y + rect.height);

        float dX = ball.getCenterX() - closestX;
        float dY = ball.getCenterY() - closestY;

        return (dX * dX + dY * dY) < (ball.getRadius() * ball.getRadius());
    }

    private void checkBrickCollisions(List<Brick> bricks) {
        Rectangle ballRect = getBounds();
        float ballCenterX = ball.getCenterX();
        float ballCenterY = ball.getCenterY();

        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            Rectangle brickRect = brick.getBounds();

            if (!circleCheckCollision(brickRect)) continue;

            // Tính vị trí tương đối của ball với brick
            float brickCenterX = brickRect.x + brickRect.width / 2f;
            float brickCenterY = brickRect.y + brickRect.height / 2f;

            float deltaX = ballCenterX - brickCenterX;
            float deltaY = ballCenterY - brickCenterY;

            // Tính overlap cho mỗi cạnh
            float overlapX = (brickRect.width / 2f + ball.getRadius()) - Math.abs(deltaX);
            float overlapY = (brickRect.height / 2f + ball.getRadius()) - Math.abs(deltaY);

            // Va chạm theo trục có overlap nhỏ hơn
            if (overlapX < overlapY) {
                // Va chạm ngang (trái/phải)
                if (deltaX > 0) {
                    // Va chạm từ bên trái brick
                    ball.setX(brickRect.x + brickRect.width + 0.5f);
                } else {
                    // Va chạm từ bên phải brick
                    ball.setX(brickRect.x - ball.getWidth() - 0.5f);
                }
                ball.setDx(-ball.getDx());
            } else {
                // Va chạm dọc (trên/dưới)
                if (deltaY > 0) {
                    // Va chạm từ trên brick
                    ball.setY(brickRect.y + brickRect.height + 0.5f);
                } else {
                    // Va chạm từ dưới brick
                    ball.setY(brickRect.y - ball.getHeight() - 0.5f);
                }
                ball.setDy(-ball.getDy());
            }

            // Normalize lại velocity để giữ tốc độ ổn định
            normalizeVelocity();

            // Xử lý brick
            brick.takeHit();
            if (ball.hasTripleDamage() && !brick.isDestroyed()) {
                brick.takeHit();
                brick.takeHit();
            }
            if (brick.isDestroyed()) {
                it.remove();
                score += 100;
                if (ball.isExplosive()) {
                    float explosionRadius = 80f * scaleX; // bán kính nổ (tuỳ chỉnh)
                    ExplosiveBallPowerUp.explodeAt(bricks, brick.getX() + brick.getWidth()/2f, brick.getY() + brick.getHeight()/2f, explosionRadius);
                }
                if (rand.nextDouble() < 0.2) {
                    int type = rand.nextInt(3); // 0,1,2
                    PowerUp pu;
                    if (type == 0) {
                        pu = new ExpandPaddlePowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                24, 24, 8_000);
                    } else if (type == 1) {
                        pu = new FastBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                24, 24, 6_000);
                    } else { // type == 2
                        pu = new BigBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                24, 24, 7_000);
                    }
                    powerUps.add(pu);
                }
            }
        }
    }

    /**
     * Normalize velocity để duy trì tốc độ ổn định
     * Fix bug: tốc độ bóng tăng/giảm sau nhiều lần va chạm
     */
    private void normalizeVelocity() {
        float currentMagnitude = (float) Math.hypot(ball.getDx(), ball.getDy());
        if (currentMagnitude > EPSILON && Math.abs(currentMagnitude - ball.getSpeed()) > EPSILON) {
            ball.setDx((ball.getDx() / currentMagnitude) * ball.getSpeed());
            ball.setDy((ball.getDy() / currentMagnitude) * ball.getSpeed());
        }
    }

    /**
     * Kiểm tra nếu bóng bị stuck (vận tốc quá nhỏ hoặc góc quá ngang)
     * Tự động fix bằng cách đẩy bóng đi lên
     */
    public void checkAndFixStuck() {
        if (!Ball.launched) return;

        float currentSpeed = (float) Math.hypot(ball.getDx(), ball.getDy());

        // Nếu tốc độ quá chậm
        if (currentSpeed < ball.getSpeed() * 0.5f) {
            ball.launch(0, -1); // Đẩy bóng đi thẳng lên
        }

        // Nếu góc quá ngang (dy quá nhỏ so với dx)
        if (Math.abs(ball.getDy()) < Math.abs(ball.getDx()) * 0.1f) {
            float direction = ball.getDy() >= 0 ? 1 : -1;
            ball.setDy(direction * Math.abs(ball.getDx()) * 0.3f);
            normalizeVelocity();
        }
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

    public void increaseScore(int v) { score += v; }
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