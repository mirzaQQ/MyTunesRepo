package dk.easv.mytunes.gui;

import dk.easv.mytunes.bll.Logic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class MyTunesPlaylistController {
    @FXML
    private Label lblException;
    @FXML
    private TextField txtPlaylist;
    @FXML
    private Button closeButton;
    Logic logic =  new Logic();
    public void btnCloseOnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void btnSaveOnClick(ActionEvent actionEvent) throws SQLException {
        String trimmedPlaylist = txtPlaylist.getText().trim();
        if (trimmedPlaylist.isEmpty()) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" \"Name\" field is empty ");
        }
        else if (logic.checkIfPlaylistExists(trimmedPlaylist)) {
            lblException.setVisible(true);
            lblException.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
            lblException.setText(" Playlist already exists ");
        }
        else {
            logic.addPlaylist(txtPlaylist.getText());
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        }
    }
}
