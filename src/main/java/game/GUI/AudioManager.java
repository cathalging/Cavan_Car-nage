package game.GUI;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.AudioClip;

import java.net.URL;

public class AudioManager {
    private static MediaPlayer musicPlayer;

    public static void playBackground(String path) {
        stop();
        Media media = new Media(AudioManager.class.getResource(path).toExternalForm());
        musicPlayer = new MediaPlayer(media);
        musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        musicPlayer.play();
    }

    public static void playSfx(String path) {
        URL url = AudioManager.class.getResource(path);
        AudioClip clip = new AudioClip(url.toString());
        clip.play();
    }
    public static void stop() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public static void setVolume(double v) {
        if (musicPlayer != null) {
            musicPlayer.setVolume(v);
        }
    }
}
