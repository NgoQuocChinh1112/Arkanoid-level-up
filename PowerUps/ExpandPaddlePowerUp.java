package PowerUps;

import Objects.Paddle;

public class ExpandPaddlePowerUp extends PowerUp {
    private int OriginalWidth; // kích thuoc ban đầu
    public ExpandPaddlePowerUp(float duration) {
        super(duration, "EXPAND_PADDLE");
    }

    @Override
    public void applyEffect(Paddle paddle) {
        OriginalWidth = paddle.width; // lưu kích thước gốc
        paddle.width = OriginalWidth * 2;
    }

    @Override
    public void removeEffect(Paddle paddle) {
        paddle.width = OriginalWidth;
    }
}
