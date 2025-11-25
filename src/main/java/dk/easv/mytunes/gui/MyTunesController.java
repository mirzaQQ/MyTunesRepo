package dk.easv.mytunes.gui;

import dk.easv.mytunes.bll.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;

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


}
