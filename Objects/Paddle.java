package Objects;

import Game.GameManager;

import java.awt.*;

public class Paddle extends MovableObject {
    private float speed = 6f;

    public Paddle(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void update() {
        move();
        // giữ paddle trong khung
        if (x < 0) x = 0;
        if (x + width > GameManager.WIDTH) {
            x = GameManager.WIDTH - width;
        }
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRoundRect(Math.round(x), Math.round(y), width, height, 8, 8);
        g2.setColor(Color.DARK_GRAY);
        g2.drawRoundRect(Math.round(x), Math.round(y), width, height, 8, 8);
    }

    public void moveLeft() { dx = -speed; }
    public void moveRight() { dx = speed; }

    public float getSpeed() { return speed; }
    public void setSpeed(float s) { this.speed = s; }

    public void setWidth(int w) { this.width = w; }
    public int getWidth() { return width; }
}
