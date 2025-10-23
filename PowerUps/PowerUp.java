package PowerUps;

import Objects.Ball;
import Objects.GameObject;
import Objects.Paddle;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

public abstract class PowerUp extends GameObject {
    protected long durationMs;
    protected String type;
    protected float dy = 2.0f;
    protected Clip clip;
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


    public abstract void render(Graphics2D g2);

    public abstract void applyEffect(Paddle paddle, Ball ball, Object gameManager);

    public String getType() { return type; }
    public long getDurationMs() { return durationMs; }

    public boolean isCollectedOrOffscreen() { return collectedOrOffscreen; }
    public void markCollectedOrOffscreen() { collectedOrOffscreen = true; }
}
