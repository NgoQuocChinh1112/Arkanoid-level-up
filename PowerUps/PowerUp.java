package PowerUps;

import Objects.Ball;
import Objects.GameObject;
import Objects.Paddle;
import java.awt.*;

public abstract class PowerUp extends GameObject {
    protected long durationMs;
    protected String type;
    protected float dy = 2.0f;
    protected boolean collectedOrOffscreen = false;

    /**
     * Constructor.
     */
    public PowerUp(float x, float y, int width, int height, long durationMs, String type) {
        super(x,y,width,height);
        this.durationMs = durationMs;
        this.type = type;
    }

    /**
     * PowerUp falling.
     */
    @Override
    public void update() {
        y += dy;
    }

    /**
     * Vẽ.
     */
    public abstract void render(Graphics2D g2);

    /**
     * Áp dụng.
     */
    public abstract void applyEffect(Paddle paddle, Ball ball, Object gameManager);

    /**
     * Trả về kiểu powerup.
     */
    public String getType() { return type; }

    /**
     * Trả về thời gian hoạt động.
     */
    public long getDurationMs() { return durationMs; }

    /**
     * Trả về trạng thái ra ngoài hay ăn của powerup.
     */
    public boolean isCollectedOrOffscreen() { return collectedOrOffscreen; }

    /**
     * Thiết lập trạng thái ra ngoài hay ăn của powerup.
     */
    public void markCollectedOrOffscreen() { collectedOrOffscreen = true; }
}
