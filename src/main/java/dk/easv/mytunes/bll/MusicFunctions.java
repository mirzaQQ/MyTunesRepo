package dk.easv.mytunes.bll;
import java.io.File;
import java.sql.SQLException;
import dk.easv.mytunes.dal.SongsDAO;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import static java.lang.Double.parseDouble;

public class MusicFunctions {
    private MediaPlayer mediaPlayer;
    private String bip;
    private Media hit;


    public MediaPlayer song(int songid) throws SQLException {
        SongsDAO songsDAO = new SongsDAO();
        bip = songsDAO.playSong(songid);
        hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
       return mediaPlayer;
    }

    /**
    SongsDAO songsDAO = new SongsDAO();
    private String bip = "music.mp3";
    private Media hit = new Media(new File(bip).toURI().toString());
    private MediaPlayer mediaPlayer = new MediaPlayer(hit);*/

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

    public void setVolume(double volume) {
        double rounded = Math.round(volume * 10) / 10.0;
        mediaPlayer.setVolume(volume);
    }

    public String getDuration() {
        /**
        *Duration in in ms and must be converted to string to cut of the "ms" part
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
        String regex = "[\\\\]";
        String[] splitedBip = bip.split(regex);
        String music = splitedBip[splitedBip.length - 1];
        if(music.contains(".wav"))
            return music.replace(".wav", "").trim();
        else{
            return music.replace(".mp3", "").trim();
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

}
