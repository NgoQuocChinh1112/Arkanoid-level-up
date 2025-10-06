
public class FastBallPowerUp extends PowerUp {
    public FastBallPowerUp(float duration) {
        super(duration, "FAST_BALL");
    }

    @Override
    public void applyEffect(Paddle paddle) {
        // Usually affects the ball; reference GameManager to find ball(s) if needed
    }

    @Override
    public void removeEffect(Paddle paddle) {
        // TODO
    }
}
