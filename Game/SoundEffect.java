package Game;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;


public class SoundEffect {
    public Clip clip;
    private static Map<String, SoundEffect> sounds = new HashMap<>();


    /**
     * Load âm thanh.
     * @param fileName SoundFile.
     */
    public void loadSound(String fileName) {
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

    // Load tất cả âm thanh một lần khi khởi động game
    public static void loadAllSounds() {
        addSound("break", "sound/breaker.wav");
        addSound("collision", "sound/collision.wav");
        addSound("bgm", "sound/bgsound.wav");
        addSound("bigball",  "sound/BigBall.wav");
        addSound("expandpad",  "sound/ExpandPaddle.wav");
        addSound("explosiveball", "sound/ExplosiveBall.wav");
        addSound("explosive",  "sound/ExplosiveBall.wav");
        addSound("fastball",  "sound/FastBall.wav");
    }

    private static void addSound(String name, String filePath) {
        SoundEffect s = new SoundEffect();
        s.loadSound(filePath);
        sounds.put(name, s);
    }

    // Phát âm thanh hiệu ứng (một lần)
    public static void play(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null) {
            s.play(); // ta sẽ thêm hàm play() trong SoundEffect bên dưới
        } else {
            System.err.println("Không tìm thấy âm thanh: " + name);
        }
    }

    // Lặp nhạc nền
    public static void loop(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null && s.clip != null) {
            s.clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Dừng nhạc nền
    public static void stop(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null && s.clip.isRunning()) {
            s.clip.stop();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // quay lại đầu file
            clip.start();
        }
    }

}
