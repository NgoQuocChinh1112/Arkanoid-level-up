public abstract class MovableObject extends GameObject {
    protected float dx;
    protected float dy;

    public MovableObject(float x, float y, int width, int height, float dx, float dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    // Hàm di chuyển move().
    public void move() {
        x += dx;
        y += dy;
    }

    // getter / setter.
    public float getDx() {return dx;}
    public float getDy() {return dy;}
    public void setDx(float dx) {this.dx = dx;}
    public void setDy(float dy) {this.dy = dy;}
}
