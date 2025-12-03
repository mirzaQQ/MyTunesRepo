package dk.easv.mytunes.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class MyTunesPlaylistController {
    @FXML
    private Label lblException;
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
        if (trimmedPlaylist.isEmpty()) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" \"Name\" field is empty ");
        }
        else {
            System.out.println(txtPlaylist.getText());
        }
    }
}
