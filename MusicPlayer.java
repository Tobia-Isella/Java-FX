import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    public MusicPlayer(String filepath) {
        Media media = new Media(new File(filepath).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
        mediaPlayer.setVolume(0.5); // Default volume
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume); // Volume range: 0.0 to 1.0
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }
}
