package PowerUps;

import Objects.Ball;
import Objects.GameObject;
import Objects.Paddle;

import java.awt.*;
import javax.swing.Timer;

public abstract class PowerUp extends GameObject {
    protected long durationMs;
    protected String type;
    protected float dy = 2.0f;
    protected boolean collectedOrOffscreen = false;
    public PowerUp(float x, float y, int width, int height, long durationMs, String type) {
        super(x,y,width,height);
        this.durationMs = durationMs;
        this.type = type;
    }

    @Override
    public void update() {
        y += dy;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.YELLOW);
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = getType().substring(0,1);
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }

    public abstract void applyEffect(Paddle paddle, Ball ball, Object gameManager);

    public String getType() { return type; }
    public long getDurationMs() { return durationMs; }

    public boolean isCollectedOrOffscreen() { return collectedOrOffscreen; }
    public void markCollectedOrOffscreen() { collectedOrOffscreen = true; }
}
