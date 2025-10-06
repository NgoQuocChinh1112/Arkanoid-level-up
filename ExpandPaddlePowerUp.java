
public class ExpandPaddlePowerUp extends PowerUp {
    public ExpandPaddlePowerUp(float duration) {
        super(duration, "EXPAND_PADDLE");
    }

    @Override
    public void applyEffect(Paddle paddle) {
        // TODO: increase paddle width
    }

    @Override
    public void removeEffect(Paddle paddle) {
        // TODO: restore paddle width
    }
}
