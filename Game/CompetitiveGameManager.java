package Game;

import Objects.*;
import PowerUps.*;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static Game.GamePanel.scale;

/**
 * Chế độ thi đấu 2 người - chia màn hình theo chiều dọc
 * Player 1: Bên trái (điều khiển: ←→ + Space)
 * Player 2: Bên phải (điều khiển: A D + W)
 */
public class CompetitiveGameManager extends GameManager {
    private int dividerX;
    
    // ========== PLAYER 1 (BÊN TRÁI) ==========
    private Paddle paddle1;
    private Ball ball1;
    private List<Brick> bricks1;
    private List<PowerUp> powerUps1;
    private int score1 = 0;
    private int lives1 = 3;
    private float launchAngle1 = 90f;
    private boolean angleSweepingRight1 = true;
    
    // ========== PLAYER 2 (BÊN PHẢI) ==========
    private Paddle paddle2;
    private Ball ball2;
    private List<Brick> bricks2;
    private List<PowerUp> powerUps2;
    private int score2 = 0;
    private int lives2 = 3;
    private float launchAngle2 = 90f;
    private boolean angleSweepingRight2 = true;
    
    // ========== TIMER ==========
    private long gameStartTime;
    private long gameDuration = 180000;  // 3 phút
    private long timeRemaining;
    private long pauseStartTime;
    private long totalPausedTime = 0;
    
    private boolean hoverResume = false;
    private boolean hoverRestart = false;
    private boolean hoverMenu = false;

    @Override
    public void setLevel(int level) {
        this.currentLevel = level;
        restart();
    }
    
