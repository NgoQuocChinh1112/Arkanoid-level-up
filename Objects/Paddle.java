package Objects;

public class Paddle extends MovableObject {
    private float speed;
    private PowerUp currentPowerUp;

    public Paddle(float x, float y, int width, int height, float speed) {
        super(x, y, width, height, 0f, 0f);
        this.speed = speed;
    }

    @Override
    public void update() {
        // TODO
    }

    @Override
    public void render(Renderer renderer) {
        renderer.draw(this);
    }

    public void moveLeft() {
        // TODO
    }

    public void moveRight() {
        // TODO
    }

    public void applyPowerUp(PowerUp p) {
        // TODO
    }
}
