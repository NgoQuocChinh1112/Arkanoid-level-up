package PowerUps;

import Objects.Ball;
import Objects.Paddle;

import java.awt.*;

public class BigBallPowerUp extends PowerUp {
    public BigBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "BIG_BALL");
    }

    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        int oldwidth = ball.getWidth();
        int oldheight = ball.getHeight();
        if (ball.isEnlarged()) {
            // kiểm tra bóng đã phóng to chưa
            return;
        }
        int factor = 3;
        ball.setEnlarged(true);
        ball.setwidth( factor * ball.getWidth());
        ball.setHeight(factor * ball.getHeight());
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {}
            // reduce back if still in motion (approx)
            ball.setwidth(oldwidth);
            ball.setHeight(oldheight);
            ball.setEnlarged(false);
        }).start();

    }

    @Override
    public void render(java.awt.Graphics2D g2) {
        g2.setColor(new Color(220, 120, 40));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "B";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }

}
