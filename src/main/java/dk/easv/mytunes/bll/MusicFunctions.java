package dk.easv.mytunes.bll;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicFunctions {
    private String bip = "music.mp3";
    private Media hit = new Media(new File(bip).toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(hit);
    //private String duration = mediaPlayer.getTotalDuration().toString();
    //private String durationTrimed = duration.replace("ms", "").trim();
    //private int durationInt = Integer.parseInt(durationTrimed);

    public void playMusic() {
        mediaPlayer.play();
    }
    public void pauseMusic() {
        mediaPlayer.pause();
    }
    public String getStatus() {
        /** Status can be PLAYING, READY or stopped
         * we can use this to play and stop the music
         * with the same button
         */
        return mediaPlayer.getStatus().toString();
    }

    public void setVolume(int volume) {
        mediaPlayer.setVolume(volume);
    }
    public String getDuration() {
        /**Duration in in ms and must be converted to string to cut of the "ms" part
         * then into double because the duration of the last song was x.0 so it cannot be converted into int from string
         * therefor it must be double and then integer
         */
        String duration = mediaPlayer.getTotalDuration().toString().replace("ms", "").trim();
        double durationDouble = Double.parseDouble(duration);
        int durationInt = (int) durationDouble;
        int totalseconds = durationInt / 1000;
        int minutes = totalseconds / 60;
        int seconds = totalseconds % 60;
        String fullDuration = String.format("%02d:%02d", minutes, seconds);

        return fullDuration;
    }
    public String getMusic() {
        return bip.replace(".mp3", "").trim();
    }

}
