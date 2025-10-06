package Objects;

import java.awt.*;

public class Ball extends MovableObject {
    private float speed = 5f;
    private boolean launched = false;

    public Ball(float x, float y, int width, int height) {
        super(x, y, width, height);
        dx = 0;
        dy = 0;
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.GRAY);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
    }

    public void launch(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
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
}
