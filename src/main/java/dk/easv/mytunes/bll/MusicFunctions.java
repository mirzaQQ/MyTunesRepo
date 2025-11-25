package dk.easv.mytunes.bll;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicFunctions {
    String bip = "music.mp3";
    Media hit = new Media(new File(bip).toURI().toString());
    MediaPlayer mediaPlayer = new MediaPlayer(hit);

    public void playMusic() {
        mediaPlayer.play();
    }
    public void stopMusic() {
        mediaPlayer.stop();
    }
    public String getStatus() {
        /** Status can be PLAYING, READY or stopped
         * we can use this to play and stop the music
         * with the same button
         */
        return mediaPlayer.getStatus().toString();

    }

}
