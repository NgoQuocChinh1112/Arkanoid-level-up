package Game;

import Objects.Brick;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Level {
    public static List<Brick> buildLevel(int level, int WIDTH, int HEIGHT, float scale) {
        List<Brick> bricks = new ArrayList<>();

        String fileName = "/levels/level" + level + ".txt";
        List<String> lines = new ArrayList<>();

        try(InputStream is = Level.class.getResourceAsStream(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while((line = br.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return bricks;
        }

        int brickW = (int)(64 * scale);
        int brickH = (int)(24  * scale);
        int offsetY = (int)(60 * scale);

        int row = lines.size();
        int cols = lines.getFirst().split("\\s+").length;
        int offsetX = (WIDTH - (cols * brickW)) / 2;

        for(int r = 0; r < row; r++) {
            String[] nums = lines.get(r).split("\\s+");
            for(int c = 0; c < cols; c++) {
                int type = Integer.parseInt(nums[c]);
                if(type != 0) {
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
    * Tạo level cho 1 người chơi trong chế độ competitive (đọc từ file)
    * @param level Level hiện tại (1, 2, 3)
    * @param offsetX Vị trí bắt đầu theo trục X (0 cho P1, WIDTH/2 cho P2)
    * @param areaWidth Chiều rộng khu vực của player (WIDTH/2)
    * @param areaHeight Chiều cao màn hình
    * @param scale Scale theo X và Y
    * @return Danh sách các brick
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
        System.out.println("Total Gap Width: " + totalGapWidth + ", Total Gap Height: " + totalGapHeight);

        float brickW = (areaWidth - totalGapWidth) / cols;
        float brickH = (areaHeight * 0.25f - totalGapHeight) / rows;// chiếm khoảng 1/4 chiều cao màn hình
        System.out.println("brickW: " + brickW + " brickH: " + brickH);
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
