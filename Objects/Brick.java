package Objects;

import java.awt.*;
import Game.Renderer;
import Game.SoundEffect;
import java.util.List;

import java.awt.image.BufferedImage;

public class Brick extends MovableObject {
    private final int hitPoints;
    private int heart;
    private float speed = 2f;
    private final int type;
    private final int dis;
    private float maxX;
    private float minX;
    private float maxY;
    private float minY;
    private final List<List<BufferedImage>> textures = Renderer.loadBrickTexture();


    public Brick(float x, float y, int width, int height, int temp) {
        super(x, y, width, height);
        this.hitPoints = temp / 100;
        this.heart = temp / 100;
        this.type = (temp / 10) % 10;
        this.dis = temp % 10;
        setVector();
        if (this.hitPoints == 6) {
            texture = textures.get(this.hitPoints - 1).getFirst();
        } else {
            texture = textures.get(this.hitPoints - 1).get(this.heart - 1);
        }
    }

    @Override
    public void render(Graphics2D g2) {
        if (!isDestroyed()) {
            g2.drawImage(texture, (int)x, (int)y, width, height, null);
        }
    }

    public void takeHit() {
        if (hitPoints > 0) {
            SoundEffect.play("BallBrickCol");
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
            texture = null;
        }
    }

    public boolean isDestroyed() {
        return heart <= 0;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getMinX() {
        return this.minX;
    }
    public float getMaxX() {
        return this.maxX;
    }
    public float getMinY() {
        return this.minY;
    }
    public float getMaxY() {
        return this.maxY;
    }
    public int getType() {
        return type;
    }

    public void changeVector() {
        if (x > maxX && maxX != 0) {
            x = maxX - speed;
            dx = -dx;
        } else if (x < minX && minX != 0) {
            x = minX + speed;
            dx = -dx;
        }
        if (y > maxY && maxY != 0) {
            y = maxY - speed;
            dy = -dy;
        } else if (y < minY && minY != 0) {
            y = minY + speed;
            dy = -dy;
        }
    }
    public void setVector() {
        switch (type) {
            case 0:
                dx = 0;
                dy = 0;
                break;
            case 1:
                this.minX = x;
                this.maxX = x + dis *  64;
                dx = speed;
                dy = 0;
                break;
            case 2:
                this.minX = x - dis * 64;
                this.maxX = x;
                dx = -speed;
                dy = 0;
                break;
            case 3:
                this.minY = y;
                this.maxY = y + dis * 24;
                dx = 0;
                dy = speed;
                break;
            case 4:
                this.minY = y - dis * 24;
                this.maxY = y;
                dx = 0;
                dy = -speed;
                break;
            default:
                System.out.println("Sai rồi má!");
                break;
        }
    }
}