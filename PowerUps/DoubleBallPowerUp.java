package PowerUps;

import Game.GameManager;
import Objects.Ball;
import Objects.Paddle;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DoubleBallPowerUp extends PowerUp {

    private static final int BALL_SIZE = 16;
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private Timer timer; // Lưu timer để quản lý

    public DoubleBallPowerUp(float x, float y, int width, int height, long durationMs) {
        super(x, y, width, height, durationMs, "DOUBLE_BALL");
    }

    @Override
    public void applyEffect(Paddle paddle, Ball originalBall, Object gameManagerObj) {
        GameManager gameManager = (GameManager) gameManagerObj;

        // CHỈ TẠO BÓNG PHỤ NẾU CHƯA CÓ
        if (gameManager.extraBall != null) {
            return; // Đã có bóng phụ → bỏ qua
        }

        float scaleY = (float) width / 24f;

        // Tạo bóng phụ
        Ball extra = new Ball(
                x + width / 2f - (BALL_SIZE * scaleY) / 2f,
                y,
                (int)(BALL_SIZE * scaleY),
                (int)(BALL_SIZE * scaleY)
        );
        extra.launch(0, 6f); // Phóng thẳng xuống

        // GHI NHẬN THỜI GIAN BẮT ĐẦU
        LocalDateTime startTime = LocalDateTime.now();
        String startStr = startTime.format(TIME_FORMAT);
        System.out.println("[DoubleBall] KHỞI ĐỘNG lúc: " + startStr);

        // Thêm bóng vào game
        gameManager.addExtraBall(extra);

        // HỦY TIMER CŨ NẾU CÓ (tránh chồng chéo)
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // TẠO TIMER MỚI
        timer = new Timer((int) durationMs, e -> {
            LocalDateTime endTime = LocalDateTime.now();
            String endStr = endTime.format(TIME_FORMAT);
            long actualDurationMs = java.time.Duration.between(startTime, endTime).toMillis();

            System.out.println("[DoubleBall] KẾT THÚC lúc: " + endStr);
            System.out.println("[DoubleBall] HOẠT ĐỘNG: " + actualDurationMs + " ms");

            gameManager.removeExtraBall(extra);
        });

        timer.setRepeats(false); // Chỉ chạy 1 lần
        timer.start();           // Bắt đầu đếm ngược
    }

    @Override
    public void render(Graphics2D g2) {
        g2.setColor(new Color(220, 120, 40)); // Cam
        g2.fillOval(Math.round(x), Math.round(y), width, height);
        g2.setColor(Color.BLACK);
        g2.drawOval(Math.round(x), Math.round(y), width, height);

        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = "D";
        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw) / 2, Math.round(y) + height / 2 + 4);
    }
}