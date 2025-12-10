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
    private double currentvolume = 0.1;


    public MediaPlayer song(int songid) throws SQLException {
        SongsDAO songsDAO = new SongsDAO();
        bip = songsDAO.playSong(songid);
        Media hit = new Media(new File(bip).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.setVolume(currentvolume);
        return mediaPlayer;
    }

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
        currentvolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(currentvolume);
        }
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

    public void stopMusic() {
        mediaPlayer.stop();
        mediaPlayer.dispose();
    }
}
