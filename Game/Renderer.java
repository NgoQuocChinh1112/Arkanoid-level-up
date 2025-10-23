package Game;

import Objects.GameObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Renderer {

    private static BufferedImage[] background_level;

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

    public static BufferedImage loadPauseImage() {
        return loadTexture("/assets/start_top.png");
    }

    public static BufferedImage loadMenuPauseImage() {
        return loadTexture("/assets/menu_paused_top.png");
    }

    public static BufferedImage[] loadBrickTexture() {
        BufferedImage[] textures = new  BufferedImage[6];
        textures[0] = loadTexture("/assets/brick_white.png");           // level 1
        textures[1] = loadTexture("/assets/brick_blue.png");            // level 2
        textures[2] = loadTexture("/assets/brick_green.png");           // level 3
        textures[3] = loadTexture("/assets/brick_yellow.png");          // level 4
        textures[4] = loadTexture("/assets/brick_red.png");             // level 5
        textures[5] = loadTexture("/assets/unbreakable_brick.png");     // level 6
        return textures;
    }

    public static BufferedImage loadBallTexture() {
        return loadTexture("/assets/ball_1.png");
    }

    public static BufferedImage loadBgroundTexture(int currentLevel) {
        background_level = new BufferedImage[10];

        background_level[0] = loadTexture("/assets/BG_Level_1.png");
        background_level[1] = loadTexture("/assets/BG_Level_2.jpg");
        background_level[2] = loadTexture("/assets/BG_Level_3.png");
        background_level[3] = loadTexture("/assets/BG_Level_4.png");
        background_level[4] = loadTexture("/assets/BG_Level_5.png");
        background_level[5] = loadTexture("/assets/BG_Level_6.png");
        background_level[6] = loadTexture("/assets/BG_Level_7.png");
        background_level[7] = loadTexture("/assets/BG_Level_8.png");
        background_level[8] = loadTexture("/assets/BG_Level_9.png");
        background_level[9] = loadTexture("/assets/BG_Level_10.jpg");
        return background_level[currentLevel - 1];
    }

    public static BufferedImage[] loadbuttonTexture() {
        BufferedImage[] button = new BufferedImage[12];
        button[0] = loadTexture(("/assets/menu.png"));
        button[1] = loadTexture(("/assets/pause_button.png"));
        button[2] = loadTexture(("/assets/menu_button.png"));
        button[3] = loadTexture(("/assets/menu_button_bot.png"));
        button[4] = loadTexture(("/assets/resume_button.png"));
        button[5] = loadTexture(("/assets/resume_button_bot.png"));
        button[6] = loadTexture(("/assets/restart_button.png"));
        button[7] = loadTexture(("/assets/restart_button_bot.png"));
        button[8] = loadTexture(("/assets/setting_button.png"));
        button[9] = loadTexture(("/assets/setting_button_bot.png"));
        button[10] = loadTexture(("/assets/level_button.png"));
        button[11] = loadTexture(("/assets/level_button_bot.png"));
        return button;
    }

    public static BufferedImage loadPaddleTexture() {
        return loadTexture("/assets/paddle_1.png");
    }

    
}