package Objects;

import Game.Renderer;
import Game.GameManager;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Ball extends MovableObject {
    private float speed = 2f;
    private boolean launched = false;

    public Ball(float x, float y, int width, int height) {
        super(x, y, width, height);
        dx = 0;
        dy = 0;
        texture = Renderer.loadBallTexture();
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
    }

    public void launch(float dx, float dy) {
        this.dx = dx * speed;
        this.dy = dy * speed;
        launched = true;
    }

    public boolean isLaunched() { return launched; }

    public void resetToPaddle(Paddle paddle) {
        launched = false;
        dx = 0; dy = 0;
        setX(paddle.getX() + paddle.getWidth() / 2f - getWidth() / 2f);
        setY(paddle.getY() - getHeight() - 1);
    }

    public void setSpeed(float s) { this.speed = s; }
    public float getSpeed() { return speed; }

    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), width, height);
    }

    public void checkCollisions(Paddle paddle, List<Brick> bricks) {
        Rectangle ballRect = getBounds();

        if (x <= 0) {
            x = 0;
            dx = -dx;
        } else if (x + width >= GameManager.WIDTH) {
            x = GameManager.WIDTH - width;
            dx = -dx;
        }
        if (y <= 0) {
            y = 0;
            dy = -dy;
        } else if (y + height >= GameManager.HEIGHT) {
            // rơi xuống đáy
            launched = false;
            dx = 0;
            dy = 0;
            return;
        }

        Rectangle p = paddle.getBounds();
        float paddleTop = p.y;
        float paddleBottom = p.y + p.height;

        float prevX = x - dx;
        float prevY = y - dy;
        float RADIUS = width / 2f;

        boolean goingDown = (dy > 0);
        boolean goingUp = (dy < 0);

        boolean crossTop = (prevY + height <= paddleTop) && (y + height >= paddleTop);
        boolean overlapX = (x + RADIUS >= p.x) && (x <= p.x + p.width);

        if (goingDown && crossTop && overlapX) {
            y = paddleTop - height - 1;
            float paddleCenter = p.x + p.width / 2f;
            float ballCenter = x + width / 2f;

            float hitRel = (ballCenter - paddleCenter) / (p.width / 2f);
            float MAX_BOUNCE_DEG = 60f;
            float outDeg = 90f - hitRel * MAX_BOUNCE_DEG;

            if (outDeg > 165f) outDeg = 165f;
            if (outDeg < 15f) outDeg = 15f;

            double angle = Math.toRadians(outDeg);
            float speedMag = (float) Math.hypot(dx, dy);

            dx = (float) (speedMag * Math.cos(angle));
            dy = (float) (-Math.abs(speedMag * Math.sin(angle)));
        }
        else {
            boolean overlapY = (y + height >= paddleTop) && (y <= paddleBottom);
            double leftSideX = p.x - width;
            double rightSideX = p.x + p.width + width;
            boolean goingRight = (dx > 0);
            boolean goingLeft = (dx < 0);
            boolean crossLeftSide = (prevX + width <= leftSideX) && (x + width >= leftSideX);
            boolean crossRightSide = (prevX >= rightSideX) && (x <= rightSideX);

            if (overlapY && goingRight && crossLeftSide) {
                x = (float) leftSideX - 1;
                dx = -dx;
            } else if (overlapY && goingLeft && crossRightSide) {
                x = (float) rightSideX + 1;
                dx = -dx;
            }
        }

        Iterator<Brick> it = bricks.iterator();
        while (it.hasNext()) {
            Brick brick = it.next();
            Rectangle b = brick.getBounds();

            if (ballRect.intersects(b)) {

                float overlapLeft = ballRect.x + ballRect.width - b.x;
                float overlapRight = b.x + b.width - ballRect.x;
                float overlapTop = ballRect.y + ballRect.height - b.y;
                float overlapBottom = b.y + b.height - ballRect.y;

                boolean fromLeft = overlapLeft < overlapRight && overlapLeft < overlapTop && overlapLeft < overlapBottom;
                boolean fromRight = overlapRight < overlapLeft && overlapRight < overlapTop && overlapRight < overlapBottom;
                boolean fromTop = overlapTop < overlapBottom && overlapTop < overlapLeft && overlapTop < overlapRight;
                boolean fromBottom = overlapBottom < overlapTop && overlapBottom < overlapLeft && overlapBottom < overlapRight;

                if (fromLeft || fromRight) {
                    dx = -dx;
                } else if (fromTop || fromBottom) {
                    dy = -dy;
                }

                brick.takeHit();
                if (brick.isDestroyed()) {
                    it.remove();
                }

                break;
            }
        }
    }

}
