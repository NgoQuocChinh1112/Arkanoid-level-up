package PowerUps;

import Objects.Ball;
import Objects.Paddle;
import Objects.Brick;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;

public class ExplosiveBallPowerUp extends PowerUp {
    private static BufferedImage explosionImg;
    private boolean active = false;

    private static final List<Float> explosionX = new ArrayList<>();
    private static final List<Float> explosionY = new ArrayList<>();
    private static final List<Integer> explosionFrame = new ArrayList<>();
    private static final List<Integer> explosionCounter = new ArrayList<>();


    private static final int TOTAL_FRAMES = 6;   //  6 frame
    private static final int FRAME_DELAY = 3;    // tốc độ chuyển frame

    public ExplosiveBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXPLOSIVE_BALL");
        loadImage();
    }

    private void loadImage() {
        if (explosionImg == null) {
            try {
                explosionImg = ImageIO.read(getClass().getResourceAsStream("/assets/explosive.png"));
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

        // Tạo Timer để tắt hiệu ứng sau durationMs mili-giây
        Timer timer = new Timer((int) durationMs, e -> {
            ball.setExplosive(false);
            active = false;
        });

        timer.setRepeats(false); // chỉ chạy một lần
        timer.start();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(220, 120, 40));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "E";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw) / 2, Math.round(y) + height / 2 + 4);
    }

    //
    public static void explodeAt(List<Brick> bricks, float centerX, float centerY, float radius) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                float bx = brick.getX() + brick.getWidth() / 2f;
                float by = brick.getY() + brick.getHeight() / 2f;
                float dist = (float) Math.hypot(centerX - bx, centerY - by);
                if (dist < radius) {

                    for (int i = 0; i < 5; i++) brick.takeHit();
                }
            }
        }


        explosionX.add(centerX);
        explosionY.add(centerY);
        explosionFrame.add(0);
        explosionCounter.add(0);
    }

    public static void updateExplosions() {
        for (int i = 0; i < explosionFrame.size(); i++) {
            int counter = explosionCounter.get(i) + 1;
            if (counter >= FRAME_DELAY) {
                counter = 0;
                int frame = explosionFrame.get(i) + 1;
                explosionFrame.set(i, frame);
                if (frame >= TOTAL_FRAMES) {
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

    public static void drawExplosions(Graphics2D g2) {
        for (int i = 0; i < explosionFrame.size(); i++) {
            renderExplosion(g2, explosionX.get(i), explosionY.get(i), explosionFrame.get(i));
        }
    }

    private static void renderExplosion(Graphics2D g2, float x, float y, int frame) {
        if (explosionImg == null) return;

        int totalWidth = explosionImg.getWidth();   // ảnh sprite tổng
        int totalHeight = explosionImg.getHeight(); // chiều cao ảnh
        int totalFrames = TOTAL_FRAMES;

        // Tính vùng cắt đều chính xác cho 6 frame
        int frameWidth = Math.round(totalWidth / (float) totalFrames);
        int sx1 = frame * frameWidth;
        int sx2 = sx1 + frameWidth;

        int drawSize = 96;
        int half = drawSize / 2;

        g2.setComposite(AlphaComposite.SrcOver.derive(0.9f));
        g2.drawImage(
                explosionImg,
                (int) (x - half), (int) (y - half),
                (int) (x + half), (int) (y + half),
                sx1, 0, sx2, totalHeight,
                null
        );
        g2.setComposite(AlphaComposite.SrcOver);
    }
}
