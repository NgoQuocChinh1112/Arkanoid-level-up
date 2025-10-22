package PowerUps;

import Objects.Ball;
import Objects.Paddle;

import java.awt.*;

public class BigBallPowerUp extends PowerUp {
    public BigBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "BIG_BALL");
    }

    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (ball.isEnlarged()) return;

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

        javax.swing.Timer timer = new javax.swing.Timer((int) durationMs, e -> {
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
