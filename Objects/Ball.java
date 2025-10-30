package Objects;

import Game.GameManager;
import Game.Renderer;
import java.awt.*;

public class Ball extends MovableObject {
    public float speed = 8f;
    public static boolean launched = false;
    private float radius;
    private final Rectangle boundsCache;

    public void setSpeed(float s) {
        if (s > 0) {
            this.speed = s;
            // Cập nhật lại velocity với speed mới nếu đã launch
            if (launched && (Math.abs(dx) > GameManager.EPSILON || Math.abs(dy) > GameManager.EPSILON)) {
                float magnitude = (float) Math.hypot(dx, dy);
                dx = (dx / magnitude) * speed;
                dy = (dy / magnitude) * speed;
            }
        }
    }

    public float getSpeed() {
        return speed;
    }

    public Ball(float x, float y, int width, int height) {
        super(x, y, width, height);
        dx = 0;
        dy = 0;
        radius = width / 2f;
        boundsCache = new Rectangle();
        texture = Renderer.loadBallTexture();
    }

    /**
     * Trả về bán kính.
     * @return bán kính.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Thiết lập bán kính.
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setWidth(int width){
        this.width = width;
        this.radius = width / 2f;
    }

    public void setHeight(int height){
        this.height = height;
        this.radius = height / 2f;
    }

    @Override
    public void render(Graphics2D g2) {
        g2.drawImage(texture, Math.round(x), Math.round(y), width, height, null);
    }

    public void launch(float dx, float dy) {
        float magnitude = (float) Math.hypot(dx, dy);
        if (magnitude > GameManager.EPSILON) {
            this.dx = (dx / magnitude) * speed;
            this.dy = (dy / magnitude) * speed;
            launched = true;
        }
    }

    public boolean isLaunched() {
        return launched;
    }

    public void resetToPaddle(Paddle paddle) {
        launched = false;
        dx = 0; dy = 0;
        setX(paddle.getX() + paddle.getWidth() / 2f - getWidth() / 2f);
        setY(paddle.getY() - getHeight() - 1);
    }

    public Rectangle getBounds() {
        // Sử dụng cache để tránh tạo object mới liên tục
        boundsCache.setBounds(Math.round(x), Math.round(y), width, height);
        return boundsCache;
    }

    public float getCenterX() {
        return x + radius;
    }

    public float getCenterY() {
        return y + radius;
    }

    private boolean fast = false;

    /**
     * Trả về trạng thái nhanh hay chậm của bóng.
     * @return
     */
    public boolean isFast() {
        return fast;
    }

    /**
     * Thiết lập trạng thái tốc độ của bóng.
     * @param isFast
     */
    public void setFast(boolean isFast) {
        this.fast = isFast;
    }

    private boolean enlarged = false;

    /**
     * Trả về trạng thái kích thước bóng.
     * @return
     */
    public boolean isEnlarged() {
        return enlarged;
    }

    /**
     * Thiết lập trạng thái kích cỡ bóng
     * @param enlarged
     */
    public void setEnlarged(boolean enlarged) {
        this.enlarged = enlarged;
    }

    private boolean explosive = false;

    /**
     * Trả về bóng có đang nổ không.
     * @return
     */
    public boolean isExplosive() {
        return explosive;
    }

    /**
     * Thiết lập trạng thái nổ của bóng.
     * @param explosive
     */
    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }

}