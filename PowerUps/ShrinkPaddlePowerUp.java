package PowerUps;

import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;
import java.awt.*;
import javax.swing.Timer;

public class ShrinkPaddlePowerUp extends PowerUp {
    private static int originalWidth = -1;
    private Timer timer;


    /**
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     */
    public ShrinkPaddlePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "SHRINK_PADDLE");
    }

    /**
     * Áp dụng hiệu ứng.
     * @param paddle
     * @param ball
     * @param gameManager
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (originalWidth == -1) {
            originalWidth = paddle.getWidth();
        }
        SoundEffect.play("shrinkpad");
        int shrunkWidth = Math.max(40, originalWidth - 60);
        paddle.setWidth(shrunkWidth);
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer((int) durationMs, e -> {
            paddle.setWidth(originalWidth);
            timer.stop();
        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Vẽ.
     * @param g2
     */
    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(60, 180, 75));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "S";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }
}
