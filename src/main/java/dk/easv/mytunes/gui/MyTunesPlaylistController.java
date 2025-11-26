package dk.easv.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MyTunesPlaylistController {
    @FXML
    private TextField txtPlaylist;
    @FXML
    private Button closeButton;

    public void btnCloseOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void btnSaveOnClick(ActionEvent actionEvent) {
        System.out.println(txtPlaylist.getText());
    }
}
