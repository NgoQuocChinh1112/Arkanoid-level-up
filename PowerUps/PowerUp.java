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
     *
     * Constructor.
     * @param x
     * @param y
     * @param width
     * @param height
     * @param durationMs
     * @param type
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
     * @param g2
     */
    public abstract void render(Graphics2D g2);

    /**
     * Áp dụng.
     * @param paddle
     * @param ball
     * @param gameManager
     */
    public abstract void applyEffect(Paddle paddle, Ball ball, Object gameManager);

    /**
     * Trả về kiểu powerup.
     * @return type
     */
    public String getType() { return type; }

    /**
     * Trả về thời gian hoạt động.
     * @return
     */
    public long getDurationMs() { return durationMs; }

    /**
     * Trả về trạng thái ra ngoài hay ăn của powerup.
     * @return
     */
    public boolean isCollectedOrOffscreen() { return collectedOrOffscreen; }

    /**
     * Thiết lập trạng thái ra ngoài hay ăn của powerup.
     */
    public void markCollectedOrOffscreen() { collectedOrOffscreen = true; }
}
