package PowerUps;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import Game.GameManager;
import Game.Renderer;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;

public class ExtraLifePowerUp extends PowerUp {
    /**
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     */
    public ExtraLifePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXTRA_LIFE");
        this.durationMs = 0;
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[5];

            if (texture == null) {
                throw new Exception("Texture  bị null.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh " + e.getMessage());
            texture = null;
        }
    }

    /**
     * Áp dụng hiệu ứng.
     * @param paddle
     * @param mainBall
     * @param gameManager
     */
    @Override
    public void applyEffect(Paddle paddle, Ball mainBall, Object gameManager ) {
        if (gameManager instanceof GameManager) {
            GameManager Game = (GameManager) gameManager;
            Game.addLife();
            SoundEffect.play("extralife");
        }
    }
    /**
     * Vẽ.
     * @param g2
     */
    public void render(Graphics2D g2) {
        try {
            if (texture == null) {
                throw new IOException("Ảnh  bị null");
            }
            g2.drawImage(texture, Math.round(x), Math.round(y), width * 2, height * 2, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh  " + e.getMessage());
        }
    }

}
