package Game;

import Objects.*;
import PowerUps.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static Game.GamePanel.scale;

public class GameManager extends JPanel implements KeyListener, ActionListener {
    private final GamePanel parent;

    private int WIDTH;
    private int HEIGHT;

    private final Timer gameTimer;
    private final int FPS = 60;
    private int currentLevel = 1;

    private Paddle paddle1;
    private Paddle paddle2;
    private Ball mainBall;                          // Bóng chính
    private final List<Ball> balls = new ArrayList<>();   // Danh sách tất cả bóng (chính + phụ)
    public Ball extraBall = null;

    private List<Brick> bricks;
    private List<PowerUp> powerUps;
    private final List<Brick> toRemove = new ArrayList<>();

    private int score = 0;
    private int lives = 3;
    private String gameState = "MENU"; // MENU, RUNNING, LOSE, WIN, PAUSED

    private boolean twoPlayerMode = false;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    private float launchAngle = 90f;
    private final float angleSpeed = 90f;
    private boolean angleSweepingRight = true;

    private static final float MIN_LAUNCH_ANGLE = 0f;
    private static final float MAX_LAUNCH_ANGLE = 180F;

    private BufferedImage backgroundImage;
    private final BufferedImage[] button = Renderer.loadbuttonTexture();
    private boolean hoverResume = false;
    private boolean hoverMenu = false;
    private boolean hoverLs = false;

    private static boolean switchVol = true;

    private final Random rand = new Random();

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

    public void addLife() {
        lives++;
    }

    public void setCurrentLevel(int level) {
        this.currentLevel = level;
    }

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

