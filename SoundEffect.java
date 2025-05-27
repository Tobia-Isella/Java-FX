import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class SoundEffect {
    private static double volume = 0.5; // Default volume for sound effects (0.0 to 1.0)

    public static void playSound(String filepath) {
        Media sound = new Media(new File(filepath).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(volume);

        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose); // Free resources after playback

        mediaPlayer.play();
    }

    public static void setVolume(double newVolume) {
        volume = Math.max(0.0, Math.min(1.0, newVolume)); // Clamp between 0.0 and 1.0
    }

    public static double getVolume() {
        return volume;
    }
}
