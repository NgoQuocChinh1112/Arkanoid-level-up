package Game;

import javax.imageio.ImageIO;
import static Game.GamePanel.scale;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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

    public static Font loadFond(Graphics2D g2, int size) {
        Font customFont;
        try {
            customFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File("assets/font.ttf")
            ).deriveFont(Font.BOLD, (float)size);
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("SansSerif", Font.BOLD, (int)(size * scale));
        }
        return customFont;
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
        BufferedImage[] button = new BufferedImage[16];
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
        button[12] = loadTexture(("/assets/next_level.png"));
        button[13] = loadTexture(("/assets/next_level_bot.png"));
        button[14] = loadTexture(("/assets/on_volume.png"));
        button[15] = loadTexture(("/assets/off_volume.png"));
        return button;
    }

    public static BufferedImage loadPaddleTexture() {
        return loadTexture("/assets/paddle_1.png");
    }

    public static BufferedImage loadHeartTexture() {
        return loadTexture("/assets/heart.png");
    }

    public static BufferedImage loadDamageTexture() {
        return loadTexture("/assets/damage.png");
    }

    public static BufferedImage loadGameOverTexture() {
        return loadTexture("/assets/gameover.png");
    }
    
    public static BufferedImage loadPressStartTexture() {
        return loadTexture("/assets/press_start.png");
    }
    public static BufferedImage loadArrowTexture() {
        return loadTexture("/assets/arrow.png");
    }

    public static BufferedImage[] loadPowerUpTexture() {
        BufferedImage[] PowerUp = new BufferedImage[7];
        PowerUp[0] = loadTexture(("/assets/BigBall.png"));
        PowerUp[1] = loadTexture(("/assets/FastBall.png"));
        PowerUp[2] = loadTexture(("/assets/ExplosiveBall.png"));
        PowerUp[3] = loadTexture(("/assets/ExpandPaddle.png"));
        PowerUp[4] = loadTexture(("/assets/ShrinkPaddle.png"));
        PowerUp[5] = loadTexture(("/assets/ExtraLife.png"));
        PowerUp[6] = loadTexture(("/assets/DoubleBall.png"));
        return PowerUp;
    }

    public static BufferedImage loadExplosionTexture() {
        BufferedImage img = textureCache.get("explosive_effect");
        if (img != null) return img;

        try {
            img = ImageIO.read(Renderer.class.getResourceAsStream("/assets/explosive.png"));
            textureCache.put("explosive_effect", img);
            return img;
        } catch (IOException | NullPointerException e) {
            System.err.println("Không thể tải ảnh explosion: " + e.getMessage());
            return null;
        }
    }



}