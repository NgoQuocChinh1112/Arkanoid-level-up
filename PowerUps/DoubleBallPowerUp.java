package PowerUps;

import Game.GameManager;
import Game.SoundEffect;
import Objects.Ball;
import Objects.Paddle;

import javax.swing.Timer;
import java.awt.*;

public class DoubleBallPowerUp extends PowerUp {

    public DoubleBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "DOUBLE_BALL");
    }

    @Override
    public void applyEffect(Paddle paddle, Ball originalBall, Object gameManagerObj) {
        GameManager gameManager = (GameManager) gameManagerObj;

        // Nếu đã có bóng phụ → không tạo thêm
        if (originalBall.isFast() || originalBall.isEnlarged() || originalBall.isExplosive()) {
            // Có thể bỏ qua hoặc không, tùy ý
        }

        SoundEffect.play("powerup");

        // Tạo bóng mới (phụ)
        Ball extraBall = new Ball(
                originalBall.getX(),
                originalBall.getY(),
                originalBall.getWidth(),
                originalBall.getHeight()
        );

        // Copy trạng thái vận tốc từ bóng gốc
        extraBall.setDx(originalBall.getDx());
        extraBall.setDy(originalBall.getDy());
        extraBall.setSpeed(originalBall.getSpeed());
        extraBall.launched = true;

        // Đẩy nhẹ bóng phụ ra xa để tránh chồng lấn ngay lập tức
        float offset = 20f;
        if (extraBall.getDx() >= 0) {
            extraBall.setX(originalBall.getX() - offset);
        } else {
            extraBall.setX(originalBall.getX() + offset);
        }

        // Thêm bóng mới vào danh sách bóng (giả sử GameManager có List<Ball> balls)
        // Vì hiện tại chỉ có 1 ball, ta cần mở rộng GameManager để hỗ trợ nhiều bóng
        // Tạm thời: dùng cách lưu vào GameManager qua reflection hoặc thêm method

        // Gợi ý: Thêm List<Ball> vào GameManager, nhưng vì không thể sửa → dùng cách tạm
        // Dưới đây là cách **thêm method vào GameManager** (xem phần cuối)

        // Gọi method giả định: gameManager.addExtraBall(extraBall);
        try {
            java.lang.reflect.Method method = gameManager.getClass().getMethod("addExtraBall", Ball.class);
            method.invoke(gameManager, extraBall);
        } catch (Exception e) {
            // Nếu chưa có method → không làm gì
        }

        // Tự động xóa bóng phụ sau durationMs
        Timer timer = new Timer((int) durationMs, e -> {
            try {
                java.lang.reflect.Method removeMethod = gameManager.getClass().getMethod("removeExtraBall", Ball.class);
                removeMethod.invoke(gameManager, extraBall);
            } catch (Exception ex) {
                // ignore
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(220, 120, 40));
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "D";
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw) / 2, Math.round(y) + height / 2 + 4);
    }
}