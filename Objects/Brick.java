package Objects;

import java.awt.*;

public class Brick extends GameObject {
    protected int hitPoints;
    protected String type;
    protected boolean destroyed = false;

    public Brick(float x, float y, int width, int height, int hitPoints, String type) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.type = type;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D g2) {
        Color fill = type.equals("STRONG") ? new Color(180, 50, 50) : new Color(50, 150, 220);
        g2.setColor(fill);
        g2.fillRoundRect(Math.round(x), Math.round(y), width, height, 6, 6);
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(Math.round(x), Math.round(y), width, height, 6, 6);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        String s = String.valueOf(hitPoints);
        int tw = g2.getFontMetrics().stringWidth(s);
        g2.drawString(s, Math.round(x) + (width - tw)/2, Math.round(y) + height/2 + 5);
    }

    public void takeHit() {
        hitPoints--;
        if (hitPoints <= 0) destroyed = true;
    }

    public boolean isDestroyed() { return destroyed; }

    public int getHitPoints() { return hitPoints; }

    public String getType() { return type; }
}
