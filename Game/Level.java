package Game;

import Objects.Brick;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Level chịu trách nhiệm tải và xây dựng bố cục (layout)
 */
public class Level {

    /**
     * Tạo danh sách gạch (Brick) cho chế độ chơi đơn thông thường.
     *
     * @param level  Số thứ tự level cần tải (ví dụ: 1, 2, 3)
     * @param WIDTH  Chiều rộng màn hình hiển thị
     * @param HEIGHT Chiều cao màn hình hiển thị
     * @param scale  Tỷ lệ thu phóng (scale) dùng để thay đổi kích thước gạch theo độ phân giải
     * @return Danh sách các đối tượng {@link Brick} đại diện cho bố cục level
     */
    public static List<Brick> buildLevel(int level, int WIDTH, int HEIGHT, float scale) {
        List<Brick> bricks = new ArrayList<>();

        String fileName = "/levels/level" + level + ".txt";
        List<String> lines = new ArrayList<>();

        try (InputStream is = Level.class.getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return bricks;
        }

        //--- Kích thước gạch và khoảng cách ---
        int brickW = (int) (64 * scale);
        int brickH = (int) (24 * scale);
        int offsetY = (int) (60 * scale);

        int row = lines.size();
        int cols = lines.getFirst().split("\\s+").length;
        int offsetX = (WIDTH - (cols * brickW)) / 2;

        //--- Tạo gạch bằng cách chuyển đối từ số sang loại gạch ---
        for (int r = 0; r < row; r++) {
            String[] nums = lines.get(r).split("\\s+");
            for (int c = 0; c < cols; c++) {
                int type = Integer.parseInt(nums[c]);
                if (type != 0) {
                    int x = offsetX + c * brickW;
                    int y = offsetY + r * brickH;
                    Brick brick = new Brick(x, y, brickW, brickH, type);
                    bricks.add(brick);
                }
            }
        }
        return bricks;
    }

    /**
     * Tạo danh sách gạch (Brick) cho một người chơi trong chế độ thi đấu đối kháng (competitive mode),
     * bằng cách đọc bố cục từ file tương ứng.
     *
     * @param level      Số thứ tự level (ví dụ: 1, 2, 3)
     * @param offsetX    Vị trí bắt đầu theo trục X trong toàn màn hình (0 cho Player 1, WIDTH/2 cho Player 2)
     * @param areaWidth  Chiều rộng khu vực hiển thị của người chơi (thường là WIDTH/2)
     * @param areaHeight Chiều cao khu vực hiển thị
     * @param scale      Tỷ lệ thu phóng hiển thị
     * @return Danh sách các đối tượng {@link Brick} tương ứng với khu vực chơi của người chơi
     */
    public static List<Brick> buildLevelForPlayer(int level, int offsetX, int areaWidth,
                                                  int areaHeight, float scale) {
        List<Brick> bricks = new ArrayList<>();
        String fileName = "/levels/level" + level + ".txt";
        List<String> lines = new ArrayList<>();

        try (InputStream is = Level.class.getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return bricks;
        }

        if (lines.isEmpty()) return bricks;

        // ---- Cấu hình ma trận ----
        int rows = 10;   // số hàng hiển thị
        int cols = 11;   // số cột hiển thị
        float gapX = 2f * scale;
        float gapY = 2f * scale;

        // ---- Tính kích thước gạch động ----
        float totalGapWidth = (cols - 1) * gapX;
        float totalGapHeight = (rows - 1) * gapY;

        float brickW = (areaWidth - totalGapWidth) / cols;
        float brickH = (areaHeight * 0.25f - totalGapHeight) / rows;  // chiếm khoảng 1/4 chiều cao màn hình
        float offsetY = 60f * scale;

        // ---- Căn giữa trong khu vực người chơi ----
        float totalBricksWidth = cols * brickW + totalGapWidth;
        float playerOffsetX = offsetX + (areaWidth - totalBricksWidth) / 2f;

        // ---- Tạo gạch ----
        for (int r = 0; r < Math.min(rows, lines.size()); r++) {
            String[] nums = lines.get(r).split("\\s+");
            for (int c = 0; c < Math.min(cols, nums.length); c++) {
                int type = Integer.parseInt(nums[c]);
                if (type != 0) {
                    float x = playerOffsetX + c * (brickW + gapX);
                    float y = offsetY + r * (brickH + gapY);
                    bricks.add(new Brick(Math.round(x), Math.round(y),
                            Math.round(brickW), Math.round(brickH), type));
                }
            }
        }
        return bricks;
    }
}
