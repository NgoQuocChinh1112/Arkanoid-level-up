package Objects;

import java.awt.*;
import Game.Renderer;
import Game.SoundEffect;
import java.util.List;

import java.awt.image.BufferedImage;

public class Brick extends GameObject {
    private final int hitPoints;
    private int heart;
    private final List<List<BufferedImage>> textures = Renderer.loadBrickTexture();

    public Brick(float x, float y, int width, int height, int level) {
        super(x, y, width, height);
        this.hitPoints = level;
        this.heart = level;
        if (level == 6) {
            texture = textures.get(this.hitPoints - 1).getFirst();
        } else {
            texture = textures.get(this.hitPoints - 1).get(this.heart - 1);
        }
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
            SoundEffect.play("collision");
            if (hitPoints != 6 && heart > 0) {
                heart--;
            }
        }
        if (hitPoints > 0) {
            if (heart <= 0) {
                texture = textures.get(hitPoints - 1).getFirst();
            }  else {
                if (hitPoints == 6) {
                    texture = textures.get(hitPoints - 1).getFirst();
                } else {
                    texture = textures.get(hitPoints - 1).get(heart - 1);
                }
            }
        } else {
            SoundEffect.play("break");
            texture = null;
        }
    }

    public boolean isDestroyed() {
        return heart <= 0;
    }

    public int getHitPoints() {
        return hitPoints;
    }
}
