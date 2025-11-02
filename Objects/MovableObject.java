package Objects;

public abstract class MovableObject extends GameObject {
    protected float dx = 0;
    protected float dy = 0;

    public MovableObject(float x, float y, int width, int height) {
        super(x,y,width,height);
    }

    public void move() {
        this.x += dx;
        this.y += dy;
    }

    @Override
    public void update() {
        move();
    }

    // getters/setters
    public float getDx() { return dx; }
    public float getDy() { return dy; }
    public void setDx(float dx) { this.dx = dx; }
    public void setDy(float dy) { this.dy = dy; }
}
