package PowerUps;

import java.awt.*;
import Game.GameManager;
import Objects.Ball;
import Objects.Paddle;

public class ExtraLifePowerUp extends PowerUp {
    /**
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     */
    public ExtraLifePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXTRA_LIFE");
        this.durationMs = 0;
    }

    /**
     * Áp dụng hiệu ứng.
     * @param paddle
     * @param mainBall
     * @param gameManager
     */
    @Override
    public void applyEffect(Paddle paddle, Ball mainBall, Object gameManager ) {
        if (gameManager instanceof GameManager) {
            GameManager game = (GameManager) gameManager;
            game.addLife();
        }
    }
    /**
     * Vẽ.
     * @param g2
     */
    public void render(Graphics2D g2) {
        g2.setColor(new Color(250, 50, 50));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "EX";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }

}
