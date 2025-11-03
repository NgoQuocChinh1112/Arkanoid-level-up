package Objects;

/**
 * Lớp trừu tượng đại diện cho một đối tượng có thể di chuyển trong trò chơi.
 * Kế thừa từ GameObject, lớp này bổ sung thêm các thuộc tính về vận tốc
 */
public abstract class MovableObject extends GameObject {

    /** Vận tốc theo trục X (thay đổi vị trí theo chiều ngang mỗi khung hình). */
    protected float dx = 0;

    /** Vận tốc theo trục Y (thay đổi vị trí theo chiều dọc mỗi khung hình). */
    protected float dy = 0;

    /**
     * Khởi tạo một đối tượng có thể di chuyển với vị trí và kích thước cụ thể.
     *
     * @param x      Tọa độ X ban đầu
     * @param y      Tọa độ Y ban đầu
     * @param width  Chiều rộng đối tượng
     * @param height Chiều cao đối tượng
     */
    public MovableObject(float x, float y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Cập nhật vị trí của đối tượng dựa trên vận tốc hiện tại (dx, dy).
     * Gọi phương thức này sẽ dịch chuyển đối tượng theo hướng và tốc độ hiện tại.
     */
    public void move() {
        this.x += dx;
        this.y += dy;
    }

    /**
     * Cập nhật trạng thái của đối tượng trong mỗi khung hình.
     * Mặc định gọi {@link #move()} để đối tượng di chuyển.
     * Các lớp con có thể ghi đè để bổ sung logic khác (va chạm, hiệu ứng,...).
     */
    @Override
    public void update() {
        move();
    }

    /**
     * Lấy vận tốc theo trục X hiện tại.
     * @return Giá trị dx
     */
    public float getDx() { return dx; }

    /**
     * Lấy vận tốc theo trục Y hiện tại.
     * @return Giá trị dy
     */
    public float getDy() { return dy; }

    /**
     * Thiết lập vận tốc theo trục X.
     * @param dx Giá trị dx mới
     */
    public void setDx(float dx) { this.dx = dx; }

    /**
     * Thiết lập vận tốc theo trục Y.
     * @param dy Giá trị dy mới
     */
    public void setDy(float dy) { this.dy = dy; }
}
