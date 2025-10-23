package PowerUps;

import Objects.Ball;
import Objects.Paddle;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;


public class ExpandPaddlePowerUp extends PowerUp {
    private static int originalWidth = -1;
    private static long expandEndTime = 0;
    private static final Timer timer = new Timer(true);  // Daemon timer for cleanup
    private static TimerTask currentTask = null;

    public ExpandPaddlePowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXPAND_PADDLE");
        loadSound("assets/ExpandPaddle.wav");
    }

    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        long now = System.currentTimeMillis();

        if (originalWidth == -1) {
            originalWidth = paddle.getWidth();
        }

        long newEndTime = now + this.durationMs;
        if (newEndTime > expandEndTime) {
            expandEndTime = newEndTime;
        }

        int expandedWidth = Math.min(300, originalWidth + 80);

        playSound("assets/ExpandPaddle.wav");

        paddle.setWidth(expandedWidth);

        if (currentTask != null) {
            currentTask.cancel();
        }
        currentTask = new TimerTask() {
            @Override
            public void run() {
                if (now < expandEndTime) {
                    paddle.setWidth(originalWidth);
                }
                currentTask = null;
            }
        };
        timer.schedule(currentTask, expandEndTime - now);
    }


    public static void resetState() {
        if (currentTask != null) {
            currentTask.cancel();
            currentTask = null;
        }
        originalWidth = -1;
        expandEndTime = 0;
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