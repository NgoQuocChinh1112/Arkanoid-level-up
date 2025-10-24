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
    private final GamePanel parent;

    private int WIDTH;
    private int HEIGHT;

    private Timer gameTimer;
    private int currentLevel = 1;

    private Paddle paddle1;
    private Paddle paddle2;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerUps;

    private int score = 0;
    private int lives = 3;
    private String gameState = "MENU"; // MENU, RUNNING, GAMEOVER, WIN, PAUSED
    private boolean twoPlayerMode = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;


    private BufferedImage backgroundImage;
    private final BufferedImage[] button = Renderer.loadbuttonTexture();
    private boolean hoverResume = false;
    private boolean hoverMenu = false;
    private boolean hoverLs = false;

    private Random rand = new Random();

    // Constants để tránh magic numbers
    public static final float MAX_BOUNCE_ANGLE = 60f;
    public static final float MIN_ANGLE = 15f;
    public static final float MAX_ANGLE = 165f;
    public static final float VERTICAL_ANGLE = 90f;
    public static final float EPSILON = 0.001f; // Để so sánh float

    public boolean getTwoPlayerMode() {
        return twoPlayerMode;
    }

    public void setTwoPlayerMode() {
        twoPlayerMode = !twoPlayerMode;
        initGame();
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

    // Cache để tránh tính toán lại

    public void setGameSize(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        if (backgroundImage != null) {
            backgroundImage = resizeImage(backgroundImage, width, height);
        }
        revalidate(); // cập nhật layout nếu cần
    }

    public GameManager(GamePanel parent, int width, int height) {
        this.parent = parent;
        this.WIDTH = width;
        this.HEIGHT = height;
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        requestFocus();
        addKeyListener(this);

        setFocusable(true);
        requestFocusInWindow();

        backgroundImage = Renderer.loadBgroundTexture(currentLevel);
        if (backgroundImage != null) {
            backgroundImage = resizeImage(backgroundImage, width, height);
        }
        initGame();

        int FPS = 60;
        int delay = 1000 / FPS;
        gameTimer = new Timer(delay, this);
        gameTimer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (gameState.equals("RUNNING")) {
                    int butW = (int)(30 * GamePanel.scaleY), butH = (int)(30 * GamePanel.scaleY);
                    int butX = WIDTH - (int)(50 * GamePanel.scaleY);
                    int butY = (int)(20 * GamePanel.scaleY);
                    Rectangle buttonRect = new Rectangle(butX, butY, butW, butH);
                    if (buttonRect.contains(p)) {
                        gameState = "PAUSED";
                    }
                }
                if (gameState.equals("PAUSED")) {
                    int boxX = (WIDTH - (int)(300 * GamePanel.scaleY)) / 2;
                    int boxY = (HEIGHT - (int)(400 * GamePanel.scaleY)) / 2;
                    int btnW =(int)(180 * GamePanel.scaleY), btnH = (int)(50 * GamePanel.scaleY);
                    int resumeY = boxY + (int) (60 * GamePanel.scaleY);
                    int resY = resumeY + (int) (60 * GamePanel.scaleY);
                    int menuY = resY + (int) (60 * GamePanel.scaleY);
                    int btnX = boxX + ((int)(300 * GamePanel.scaleY) - btnW) / 2;

                    Rectangle resumeRect = new Rectangle(btnX, resumeY, btnW, btnH);
                    Rectangle menuRect = new Rectangle(btnX, menuY, btnW, btnH);
                    Rectangle LsRect = new Rectangle(btnX, resY, btnW, btnH);
                    if (resumeRect.contains(p)) {
                        gameState = "RUNNING"; // tiếp tục
                    } else if (menuRect.contains(p)) {
                        parent.showMenu();
                    } else if(LsRect.contains(p)) {
                        restart();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int boxW = (int)(300 * GamePanel.scaleY), boxH = (int)(400 * GamePanel.scaleY);
                int boxX = (WIDTH - boxW) / 2;
                int boxY = (HEIGHT - boxH) / 2;
                int btnW =(int)(180 * GamePanel.scaleY), btnH = (int)(50 * GamePanel.scaleY);
                int resumeY = boxY + (int) (60 * GamePanel.scaleY);
                int resY = resumeY + (int) (60 * GamePanel.scaleY);
                int menuY = resY + (int) (60 * GamePanel.scaleY);
                int btnX = boxX + (boxW - btnW) / 2;

                Rectangle resumeRect = new Rectangle(btnX, resumeY, btnW, btnH);
                Rectangle menuRect = new Rectangle(btnX, menuY, btnW, btnH);
                Rectangle LsRect = new Rectangle(btnX, resY, btnW, btnH);
                boolean oldHoverResume = hoverResume;
                hoverResume = resumeRect.contains(p);
                boolean oldHoverMenu = hoverMenu;
                hoverMenu = menuRect.contains(p);
                boolean oldHoverLs = hoverLs;
                hoverLs = LsRect.contains(p);
                if (oldHoverResume != hoverResume || oldHoverMenu != hoverMenu
                        || oldHoverLs != hoverLs) {
                    repaint();
                }
            }
        });
    }

    private void showMenu(Graphics g) {
        // lớp phủ mờ
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // khung menu pause
        int boxW = (int)(300 * GamePanel.scaleY), boxH = (int)(400 * GamePanel.scaleY);
        int boxX = (WIDTH - boxW) / 2;
        int boxY = (HEIGHT - boxH) / 2;
        if (button[0] != null) {
            g.drawImage(button[0], boxX, boxY, boxW, boxH, null);
        } else {
            g.setColor(new Color(255, 255, 255, 180));
            g.fillRoundRect(boxX, boxY, boxW, boxH, 30, 30);
        }

        int btnW =(int)(180 * GamePanel.scaleY), btnH = (int)(50 * GamePanel.scaleY);
        int resumeY = boxY + (int) (60 * GamePanel.scaleY);
        int resY = resumeY + (int) (60 * GamePanel.scaleY);
        int menuY = resY + (int) (60 * GamePanel.scaleY);
        int btnX = boxX + (boxW - btnW) / 2;

        // Vẽ 2 nút (ảnh hoặc chữ)
        if (button[4] != null && hoverResume) {
            g.drawImage(button[4], btnX, resumeY, btnW, btnH, null);

        } else if (button[5] != null) {
            g.drawImage(button[5], btnX, resumeY, btnW, btnH, null);
        }
        if (button[2] != null && hoverMenu) {
            g.drawImage(button[2], btnX, menuY, btnW, btnH, null);
        } else if (button[3] != null) {
            g.drawImage(button[3], btnX, menuY, btnW, btnH, null);
        }
        if (button[6] != null && hoverLs) {
            g.drawImage(button[6], btnX, resY, btnW, btnH, null);
        } else if (button[7] != null) {
            g.drawImage(button[7], btnX, resY, btnW, btnH, null);
        }
    }

    //todo
    private void initGame() {
        System.out.println(WIDTH + " "  + HEIGHT);
        paddle1 = new Paddle((WIDTH / 2f - (int)(60 * GamePanel.scaleY)), HEIGHT - (int)(60 * GamePanel.scaleY), (int)(120 * GamePanel.scaleY), (int)(16 * GamePanel.scaleY));
        if(twoPlayerMode) {
            paddle2 = new Paddle((WIDTH / 2f - (int)(60 * GamePanel.scaleY)), HEIGHT - (int)(140 * GamePanel.scaleY), (int)(120 * GamePanel.scaleY), (int)(16 * GamePanel.scaleY));
        }    

        if (twoPlayerMode) {
            // Bóng gắn với paddle2
            float bx = paddle2.getX() + paddle2.getWidth() / 2f - (int)(8 * GamePanel.scaleY);
            float by = paddle2.getY() - (int)(16 * GamePanel.scaleY) - 1;
            ball = new Ball(bx, by, (int)(16 * GamePanel.scaleY), (int)(16 * GamePanel.scaleY));
        } else {
            // Bóng gắn với paddle1
            float bx = paddle1.getX() + paddle1.getWidth() / 2f - (int)(8 * GamePanel.scaleY);
            float by = paddle1.getY() - (int)(16 * GamePanel.scaleY) - 1;
            ball = new Ball(bx, by, (int)(16 * GamePanel.scaleY), (int)(16 * GamePanel.scaleY));
        }

        bricks = new  ArrayList<>();
        bricks = Level.buildLevel(currentLevel, WIDTH, HEIGHT, GamePanel.scaleY, GamePanel.scaleY);
        powerUps = new ArrayList<>();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    //todo
    private void updateGame() {
        if (!gameState.equals("RUNNING")) return;

        // xử lý input & di chuyển paddle
        handleInput();
        paddle1.update();
        if(twoPlayerMode) paddle2.update();
        // clamp paddle inside screen
        if (paddle1.getX() < 0) paddle1.setX(0);
        if (paddle1.getX() + paddle1.getWidth() > WIDTH) paddle1.setX(WIDTH - paddle1.getWidth());
        if (twoPlayerMode) {
            if (paddle2.getX() < 0) paddle2.setX(0);
            if (paddle2.getX() + paddle2.getWidth() > WIDTH) paddle2.setX(WIDTH - paddle2.getWidth());
        }

        // Ball sticks to paddle until launched
        if (!ball.isLaunched()) {
            if (twoPlayerMode) {
            ball.setX(paddle2.getX() + paddle2.getWidth() / 2f - ball.getWidth() / 2f);
            ball.setY(paddle2.getY() - ball.getHeight() - 1);
        } else {
            ball.setX(paddle1.getX() + paddle1.getWidth() / 2f - ball.getWidth() / 2f);
            ball.setY(paddle1.getY() - ball.getHeight() - 1);
        }
        } else {
            ball.update();
        }

        // update powerups (falling)if (twoPlayerMode) {
        for (PowerUp p : powerUps) p.update();

        checkCollisions(paddle1, bricks, powerUps);
        if (twoPlayerMode) checkCollisions(paddle2, bricks, powerUps);


        // remove expired/collected powerups from list
        powerUps.removeIf(PowerUp::isCollectedOrOffscreen);

        // check win/lose
        if (bricks.isEmpty()) {
            currentLevel++;
            if (currentLevel > 3) {
                gameState = "WIN";
            } else {
                if(!twoPlayerMode) ball.resetToPaddle(paddle1); // reset sets launched=false already
                else ball.resetToPaddle(paddle2);
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
        float sp = paddle1.getSpeed();
        if (leftPressed && !rightPressed) paddle1.setDx(-sp);
        else if (rightPressed && !leftPressed) paddle1.setDx(sp);
        else paddle1.setDx(0);

        if (twoPlayerMode) {
        if (aPressed && !dPressed) paddle2.setDx(-sp);
        else if (dPressed && !aPressed) paddle2.setDx(sp);
        else paddle2.setDx(0);
        }
    }

    public void checkCollisions(Paddle paddle, List<Brick> bricks, List<PowerUp> powerUps) {
        if (!ball.launched) return;

        // Kiểm tra va chạm với tường
        checkWallCollisions();

        // Kiểm tra va chạm với paddle
        checkPaddleCollision(paddle);

        // Kiểm tra va chạm với bricks, powerup
        checkBrickCollisions(bricks, powerUps);
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

        // 🟡 BỎ QUA va chạm với mặt dưới paddle2 nếu bóng đi lên
        if (twoPlayerMode && paddle == paddle2 && ball.getDy() < 0) {
            // Bóng đang đi lên, chạm đáy paddle2 → bỏ qua
            return;
        }

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

    private void checkBrickCollisions(List<Brick> bricks, List<PowerUp> powerUps) {
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
            if (ball.isEnlarged() && !brick.isDestroyed()) {
                brick.takeHit();
                brick.takeHit();
            }
            if (brick.isDestroyed()) {
                it.remove();
                score += 100;
                if (ball.isExplosive()) {
                    float explosionRadius = 80f * GamePanel.scaleY; // bán kính nổ (tuỳ chỉnh)
                    ExplosiveBallPowerUp.explodeAt(bricks, brick.getX() + brick.getWidth()/2f, brick.getY() + brick.getHeight()/2f, explosionRadius);
                }
                if (rand.nextDouble() < 0.2) {
                    int type = rand.nextInt(3); // 0,1,2
                    PowerUp pu = null  ;
                    if (type == 0) {
                        pu = new ExpandPaddlePowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * GamePanel.scaleY), (int)(24 * GamePanel.scaleY), 8_000);
                    } else if (type == 1) {
                        pu = new FastBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * GamePanel.scaleY), (int)(24 * GamePanel.scaleY), 6_000);
                    } else if ( type == 2) {
                        pu = new BigBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * GamePanel.scaleY), (int)(24 * GamePanel.scaleY), 7_000);
                    } else if (type == 3) {
                        pu = new ExplosiveBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                24, 24, 6_000);
                    }
                    if (pu != null) {

                        powerUps.add(pu);
                    }

                }
            }
        }
        Iterator<PowerUp> pit = powerUps.iterator();
        while (pit.hasNext()) {
            PowerUp pu = pit.next();
            if (pu.getY() > HEIGHT) {
                pu.markCollectedOrOffscreen();
                pit.remove();
                continue;
            }
            if (pu.intersects(paddle1)) {
                pu.applyEffect(paddle1, ball, this);
                pu.markCollectedOrOffscreen();
                pit.remove();
            }
            if (twoPlayerMode) {
                if (pu.intersects(paddle2)) {
                    pu.applyEffect(paddle2, ball, this);
                    pu.markCollectedOrOffscreen();
                    pit.remove();
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
        g2.setFont(new Font("Arial", Font.PLAIN, (int) (18 * GamePanel.scaleY)));
        g2.drawString("Score: " + score, (int)(12 * GamePanel.scaleY), (int) (22 * GamePanel.scaleY));
        g2.drawString("Lives: " + lives, (int)(12* GamePanel.scaleY), (int) (44* GamePanel.scaleY));

        // draw paddles, ball, bricks, powerups
        paddle1.render(g2);
        if (twoPlayerMode) paddle2.render(g2);

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
        } else if (gameState.equals("PAUSED")) {
            showMenu(g2);
        } else if (gameState.equals("RUNNING")) {
            buttonMenu(g2);
        }
    }

    private void buttonMenu (Graphics g) {
        int butW = (int)(30 * GamePanel.scaleY), butH = (int)(30 * GamePanel.scaleY);
        int butX = WIDTH - (int)(50 * GamePanel.scaleY);
        int butY = (int)(20 *  GamePanel.scaleY);
        if (button[1] != null) {
            g.drawImage(button[1], butX, butY, butW, butH, null);
        }
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
    @Override // todo
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = true;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = true;
        if (kc == KeyEvent.VK_A) aPressed = true;
        if (kc == KeyEvent.VK_D) dPressed = true;
        if (kc == KeyEvent.VK_SPACE) {
            if (gameState.equals("MENU")) {
                gameState = "RUNNING";
                if (twoPlayerMode) ball.resetToPaddle(paddle2);
                else if (!twoPlayerMode) ball.resetToPaddle(paddle1);
                ball.launch(4f, -4f);
            } else if (gameState.equals("RUNNING")) {
                if (!ball.isLaunched()) ball.launch(4f, -4f);
            }
        }
        if (kc == KeyEvent.VK_P) {
            if (gameState.equals("RUNNING")) {
                gameState = "PAUSED";
            } else if (gameState.equals("PAUSED")) {
                gameState = "RUNNING";
            }
        }
        if (kc == KeyEvent.VK_R) {
            if (gameState.equals("GAMEOVER") || gameState.equals("WIN")) {
                restart();
            }
        }
    }
    @Override // todo
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = false;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = false;
        if (kc == KeyEvent.VK_A) aPressed = false;
        if (kc == KeyEvent.VK_D) dPressed = false;
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
        backgroundImage = Renderer.loadBgroundTexture(currentLevel);
        if (backgroundImage != null) {
            backgroundImage = resizeImage(backgroundImage, WIDTH, HEIGHT);
        }
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