        int delay = 1000 / FPS;
        gameTimer = new Timer(delay, this);
        gameTimer.start();
        SoundEffect.setVolume(6);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (gameState.equals("RUNNING")) {
                    int butW = (int)(30 * scale), butH = (int)(30 * scale);
                    int pauX = WIDTH - (int)(50 * GamePanel.scale);
                    int volX = pauX - (int)(40 * scale);
                    int butY = (int)(20 * scale);
                    Rectangle pauRect = new Rectangle(pauX, butY, butW, butH);
                    Rectangle volRect = new Rectangle(volX, butY, butW, butH);
                    if (pauRect.contains(p)) {
                        gameState = "PAUSED";
                    }
                    if (volRect.contains(p)) {
                        switchVol = !switchVol;
                        repaint();
                        if (switchVol) {
                            SoundEffect.setVolume(6);
                        } else {
                            SoundEffect.setVolume(-80);
                        }
                        repaint();
                    }
                }
                if (gameState.equals("PAUSED") || gameState.equals("LOSE") || gameState.equals("WIN")) {
                    int boxX = (WIDTH - (int) (300 * scale)) / 2;
                    int boxY = (HEIGHT - (int) (245 * scale)) / 2;
                    int btnW = (int) (180 * scale);
                    int btnH = (int) (50 * scale);
                    int resY = boxY + (int) (35 * scale);
                    int resumeY = resY + (int) (60 * scale);
                    int menuY = resumeY + (int) (60 * scale);
                    int btnX = boxX + ((int) (300 * scale) - btnW) / 2;

                    Rectangle resumeRect = new Rectangle(btnX, resumeY, btnW, btnH);
                    Rectangle LsRect = new Rectangle(btnX, resY, btnW, btnH);
                    Rectangle menuRect = new Rectangle(btnX, menuY, btnW, btnH);

                    if (resumeRect.contains(p)) {
                        switch (gameState) {
                            case "PAUSED" -> gameState = "RUNNING";
                            case "LOSE" -> parent.showLevelPanel();
                            case "WIN" -> {
                                currentLevel++;
                                setLevel(currentLevel);
                            }
                        }
                    } else if (menuRect.contains(p)) {
                        parent.showMenu();
                    } else if (LsRect.contains(p)) {
                        restart();
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                if (gameState.equals("PAUSED") || gameState.equals("LOSE") || gameState.equals("WIN")) {
                    int boxW = (int) (300 * scale);
                    int boxH = (int) (245 * GamePanel.scale);
                    int boxX = (WIDTH - boxW) / 2;
                    int boxY = (HEIGHT - boxH) / 2;
                    int btnW = (int) (180 * scale);
                    int btnH = (int) (50 * GamePanel.scale);
                    int resY = boxY + (int) (35 * GamePanel.scale);
                    int resumeY = resY + (int) (60 * GamePanel.scale);
                    int menuY = resumeY + (int) (60 * GamePanel.scale);
                    int btnX = boxX + (boxW - btnW) / 2;

                    Rectangle resumeRect = new Rectangle(btnX, resumeY, btnW, btnH);
                    Rectangle LsRect = new Rectangle(btnX, resY, btnW, btnH);
                    Rectangle menuRect = new Rectangle(btnX, menuY, btnW, btnH);

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
            }
        });
    }

    private void showMenu(Graphics g) {
        // lớp phủ mờ
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        if (gameState.equals("LOSE")) {
            int wGameOver = (int)(400 * scale);
            int hGameOver = (int)(100 * GamePanel.scale);
            int x = (int)((WIDTH - wGameOver) / 2f);
            int y = (int)(0 * GamePanel.scale);
            if ((System.currentTimeMillis() / 400) % 2 == 0) {
                g.drawImage(Renderer.loadGameOverTexture(), x, y, wGameOver, hGameOver, null);
            }
        }

        // khung menu pause
        int boxW = (int)(300 * GamePanel.scale);
        int boxH = (int)(245 * GamePanel.scale);
        int boxX = (WIDTH - boxW) / 2;
        int boxY = (HEIGHT - boxH) / 2;
        if (button[0] != null) {
            g.drawImage(button[0], boxX, boxY, boxW, boxH, null);
        } else {
            g.setColor(new Color(255, 255, 255, 180));
            g.fillRoundRect(boxX, boxY, boxW, boxH, 30, 30);
        }

        int btnW =(int)(180 * GamePanel.scale), btnH = (int)(50 * GamePanel.scale);
        int resY = boxY + (int) (35 * GamePanel.scale);
        int resumeY = resY + (int) (60 * GamePanel.scale);
        int menuY = resumeY + (int) (60 * GamePanel.scale);
        int btnX = boxX + (boxW - btnW) / 2;

        // Vẽ 2 nút (ảnh hoặc chữ)
        if (gameState.equals("PAUSED")) {
            if (button[4] != null && hoverResume) {
                g.drawImage(button[4], btnX, resumeY, btnW, btnH, null);
            } else if (button[5] != null) {
                g.drawImage(button[5], btnX, resumeY, btnW, btnH, null);
            }
        } else if (gameState.equals("LOSE")) {
            if (button[10] != null && hoverResume) {
                g.drawImage(button[10], btnX, resumeY, btnW, btnH, null);
            } else if (button[11] != null) {
                g.drawImage(button[11], btnX, resumeY, btnW, btnH, null);
            }
        } else if (gameState.equals("WIN")) {
            if (button[12] != null && hoverResume) {
                g.drawImage(button[12], btnX, resumeY, btnW, btnH, null);
            } else if (button[13] != null) {
                g.drawImage(button[13], btnX, resumeY, btnW, btnH, null);
            }
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

    private void initGame() {
        paddle1 = new Paddle((WIDTH / 2f - (int)(60 * scale)), HEIGHT - (int)(60 * GamePanel.scale), (int)(120 * scale), (int)(16 * GamePanel.scale));
        if(twoPlayerMode) {
            paddle2 = new Paddle((WIDTH / 2f - (int)(60 * scale)), HEIGHT - (int)(140 * GamePanel.scale), (int)(120 * scale), (int)(16 * GamePanel.scale));
        }

        float bx, by;
        if (twoPlayerMode) {
            // Bóng gắn với paddle2
            bx = paddle2.getX() + paddle2.getWidth() / 2f - (int)(8 * scale);
            by = paddle2.getY() - (int)(16 * scale) - 1;
        } else {
            // Bóng gắn với paddle1
            bx = paddle1.getX() + paddle1.getWidth() / 2f - (int)(8 * scale);
            by = paddle1.getY() - (int)(16 * scale) - 1;
        }
        mainBall = new Ball(bx, by, (int)(16 * scale), (int)(16 * scale));
        mainBall.resetToPaddle(twoPlayerMode ? paddle2 : paddle1);

        balls.clear();
        balls.add(mainBall);
        extraBall = null;

        bricks = Level.buildLevel(currentLevel, WIDTH, HEIGHT, scale, scale);
        powerUps = new ArrayList<>();
        toRemove.clear();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    private boolean checkWin(List<Brick> bricks) {
        for (Brick brick : bricks) {
            if (brick.getHitPoints() < 6 && brick.getHitPoints() > 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkLose(int live) {
        return live <= 0;
    }

    private void updateGame() {
        if (gameState.equals("MENU") || gameState.equals("RUNNING")) {
            if (!mainBall.isLaunched()) {
                updateLaunchAngle();
            }
            // xử lý input & di chuyển paddle
            handleInput();
            paddle1.update();
            for (int i = 0; i < bricks.size(); i++) {
                if (!bricks.get(i).isDestroyed()) {
                    bricks.get(i).update();
                    checkBrickWithWall(bricks.get(i));
                    for (int j = i + 1; j < bricks.size(); j++ ) {
                        checkBrickHeadOn(bricks.get(i), bricks.get(j));
                        checkBrickCross(bricks.get(i), bricks.get(j));
                    }
                    bricks.get(i).changeVector();
                }
            }
            if(twoPlayerMode) paddle2.update();

            // clamp paddle inside screen
            if (paddle1.getX() < 0) paddle1.setX(0);
            if (paddle1.getX() + paddle1.getWidth() > WIDTH) {
                paddle1.setX(WIDTH - paddle1.getWidth());
            }
            if (twoPlayerMode) {
                if (paddle2.getX() < 0) paddle2.setX(0);
                if (paddle2.getX() + paddle2.getWidth() > WIDTH) {
                    paddle2.setX(WIDTH - paddle2.getWidth());
                }
            }
            // Ball sticks to paddle until launched
            for (Ball b : new ArrayList<>(balls)) {
                if (!b.isLaunched()) {
                    Paddle p = twoPlayerMode ? paddle2 : paddle1;
                    b.setX(p.getX() + p.getWidth() / 2f - b.getWidth() / 2f);
                    b.setY(p.getY() - b.getHeight() - 1);
                }
            }
        }

        if (!gameState.equals("RUNNING")) return;

        for (Ball b : new ArrayList<>(balls)) {
            if (b.isLaunched()) b.update();
            checkCollisionsWithBall(b);
        }

        // update powerups (falling)if (twoPlayerMode)
        for (PowerUp p : powerUps) p.update();

        // remove expired/collected powerups from list
        powerUps.removeIf(PowerUp::isCollectedOrOffscreen);

        // check win/lose
        if(checkWin(bricks)) {
            gameState = "WIN";
        }

        if (checkLose(lives)) {
            gameState = "LOSE";
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

    private void checkBrickWithWall(Brick brick) {
        if (brick.getX() < 0) {
            brick.setX(brick.getX() + brick.getSpeed());
            brick.setDx(-brick.getDx());
        } else if (brick.getX() + brick.getWidth() > WIDTH) {
            brick.setX(brick.getX() - brick.getSpeed());
            brick.setDx(-brick.getDx());
        }
        if (brick.getY() < 0) {
            brick.setY(brick.getY() + brick.getSpeed());
            brick.setDy(-brick.getDy());
        } else if (brick.getY() + brick.getHeight() > HEIGHT) {
            brick.setY(brick.getY() - brick.getSpeed());
            brick.setDy(-brick.getDy());
        }
    }
    private void checkBrickHeadOn(Brick brick, Brick other) {
        if (checkCollisionsWithBrick(brick, other)) {
            if (brick.getDx() == 0 && other.getDx() == 0
                    && brick.getDy() ==  - other.getDy()) {
                brick.setDy(-brick.getDy());
                other.setDy(-other.getDy());
            } else if (brick.getDy() == 0 && other.getDy() == 0
                    && brick.getDx() == -other.getDx()) {
                brick.setDx(-brick.getDx());
                other.setDx(-other.getDx());
            }
        }
    }

    private void setColX(Brick brick, Brick other) {
        // Va chạm ngang
        brick.setDx(-brick.getDx());
        other.setDx(-other.getDx());

        // Đẩy nhau
        if (brick.getX() < other.getX()) {
            if (brick.getType() != 0) {
                brick.setX(brick.getX() - brick.getSpeed()/2);
            }
            if (other.getType() != 0) {
                other.setX(other.getX() + other.getSpeed()/2);
            }
        } else {
            if (brick.getType() != 0) {
                brick.setX(brick.getX() + brick.getSpeed()/2);
            }
            if (other.getType() != 0) {
                other.setX(other.getX() - other.getSpeed()/2);
            }
        }
    }

    private void setColY(Brick brick, Brick other) {
        // Va chạm dọc
        brick.setDy(-brick.getDy());
        other.setDy(-other.getDy());
        // Đây nhau
        if (brick.getY() < other.getY()) {
            if (brick.getType() != 0) {
                brick.setY(brick.getY() - brick.getSpeed()/2);
            }
            if (other.getType() != 0) {
                other.setY(other.getY() + brick.getSpeed()/2);
            }
        } else {
            if (brick.getType() != 0) {
                brick.setY(brick.getY() + brick.getSpeed()/2);
            }
            if (other.getType() != 0) {
                other.setY(other.getY() - brick.getSpeed()/2);
            }
        }
    }

    private void checkBrickCross(Brick brick, Brick other) {
        if (checkCollisionsWithBrick(brick, other)) {
                float disX = Math.min(brick.getX() + brick.getWidth(), other.getX() + other.getWidth())
                        - Math.max(brick.getX(), other.getX());
                float disY = Math.min(brick.getY() + brick.getHeight(), other.getY() + other.getHeight())
                        - Math.max(brick.getY(), other.getY());
                if ((other.getDx() == 0 && brick.getDy() == 0)
                        || (other.getDy() == 0 && brick.getDx() == 0)) {
                    if (Math.abs(disX - disY) > EPSILON) {
                        if (disX - disY < 0){
                            setColX(brick, other);
                        } else {
                            setColY(brick, other);
                        }
                    } else {
                        // Va chạm góc
                        setColX(brick, other);
                        setColY(brick, other);
                    }
                }
            }
        }

    private boolean checkCollisionsWithBrick(Brick brick, Brick other) {
        if (brick == null || other == null) {
            return false;
        }
        return brick.getX() < other.getX() + other.getWidth() &&
                brick.getX() + brick.getWidth() > other.getX() &&
                brick.getY() < other.getY() + other.getHeight() &&
                brick.getY() + brick.getHeight() > other.getY();
    }

    private void checkCollisionsWithBall(Ball ball) {
        if (!ball.isLaunched()) return;

        // Kiểm tra va chạm với tường
        checkWallCollisions(ball);

        // Kiểm tra va chạm với paddle
        checkPaddleCollision(ball, paddle1);
        if (twoPlayerMode) checkPaddleCollision(ball, paddle2);

        // Kiểm tra va chạm với bricks, powerup
        checkBrickCollisions(ball, bricks, powerUps);
    }

    private void checkWallCollisions(Ball ball) {
        boolean collided = false;

        //Tường trái
        if (ball.getX() <= 0) {
            ball.setX(0);
            ball.setDx(Math.abs(ball.getDx()));
            collided = true;
        }
        //Tường phải
        else if (ball.getX() + ball.getWidth() >= WIDTH) {
            ball.setX(WIDTH - ball.getWidth());
            ball.setDx(-Math.abs(ball.getDx()));
            collided = true;
        }
        //Tường trên
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(Math.abs(ball.getDy()));
            collided = true;
        }
        //Tường dưới
        else if (ball.getY() + ball.getHeight() >= HEIGHT) {
            if (ball == mainBall) {
                ball.setY(HEIGHT - ball.getHeight());
                ball.setDy(-Math.abs(ball.getDy()));
                Ball.launched = false;
                ball.setDx(0);
                ball.setDy(0);
                lives--;

                if (extraBall != null) {
                    removeExtraBall(extraBall);
                }
            } else {
                removeExtraBall(ball);
            }
            return;
        }

        if (collided) {
            normalizeVelocity(ball);
            SoundEffect.play("collision");
        }
    }

    private void checkPaddleCollision(Ball ball, Paddle paddle) {
        Rectangle paddleRect = paddle.getBounds();

        float ballCenterX = ball.getCenterX();
        float ballCenterY = ball.getCenterY();
        float ballBottom = ball.getY() + ball.getHeight();

        float paddleTop = paddleRect.y;
        float paddleBottom = paddleRect.y + paddleRect.height;
        float paddleLeft = paddleRect.x;
        float paddleRight = paddleRect.x + paddleRect.width;

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
            handlePaddleTopCollision(ball, paddle, paddleRect, ballCenterX);
            SoundEffect.play("collision");
            // Va chạm từ bên
        } else {
            handlePaddleSideCollision(ball, paddleRect);
            SoundEffect.play("collision");
        }
    }

    private void handlePaddleTopCollision(Ball ball, Paddle paddle, Rectangle paddleRect, float ballCenterX) {
        // Đặt bóng lên trên paddle
        ball.setY(paddleRect.y - ball.getHeight() - 0.5f);

        // Tính góc phản xạ dựa trên vị trí va chạm
        double angleInRadians = getAngleInRadians(paddleRect, ballCenterX);
        float speedMagnitude = ball.getSpeed();
        ball.setDx((float) (speedMagnitude * Math.cos(angleInRadians)));
        ball.setDy(-(float) (speedMagnitude * Math.sin(angleInRadians)));// Âm vì đi lên

        // Đảm bảo dy luôn âm (đi lên)
        if (ball.getDy() > 0) {
            ball.setDy(-ball.getDy());
        }
    }

    private static double getAngleInRadians(Rectangle paddleRect, float ballCenterX) {
        float paddleCenter = paddleRect.x + paddleRect.width / 2f;
        float hitPosition = (ballCenterX - paddleCenter) / (paddleRect.width / 2f);

        // Clamp hitPosition trong khoảng [-1, 1]
        hitPosition = Math.max(-1f, Math.min(1f, hitPosition));

        // Tính góc output (90° = thẳng lên, giảm dần về 2 bên)
        float angleInDegrees = VERTICAL_ANGLE - hitPosition * MAX_BOUNCE_ANGLE;

        // Clamp angle để tránh góc quá ngang
        angleInDegrees = Math.max(MIN_ANGLE, Math.min(MAX_ANGLE, angleInDegrees));

        // Convert sang radians và set velocity mới
        return Math.toRadians(angleInDegrees);
    }

    private void handlePaddleSideCollision(Ball ball, Rectangle paddleRect) {
        float ballCenterX = ball.getCenterX();
        float prevCenterX = ball.getX() - ball.getDx() + ball.getRadius();

        float paddleLeft = paddleRect.x;
        float paddleRight = paddleRect.x + paddleRect.width;

        // Xác định va chạm từ bên trái
        if (prevCenterX < paddleLeft && ballCenterX >= paddleLeft) {
            ball.setX(paddleLeft - ball.getWidth());
            ball.setDx(-Math.abs(ball.getDx())); // Đảo chiều sang trái
            normalizeVelocity(ball);
            SoundEffect.play("collision");
        }
        // Xác định va chạm từ bên phải
        else if (prevCenterX > paddleRight && ballCenterX <= paddleRight) {
            ball.setX(paddleRight);
            ball.setDx(Math.abs(ball.getDx())); // Đảo chiều sang phải
            normalizeVelocity(ball);
            SoundEffect.play("collision");
        }
    }

    public float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    public boolean circleCheckCollision(Ball ball, Rectangle rect) {
        float closestX = clamp(ball.getCenterX(), rect.x, rect.x + rect.width);
        float closestY = clamp(ball.getCenterY(), rect.y, rect.y + rect.height);
        float dX = ball.getCenterX() - closestX;
        float dY = ball.getCenterY() - closestY;
        return (dX * dX + dY * dY) < (ball.getRadius() * ball.getRadius());
    }

    private void checkBrickCollisions(Ball ball, List<Brick> bricks, List<PowerUp> powerUps) {
        Rectangle ballRect = ball.getBounds();
        float ballCenterX = ball.getCenterX();
        float ballCenterY = ball.getCenterY();

        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            Rectangle brickRect = brick.getBounds();

            if (!circleCheckCollision(ball, brickRect)) continue;

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
            normalizeVelocity(ball);

            // Xử lý brick
            brick.takeHit();
            if (ball.isEnlarged() && !brick.isDestroyed()) {
                brick.takeHit();
            }

            if (ball.isExplosive()) {
                float explosionRadius = 80f * scale;
                ExplosiveBallPowerUp.explodeAt(bricks,
                        ball.getX() + ball.getWidth()/2f,
                        ball.getY() + ball.getHeight()/2f,
                        explosionRadius,toRemove);
            }

            if (brick.isDestroyed()) {
                score += brick.getHitPoints() * 100;
                it.remove();

                if (rand.nextDouble() < 0.2) {
                    int type = rand.nextInt(7);
                    PowerUp pu;
                    if (type == 0) {
                        pu = new ExpandPaddlePowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 5000);
                    } else if (type == 1) {
                        pu = new FastBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 5000);
                    } else if (type == 2) {
                        pu = new BigBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 5000);
                    } else if (type == 3) {
                        pu = new ExplosiveBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 2000);
                    } else if (type == 4) {
                        pu = new ShrinkPaddlePowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 5000);
                    } else if (type == 5) {
                        pu = new DoubleBallPowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 5000);
                    } else  {
                        pu = new ExtraLifePowerUp(brick.getX() + brick.getWidth()/2f - 12,
                                brick.getY() + brick.getHeight()/2f,
                                (int)(24 * scale), (int)(24 * scale), 5000);
                    }
                    powerUps.add(pu);
                }
            }
        }
        if (!toRemove.isEmpty()) {
            bricks.removeAll(toRemove);
            toRemove.clear();
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
                pu.applyEffect(paddle1, mainBall, this);
                pu.markCollectedOrOffscreen();
                pit.remove();
            }
            if (twoPlayerMode) {
                if (pu.intersects(paddle2)) {
                    pu.applyEffect(paddle2, mainBall, this);
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
    private void normalizeVelocity(Ball ball) {
        if (ball.isFast()) return;
        float currentMagnitude = (float) Math.hypot(ball.getDx(), ball.getDy());
        if (currentMagnitude > EPSILON && Math.abs(currentMagnitude - ball.getSpeed()) > EPSILON) {
            ball.setDx((ball.getDx() / currentMagnitude) * ball.getSpeed());
            ball.setDy((ball.getDy() / currentMagnitude) * ball.getSpeed());
        }
    }

    public void addExtraBall(Ball ball) {
        if (extraBall != null || balls.contains(ball)) return;
        balls.add(ball);
        extraBall = ball;
    }

    public void removeExtraBall(Ball ball) {
        if (extraBall == ball) {
            balls.remove(ball);
            extraBall = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // background
        Graphics2D g2 = (Graphics2D) g.create();

        int offsetX = (int)((parent.getWIDTH() - 800 * scale) / 2);
        int offsetY = (int)((parent.getHEIGHT() - 600 * scale) / 2);

        // Áp dụng scale và dịch
        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, null);
        } else {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(0, 0, WIDTH, HEIGHT);
        }

        // draw HUD
        g2.setColor(Color.WHITE);
        try {
            Font customFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("assets/font.ttf")
            ).deriveFont(Font.BOLD, 30f);
            g2.setFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
            g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        }
        String scoreText = String.format("%06d", score);
        g2.drawString(scoreText, (int)(12 * scale), (int)(590 * scale));

        int wLives = 30, hLives = 30;
        if (lives >= 1) {
            g2.drawImage(Renderer.loadHeartTexture(), (int)(750* scale),
                    (int)(570* scale),(int)(wLives * scale), (int)(hLives * scale), null);
        } else {
            g2.drawImage(Renderer.loadDamageTexture(), (int)(750* scale),
                    (int)(570 * scale),(int)(wLives * scale), (int)(hLives * scale), null);
        }
        if (lives >= 2) {
            g2.drawImage(Renderer.loadHeartTexture(), (int)(720 * scale),
                    (int)(570* scale),(int)(wLives * scale), (int)(hLives * scale), null);
        } else {
            g2.drawImage(Renderer.loadDamageTexture(), (int)(720* scale),
                    (int)(570 * scale),(int)(wLives* scale), (int)(hLives * scale), null);
        }
        if (lives >= 3) {
            g2.drawImage(Renderer.loadHeartTexture(), (int)(690* scale),
                    (int)(570 * scale),(int)(wLives * scale), (int)(hLives * scale), null);
        } else {
            g2.drawImage(Renderer.loadDamageTexture(), (int)(690* scale),
                    (int)(570 * scale),(int)(wLives * scale), (int)(hLives * scale), null);
        }

        // draw paddles, ball, bricks, powerups, arrow
        paddle1.render(g2);
        if (twoPlayerMode) paddle2.render(g2);

        if (!mainBall.isLaunched()) {
            drawLaunchArrow(g2, mainBall, launchAngle);
        }

        for (Ball b : balls) b.render(g2);

        for (Brick b : bricks) b.render(g2);
        for (PowerUp p : powerUps) p.render(g2);
        ExplosiveBallPowerUp.drawExplosions(g2);

        // overlays
        if (gameState.equals("MENU")) {
            int wPressStart = (int)(400 * scale);
            int hPressStart = (int)(100 * GamePanel.scale);

            int x = (int)((WIDTH - wPressStart) / 2f);
            int y = (int)((HEIGHT - hPressStart) / 2f);
            if ((System.currentTimeMillis() / 300) % 2 == 0) {
                g2.drawImage(Renderer.loadPressStartTexture(), x, y, wPressStart, hPressStart, null);
            }
        } else if (gameState.equals("PAUSED") || gameState.equals("LOSE")
                || gameState.equals("WIN")) {
            showMenu(g2);
        } else if (gameState.equals("RUNNING")) {
            buttonInGame(g2);
        }
        g2.dispose();
    }

    private void buttonInGame(Graphics g) {
        int butW = (int)(30 * scale), butH = (int)(30 * scale);
        int pauX = WIDTH - (int)(50 * scale);
        int volX = pauX - (int)(40 * scale);
        int butY = (int)(20 * scale);
        if (button[1] != null) {
            g.drawImage(button[1], pauX, butY, butW, butH, null);
        }
        if (button[14] != null && switchVol) {
            g.drawImage(button[14], volX, butY, butW, butH, null);
        } else if (button[15] != null) {
            g.drawImage(button[15], volX, butY, butW, butH, null);
        }
    }

    // KeyListener
    @Override
    public void keyTyped(KeyEvent e) { }
    @Override
    public void keyPressed(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = true;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = true;
        if (kc == KeyEvent.VK_A) aPressed = true;
        if (kc == KeyEvent.VK_D) dPressed = true;
        if (kc == KeyEvent.VK_SPACE) {
            if (gameState.equals("MENU")) {
                gameState = "RUNNING";
                mainBall.resetToPaddle(twoPlayerMode ? paddle2 : paddle1);
                launchBallAtAngle(mainBall, launchAngle);
            } else if (gameState.equals("RUNNING") && !mainBall.isLaunched()) {
                launchBallAtAngle(mainBall, launchAngle);
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
            if (gameState.equals("LOSE") || gameState.equals("WIN")) {
                restart();
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int kc = e.getKeyCode();
        if (kc == KeyEvent.VK_LEFT) leftPressed = false;
        if (kc == KeyEvent.VK_RIGHT) rightPressed = false;
        if (kc == KeyEvent.VK_A) aPressed = false;
        if (kc == KeyEvent.VK_D) dPressed = false;
    }

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
        restart();// khởi động lại game với level mới
    }


    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image tmp = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    /**
     * Quet goc ban lien tuc.
     */
    private void updateLaunchAngle() {
        float deltaAngle = angleSpeed / FPS;

        if (angleSweepingRight) {
            launchAngle += deltaAngle;
            if (launchAngle >= MAX_LAUNCH_ANGLE) {
                launchAngle = MAX_LAUNCH_ANGLE;
                angleSweepingRight = false;
            }
        } else {
            launchAngle -= deltaAngle;
            if (launchAngle <= MIN_LAUNCH_ANGLE) {
                launchAngle = MIN_LAUNCH_ANGLE;
                angleSweepingRight = true;
            }
        }
    }

    /**
     * Phong bong theo goc.
     * @param ball bong
     * @param angleDegrees goc(do)
     */
    private void launchBallAtAngle(Ball ball, float angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);

        float dx = (float) (Math.cos(angleRadians));
        float dy = -(float) (Math.sin(angleRadians));

        ball.launch(dx, dy);
    }

    private void drawLaunchArrow(Graphics2D g2, Ball ball, float angleDegrees) {
        float ballCenterX = ball.getX() + ball.getWidth() / 2f;
        float ballCenterY = ball.getY() + ball.getHeight() / 2f;

        int arrowW = (int) (40 * scale);
        int arrowH = (int) (40 * scale);

        AffineTransform oldTransform = g2.getTransform();

        g2.translate(ballCenterX, ballCenterY);
        g2.rotate(Math.toRadians(-angleDegrees + 90));
        g2.drawImage(Renderer.loadArrowTexture(), -arrowW/2, -(int)(50 * scale), arrowW, arrowH, null);

        g2.setTransform(oldTransform);
    }
}