package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlists;
import dk.easv.mytunes.bll.Logic;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;

public class NewPlaylistController {
    @FXML
    private Label lblException;
    @FXML
    private TextField txtPlaylist;
    @FXML
    private Button closeButton;

    Logic logic =  new Logic();
    private Playlists playlistToEdit = null;

    public void setPlaylistToEdit(Playlists playlist) {
        this.playlistToEdit = playlist;
        txtPlaylist.setText(playlist.getName());
    }

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
            return;
        }

        if (playlistToEdit != null) {
            if (trimmedPlaylist.equals(playlistToEdit.getName()) && logic.checkIfPlaylistExists(playlistToEdit.getName())) {
                lblException.setVisible(true);
                lblException.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
                lblException.setText(" Playlist already exists");
                return;
            }
            logic.updatePlaylistName(playlistToEdit.getPlaylist_id(), trimmedPlaylist);
        }
        else {
            if (logic.checkIfPlaylistExists(trimmedPlaylist)) {
                lblException.setVisible(true);
                lblException.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
                lblException.setText(" Playlist already exists");
                return;
            }
            logic.addPlaylist(trimmedPlaylist);
        }
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
