package PowerUps;

import Game.Renderer;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Timer;



public class ExpandPaddlePowerUp extends PowerUp {
    private static int originalWidth = -1;
    private Timer timer;

    /**
     * Constructor.
     */
    public ExpandPaddlePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXPAND_PADDLE");
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[3];

            if (texture == null) {
                throw new Exception("Texture  bị null.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh " + e.getMessage());
            texture = null;
        }
    }

    /**
     * Áp dụng hiệu ứng
     */
    public void applyEffect(Paddle paddle, Ball ball,Object gameManager) {
        if (originalWidth == -1) {
            originalWidth = paddle.getWidth();
        }
        if (paddle.getWidth() > originalWidth) {
            return;
        }
        SoundEffect.play("expandpad");

        int expandedWidth = Math.min(300, originalWidth + 80);
        paddle.setWidth(expandedWidth);

        // Hủy timer cũ nếu có
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
     */
    @Override
    public void render(Graphics2D g2) {
        try {
            if (texture == null) {
                throw new IOException("Ảnh  bị null");
            }
            g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh " + e.getMessage());
        }
    }
}