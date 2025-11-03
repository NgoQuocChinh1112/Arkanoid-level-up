package Objects;

import Game.GameManager;
import Game.Renderer;
import java.awt.*;

/**
 * Lớp Ball đại diện cho quả bóng trong trò chơi.
 */
public class Ball extends MovableObject {
    /** Tốc độ di chuyển của bóng. */
    public float speed = 8f;

    /** Trạng thái cho biết bóng đã được phóng ra hay chưa. */
    public boolean launched = false;

    /** Bán kính của bóng. */
    private float radius;

    /** Dùng để lưu vùng va chạm, giúp giảm cấp phát bộ nhớ lặp lại. */
    private final Rectangle boundsCache;

    /** Trạng thái cho biết bóng đang ở chế độ tốc độ cao. */
    private boolean fast = false;

    /** Trạng thái cho biết bóng có kích thước lớn hơn bình thường hay không. */
    private boolean enlarged = false;

    /** Trạng thái cho biết bóng có khả năng nổ hay không. */
    private boolean explosive = false;

    /**
     * Khởi tạo đối tượng Ball.
     *
     * @param x      Tọa độ X ban đầu
     * @param y      Tọa độ Y ban đầu
     * @param width  Chiều rộng của bóng
     * @param height Chiều cao của bóng
     */
    public Ball(float x, float y, int width, int height) {
        super(x, y, width, height);
        dx = 0;
        dy = 0;
        radius = width / 2f;
        boundsCache = new Rectangle();
        texture = Renderer.loadBallTexture();
    }

    /**
     * Thiết lập tốc độ di chuyển của bóng.
     * Nếu bóng đã được phóng, vector vận tốc sẽ được cập nhật theo tốc độ mới.
     *
     * @param s Tốc độ mới (phải lớn hơn 0)
     */
    public void setSpeed(float s) {
        if (s > 0) {
            this.speed = s;
            // Cập nhật lại velocity với speed mới nếu đã launch
            if (launched && (Math.abs(dx) > GameManager.EPSILON || Math.abs(dy) > GameManager.EPSILON)) {
                float magnitude = (float) Math.hypot(dx, dy);
                dx = (dx / magnitude) * speed;
                dy = (dy / magnitude) * speed;
            }
        }
    }

    /**
     * Lấy tốc độ hiện tại của bóng.
     *
     * @return tốc độ của bóng
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Trả về bán kính hiện tại của bóng.
     *
     * @return bán kính
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Thiết lập bán kính của bóng.
     *
     * @param radius giá trị bán kính mới
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * Thiết lập chiều rộng của bóng và cập nhật lại bán kính.
     *
     * @param width chiều rộng mới
     */
    public void setWidth(int width) {
        this.width = width;
        this.radius = width / 2f;
    }

    /**
     * Thiết lập chiều cao của bóng và cập nhật lại bán kính.
     *
     * @param height chiều cao mới
     */
    public void setHeight(int height) {
        this.height = height;
        this.radius = height / 2f;
    }

    /**
     * Vẽ bóng lên màn hình.
     *
     * @param g2 Đối tượng {@link Graphics2D} dùng để render
     */
    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
    }

    /**
     * Phóng bóng theo một hướng xác định.
     *
     * @param dx Vận tốc theo trục X
     * @param dy Vận tốc theo trục Y
     */
    public void launch(float dx, float dy) {
        float magnitude = (float) Math.hypot(dx, dy);
        if (magnitude > GameManager.EPSILON) {
            this.dx = (dx / magnitude) * speed;
            this.dy = (dy / magnitude) * speed;
            launched = true;
        }
    }

    /**
     * Kiểm tra xem bóng đã được phóng ra chưa.
     *
     * @return {@code true} nếu bóng đã được phóng, {@code false} nếu chưa
     */
    public boolean isLaunched() {
        return launched;
    }

    /**
     * Thiết lập trạng thái phóng của bóng.
     *
     * @param launched {@code true} nếu bóng được phóng ra, {@code false} nếu ở trạng thái chờ
     */
    public void setLaunched(boolean launched) {
        this.launched = launched;
    }

    /**
     * Đặt lại vị trí bóng lên trên thanh trượt (Paddle),
     * thường được gọi khi bắt đầu hoặc sau khi mất mạng.
     *
     * @param paddle Đối tượng {@link Paddle} để căn chỉnh vị trí bóng
     */
    public void resetToPaddle(Paddle paddle) {
        launched = false;
        dx = 0;
        dy = 0;
        setX(paddle.getX() + paddle.getWidth() / 2f - getWidth() / 2f);
        setY(paddle.getY() - getHeight() - 1);
    }

    /**
     * Lấy vùng va chạm (hitbox) hiện tại của bóng.
     *
     * @return một {@link Rectangle} biểu diễn vùng va chạm
     */
    public Rectangle getBounds() {
        // Sử dụng cache để tránh tạo object mới liên tục
        boundsCache.setBounds(Math.round(x), Math.round(y), width, height);
        return boundsCache;
    }

    /**
     * Lấy tọa độ X của tâm bóng.
     *
     * @return giá trị X của tâm bóng
     */
    public float getCenterX() {
        return x + radius;
    }

    /**
     * Lấy tọa độ Y của tâm bóng.
     *
     * @return giá trị Y của tâm bóng
     */
    public float getCenterY() {
        return y + radius;
    }

    /**
     * Kiểm tra xem bóng có đang ở trạng thái "nhanh" không.
     * @return {@code true} nếu bóng nhanh, {@code false} nếu bình thường
     */
    public boolean isFast() {
        return fast;
    }

    /**
     * Thiết lập trạng thái nhanh hay chậm của bóng.
     * @param isFast {@code true} để kích hoạt chế độ nhanh, {@code false} để tắt
     */
    public void setFast(boolean isFast) {
        this.fast = isFast;
    }

    /**
     * Kiểm tra xem bóng có đang ở trạng thái kích thước lớn hơn bình thường không.
     * @return {@code true} nếu bóng lớn, {@code false} nếu bình thường
     */
    public boolean isEnlarged() {
        return enlarged;
    }

    /**
     * Thiết lập trạng thái kích thước của bóng.
     * @param enlarged {@code true} để phóng to bóng, {@code false} để trở lại bình thường
     */
    public void setEnlarged(boolean enlarged) {
        this.enlarged = enlarged;
    }

    /**
     * Kiểm tra xem bóng có khả năng nổ không.
     * @return {@code true} nếu bóng nổ, {@code false} nếu không
     */
    public boolean isExplosive() {
        return explosive;
    }

    /**
     * Thiết lập trạng thái nổ của bóng.
     * @param explosive {@code true} để bật chế độ nổ, {@code false} để tắt
     */
    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }
}
