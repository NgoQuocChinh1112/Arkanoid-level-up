package Game;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;


public class SoundEffect {
    public Clip clip;
    private static final Map<String, SoundEffect> sounds = new HashMap<>();
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

    /**
     * Load tất cả âm thanh một lần khi khởi động.
     */
    public static void loadAllSounds() {
        addSound("collision", "sound/collision.wav");
        addSound("bgm", "sound/bgsound.wav");
        addSound("bigball",  "sound/BigBall.wav");
        addSound("expandpad",  "sound/ExpandPaddle.wav");
        addSound("explosiveball", "sound/ExplosiveBall.wav");
        addSound("explosive",  "sound/ExplosiveBall.wav");
        addSound("fastball",  "sound/FastBall.wav");
        addSound("shrinkpad",  "sound/ShrinkPaddle.wav");
        addSound("extralife",  "sound/ExtraLife.wav");
        addSound("soundMenu", "sound/sound_menu.wav");
        addSound("BallBrickCol", "sound/BallBrickCollision.wav");
        addSound("BallPaddleCol", "sound/BallPaddleCollision.wav");
        addSound("BallWallCol", "sound/BallWallCollision.wav");
        addSound("click", "sound/click.wav");
        addSound("lose", "sound/lose.wav");
        addSound("score", "sound/score.wav");
        addSound("win", "sound/win.wav");
    }

    /**
     * thêm âm thanh vào Map.
     * @param name khóa
     * @param filePath đường dẫn
     */
    private static void addSound(String name, String filePath) {
        SoundEffect s = new SoundEffect();
        s.loadSound(filePath);
        sounds.put(name, s);
    }

    /**
     * phát âm thanh một lần.
     * @param name dường dẫn
     */
    public static void play(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null) {
            s.play(); // ta sẽ thêm hàm play() trong SoundEffect bên dưới
        } else {
            System.err.println("Không tìm thấy âm thanh: " + name);
        }
    }

    /**
     * lặp nhạc nền.
     * @param name khóa truy cập Map.
     */
    public static void loop(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null && s.clip != null) {
            s.clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * dừng nhạc nền.
     * @param name khóa truy cập map.
     */
    public static void stop(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null && s.clip.isRunning()) {
            s.clip.stop();
        }
    }

    /**
     * phát âm thanh từ đầu.
     */
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * chỉnh âm lượng toàn cục.
     * @param volume giá trị đơn vị dB (-80dB to 6dB)
     */
    public static void setVolume(float volume) {
        for (SoundEffect s : sounds.values()) {
            s.volumeControl.setValue(volume);
        }
    }

    /**
     * phát tiếp nhạc đang dùng
     * @param name khóa truy cập map.
     */
    public static void resume(String name) {
        SoundEffect s = sounds.get(name);
        if (s != null && s.clip != null) {
            s.clip.start();
        }
    }
}
