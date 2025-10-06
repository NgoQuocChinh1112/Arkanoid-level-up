
public class Brick extends GameObject {
    protected int hitPoints;
    protected String type;

    public Brick(int hitPoints, String type, float x, float y, int width, int height) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    @Override
    public void update() {
        // Bricks are static by default
    }

    @Override
    public void render(Renderer renderer) {
        renderer.draw(this);
    }

    public int getHitPoints() { return hitPoints; }

    public void takeHit() {
        // TODO: decrease hitPoints and possibly spawn power-up
    }

    public boolean isDestroyed() {
        return hitPoints <= 0;
    }
}
