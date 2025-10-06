package Objects;

import PowerUps.PowerUp;

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
        x -= speed;
        if (x < 0) {
            x = 0;
        }
    }

    public void moveRight() {
        // TODO
        x += speed;
        if (x > width) {
            x = width;
        }
    }

    public void applyPowerUp(PowerUp p) {
        // TODO
    }
}
