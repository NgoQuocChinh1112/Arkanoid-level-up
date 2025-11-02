package PowerUps;

import Game.Renderer;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Timer;

public class ShrinkPaddlePowerUp extends PowerUp {
    private static int originalWidth = -1;
    private Timer timer;


    /**
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     */
    public ShrinkPaddlePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "SHRINK_PADDLE");
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[4];

            if (texture == null) {
                throw new Exception("Texture bị null.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh " + e.getMessage());
            texture = null;
        }
    }

    /**
     * Áp dụng hiệu ứng.
     * @param paddle
     * @param ball
     * @param gameManager
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (originalWidth == -1) {
            originalWidth = paddle.getWidth();
        }
        SoundEffect.play("shrinkpad");
        int shrunkWidth = Math.max(40, originalWidth - 60);
        paddle.setWidth(shrunkWidth);
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer((int) durationMs, e -> {
            paddle.setWidth(originalWidth);
            timer.stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Vẽ.
     * @param g2
     */
    @Override
    public void render(Graphics2D g2) {
        try {
            if (texture == null) {
                throw new IOException("Ảnh bị null");
            }
            g2.drawImage(texture, Math.round(x), Math.round(y), width * 2, height * 2, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh" + e.getMessage());
        }
    }
}
