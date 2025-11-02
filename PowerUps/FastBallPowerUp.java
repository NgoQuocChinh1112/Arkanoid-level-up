package PowerUps;

import Game.Renderer;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Timer;

public class FastBallPowerUp extends PowerUp {

    private Timer timer;
    private BufferedImage texture;

    /**
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     */
    public FastBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "FAST_BALL");
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[1];

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
     * @param ball
     * @param gameManager
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (ball.isFast()) return;

        SoundEffect.play("fastball");

        float factor = 1.6f;
        ball.setFast(true);
        ball.setDx(ball.getDx() * factor);
        ball.setDy(ball.getDy() * factor);


        timer = new Timer((int) durationMs, e -> {
            ball.setFast(false);
            ball.setDx(ball.getDx() / factor);
            ball.setDy(ball.getDy() / factor);
            timer.stop();
        });
        timer.setRepeats(false); // chỉ chạy 1 lần
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
            g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh  " + e.getMessage());
        }
    }
}
