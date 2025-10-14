package Objects;

import Game.Renderer;
import Game.GameManager;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Ball extends MovableObject {
    private float speed = 7f;
    private boolean launched = false;

    // Constants để tránh magic numbers
    private static final float MAX_BOUNCE_ANGLE = 60f;
    private static final float MIN_ANGLE = 15f;
    private static final float MAX_ANGLE = 165f;
    private static final float VERTICAL_ANGLE = 90f;
    private static final float EPSILON = 0.001f; // Để so sánh float

    // Cache để tránh tính toán lại
    private float radius;
    private Rectangle boundsCache;

    public Ball(float x, float y, int width, int height) {
        super(x, y, width, height);
        dx = 0;
        dy = 0;
        radius = width / 2f;
        boundsCache = new Rectangle();
        texture = Renderer.loadBallTexture();
    }

    @Override
    public void update() {
        if (launched) {
            move();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
    }

    public void launch(float dx, float dy) {
        // Normalize và apply speed
        float magnitude = (float) Math.hypot(dx, dy);
        if (magnitude > EPSILON) {
            this.dx = (dx / magnitude) * speed;
            this.dy = (dy / magnitude) * speed;
            launched = true;
        }
    }

    public boolean isLaunched() {
        return launched;
    }

    public void resetToPaddle(Paddle paddle) {
        launched = false;
        dx = 0;
        dy = 0;
        setX(paddle.getX() + paddle.getWidth() / 2f - width / 2f);
        setY(paddle.getY() - height - 1);
    }

    public void setSpeed(float s) {
        if (s > 0) {
            this.speed = s;
            // Cập nhật lại velocity với speed mới nếu đã launch
            if (launched && (Math.abs(dx) > EPSILON || Math.abs(dy) > EPSILON)) {
                float magnitude = (float) Math.hypot(dx, dy);
                dx = (dx / magnitude) * speed;
                dy = (dy / magnitude) * speed;
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public Rectangle getBounds() {
        // Sử dụng cache để tránh tạo object mới liên tục
        boundsCache.setBounds(Math.round(x), Math.round(y), width, height);
        return boundsCache;
    }

    public float getCenterX() {
        return x + radius;
    }

    public float getCenterY() {
        return y + radius;
    }

    public void checkCollisions(Paddle paddle, List<Brick> bricks) {
        if (!launched) return;

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
        if (x <= 0) {
            x = 0;
            dx = Math.abs(dx);
            collided = true;
        }
        // Tường phải
        else if (x + width >= GameManager.WIDTH) {
            x = GameManager.WIDTH - width;
            dx = -Math.abs(dx);
            collided = true;
        }

        // Tường trên
        if (y <= 0) {
            y = 0;
            dy = Math.abs(dy);
            collided = true;
        }
        // Tường dưới
        else if (y + height >= GameManager.HEIGHT) {
            launched = false;
            dx = 0;
            dy = 0;
            return;
        }

        if (collided) {
            normalizeVelocity();
        }
    }

    private void checkPaddleCollision(Paddle paddle) {
        Rectangle paddleRect = paddle.getBounds();

        float ballCenterX = getCenterX();
        float ballCenterY = getCenterY();
        float ballBottom = y + height;

        float paddleTop = paddleRect.y;
        float paddleBottom = paddleRect.y + paddleRect.height;
        float paddleLeft = paddleRect.x;
        float paddleRight = paddleRect.x + paddleRect.width;

        // Kiểm tra overlap
        boolean overlapX = ballCenterX >= paddleLeft && ballCenterX <= paddleRight;
        boolean overlapY = ballBottom >= paddleTop && y <= paddleBottom;

        if (!overlapX || !overlapY) return;

        float prevY = y - dy;
        float prevBottom = prevY + height;

        // Va chạm từ trên xuống
        if (dy > 0 && prevBottom <= paddleTop) {
            handlePaddleTopCollision(paddle, paddleRect, ballCenterX);
        }
        // Va chạm từ bên
        else {
            handlePaddleSideCollision(paddleRect, ballCenterX, ballCenterY);
        }
    }

    private void handlePaddleTopCollision(Paddle paddle, Rectangle paddleRect, float ballCenterX) {
        // Đặt bóng lên trên paddle
        y = paddleRect.y - height - 0.5f;

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
        float speedMagnitude = speed;

        dx = (float) (speedMagnitude * Math.cos(angleInRadians));
        dy = -(float) (speedMagnitude * Math.sin(angleInRadians)); // Âm vì đi lên

        // Đảm bảo dy luôn âm (đi lên)
        if (dy > 0) dy = -dy;
    }

    private void handlePaddleSideCollision(Rectangle paddleRect, float ballCenterX, float ballCenterY) {
        float paddleLeft = paddleRect.x;
        float paddleRight = paddleRect.x + paddleRect.width;

        float prevX = x - dx;
        float prevCenterX = prevX + radius;

        // Xác định va chạm bên trái hay phải
        boolean hitFromLeft = prevCenterX < paddleLeft && ballCenterX >= paddleLeft;
        boolean hitFromRight = prevCenterX > paddleRight && ballCenterX <= paddleRight;

        if (hitFromLeft) {
            x = paddleLeft - width - 0.5f;
            dx = -Math.abs(dx);
            normalizeVelocity();
        } else if (hitFromRight) {
            x = paddleRight + 0.5f;
            dx = Math.abs(dx);
            normalizeVelocity();
        }
    }

    private void checkBrickCollisions(List<Brick> bricks) {
        Rectangle ballRect = getBounds();
        float ballCenterX = getCenterX();
        float ballCenterY = getCenterY();

        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            Rectangle brickRect = brick.getBounds();

            if (!ballRect.intersects(brickRect)) continue;

            // Tính vị trí tương đối của ball với brick
            float brickCenterX = brickRect.x + brickRect.width / 2f;
            float brickCenterY = brickRect.y + brickRect.height / 2f;

            float deltaX = ballCenterX - brickCenterX;
            float deltaY = ballCenterY - brickCenterY;

            // Tính overlap cho mỗi cạnh
            float overlapX = (brickRect.width / 2f + radius) - Math.abs(deltaX);
            float overlapY = (brickRect.height / 2f + radius) - Math.abs(deltaY);

            // Va chạm theo trục có overlap nhỏ hơn
            if (overlapX < overlapY) {
                // Va chạm ngang (trái/phải)
                if (deltaX > 0) {
                    // Va chạm từ bên trái brick
                    x = brickRect.x + brickRect.width + 0.5f;
                } else {
                    // Va chạm từ bên phải brick
                    x = brickRect.x - width - 0.5f;
                }
                dx = -dx;
            } else {
                // Va chạm dọc (trên/dưới)
                if (deltaY > 0) {
                    // Va chạm từ trên brick
                    y = brickRect.y + brickRect.height + 0.5f;
                } else {
                    // Va chạm từ dưới brick
                    y = brickRect.y - height - 0.5f;
                }
                dy = -dy;
            }

            // Normalize lại velocity để giữ tốc độ ổn định
            normalizeVelocity();

            // Xử lý brick
            brick.takeHit();
            if (brick.isDestroyed()) {
                it.remove();
            }

            // Chỉ xử lý 1 brick collision mỗi frame
            break;
        }
    }

    /**
     * Normalize velocity để duy trì tốc độ ổn định
     * Fix bug: tốc độ bóng tăng/giảm sau nhiều lần va chạm
     */
    private void normalizeVelocity() {
        float currentMagnitude = (float) Math.hypot(dx, dy);
        if (currentMagnitude > EPSILON && Math.abs(currentMagnitude - speed) > EPSILON) {
            dx = (dx / currentMagnitude) * speed;
            dy = (dy / currentMagnitude) * speed;
        }
    }

    /**
     * Kiểm tra nếu bóng bị stuck (vận tốc quá nhỏ hoặc góc quá ngang)
     * Tự động fix bằng cách đẩy bóng đi lên
     */
    public void checkAndFixStuck() {
        if (!launched) return;

        float currentSpeed = (float) Math.hypot(dx, dy);

        // Nếu tốc độ quá chậm
        if (currentSpeed < speed * 0.5f) {
            launch(0, -1); // Đẩy bóng đi thẳng lên
        }

        // Nếu góc quá ngang (dy quá nhỏ so với dx)
        if (Math.abs(dy) < Math.abs(dx) * 0.1f) {
            float direction = dy >= 0 ? 1 : -1;
            dy = direction * Math.abs(dx) * 0.3f;
            normalizeVelocity();
        }
    }
}