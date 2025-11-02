package Game;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;


public class SoundEffect {
    public Clip clip;
    private static Map<String, SoundEffect> sounds = new HashMap<>();
    private FloatControl volumeControl;

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
            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println(" Lỗi load âm thanh: " + e.getMessage());
        }
    }

    // Load tất cả âm thanh một lần khi khởi động game
    public static void loadAllSounds() {
        addSound("break", "src/sound/breaker.wav");
        addSound("collision", "src/sound/collision.wav");
        addSound("bgm", "src/sound/bgsound.wav");
        addSound("bigball",  "src/sound/BigBall.wav");
        addSound("expandpad",  "src/sound/ExpandPaddle.wav");
        addSound("explosiveball", "src/sound/ExplosiveBall.wav");
        addSound("explosive",  "src/sound/ExplosiveBall.wav");
        addSound("fastball",  "src/sound/FastBall.wav");
        addSound("shrinkpad",  "src/sound/ShrinkPaddle.wav");
        addSound("extralife",  "src/sound/ExtraLife.wav");
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

    //chỉnh âm lượng toàn bộ
    public static void setVolume(float volume) {
        for (SoundEffect s : sounds.values()) {
            s.volumeControl.setValue(volume);
        }
    }
}
