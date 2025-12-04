package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.Playlists;
import dk.easv.mytunes.be.Songs;
import dk.easv.mytunes.bll.*;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MyTunesController {
    @FXML
    private Button closeButton;
    @FXML
    private Button btnPlay;
    @FXML
    private Label lblName;
    @FXML
    private Label lblDuration;
    @FXML
    private Slider sliderVolume;
    @FXML
    private TableView<Songs> tableSongs;
    @FXML
    private TableColumn<Songs, String> tableSongsTitle;
    @FXML
    private TableColumn<Songs, String> tableSongsArtist;
    @FXML
    private TableColumn<Songs, Integer> tableSongsCategory;
    @FXML
    private TableColumn<Songs, String> tableSongsTime;
    @FXML
    private TableView<Playlists> tablePlaylist;
    @FXML
    private TableColumn<Playlists, String> tablePlaylistName;
    @FXML
    private TableColumn<Playlists, Integer> tablePlaylistSongs;
    @FXML
    private TableColumn<Playlists, String> tablePlaylistTime;
    private Songs currentsong;

    private final Logic logic = new Logic();
    private final ObservableList<Songs> songsObservableList = FXCollections.observableArrayList();
    private final ObservableList<Playlists> playlistsObservableList = FXCollections.observableArrayList();
    MusicFunctions musicFunctions = new MusicFunctions();

    public void initialize() {
        tableSongsTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableSongsArtist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        tableSongsCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tableSongsTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        try {
            List<Songs> songs = logic.getAllSongsFromDB();
            songsObservableList.addAll(songs);
            tableSongs.setItems(songsObservableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tablePlaylistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tablePlaylistSongs.setCellValueFactory(new PropertyValueFactory<>("songsNumber"));
        tablePlaylistTime.setCellValueFactory(new PropertyValueFactory<>("totalTime"));

        try {
            List<Playlists> playlists = logic.getAllPlaylistsFromDB();
            playlistsObservableList.addAll(playlists);
            tablePlaylist.setItems(playlistsObservableList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateSongTable() throws SQLException {
        songsObservableList.clear();
        List<Songs> songs = logic.getAllSongsFromDB();
        songsObservableList.addAll(songs);
    }

    public void updatePlaylistTable() throws SQLException {
        playlistsObservableList.clear();
        List<Playlists> playlists = logic.getAllPlaylistsFromDB();
        playlistsObservableList.addAll(playlists);
    }

    public void btnPlayOnClick(ActionEvent actionEvent) throws SQLException {

        Songs song = tableSongs.getSelectionModel().getSelectedItem();
        if (song == null) {
            return;
        }
        if(song == null || !song.equals(currentsong)) {
            musicFunctions.song(song.getSong_id());
            currentsong = song;
            btnPlay.setText("⏸");
            btnPlay.setFont(new Font(20));
            lblName.setText(musicFunctions.getMusic());
            musicFunctions.playMusic();


            return;
        }
        if (musicFunctions.getStatus().equals("PLAYING") || musicFunctions.getStatus().equals("READY")) {
            musicFunctions.pauseMusic();
            lblDuration.setText(musicFunctions.getDuration());
            btnPlay.setText("▶");
            btnPlay.setFont(new Font(24));
            return;

        } if (musicFunctions.getStatus().equals("PAUSED") || musicFunctions.getStatus().equals("READY")) {
            musicFunctions.playMusic();


            btnPlay.setText("⏸");
            btnPlay.setFont(new Font(20));
            return;
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

    public void btnNewSongOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("New-Song.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add song");
        stage.setResizable(false);
        stage.showAndWait();
        updateSongTable();
    }

    public void btnCloseClick(ActionEvent actionEvent) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void btnNewPlaylistOnClick(ActionEvent actionEvent) throws IOException, SQLException {
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesPlaylistView.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Add playlist");
        stage.setResizable(false);
        stage.showAndWait();
        updatePlaylistTable();
    }

    public void btnDeletePlaylistOnClick (ActionEvent actionEvent) throws SQLException {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        /*
        ToDo
        Ask user if he wants to delete playlist
         */
        if (selectedPlaylist != null) {
            logic.deletePlaylistFromDB(selectedPlaylist.getPlaylist_id());
            playlistsObservableList.remove(selectedPlaylist);
        }
        else {
            /*
            ToDo
            Implement label that will informs user if no playlist is selected for deletion
             */
        }
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

    public void BtnDeleteSongOnClick(ActionEvent actionEvent) throws SQLException {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        /*
        ToDo
        Ask user if he wants to delete song
         */
        if (selectedSong != null) {
            logic.deleteSongFromDB(selectedSong.getSong_id());
            songsObservableList.remove(selectedSong);
        }
        else {
            /*
            ToDo
            Implement label that will informs user if no song is selected for deletion
             */
        }
    }

    public void btnStatus(ActionEvent actionEvent) {
        System.out.println(musicFunctions.getStatus());
    }
}
