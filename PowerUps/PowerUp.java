package PowerUps;

import Objects.Ball;
import Objects.GameObject;
import Objects.Paddle;

import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;

public abstract class PowerUp extends GameObject {
    protected long durationMs;
    protected String type;
    protected float dy = 2.0f;
    protected Clip clip;
    protected boolean collectedOrOffscreen = false;
    public PowerUp(float x, float y, int width, int height, long durationMs, String type) {
        super(x,y,width,height);
        this.durationMs = durationMs;
        this.type = type;
    }

    /**
     * Load âm thanh.
     * @param fileName SoundFile.
     */
    protected void loadSound(String fileName) {
        try {
            File soundFile = new File( fileName);
            if (!soundFile.exists()) {
                System.out.println("Không tìm thấy file: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            System.out.println(" Đã load " + fileName);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println(" Lỗi load âm thanh: " + e.getMessage());
        }
    }

    /**
     * Phát âm thanh.
     * @param fileName SoundFile
     */
    protected static void playSound(String fileName) {
        try {
            File soundFile = new File(fileName);
            if (!soundFile.exists()) {
                System.out.println(" Không tìm thấy file: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip tempClip = AudioSystem.getClip();
            tempClip.open(audioIn);
            tempClip.start();
            System.out.println(" Đã phát " + fileName);

            // Giải phóng tài nguyên sau khi phát xong
            tempClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    tempClip.close();
                }
            });

        } catch (Exception e) {
            System.err.println("Lỗi");
        }
    }

    @Override
    public void update() {
        y += dy;
    }


    public abstract void render(Graphics2D g2);

    public abstract void applyEffect(Paddle paddle, Ball ball, Object gameManager);

    public String getType() { return type; }
    public long getDurationMs() { return durationMs; }

    public boolean isCollectedOrOffscreen() { return collectedOrOffscreen; }
    public void markCollectedOrOffscreen() { collectedOrOffscreen = true; }
}
