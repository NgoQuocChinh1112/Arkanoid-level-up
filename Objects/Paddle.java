package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

import Game.Renderer;

public class Paddle extends MovableObject {
    private float speed = 6f;
    private BufferedImage paddleImage;

    public Paddle(float x, float y, int width, int height) {
        super(x, y, width, height);
        paddleImage = Renderer.loadPaddleTexture();
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(paddleImage, Math.round(x), Math.round(y), width, height, null);
    }

    public void moveLeft() { dx = -speed; }
    public void moveRight() { dx = speed; }

    public float getSpeed() { return speed; }
    public void setSpeed(float s) { this.speed = s; }

    public void setWidth(int w) { this.width = w; }
    public int getWidth() { return width; }
}
