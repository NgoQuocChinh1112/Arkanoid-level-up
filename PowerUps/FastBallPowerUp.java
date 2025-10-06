package PowerUps;

import Objects.Ball;
import Objects.Paddle;
import java.awt.*;

public class FastBallPowerUp extends PowerUp {
    private static final float SPEED_FACTOR = 1.6f;
    private static final Color MAIN_COLOR = new Color(220, 120, 40);

    public FastBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "FAST_BALL");
    }
    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        float originalDx = ball.getDx();
        float originalDy = ball.getDy();

        // Tăng tốc bóng
        ball.setDx(originalDx * SPEED_FACTOR);
        ball.setDy(originalDy * SPEED_FACTOR);

        // Sau khi hết thời gian, khôi phục tốc độ cũ
        javax.swing.Timer timer = new javax.swing.Timer((int) DurationMs, e -> {
            ball.setDx(originalDx);
            ball.setDy(originalDy);
        });
        timer.setRepeats(false); // chỉ chạy 1 lần
        timer.start();
    }



    @Override
    public void render(Graphics2D g2) {
        g2.setColor(MAIN_COLOR);
        g2.fillOval(Math.round(x), Math.round(y), width, height);

        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String label = "F";
        int textWidth = g2.getFontMetrics().stringWidth(label);
        g2.drawString(label, Math.round(x) + (width - textWidth) / 2,
                Math.round(y) + height / 2 + 4);
    }
}
