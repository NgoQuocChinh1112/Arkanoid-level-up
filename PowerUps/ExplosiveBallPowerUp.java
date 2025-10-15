package PowerUps;

import Objects.Ball;
import Objects.Paddle;
import Objects.Brick;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExplosiveBallPowerUp extends PowerUp {
    private static BufferedImage explosionImg;
    private boolean active = false;


    private static final List<Float> explosionX = new ArrayList<>();
    private static final List<Float> explosionY = new ArrayList<>();
    private static final List<Integer> explosionFrame = new ArrayList<>();
    private static final List<Integer> explosionCounter = new ArrayList<>();

    private static final int TOTAL_FRAMES = 7;  // số khung hình trong ảnh explosion.jpg
    private static final int FRAME_DELAY = 3;   // số lần cập nhật trước khi chuyển frame

    public ExplosiveBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXPLOSIVE_BALL");
        loadImage();
    }

    private void loadImage() {
        if (explosionImg == null) {
            try {
                explosionImg = ImageIO.read(getClass().getResourceAsStream("/assets/explosive.jpg"));
            } catch (IOException | NullPointerException e) {
                System.err.println("Không thể tải ảnh explosion: " + e.getMessage());
            }
        }
    }

    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (active) return;
        active = true;

        // Gắn cờ explosive cho bóng
        ball.setExplosive(true);

        // Tạo luồng đếm thời gian hiệu ứng
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {}
            ball.setExplosive(false);
            active = false;
        }).start();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "E";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 4);
    }

    /**
     * Gọi khi bóng nổ — phá gạch xung quanh và thêm hiệu ứng nổ.
     */
    public static void explodeAt(List<Brick> bricks, float centerX, float centerY, float radius) {
        // 1. Phá gạch trong vùng bán kính
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                float bx = brick.getX() + brick.getWidth() / 2f;
                float by = brick.getY() + brick.getHeight() / 2f;
                float dist = (float) Math.hypot(centerX - bx, centerY - by);
                if (dist < radius) {
                    brick.takeHit(); // phá gạch trong phạm vi
                }
            }
        }

        // 2. Thêm vụ nổ mới vào danh sách
        explosionX.add(centerX);
        explosionY.add(centerY);
        explosionFrame.add(0);
        explosionCounter.add(0);

        //  explosion.wav
        // playExplosionSound();
    }

    /**
     * Cập nhật frame của tất cả vụ nổ.
     * Gọi mỗi lần trong updateGame().
     */
    public static void updateExplosions() {
        for (int i = 0; i < explosionFrame.size(); i++) {
            int counter = explosionCounter.get(i) + 1;
            if (counter >= FRAME_DELAY) {
                counter = 0;
                int frame = explosionFrame.get(i) + 1;
                explosionFrame.set(i, frame);
                if (frame >= TOTAL_FRAMES) {
                    // Kết thúc vụ nổ này → xóa khỏi danh sách
                    explosionX.remove(i);
                    explosionY.remove(i);
                    explosionFrame.remove(i);
                    explosionCounter.remove(i);
                    i--;
                    continue;
                }
            }
            explosionCounter.set(i, counter);
        }
    }

    /**
     * Vẽ tất cả vụ nổ hiện có.
     * Gọi trong paintComponent(Graphics g).
     */
    public static void drawExplosions(Graphics2D g2) {
        for (int i = 0; i < explosionFrame.size(); i++) {
            renderExplosion(g2, explosionX.get(i), explosionY.get(i), explosionFrame.get(i));
        }
    }

    /**
     * Vẽ 1 frame cụ thể của vụ nổ.
     */
    private static void renderExplosion(Graphics2D g2, float x, float y, int frame) {
        if (explosionImg == null) return;
        int frameWidth = explosionImg.getWidth() / TOTAL_FRAMES;
        int frameHeight = explosionImg.getHeight();

        g2.drawImage(
                explosionImg,
                (int)(x - frameWidth / 2f),
                (int)(y - frameHeight / 2f),
                (int)(x + frameWidth / 2f),
                (int)(y + frameHeight / 2f),
                frame * frameWidth, 0,
                (frame + 1) * frameWidth, explosionImg.getHeight(),
                null
        );
    }


    /* temp
    private static void playExplosionSound() {
        try {
            javax.sound.sampled.AudioInputStream audioIn = javax.sound.sampled.AudioSystem.getAudioInputStream(
                    ExplosiveBallPowerUp.class.getResource("/assets/explosion.wav"));
            javax.sound.sampled.Clip clip = javax.sound.sampled.AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.err.println("Không thể phát âm thanh nổ: " + e.getMessage());
        }
    }
    */
}