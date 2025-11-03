package Objects;

import java.awt.*;
import Game.Renderer;
import Game.SoundEffect;
import java.util.List;
import java.awt.image.BufferedImage;

/**
 * Lớp Brick đại diện cho một viên gạch trong trò chơi.
 */
public class Brick extends MovableObject {

    /** Số lượng máu tối đa của viên gạch (tùy theo loại). */
    private final int hitPoints;

    /** Số lượng máu hiện tại của viên gạch. */
    private int heart;

    /** Tốc độ di chuyển của viên gạch. */
    private float speed = 2f;

    /** Loại gạch (xác định hướng hoặc kiểu di chuyển). */
    private final int type;

    /** Khoảng cách di chuyển tối đa (số ô gạch). */
    private final int dis;

    /** Giới hạn tối đa theo trục X. */
    private float maxX;

    /** Giới hạn tối thiểu theo trục X. */
    private float minX;

    /** Giới hạn tối đa theo trục Y. */
    private float maxY;

    /** Giới hạn tối thiểu theo trục Y. */
    private float minY;

    /** Danh sách các texture của gạch, được tải sẵn từ {@link Renderer}. */
    private final List<List<BufferedImage>> textures = Renderer.loadBrickTexture();

    /**
     * Khởi tạo một viên gạch với thông tin được mã hóa trong tham số {@code temp}.
     *
     * @param x      Tọa độ X ban đầu
     * @param y      Tọa độ Y ban đầu
     * @param width  Chiều rộng của viên gạch
     * @param height Chiều cao của viên gạch
     * @param temp   Mã số quy định loại, máu và kiểu di chuyển của gạch
     */
    public Brick(float x, float y, int width, int height, int temp) {
        super(x, y, width, height);
        this.hitPoints = temp / 100;
        this.heart = temp / 100;
        this.type = (temp / 10) % 10;
        this.dis = temp % 10;
        setVector();
        if (this.hitPoints == 6) {
            texture = textures.get(this.hitPoints - 1).getFirst();
        } else {
            texture = textures.get(this.hitPoints - 1).get(this.heart - 1);
        }
    }

    /**
     * Vẽ viên gạch lên màn hình nếu nó chưa bị phá hủy.
     *
     * @param g2 Đối tượng {@link Graphics2D} dùng để vẽ
     */
    @Override
    public void render(Graphics2D g2) {
        if (!isDestroyed()) {
            g2.drawImage(texture, (int) x, (int) y, width, height, null);
        }
    }

    /**
     * Xử lý khi viên gạch bị bóng va chạm.
     */
    public void takeHit() {
        if (hitPoints > 0) {
            SoundEffect.play("BallBrickCol");
            if (hitPoints != 6 && heart > 0) {
                heart--;
            }
        }
        if (hitPoints > 0) {
            if (heart <= 0) {
                texture = textures.get(hitPoints - 1).getFirst();
            } else {
                if (hitPoints == 6) {
                    texture = textures.get(hitPoints - 1).getFirst();
                } else {
                    texture = textures.get(hitPoints - 1).get(heart - 1);
                }
            }
        } else {
            texture = null;
        }
    }

    /**
     * Kiểm tra xem viên gạch đã bị phá hủy chưa.
     *
     * @return {@code true} nếu máu (heart) ≤ 0, {@code false} nếu vẫn còn
     */
    public boolean isDestroyed() {
        return heart <= 0;
    }

    /**
     * Lấy tổng số máu ban đầu của viên gạch.
     *
     * @return số máu tối đa
     */
    public int getHitPoints() {
        return hitPoints;
    }

    /**
     * Lấy tốc độ di chuyển hiện tại của viên gạch.
     *
     * @return tốc độ di chuyển
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Thiết lập tốc độ di chuyển của viên gạch.
     *
     * @param speed tốc độ mới
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Lấy giới hạn nhỏ nhất theo trục X mà viên gạch có thể di chuyển đến.
     *
     * @return giá trị minX
     */
    public float getMinX() {
        return this.minX;
    }

    /**
     * Lấy giới hạn lớn nhất theo trục X mà viên gạch có thể di chuyển đến.
     *
     * @return giá trị maxX
     */
    public float getMaxX() {
        return this.maxX;
    }

    /**
     * Lấy giới hạn nhỏ nhất theo trục Y mà viên gạch có thể di chuyển đến.
     *
     * @return giá trị minY
     */
    public float getMinY() {
        return this.minY;
    }

    /**
     * Lấy giới hạn lớn nhất theo trục Y mà viên gạch có thể di chuyển đến.
     *
     * @return giá trị maxY
     */
    public float getMaxY() {
        return this.maxY;
    }

    /**
     * Lấy loại (type) của viên gạch.
     *
     * @return mã loại gạch
     */
    public int getType() {
        return type;
    }

    /**
     * Thay đổi hướng di chuyển của viên gạch nếu chạm đến giới hạn di chuyển.
     */
    public void changeVector() {
        if (x > maxX && maxX != 0) {
            x = maxX - speed;
            dx = -dx;
        } else if (x < minX && minX != 0) {
            x = minX + speed;
            dx = -dx;
        }
        if (y > maxY && maxY != 0) {
            y = maxY - speed;
            dy = -dy;
        } else if (y < minY && minY != 0) {
            y = minY + speed;
            dy = -dy;
        }
    }

    /**
     * Thiết lập hướng và giới hạn di chuyển ban đầu cho viên gạch dựa trên type.
     */
    public void setVector() {
        switch (type) {
            case 0:
                dx = 0;
                dy = 0;
                break;
            case 1:
                this.minX = x;
                this.maxX = x + dis * 64;
                dx = speed;
                dy = 0;
                break;
            case 2:
                this.minX = x - dis * 64;
                this.maxX = x;
                dx = -speed;
                dy = 0;
                break;
            case 3:
                this.minY = y;
                this.maxY = y + dis * 24;
                dx = 0;
                dy = speed;
                break;
            case 4:
                this.minY = y - dis * 24;
                this.maxY = y;
                dx = 0;
                dy = -speed;
                break;
            default:
                System.out.println("Sai rồi má!");
                break;
        }
    }
}
