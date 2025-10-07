package Game;

import Objects.Brick;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Level {
    public static List<Brick> buildLevel(int level, int WIDTH, int HEIGHT, float scaleX, float scaleY) {
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

        int brickW = (int)(64f * scaleX);
        int brickH = (int)(24f * scaleY);
        int offsetY = (int)(60 * scaleY);

        int row = lines.size();
        int cols = lines.get(0).split("\\s+").length;
        int offsetX = (WIDTH - (cols * brickW)) / 2;

        for(int r = 0; r < row; r++) {
            String[] nums = lines.get(r).split("\\s+");
            for(int c = 0; c < cols; c++) {
                int type = Integer.parseInt(nums[c]);
                if(type != 0) {
                    int x = offsetX + c * brickW;
                    int y = offsetY + r * brickH;
                    bricks.add(new Brick(x, y, brickW, brickH, type, type));
                }
            }
        }
        return bricks;
    }
}
