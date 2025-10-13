package Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected int width;
    protected int height;
    protected BufferedImage texture;

    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();
    public abstract void render(Graphics2D g2);

    /**
     * Trả về vùng bao của đối tượng dưới dạng hình chữ nhật.
     * @return Hình chữ nhật đại diện cho vùng chiếm chỗ của đối tượng
     */
    public Rectangle getBounds() {
        return new Rectangle(Math.round(x), Math.round(y), width, height);
    }

    /**
     * Kiểm tra xem đối tượng này có va chạm với đối tượng khác hay không.
     * @param other Đối tượng cần kiểm tra va chạm
     * @return true nếu hai đối tượng giao nhau, ngược lại false
     */
    public boolean intersects(GameObject other) { return this.getBounds().intersects(other.getBounds());
    }

    // getters / setters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public void setwidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
}
