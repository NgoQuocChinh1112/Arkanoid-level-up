package PowerUps;

import Objects.Ball;
import Objects.Paddle;
import Objects.Brick;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;

public class ExplosiveBallPowerUp extends PowerUp {
    private static BufferedImage explosionImg;
    private boolean active = false;

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
        //ball.setExplosive(true);(tạm thời)

        // Tạo luồng đếm thời gian hiệu ứng
        new Thread(() -> {
            try {
                Thread.sleep(durationMs);
            } catch (InterruptedException ignored) {}
            //ball.setExplosive(false);(tạm thời)
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
     * Hàm nổ được gọi khi bóng chạm Brick, nằm trong Ball hoặc GameManager.
     * Phá hủy gạch lân cận (bán kính 1 ô hoặc khoảng cách < R).
     */
    public static void explodeAt(List<Brick> bricks, float centerX, float centerY, float radius) {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                float bx = brick.getX() + brick.getWidth() / 2f;
                float by = brick.getY() + brick.getHeight() / 2f;
                float dist = (float)Math.hypot(centerX - bx, centerY - by);
                if (dist < radius) {
                    brick.takeHit(); // phá gạch trong phạm vi
                }
            }
        }
    }

    public static void renderExplosion(Graphics2D g2, float x, float y, int frame, int totalFrames) {
        if (explosionImg == null) return;
        int frameWidth = explosionImg.getWidth() / totalFrames;
        g2.drawImage(
                explosionImg,
                (int)(x - frameWidth/2),
                (int)(y - frameWidth/2),
                (int)(x + frameWidth/2),
                (int)(y + frameWidth/2),
                frame * frameWidth, 0,
                (frame + 1) * frameWidth, explosionImg.getHeight(),
                null
        );
    }
}
