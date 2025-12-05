package dk.easv.mytunes.gui;

import dk.easv.mytunes.be.PlaylistSong;
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
    private Label lblexception;
    @FXML
    private Slider sliderVolume;
    @FXML
    private ListView<String> listSongsOnPlaylist;
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
    private final ObservableList<String> playlistSongObservableList = FXCollections.observableArrayList();
    MusicFunctions musicFunctions = new MusicFunctions();
    boolean isPlaylistSelected = false;
    boolean isSongSelected = false;

    public void initialize() throws SQLException {
        initializeSongTable();
        initializePlaylistTable();

        listSongsOnPlaylist.setItems(playlistSongObservableList);

        tablePlaylist.getSelectionModel().selectedItemProperty().addListener(
                (_, _, _) -> setSelectedPlaylist());
        tableSongs.getSelectionModel().selectedItemProperty().addListener(
                (_, _, _) -> setSelectedSong());
    }

    public void initializeSongTable() throws SQLException {
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
    }

    public void initializePlaylistTable() throws SQLException {
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

    public void updatePlaylistSongList() throws SQLException {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) {
            playlistSongObservableList.clear();
            return;
        }
        playlistSongObservableList.clear();
        List<PlaylistSong> playlistSongs = logic.getAllPlaylistSongsFromDB(selectedPlaylist.getPlaylist_id());

        for (PlaylistSong ps : playlistSongs)
            playlistSongObservableList.add(ps.gettitle());
    }


    public void setSelectedPlaylist() {
        Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist != null) {
            isPlaylistSelected = true;
            try {
                lblexception.setVisible(true);
                lblexception.setStyle("-fx-text-fill: black;");
                lblexception.setText("Selected playlist: " + selectedPlaylist.getName());
                updatePlaylistSongList();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            isPlaylistSelected = false;
            playlistSongObservableList.clear();
            lblexception.setVisible(false);
        }
    }

    public void setSelectedSong() {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        isSongSelected = selectedSong != null;
    }

    public void btnPlayOnClick(ActionEvent actionEvent) throws SQLException {

        Songs song = tableSongs.getSelectionModel().getSelectedItem();
        if (song == null) {
            return;
        }
        if(song == null || !song.equals(currentsong)) {
            musicFunctions.song(song.getSong_id());
            
            musicFunctions.playMusic();
            currentsong = song;
            btnPlay.setText("⏸");
            btnPlay.setFont(new Font(20));
            lblName.setText(musicFunctions.getMusic());
            if (musicFunctions.getStatus().equals("READY") || musicFunctions.getStatus().equals("PLAYING") || musicFunctions.getStatus().equals("PAUSED")) {
                lblDuration.setText((musicFunctions.getDuration()));
            }

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
            lblDuration.setText(musicFunctions.getDuration());
           //System.out.println(musicFunctions.getDuration());
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
        Parent root = FXMLLoader.load(getClass().getResource("MyTunesSongView.fxml"));
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
        /**
         *ToDo
         *Ask user if he wants to delete playlist
         */
        if (selectedPlaylist != null) {
            logic.deletePlaylistFromDB(selectedPlaylist.getPlaylist_id());
            playlistsObservableList.remove(selectedPlaylist);
        }
        else {
            /**
             *ToDo
             *Implement label that will informs user if no playlist is selected for deletion
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
        String selectedSong = listSongsOnPlaylist.getSelectionModel().getSelectedItem();
        int selectedIndex = listSongsOnPlaylist.getSelectionModel().getSelectedIndex();

        if (selectedSong == null) {
            lblexception.setVisible(true);
            lblexception.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblexception.setText(" Please select song to delete ");
        }
        else {
            try {
                Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
                if (selectedPlaylist != null) {
                    int position = selectedIndex + 1;
                    logic.deleteSongFromPlaylist(selectedPlaylist.getPlaylist_id(), position);
                    updatePlaylistSongList();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void btnMoveSongToPlaylistOnClick(ActionEvent actionEvent) {
        if (!isPlaylistSelected) {
            lblexception.setVisible(true);
            lblexception.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblexception.setText(" Please select playlist ");
        }
        else if (!isSongSelected) {
            lblexception.setVisible(true);
            lblexception.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 5px;");
            lblexception.setText(" Please select song ");
        }
        else {
            try {
                Playlists selectedPlaylist = tablePlaylist.getSelectionModel().getSelectedItem();
                Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
                logic.addSongToPlaylist(selectedSong.getSong_id(), selectedPlaylist.getPlaylist_id());
                updatePlaylistSongList();

                lblexception.setVisible(true);
                lblexception.setStyle("-fx-text-fill: black;");
                lblexception.setText(" Song \"" + tableSongs.getSelectionModel().getSelectedItem().getTitle() + "\" moved to \"" + tablePlaylist.getSelectionModel().getSelectedItem().getName() + "\" playlist");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void BtnEditSongOnClick(ActionEvent actionEvent) {
    }

    public void BtnDeleteSongOnClick(ActionEvent actionEvent) throws SQLException {
        Songs selectedSong = tableSongs.getSelectionModel().getSelectedItem();
        /**
         *ToDo
         *Ask user if he wants to delete song
         */
        if (selectedSong != null) {
            logic.deleteSongFromDB(selectedSong.getSong_id());
            songsObservableList.remove(selectedSong);
        }
        else {
            /**
             *ToDo
             *Implement label that will informs user if no song is selected for deletion
             */
        }
    }

    public void btnStatus(ActionEvent actionEvent) {
        System.out.println(musicFunctions.getStatus());
    }
}
