package dk.easv.mytunes.gui;

import dk.easv.mytunes.bll.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class MyTunesController {
    MusicFunctions musicFunctions = new MusicFunctions();
    public void btnPlayOnClick(ActionEvent actionEvent) {
        if (musicFunctions.getStatus().equals("PLAYING")) {
            musicFunctions.stopMusic();
        } else {
            musicFunctions.playMusic();
        }
    }
}
