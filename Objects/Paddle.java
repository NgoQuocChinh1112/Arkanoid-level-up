package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

import Game.Renderer;

public class Paddle extends MovableObject {
    private float speed = 8f;
    private final BufferedImage paddleImage;

    /**
     * Constructor.
     * @param x tọa độ x
     * @param y tọa độ y
     * @param width chiều rộng
     * @param height chiều dài
     */
    public Paddle(float x, float y, int width, int height) {
        super(x, y, width, height);
        paddleImage = Renderer.loadPaddleTexture();
    }

    /**
     * Phương thức vẽ paddle.
     */
    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(paddleImage, Math.round(x), Math.round(y), width, height, null);
    }

    /**
     * Phương thức getSpeed.
     * @return trả về tốc độ của paddle.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Phương thức setSpeed.
     * @param speed tốc độ cần thay thế.
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Phương thức setWidth.
     * @param w chiều rộng cần thay thế.
     */
    public void setWidth(int w) {
        this.width = w;
    }
}