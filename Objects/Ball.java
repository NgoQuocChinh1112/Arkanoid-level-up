package Objects;

public class Ball extends MovableObject {
    private float speed;
    private float directionX;
    private float directionY;

    public Ball(float x, float y, int width, int height, float speed, float dirX, float dirY) {
        super(x, y, width, height, dirX * speed, dirY * speed);
        this.speed = speed;
        this.directionX = dirX;
        this.directionY = dirY;
    }

    @Override
    public void update() {
        // TODO
    }

    @Override
    public void render(Renderer renderer) {
        renderer.draw(this);
    }

    public void bounceOff(GameObject obj) {
        // TODO
    }

    public void checkCollision(GameManager gameManager) {
        // TODO
    }
}
