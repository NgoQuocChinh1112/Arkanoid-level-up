package PowerUps;

import Game.GameManager;
import Objects.Ball;
import Objects.Paddle;
import javax.swing.*;
import java.awt.*;

public class DoubleBallPowerUp extends PowerUp {

    private static final int BALL_SIZE = 16;
    private Timer timer;

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
        g2.setColor(new Color(220, 120, 40)); // Cam
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "D";
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw) / 2, Math.round(y) + height / 2 + 4);
    }
}