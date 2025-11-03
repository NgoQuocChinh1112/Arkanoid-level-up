package PowerUps;

import Game.Renderer;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.Timer;

public class BigBallPowerUp extends PowerUp {
    /**
     * Constructor.
     */
    public BigBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "BIG_BALL");
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[0];

            if (texture == null) {
                throw new Exception("Texture bị null.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh  " + e.getMessage());
            texture = null;
        }
    }

    /**
     * Áp dụng hiệu ứng.
     */
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (ball.isEnlarged()) return;

        SoundEffect.play("bigball");

        int oldWidth = ball.getWidth();
        int oldHeight = ball.getHeight();
        int factor = 2;

        // Lấy tâm thật của bóng
        float oldCenterX = ball.getX() + ball.getWidth() / 2f;
        float oldCenterY = ball.getY() + ball.getHeight() / 2f;

        ball.setEnlarged(true);
        ball.setWidth(factor * oldWidth);
        ball.setHeight(factor * oldHeight);

        // Đặt lại vị trí để giữ nguyên tâm
        ball.setX(oldCenterX - ball.getWidth() / 2f);
        ball.setY(oldCenterY - ball.getHeight() / 2f);

        Timer timer = new Timer((int) durationMs, e -> {
            // Tính tâm hiện tại trước khi thu nhỏ
            float currentCenterX = ball.getX() + ball.getWidth() / 2f;
            float currentCenterY = ball.getY() + ball.getHeight() / 2f;

            ball.setWidth(oldWidth);
            ball.setHeight(oldHeight);

            // Giữ nguyên tâm hiện tại khi thu nhỏ
            ball.setX(currentCenterX - ball.getWidth() / 2f);
            ball.setY(currentCenterY - ball.getHeight() / 2f);
            ball.setEnlarged(false);


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
                throw new IOException("texture bị null");
            }
            g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh " + e.getMessage());
        }
    }
}