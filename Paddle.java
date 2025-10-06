
public class Paddle extends MovableObject {
    private float speed;
    private PowerUp currentPowerUp;

    public Paddle(float x, float y, int width, int height, float speed) {
        super(x, y, width, height, 0f, 0f);
        this.speed = speed;
    }

    @Override
    public void update() {
        // TODO: implement paddle update
    }

    @Override
    public void render(Renderer renderer) {
        renderer.draw(this);
    }

    public void moveLeft() {
        x -= speed;
    }

    public void moveRight() {
        x += speed;
    }

    public void applyPowerUp(PowerUp p) {
        // TODO
    }
}
