package PowerUps;

public abstract class PowerUp {
    protected float duration;
    protected String type;

    public PowerUp(float duration, String type) {
        this.duration = duration;
        this.type = type;
    }

    public abstract void applyEffect(Paddle paddle);
    public abstract void removeEffect(Paddle paddle);

    public String getType() { return type; }
    public float getDuration() { return duration; }
}
