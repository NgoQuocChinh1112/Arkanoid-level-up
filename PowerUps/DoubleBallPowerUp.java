package PowerUps;

import Game.GameManager;
import Game.Renderer;
import Objects.Ball;
import Objects.Paddle;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class DoubleBallPowerUp extends PowerUp {

    private static final int BALL_SIZE = 20;

    /**
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     */
    public DoubleBallPowerUp(float x, float y, int width, int height,long durationMs) {
        super(x, y, width, height, durationMs, "DOUBLE_BALL");
        this.durationMs = 0;
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[6];

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
     * @param paddle
     * @param originalBall
     * @param gameManagerobj
     */
    @Override
    public void applyEffect(Paddle paddle, Ball originalBall, Object gameManagerobj) {
        GameManager gameManager = (GameManager) gameManagerobj;

        // CHỈ TẠO BÓNG PHỤ NẾU CHƯA CÓ
        if (gameManager.extraBall != null) {
            return; // Đã có bóng phụ → bỏ qua
        }

        float scaleY = (float) width / 24f;

        // Tạo bóng phụ
        Ball extra = new Ball(x + width / 2f - (BALL_SIZE * scaleY) / 2f, y,
                (int) (BALL_SIZE * scaleY), (int) (BALL_SIZE * scaleY));

        extra.launch(0, 6f); // Phóng thẳng xuống
        gameManager.addExtraBall(extra);

    }

    /**
     * Vẽ.
     * @param g2
     */
    @Override
    public void render(Graphics2D g2) {
        try {
            if (texture == null) {
                throw new IOException("texture null");
            }
            g2.drawImage(texture, Math.round(x), Math.round(y), width * 2, height * 2, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh  " + e.getMessage());
        }
    }
}