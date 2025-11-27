package dk.easv.mytunes.gui;

import dk.easv.mytunes.HelloApplication;
import dk.easv.mytunes.bll.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MyTunesController {
    @FXML
    private Button closeButton;
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

    public void btnNewSongOnClick(ActionEvent actionEvent) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("New-Song.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add song");
        stage.setResizable(false);
        stage.show();
    }

    public void btnCloseClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void btnNewPlaylistOnClick(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesPlaylistView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add playlist");
        stage.setResizable(false);
        stage.show();

    }

    public void btnDeletePlaylistOnClick (ActionEvent actionEvent) {
    }

    public void btnFilterOnClick(ActionEvent actionEvent) {
    }

    public void btnEditPlaylistOnClick(ActionEvent actionEvent) {
    }

    public void BtnMoveSongUpOnClick(ActionEvent actionEvent) {
    }

    public void BtnMoveSongDownOnClick(ActionEvent actionEvent) {
    }

    public void BtnDeleteSongInPlaylistOnClick(ActionEvent actionEvent) {
    }

    public void btnMoveSongToPlaylistOnClick(ActionEvent actionEvent) {
    }

    public void BtnEditSongOnClick(ActionEvent actionEvent) {
    }

    public void BtnDeleteSongOnClick(ActionEvent actionEvent) {
    }
}
