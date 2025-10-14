package Game;

import Objects.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Renderer {
    public static void drawRect(Graphics2D g2, GameObject obj, Color fill, Color border) {
        g2.setColor(fill);
        g2.fillRect(Math.round(obj.getX()), Math.round(obj.getY()), obj.getWidth(), obj.getHeight());
        g2.setColor(border);
        g2.drawRect(Math.round(obj.getX()), Math.round(obj.getY()), obj.getWidth(), obj.getHeight());
    }

    public static final Map<String, BufferedImage> textureCache = new HashMap<>();
    public static BufferedImage loadTexture(String path) {
        if (textureCache.containsKey(path)) {
            return textureCache.get(path);
        }
        try {
            BufferedImage image = ImageIO.read(Renderer.class.getResource(path));
            textureCache.put(path, image);
            return image;
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Không thể tải ảnh: " + path);
            return null;
        }
    }

    public static BufferedImage[] loadBrickTexture() {
        BufferedImage[] textures = new  BufferedImage[5];
        textures[0] = loadTexture("/assets/brick_white.png");   // level 1
        textures[1] = loadTexture("/assets/brick_blue.png");    // level 2
        textures[2] = loadTexture("/assets/brick_green.png");   // level 3
        textures[3] = loadTexture("/assets/brick_yellow.png");  // level 4
        textures[4] = loadTexture("/assets/brick_red.png");     // level 5
        return textures;
    }

    public static BufferedImage loadBallTexture() {
        return loadTexture("/assets/ball_1.png");
    }

    public static BufferedImage loadBgroundTexture() {
        return loadTexture(("/assets/back_ground.png"));
    }

    public static BufferedImage loadPaddleTexture() {
            return loadTexture("/assets/paddle_1.png");
    }
    
}