package Objects;

import java.awt.*;
import javax.imageio.ImageIO;
import Game.Renderer;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Brick extends GameObject {
    protected static final int MAX_LEVEL = 5;
    protected int hitPoints;
    private BufferedImage[] textures;
    protected int level;

    public Brick(float x, float y, int width, int height, int hitPoints, int level) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.level = level;
        if (textures == null) {
            textures = Renderer.loadBrickTexture();
        }
        texture = textures[this.level - 1];
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        if (!isDestroyed()) {
            g2.drawImage(texture, (int)x, (int)y, width, height, null);
        }
    }

    public void takeHit() {
        if (hitPoints > 0) {
            hitPoints--;
            if (hitPoints > 0) {
                texture = textures[hitPoints - 1];
            } else {
                texture = null;
            }
        }
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int level() {
        return level;
    }
}