    public CompetitiveGameManager(GamePanel parent, int width, int height) {
        super(parent, width, height);
        this.dividerX = width / 2;
        for (MouseListener ml : getMouseListeners()) {
            removeMouseListener(ml);
        }
        for (MouseMotionListener mml : getMouseMotionListeners()) {
            removeMouseMotionListener(mml);
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                if (gameState.equals("PAUSED") || gameState.equals("END")) {
                    int boxX = (WIDTH - (int) (300 * scale)) / 2;
                    int boxY = (HEIGHT - (int) (330 * scale)) / 2;
                    int btnW = (int) (180 * scale);
                    int btnH = (int) (50 * scale);
                    int resumeY = boxY + (int) (45 * scale);
                    int restartY = resumeY + (int) (60 * scale);
                    int menuY = restartY + (int) (60 * scale);
                    int btnX = boxX + ((int) (300 * scale) - btnW) / 2;

                    Rectangle resumeRect = new Rectangle(btnX, resumeY, btnW, btnH);
                    Rectangle restartRect = new Rectangle(btnX, restartY, btnW, btnH);
                    Rectangle menuRect = new Rectangle(btnX, menuY, btnW, btnH);
                    if (resumeRect.contains(p)) {
                        gameState = "RUNNING";
                        totalPausedTime += System.currentTimeMillis() - pauseStartTime;
                    } else if (restartRect.contains(p)) {
                        restart();
                    } else if (menuRect.contains(p)) {
                        parent.showMenu();
                        Menu.isCompetitive = false;
                    }
                }    
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                if (gameState.equals("PAUSED") || gameState.equals("END")) {
                    int boxW = (int) (300 * scale);
                    int boxH = (int) (330 * scale);
                    int boxX = (WIDTH - boxW) / 2;
                    int boxY = (HEIGHT - boxH) / 2;
                    int btnW = (int) (180 * scale);
                    int btnH = (int) (50 * scale);
                    int resumeY = boxY + (int) (45 * scale);
                    int restartY = resumeY + (int) (60 * scale);
                    int menuY = restartY + (int) (60 * scale);
                    int btnX = boxX + (boxW - btnW) / 2;

                    Rectangle resumeRect = new Rectangle(btnX, resumeY, btnW, btnH);
                    Rectangle restartRect = new Rectangle(btnX, restartY, btnW, btnH);
                    Rectangle menuRect = new Rectangle(btnX, menuY, btnW, btnH);

                    boolean oldHoverResume = hoverResume;
                    hoverResume = resumeRect.contains(p);
                    boolean oldHoverRestart = hoverRestart;
                    hoverRestart = restartRect.contains(p);
                    boolean oldHoverMenu = hoverMenu;
                    hoverMenu = menuRect.contains(p);
                    if (oldHoverResume != hoverResume || oldHoverMenu != hoverMenu || oldHoverRestart != hoverRestart) {
                        repaint();
                    }
                } 
            }
        });
        initGame();
    }

    @Override
    protected void showMenu(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // khung menu pause
        int boxW = (int)(300 * scale);
        int boxH = (int)(330 * scale);
        int boxX = (WIDTH - boxW) / 2;
        int boxY = (HEIGHT - boxH) / 2;
        if (button[0] != null) {
            g.drawImage(button[0], boxX, boxY, boxW, boxH, null);
        } else {
            g.setColor(new Color(255, 255, 255, 180));
            g.fillRoundRect(boxX, boxY, boxW, boxH, 30, 30);
        }

        int btnW =(int)(180 * scale), btnH = (int)(50 * scale);
        int resumeY = boxY + (int) (45 * scale);
        int restartY = resumeY + (int) (60 * scale);
        int menuY = restartY + (int) (60 * scale);
        int btnX = boxX + (boxW - btnW) / 2;

        // Vẽ 2 nút (ảnh hoặc chữ)

        if (button[4] != null && hoverResume) {
            g.drawImage(button[4], btnX, resumeY, btnW, btnH, null);
        } else if (button[5] != null) {
            g.drawImage(button[5], btnX, resumeY, btnW, btnH, null);
        }
        if (button[6] != null && hoverRestart) {
            g.drawImage(button[6], btnX, restartY, btnW, btnH, null);
        } else if (button[7] != null) {
            g.drawImage(button[7], btnX, restartY, btnW, btnH, null);
        }
        if (button[2] != null && hoverMenu) {
            g.drawImage(button[2], btnX, menuY, btnW, btnH, null);
        } else if (button[3] != null) {
            g.drawImage(button[3], btnX, menuY, btnW, btnH, null);
        }
    }
    
    @Override
    protected void initGame() {
        int halfWidth = WIDTH / 2;
        int paddleW = (int)(80 * scale);
        int paddleH = (int)(16 * scale);
        int paddleY = HEIGHT - (int)(60 * scale);
        
        // Player 1
        paddle1 = new Paddle(halfWidth / 2f - paddleW / 2f, paddleY, paddleW, paddleH);
        ball1 = new Ball(paddle1.getX() + paddleW / 2f - (int)(8 * scale),
                         paddle1.getY() - (int)(16 * scale) - 1,
                         (int)(16 * scale), (int)(16 * scale));
        ball1.resetToPaddle(paddle1);
        bricks1 = Level.buildLevelForPlayer(currentLevel, 0, halfWidth, HEIGHT, scale);
        powerUps1 = new ArrayList<>();
        
        // Player 2
        paddle2 = new Paddle(dividerX + halfWidth / 2f - paddleW / 2f, paddleY, paddleW, paddleH);
        ball2 = new Ball(paddle2.getX() + paddleW / 2f - (int)(8 * scale),
                         paddle2.getY() - (int)(16 * scale) - 1,
                         (int)(16 * scale), (int)(16 * scale));
        ball2.resetToPaddle(paddle2);
        bricks2 = Level.buildLevelForPlayer(currentLevel, dividerX, halfWidth, HEIGHT, scale);
        powerUps2 = new ArrayList<>();
        
        score1 = score2 = 0;
        lives1 = lives2 = 3;
        launchAngle1 = launchAngle2 = 90f;
        angleSweepingRight1 = angleSweepingRight2 = true;
        
        gameStartTime = System.currentTimeMillis();
        timeRemaining = gameDuration;
        totalPausedTime = 0;
    }
    
    @Override
    protected void updateGame() {
        if (gameState.equals("END") || gameState.equals("PAUSED")) return;
        // Update góc bắn
        if (!ball1.isLaunched()) updateLaunchAngle(1);
        if (!ball2.isLaunched()) updateLaunchAngle(2);
        
        // Input & Paddle
        float sp = paddle1.getSpeed();
        if (leftPressed && !rightPressed) paddle1.setDx(-sp);
        else if (rightPressed && !leftPressed) paddle1.setDx(sp);
        else paddle1.setDx(0);
        
        if (aPressed && !dPressed) paddle2.setDx(-sp);
        else if (dPressed && !aPressed) paddle2.setDx(sp);
        else paddle2.setDx(0);
        
        paddle1.update();
        paddle2.update();

        for (int i = 0; i < bricks1.size(); i++) {
            if (!bricks1.get(i).isDestroyed()) {
                bricks1.get(i).update();
                checkBrickWithWall(bricks1.get(i));
                for (int j = i + 1; j < bricks1.size(); j++ ) { 
                    checkBrickHeadOn(bricks1.get(i), bricks1.get(j));
                    checkBrickCross(bricks1.get(i), bricks1.get(j));
                }
                bricks1.get(i).changeVector();
            }
        }
        for (int i = 0; i < bricks2.size(); i++) {
            if (!bricks2.get(i).isDestroyed()) {
                bricks2.get(i).update();
                checkBrickWithWall(bricks2.get(i));
                for (int j = i + 1; j < bricks2.size(); j++ ) { 
                    checkBrickHeadOn(bricks2.get(i), bricks2.get(j));
                    checkBrickCross(bricks2.get(i), bricks2.get(j));
                }
                bricks2.get(i).changeVector();
            }
        }
        
        // Clamp paddle
        paddle1.setX(clamp(paddle1.getX(), 0, dividerX - paddle1.getWidth()));
        paddle2.setX(clamp(paddle2.getX(), dividerX, WIDTH - paddle2.getWidth()));
        
        // Update balls
        updateBallPosition(ball1, paddle1, 1);
        updateBallPosition(ball2, paddle2, 2);
        
        checkCollisionsWithBall(ball1, bricks1, paddle1, powerUps1, 1);
        checkCollisionsWithBall(ball2, bricks2, paddle2, powerUps2, 2);
        
        // Update powerups
        powerUps1.forEach(PowerUp::update);
        powerUps1.removeIf(PowerUp::isCollectedOrOffscreen);
        powerUps2.forEach(PowerUp::update);
        powerUps2.removeIf(PowerUp::isCollectedOrOffscreen);
        
        if (!gameState.equals("RUNNING")) return;
        
        // Timer
        long elapsed = System.currentTimeMillis() - gameStartTime - totalPausedTime;
        timeRemaining = gameDuration - elapsed;
        
        if (timeRemaining <= 0 || (bricks1.isEmpty() || bricks2.isEmpty())) {
            timeRemaining = 0;
            gameState = "END";
        }
        if (lives1 == 0 || lives2 == 0) {
            gameState = "END";
        }
        
        ExplosiveBallPowerUp.updateExplosions();
    }
    
    private void updateBallPosition(Ball ball, Paddle paddle, int player) {
        if (!ball.isLaunched()) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2f - ball.getWidth() / 2f);
            ball.setY(paddle.getY() - ball.getHeight() - 1);
        } else {
            ball.update();
        }
    }
    
    private void updateLaunchAngle(int player) {
        float deltaAngle = angleSpeed / FPS;
        
        if (player == 1) {
            if (angleSweepingRight1) {
                launchAngle1 = Math.min(launchAngle1 + deltaAngle, MAX_LAUNCH_ANGLE);
                if (launchAngle1 >= MAX_LAUNCH_ANGLE) angleSweepingRight1 = false;
            } else {
                launchAngle1 = Math.max(launchAngle1 - deltaAngle, MIN_LAUNCH_ANGLE);
                if (launchAngle1 <= MIN_LAUNCH_ANGLE) angleSweepingRight1 = true;
            }
        } else {
            if (angleSweepingRight2) {
                launchAngle2 = Math.min(launchAngle2 + deltaAngle, MAX_LAUNCH_ANGLE);
                if (launchAngle2 >= MAX_LAUNCH_ANGLE) angleSweepingRight2 = false;
            } else {
                launchAngle2 = Math.max(launchAngle2 - deltaAngle, MIN_LAUNCH_ANGLE);
                if (launchAngle2 <= MIN_LAUNCH_ANGLE) angleSweepingRight2 = true;
            }
        }
    }
    
    private void checkCollisionsWithBall(Ball ball, List<Brick> bricks, Paddle paddle, List<PowerUp> powerUps, int player) {
        if (!ball.isLaunched()) return;
        
        checkWallCollisions(ball, player);
        checkPaddleCollision(ball, paddle);
        checkBrickCollisions(ball, bricks, powerUps, player);
    }
    
    private void checkWallCollisions(Ball ball, int player) {
        boolean collided = false;
        
        if (player == 1) {
            if (ball.getX() <= 0) {
                ball.setX(0);
                ball.setDx(Math.abs(ball.getDx()));
                collided = true;
            } else if (ball.getX() + ball.getWidth() >= dividerX) {
                ball.setX(dividerX - ball.getWidth());
                ball.setDx(-Math.abs(ball.getDx()));
                collided = true;
            }
        } else {
            if (ball.getX() <= dividerX) {
                ball.setX(dividerX);
                ball.setDx(Math.abs(ball.getDx()));
                collided = true;
            } else if (ball.getX() + ball.getWidth() >= WIDTH) {
                ball.setX(WIDTH - ball.getWidth());
                ball.setDx(-Math.abs(ball.getDx()));
                collided = true;
            }
        }
        
        if (ball.getY() <= 0) {
            ball.setY(0);
            ball.setDy(Math.abs(ball.getDy()));
            collided = true;
        } else if (ball.getY() + ball.getHeight() >= HEIGHT) {
            if (player == 1) {
                lives1--;
                ball1.resetToPaddle(paddle1);
            } else {
                lives2--;
                ball2.resetToPaddle(paddle2);
            }
            ball.setLaunched(false);
            return;
        }
        
        if (collided) {
            normalizeVelocity(ball);
            SoundEffect.play("collision");
        }
    }
    
    private void normalizeVelocity(Ball ball) {
        if (ball.isFast()) return;
        float currentMagnitude = (float) Math.hypot(ball.getDx(), ball.getDy());
        if (currentMagnitude > EPSILON && Math.abs(currentMagnitude - ball.getSpeed()) > EPSILON) {
            ball.setDx((ball.getDx() / currentMagnitude) * ball.getSpeed());
            ball.setDy((ball.getDy() / currentMagnitude) * ball.getSpeed());
        }
    }
    
    private void checkBrickCollisions(Ball ball, List<Brick> bricks, List<PowerUp> powerUps, int player) {
        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            if (!circleCheckCollision(ball, brick.getBounds())) continue;
            
            handleBrickCollision(ball, brick);
            brick.takeHit();
            
            if (ball.isEnlarged() && !brick.isDestroyed()) brick.takeHit();
            
            if (brick.isDestroyed()) {
                it.remove();
                if (player == 1) score1 += 100;
                else score2 += 100;
                
                if (rand.nextDouble() < 0.2) {
                    powerUps.add(createPowerUp(rand.nextInt(4), brick));
                }
            }
        }
        
        Ball ballToAffect = (player == 1) ? ball1 : ball2;
        Paddle paddleToAffect = (player == 1) ? paddle1 : paddle2;
        
        powerUps.removeIf(pu -> {
            if (pu.getY() > HEIGHT) {
                pu.markCollectedOrOffscreen();
                return true;
            }
            if (pu.intersects(paddleToAffect)) {
                pu.applyEffect(paddleToAffect, ballToAffect, null);
                pu.markCollectedOrOffscreen();
                return true;
            }
            return false;
        });
    }
    
    private void handleBrickCollision(Ball ball, Brick brick) {
        Rectangle brickRect = brick.getBounds();
        float ballCenterX = ball.getCenterX();
        float ballCenterY = ball.getCenterY();
        float brickCenterX = brickRect.x + brickRect.width / 2f;
        float brickCenterY = brickRect.y + brickRect.height / 2f;
        
        float deltaX = ballCenterX - brickCenterX;
        float deltaY = ballCenterY - brickCenterY;
        
        float overlapX = (brickRect.width / 2f + ball.getRadius()) - Math.abs(deltaX);
        float overlapY = (brickRect.height / 2f + ball.getRadius()) - Math.abs(deltaY);
        
        if (overlapX < overlapY) {
            ball.setX(deltaX > 0 ? brickRect.x + brickRect.width + 0.5f : brickRect.x - ball.getWidth() - 0.5f);
            ball.setDx(-ball.getDx());
        } else {
            ball.setY(deltaY > 0 ? brickRect.y + brickRect.height + 0.5f : brickRect.y - ball.getHeight() - 0.5f);
            ball.setDy(-ball.getDy());
        }
        normalizeVelocity(ball);
    }
    
    private PowerUp createPowerUp(int type, Brick brick) {
        float x = brick.getX() + brick.getWidth()/2f - 12;
        float y = brick.getY() + brick.getHeight()/2f;
        int w = (int)(24 * scale);
        int h = (int)(24 * scale);
        
        switch(type) {
            case 0: return new ExpandPaddlePowerUp(x, y, w, h, 5000);
            case 1: return new FastBallPowerUp(x, y, w, h, 5000);
            case 2: return new BigBallPowerUp(x, y, w, h, 5000);
            default : return new ShrinkPaddlePowerUp(x, y, w, h, 5000);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
    g2.fillRect(0, 0, WIDTH, HEIGHT); // vẽ nền
        
        // Đường chia
        g2.setColor(new Color(255, 255, 255, 100));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(dividerX, 0, dividerX, HEIGHT);
        
        // Vẽ game objects
        renderPlayer(g2, paddle1, ball1, bricks1, powerUps1, launchAngle1);
        renderPlayer(g2, paddle2, ball2, bricks2, powerUps2, launchAngle2);
        ExplosiveBallPowerUp.drawExplosions(g2);
        
        // HUD
        drawCompetitiveHUD(g2);
        
        // Overlays
        if (gameState.equals("MENU")) {
            drawStartScreen(g2);
        } else if (gameState.equals("PAUSED")) {
            showMenu(g2);
        } else if (gameState.equals("END")) {
            drawEndScreen(g2);
        }
    }
    
    private void renderPlayer(Graphics2D g2, Paddle paddle, Ball ball, List<Brick> bricks, List<PowerUp> powerUps, float angle) {
        paddle.render(g2);
        if (!ball.isLaunched()) {
            drawLaunchArrow(g2, ball, angle);
        }
        ball.render(g2);
        bricks.forEach(b -> b.render(g2));
        powerUps.forEach(p -> p.render(g2));
    }
    
    private void drawCompetitiveHUD(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        
        // Player 1
        g2.drawString(String.format("P1: %05d", score1), 10, 30);
        drawLives(g2, lives1, 10, 40);
        
        // Player 2
        String p2Text = String.format("P2: %05d", score2);
        int p2Width = g2.getFontMetrics().stringWidth(p2Text);
        g2.drawString(p2Text, WIDTH - p2Width - 10, 30);
        drawLives(g2, lives2, WIDTH - 100, 40);
        
        // Timer
        g2.setFont(new Font("SansSerif", Font.BOLD, 36));
        String timeText = String.format("%02d:%02d", timeRemaining / 60000, (timeRemaining % 60000) / 1000);
        int timerWidth = g2.getFontMetrics().stringWidth(timeText);
        g2.setColor(timeRemaining < 30000 ? new Color(255, 100, 100) : Color.WHITE);
        g2.drawString(timeText, (WIDTH - timerWidth) / 2, 30);
    }
    
    private void drawLives(Graphics2D g2, int lives, int x, int y) {
        int size = 25;
        for (int i = 0; i < 3; i++) {
            g2.drawImage(i < lives ? Renderer.loadHeartTexture() : Renderer.loadDamageTexture(),
                        x + i * 30, y, size, size, null);
        }
    }
    
    private void drawStartScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        
        g2.setFont(new Font("SansSerif", Font.BOLD, 40));
        g2.setColor(Color.WHITE);
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            drawCenteredText(g2, "PRESS SPACE TO START", HEIGHT / 2);
        }
        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2.setColor(new Color(200, 200, 200));
        drawCenteredText(g2, "Player 1: ← → + ↑", HEIGHT / 2 + 60);
        drawCenteredText(g2, "Player 2: A D + W", HEIGHT / 2 + 90);
    }
      
    private void drawEndScreen(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        
        String winner;
        if (lives1 > 0 && lives2 == 0) {
            winner = "PLAYER 1 WINS!";
        } else if (lives2 > 0 && lives1 == 0) {
            winner = "PLAYER 2 WINS!";
        } else {
            winner = score1 > score2 ? "PLAYER 1 WINS!" : score2 > score1 ? "PLAYER 2 WINS!" : "TIE GAME!";
        }
        
        g2.setFont(new Font("SansSerif", Font.BOLD, 48));
        g2.setColor(new Color(255, 215, 0));
        drawCenteredText(g2, winner, HEIGHT / 2 - 80);
        
        g2.setFont(new Font("SansSerif", Font.BOLD, 32));
        g2.setColor(Color.WHITE);
        drawCenteredText(g2, String.format("Player 1: %05d", score1), HEIGHT / 2);
        drawCenteredText(g2, String.format("Player 2: %05d", score2), HEIGHT / 2 + 50);
        
        g2.setFont(new Font("SansSerif", Font.PLAIN, 24));
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            drawCenteredText(g2, "PRESS P TO SELECT", HEIGHT / 2 + 120);
        }
    }
    
    private void drawCenteredText(Graphics2D g2, String text, int y) {
        int width = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (WIDTH - width) / 2, y);
    }
    
    private void drawLaunchArrow(Graphics2D g2, Ball ball, float angleDegrees) {
        float ballCenterX = ball.getX() + ball.getWidth() / 2f;
        float ballCenterY = ball.getY() + ball.getHeight() / 2f;
        
        int arrowW = (int)(40 * scale);
        int arrowH = (int)(40 * scale);
        
        var oldTransform = g2.getTransform();
        g2.translate(ballCenterX, ballCenterY);
        g2.rotate(Math.toRadians(-angleDegrees + 90));
        g2.drawImage(Renderer.loadArrowTexture(), -arrowW / 2, -(int)(50 * scale), arrowW, arrowH, null);
        g2.setTransform(oldTransform);
    }
    
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
                gameStartTime = System.currentTimeMillis();
            } 
        }
        if (gameState.equals("RUNNING")) {
            if (kc == KeyEvent.VK_UP && !ball1.isLaunched()) launchBallAtAngle(ball1, launchAngle1);
            if (kc == KeyEvent.VK_W && !ball2.isLaunched()) launchBallAtAngle(ball2, launchAngle2);
        }
        
        if (kc == KeyEvent.VK_P) {
            if (gameState.equals("RUNNING")) {
                gameState = "PAUSED";
                pauseStartTime = System.currentTimeMillis();
            } else if (gameState.equals("PAUSED")) {
                gameState = "RUNNING";
                totalPausedTime += System.currentTimeMillis() - pauseStartTime;
            } else if (gameState.equals("END")) {
                gameState = "PAUSED";
                pauseStartTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void restart() {
        gameState = "MENU";
        initGame();
    }
    
    private void launchBallAtAngle(Ball ball, float angleDegrees) {
        double angleRadians = Math.toRadians(angleDegrees);
        float speed = ball.getSpeed();
        ball.launch((float)(speed * Math.cos(angleRadians)), -(float)(speed * Math.sin(angleRadians)));
    }
}