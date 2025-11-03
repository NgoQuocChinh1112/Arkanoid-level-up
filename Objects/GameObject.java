package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Lớp trừu tượng đại diện cho một đối tượng trong trò chơi.
 */
public abstract class GameObject {

    /** Tọa độ X của đối tượng trên màn hình. */
    protected float x;

    /** Tọa độ Y của đối tượng trên màn hình. */
    protected float y;

    /** Chiều rộng của đối tượng. */
    protected int width;

    /** Chiều cao của đối tượng. */
    protected int height;

    /** Hình ảnh đại diện (texture) cho đối tượng. */
    protected BufferedImage texture;

    /**
     * Khởi tạo một đối tượng game với vị trí và kích thước cụ thể.
     *
     * @param x      Tọa độ X ban đầu
     * @param y      Tọa độ Y ban đầu
     * @param width  Chiều rộng đối tượng
     * @param height Chiều cao đối tượng
     */
    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Cập nhật trạng thái logic của đối tượng.
     * Phương thức này cần được cài đặt trong các lớp con (ví dụ: di chuyển, xử lý va chạm,...).
     */
    public abstract void update();

    /**
     * Vẽ đối tượng lên màn hình bằng đối tượng {@link Graphics2D}.
     * Các lớp con cần cài đặt chi tiết hiển thị của đối tượng.
     *
     * @param g2 Đối tượng {@link Graphics2D} dùng để vẽ
     */
    public abstract void render(Graphics2D g2);

    /**
     * Trả về vùng bao (hitbox) của đối tượng dưới dạng hình chữ nhật.
     *
     * @return Hình chữ nhật đại diện cho vùng chiếm chỗ của đối tượng.
     */
    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), width, height);
    }

    /**
     * Kiểm tra xem đối tượng này có va chạm với một đối tượng khác hay không.
     *
     * @param other Đối tượng khác cần kiểm tra va chạm
     * @return {@code true} nếu hai đối tượng giao nhau, ngược lại {@code false}.
     */
    public boolean intersects(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }

    /**
     * Lấy tọa độ X hiện tại của đối tượng.
     * @return Giá trị X
     */
    public float getX() { return x; }

    /**
     * Lấy tọa độ Y hiện tại của đối tượng.
     * @return Giá trị Y
     */
    public float getY() { return y; }

    /**
     * Lấy chiều rộng của đối tượng.
     * @return Chiều rộng
     */
    public int getWidth() { return width; }

    /**
     * Lấy chiều cao của đối tượng.
     * @return Chiều cao
     */
    public int getHeight() { return height; }

    /**
     * Thiết lập tọa độ X mới cho đối tượng.
     * @param x Giá trị X mới
     */
    public void setX(float x) { this.x = x; }

    /**
     * Thiết lập tọa độ Y mới cho đối tượng.
     * @param y Giá trị Y mới
     */
    public void setY(float y) { this.y = y; }

    /**
     * Thiết lập chiều rộng mới cho đối tượng.
     * @param width Chiều rộng mới
     */
    public void setwidth(int width) { this.width = width; }

    /**
     * Thiết lập chiều cao mới cho đối tượng.
     * @param height Chiều cao mới
     */
    public void setHeight(int height) { this.height = height; }
}
