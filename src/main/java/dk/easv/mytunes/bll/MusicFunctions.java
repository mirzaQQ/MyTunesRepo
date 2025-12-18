package dk.easv.mytunes.bll;

import java.io.File;
import java.sql.SQLException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicFunctions {
    private MediaPlayer mediaPlayer;
    private String bip;
    private double currentVolume = 0.1;

    public void song(String filepath, Runnable onEndCallback) throws SQLException {
        bip = filepath;
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setVolume(currentVolume);

        if (onEndCallback != null) {
            mediaPlayer.setOnEndOfMedia(onEndCallback);
        }
    }

    public void playMusic() {
       mediaPlayer.play();
    }
    public void pauseMusic() {
       mediaPlayer.pause();
    }
    public String getStatus() {
        /* Status can be PLAYING, READY or stopped
         * we can use this to play and stop the music
         * with the same button
         */
        if (mediaPlayer == null) {
            return "STOPPED";
        }
        return mediaPlayer.getStatus().toString();
    }

    public void setVolume(double volume) {
        currentVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(currentVolume);
        }
    }

    public void restartMusic() {
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.stop();
            mediaPlayer.play();
        }
        else{
            mediaPlayer.stop();
        }
    }

    public void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }
}
