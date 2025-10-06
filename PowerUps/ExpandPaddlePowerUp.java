package PowerUps;

import Objects.Ball;
import Objects.Paddle;

import java.awt.*;

public class ExpandPaddlePowerUp extends PowerUp {
    private int originalWidth = -1;

    public ExpandPaddlePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXPAND_PADDLE");
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (originalWidth == -1) originalWidth = paddle.getWidth();
        int newW = Math.min(300, originalWidth + 80);
        paddle.setWidth(newW);
        // schedule removal after duration
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {}
            paddle.setWidth(originalWidth);
        }).start();
    }

    @Override
    public void render(java.awt.Graphics2D g2) {
        g2.setColor(new Color(60, 180, 75));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "E";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }
}
