package Objects;

public abstract class MovableObject extends GameObject {
    protected float dx;
    protected float dy;

    public MovableObject(float x, float y, int width, int height, float dx, float dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    public void move() {
        // TODO
    }
}
