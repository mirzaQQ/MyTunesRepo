package dk.easv.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MyTunesPlaylistController {
    @FXML
    private Label lblExcpetion;
    @FXML
    private TextField txtPlaylist;
    @FXML
    private Button closeButton;

    public void btnCloseOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void btnSaveOnClick(ActionEvent actionEvent) {
        String trimmedPlaylist = txtPlaylist.getText().trim();
        if (trimmedPlaylist.equals("") || trimmedPlaylist == null) {
            lblExcpetion.setVisible(true);
            lblExcpetion.setText("Please fill out all fields");
        }
        else {
            System.out.println(txtPlaylist.getText());
        }
    }
}
