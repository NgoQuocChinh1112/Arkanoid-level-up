package PowerUps;

import Game.Renderer;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;
import Objects.Brick;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExplosiveBallPowerUp extends PowerUp {

    private static BufferedImage explosionImg;
    private boolean active = false;
    private BufferedImage texture;

    private static final List<Float> explosionX = new ArrayList<>();
    private static final List<Float> explosionY = new ArrayList<>();
    private static final List<Integer> explosionFrame = new ArrayList<>();
    private static final List<Integer> explosionCounter = new ArrayList<>();

    private static final int TOTAL_FRAMES = 6;   // 6 frame nổ
    private static final int FRAME_DELAY = 3;    // tốc độ chuyển frame

    /**
     * Constructor.
     */
    public ExplosiveBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "EXPLOSIVE_BALL");
        explosionImg = Renderer.loadExplosionTexture();

        // xử lý exception ở đây (Renderer chỉ load)
        try {
            BufferedImage[] powerUps = Renderer.loadPowerUpTexture();
            texture = powerUps[2]; // ExplosiveBall ở index 2
            if (texture == null) {
                throw new Exception("Texture  bị null.");
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi tải ảnh " + e.getMessage());
            texture = null;
        }
    }

    /**
     * Áp dụng hiệu ứng.
     */
    @Override
    public void applyEffect(Paddle paddle, Ball ball, Object gameManager) {
        if (active) return;
        active = true;

        ball.setExplosive(true);

        Timer timer = new Timer((int) durationMs, e -> {
            ball.setExplosive(false);
            active = false;
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Vẽ.
     */
    @Override
    public void render(Graphics2D g2) {
        try {
            if (texture == null) {
                throw new IOException("Ảnh  bị null");
            }
            g2.drawImage(texture, Math.round(x), Math.round(y), width * 2, height * 2, null);
        } catch (Exception e) {
            System.err.println("Không thể vẽ ảnh " + e.getMessage());

        }
    }

    /**
     * Đánh dấu các gạch bị nổ.
     */
    public static void explodeAt(List<Brick> bricks, float centerX, float centerY, float radius, List<Brick> toRemove) {
        SoundEffect.play("explosive");
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                float bx = brick.getX() + brick.getWidth() / 2f;
                float by = brick.getY() + brick.getHeight() / 2f;
                float dist = (float) Math.hypot(centerX - bx, centerY - by);

                if (dist < radius) {
                    if (brick.getHitPoints() == 6) continue;

                    while (!brick.isDestroyed()) brick.takeHit();

                    if (brick.isDestroyed()) {
                        toRemove.add(brick);
                    }
                }
            }
        }

        explosionX.add(centerX);
        explosionY.add(centerY);
        explosionFrame.add(0);
        explosionCounter.add(0);
    }

    /**
     * Cập nhật frame nổ.
     */
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

    /**
     * Vẽ vụ nổ.
     */
    private static void renderExplosion(Graphics2D g2, float x, float y, int frame) {
        if (explosionImg == null) return;

        int totalWidth = explosionImg.getWidth();
        int totalHeight = explosionImg.getHeight();

        int frameWidth = Math.round(totalWidth / (float) TOTAL_FRAMES);
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
