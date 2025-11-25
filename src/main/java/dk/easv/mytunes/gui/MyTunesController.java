package dk.easv.mytunes.gui;

import dk.easv.mytunes.bll.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;

public class MyTunesController {
    @FXML
    private Label lblName;
    @FXML
    private Label lblDuration;
    @FXML
    private Slider sliderVolume;

    MusicFunctions musicFunctions = new MusicFunctions();
    public void btnPlayOnClick(ActionEvent actionEvent) {
        lblDuration.setText(musicFunctions.getDuration());
        lblName.setText(musicFunctions.getMusic());

        if (musicFunctions.getStatus().equals("PLAYING")) {
            musicFunctions.pauseMusic();

        } else {
            musicFunctions.playMusic();

        }
    }

    public void sliderOnClick(MouseEvent mouseEvent) {

        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void sliderOnMouseDrag(MouseEvent mouseEvent) {
        musicFunctions.setVolume(sliderVolume.getValue());
    }

    public void btnBckOnClick(ActionEvent actionEvent) {
        /**
         * !!!!!!TODO!!!!!!!!!
         * First click restarts the music
         * and the second one goes back one track
         * if there is none then nothing happens or goes the last element of the playlist
         */
        musicFunctions.restartMusic();

    }
}
