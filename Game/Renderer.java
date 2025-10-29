package Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Renderer {

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

    public static List <List<BufferedImage>> loadBrickTexture() {
        List<List<BufferedImage>> textures = new ArrayList<>();
        List<BufferedImage> white = new ArrayList<>();
        white.add(loadTexture("/other_assets/brick_white_0.png"));

        List<BufferedImage> blue = new ArrayList<>();
        blue.add(loadTexture("/other_assets/brick_blue_1.png"));
        blue.add(loadTexture("/other_assets/brick_blue_0.png"));

        List<BufferedImage> green = new ArrayList<>();
        green.add(loadTexture("/other_assets/brick_green_2.png"));
        green.add(loadTexture("/other_assets/brick_green_1.png"));
        green.add(loadTexture("/other_assets/brick_green_0.png"));

        List<BufferedImage> yellow = new ArrayList<>();
        yellow.add(loadTexture("/other_assets/brick_yellow_3.png"));
        yellow.add(loadTexture("/other_assets/brick_yellow_2.png"));
        yellow.add(loadTexture("/other_assets/brick_yellow_1.png"));
        yellow.add(loadTexture("/other_assets/brick_yellow_0.png"));

        List<BufferedImage> red = new ArrayList<>();
        red.add(loadTexture("/other_assets/brick_red_4.png"));
        red.add(loadTexture("/other_assets/brick_red_3.png"));
        red.add(loadTexture("/other_assets/brick_red_2.png"));
        red.add(loadTexture("/other_assets/brick_red_1.png"));
        red.add(loadTexture("/other_assets/brick_red_0.png"));

        List<BufferedImage> unbreaker = new ArrayList<>();
        unbreaker.add(loadTexture("/other_assets/brick_unbreaker_0.png"));

        textures.add(white);
        textures.add(blue);
        textures.add(green);
        textures.add(yellow);
        textures.add(red);
        textures.add(unbreaker);
        return textures;
    }

    public static BufferedImage loadBallTexture() {
        return loadTexture("/assets/ball_1.png");
    }

    public static BufferedImage loadBgroundTexture(int currentLevel) {
        BufferedImage[] background_level = new BufferedImage[10];

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
        BufferedImage[] button = new BufferedImage[18];
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
        button[12] = loadTexture(("/assets/okay_bot.png"));
        button[13] = loadTexture(("/assets/okay_top.png"));
        button[14] = loadTexture(("/assets/cancel_bot.png"));
        button[15] = loadTexture(("/assets/cancel_top.png"));
        button[16] = loadTexture(("/assets/off.png"));
        button[17] = loadTexture(("/assets/on.png"));
        return button;
    }

    public static BufferedImage loadPaddleTexture() {
        return loadTexture("/assets/paddle_1.png");
    }

    
}