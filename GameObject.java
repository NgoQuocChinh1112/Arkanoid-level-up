import java.awt.*;

public abstract class GameObject {
    protected float x;
    protected float y;
    protected int width;
    protected int height;

    public GameObject(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();
    public abstract void render(Graphics2D g2);

    // Lấy ranh giới của các obj.
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height );
    }

    // Kiểm tra va chạm giữa các obj.
    public boolean checkCollision(GameObject other) {
        return this.getBounds().intersects(other.getBounds());
    }

    // getters / setters
    public float getX() { return x; }
    public float getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
}
