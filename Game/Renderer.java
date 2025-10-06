package Game;

import Objects.GameObject;

import java.awt.*;

public class Renderer {
    public static void drawRect(Graphics2D g2, GameObject obj, Color fill, Color border) {
        g2.setColor(fill);
        g2.fillRect(Math.round(obj.getX()), Math.round(obj.getY()), obj.getWidth(), obj.getHeight());
        g2.setColor(border);
        g2.drawRect(Math.round(obj.getX()), Math.round(obj.getY()), obj.getWidth(), obj.getHeight());
    }
}
