package PowerUps;

import Objects.Ball;
import Objects.Paddle;

import java.awt.*;
import javax.swing.Timer;

public class FastBallPowerUp extends PowerUp {
    public FastBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "FAST_BALL");
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (ball.isFast()) return;

        // Lưu tốc độ ban đầu
        float oldDx = ball.getDx();
        float oldDy = ball.getDy();
        float factor = 1.6f;

        // Đánh dấu trạng thái
        ball.setFast(true);

        // Tăng tốc độ
        ball.setDx(ball.getDx() * factor);
        ball.setDy(ball.getDy() * factor);

        // Tạo timer để khôi phục sau durationMs mili-giây
        Timer timer = new Timer((int) durationMs, e -> {
            // Chỉ khôi phục nếu bóng vẫn tồn tại
            ball.setDx(oldDx);
            ball.setDy(oldDy);
            ball.setFast(false);
        });

        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void render(java.awt.Graphics2D g2) {
        g2.setColor(new Color(220, 120, 40));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "F";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }
}
